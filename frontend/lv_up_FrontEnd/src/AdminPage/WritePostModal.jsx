import React, { useState } from 'react';
import { createBoard, updateBoard } from '../api';
import './WritePostModal.css';

export default function WritePostModal({ editingPost, onClose, onSuccess }) {
  const [formData, setFormData] = useState({
    type: editingPost?.type || "A",
    board_title: editingPost?.board_title || "",
    board_content: editingPost?.board_content || "",
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!formData.board_title.trim() || !formData.board_content.trim()) {
      alert('제목과 내용을 모두 입력해주세요.');
      return;
    }

    try {
      console.log('📤 게시글 작성 요청 시작');
      
      const requestData = {
        type: formData.type,
        board_title: formData.board_title.trim(),
        board_content: formData.board_content.trim(),
        visionable: 1
      };

      console.log('요청 데이터:', requestData);

      let response;
      if (editingPost) {
        response = await updateBoard(editingPost.board_id, requestData);
      } else {
        response = await createBoard(requestData);
      }

      console.log('✅ 서버 응답:', response);

      if (response.data) {
        alert(editingPost ? '게시글이 수정되었습니다.' : '게시글이 작성되었습니다.');
        if (onSuccess) {
          onSuccess();
        }
        onClose();
      }
    } catch (error) {
      console.error('❌ 게시글 저장 실패:', error);
      
      let errorMessage = '게시글 저장 중 오류가 발생했습니다.';
      if (error.response) {
        console.error('서버 응답:', error.response);
        errorMessage = error.response.data?.message || errorMessage;
      }
      
      alert(errorMessage);
    }
  };
  
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  return (
    <div className="modal_overlay" onClick={(e) => {
      if (e.target === e.currentTarget) onClose();
    }}>
      <div className="modal_content">
        <button className="write_close_button" onClick={onClose}>×</button>
        <h3 className="modal_title">
          {editingPost ? '게시글 수정' : '새 게시글 작성'}
        </h3>

        <form onSubmit={handleSubmit} className="post_form">
          <div className="form_group">
            <label>구분:</label>
            <select
              name="type"
              value={formData.type}
              onChange={handleChange}
              required
            >
              <option value="A">공지사항</option>
              <option value="E">이벤트</option>
            </select>
          </div>

          <div className="form_group">
            <label>제목:</label>
            <input
              type="text"
              name="board_title"
              value={formData.board_title}
              onChange={handleChange}
              required
              maxLength={100}
              placeholder="제목을 입력하세요"
            />
          </div>

          <div className="form_group">
            <label>내용:</label>
            <textarea
              name="board_content"
              value={formData.board_content}
              onChange={handleChange}
              required
              placeholder="내용을 입력하세요"
              rows="10"
            />
          </div>

          <div className="button_container">
            <button type="submit" className="submit_button">
              {editingPost ? '수정하기' : '작성하기'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}