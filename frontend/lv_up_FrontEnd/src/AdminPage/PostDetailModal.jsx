import React, { useState, useEffect } from 'react';
import { fetchBoardDetail } from '../api';
import './PostDetailModal.css';

export default function PostDetailModal({ postId, onClose }) {
  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadPostDetail();
  }, [postId]);

  const loadPostDetail = async () => {
    try {
      const response = await fetchBoardDetail(postId);
      setPost(response.data);
    } catch (error) {
      console.error('게시글 상세 정보 로딩 실패:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="modal_overlay">
        <div className="modal_content">
          <p>로딩중...</p>
        </div>
      </div>
    );
  }

  if (!post) return null;

  return (
    <div className="modal_overlay" onClick={(e) => {
      if (e.target === e.currentTarget) onClose();
    }}>
      <div className="modal_content">
        <button className="post_detail_close_button" onClick={onClose}>×</button>
        <h3 className="modal_title">
          {post.type === 'A' ? '공지사항' : '이벤트'}
        </h3>

        <div className="post_header">
          <h4 className="post_title">{post.board_title}</h4>
          <p className="post_info">
            작성일: {new Date(post.created_at).toLocaleString('ko-KR')}
            {post.updated_at !== post.created_at && 
              ` (수정일: ${new Date(post.updated_at).toLocaleString('ko-KR')})`
            }
          </p>
        </div>

        <div className="post_content">
          {post.board_content}
        </div>
      </div>
    </div>
  );
}