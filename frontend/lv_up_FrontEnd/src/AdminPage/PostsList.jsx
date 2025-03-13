import React, { useState, useEffect } from 'react';
import { fetchAnnouncements, fetchEvents, deleteBoard } from '../api';
import PostDetailModal from './PostDetailModal';
import WritePostModal from './WritePostModal';

export default function PostsList() {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedPost, setSelectedPost] = useState(null);
  const [isWriteModalOpen, setIsWriteModalOpen] = useState(false);
  const [editingPost, setEditingPost] = useState(null);
  
  // 페이지네이션 관련 상태
  const [currentPage, setCurrentPage] = useState(1);
  const postsPerPage = 10;

  // 검색 필터 상태
  const [searchFilters, setSearchFilters] = useState({
    date: '',
    title: '',
    type: ''
  });

  useEffect(() => {
    loadPosts();
  }, []);

  const loadPosts = async () => {
    try {
      setLoading(true);
      const [announcementsRes, eventsRes] = await Promise.all([
        fetchAnnouncements(),
        fetchEvents()
      ]);
      
      const allPosts = [
        ...announcementsRes.data.map(post => ({ ...post, typeText: '공지사항' })),
        ...eventsRes.data.map(post => ({ ...post, typeText: '이벤트' }))
      ];

      // 날짜 기준 정렬
      const sortedPosts = allPosts.sort((a, b) => 
        new Date(b.created_at) - new Date(a.created_at)
      );

      setPosts(sortedPosts);
    } catch (error) {
      console.error('게시글 로딩 실패:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (postId) => {
    if (window.confirm('정말 삭제하시겠습니까?')) {
      try {
        await deleteBoard(postId);
        alert('게시글이 삭제되었습니다.');
        loadPosts();
      } catch (error) {
        console.error('게시글 삭제 실패:', error);
        alert('게시글 삭제 중 오류가 발생했습니다.');
      }
    }
  };

  const handleEdit = (post) => {
    setEditingPost(post);
    setIsWriteModalOpen(true);
  };

  const handleSearch = (e) => {
    const { name, value } = e.target;
    setSearchFilters(prev => ({
      ...prev,
      [name]: value
    }));
    setCurrentPage(1); // 검색 시 첫 페이지로 이동
  };

  // 필터링된 게시글
  const filteredPosts = posts.filter(post => {
    const matchDate = !searchFilters.date || post.created_at.includes(searchFilters.date);
    const matchTitle = !searchFilters.title || 
      post.board_title.toLowerCase().includes(searchFilters.title.toLowerCase());
    const matchType = !searchFilters.type || post.typeText === searchFilters.type;

    return matchDate && matchTitle && matchType;
  });

  // 페이지네이션 로직
  const indexOfLastPost = currentPage * postsPerPage;
  const indexOfFirstPost = indexOfLastPost - postsPerPage;
  const currentPosts = filteredPosts.slice(indexOfFirstPost, indexOfLastPost);
  const totalPages = Math.ceil(filteredPosts.length / postsPerPage);

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('ko-KR');
  };

  if (loading) {
    return <div>로딩중...</div>;
  }

  return (
    <div>
      <div className="posts_header">
        <h2>공지/이벤트 통합 목록</h2>
        <button 
          className="write_button"
          onClick={() => {
            setEditingPost(null);
            setIsWriteModalOpen(true);
          }}
        >
          글쓰기
        </button>
      </div>

      {/* 검색 필터 */}
      <div className="search_container">
        <input
          type="date"
          name="date"
          value={searchFilters.date}
          onChange={handleSearch}
          placeholder="작성일 검색"
        />
        <input
          type="text"
          name="title"
          value={searchFilters.title}
          onChange={handleSearch}
          placeholder="제목 검색"
        />
        <select
          name="type"
          value={searchFilters.type}
          onChange={handleSearch}
        >
          <option value="">전체</option>
          <option value="공지사항">공지사항</option>
          <option value="이벤트">이벤트</option>
        </select>
      </div>

      <table className="cute_table">
        <thead>
          <tr>
            <th>구분</th>
            <th>제목</th>
            <th>작성일</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody>
          {currentPosts.map((post) => (
            <tr key={post.board_id}>
              <td>{post.typeText}</td>
              <td>
                <span 
                  className="post_title"
                  onClick={() => setSelectedPost(post)}
                >
                  {post.board_title}
                </span>
              </td>
              <td>{formatDate(post.created_at)}</td>
              <td>
                <button 
                  className="edit_button"
                  onClick={() => handleEdit(post)}
                >
                  수정
                </button>
                <button 
                  className="delete_button"
                  onClick={() => handleDelete(post.board_id)}
                >
                  삭제
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* 페이지네이션 */}
      <div style={{ 
        display: 'flex', 
        justifyContent: 'center', 
        gap: '10px', 
        margin: '20px 0' 
      }}>
        <button 
          onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
          disabled={currentPage === 1}
          style={{
            padding: '5px 15px',
            borderRadius: '5px',
            border: '1px solid #fa400d',
            backgroundColor: 'white',
            color: '#fa400d',
            cursor: currentPage === 1 ? 'not-allowed' : 'pointer'
          }}
        >
          이전
        </button>
        <span style={{ 
          display: 'flex', 
          alignItems: 'center',
          margin: '0 10px',
          color: '#fa400d'
        }}>
          {currentPage} / {totalPages}
        </span>
        <button 
          onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}
          disabled={currentPage === totalPages}
          style={{
            padding: '5px 15px',
            borderRadius: '5px',
            border: '1px solid #fa400d',
            backgroundColor: 'white',
            color: '#fa400d',
            cursor: currentPage === totalPages ? 'not-allowed' : 'pointer'
          }}
        >
          다음
        </button>
      </div>

      {selectedPost && (
        <PostDetailModal
          postId={selectedPost.board_id}
          onClose={() => setSelectedPost(null)}
        />
      )}

      {isWriteModalOpen && (
        <WritePostModal
          editingPost={editingPost}
          onClose={() => {
            setIsWriteModalOpen(false);
            setEditingPost(null);
          }}
          onSuccess={() => {
            setIsWriteModalOpen(false);
            setEditingPost(null);
            loadPosts();
          }}
        />
      )}
    </div>
  );
}