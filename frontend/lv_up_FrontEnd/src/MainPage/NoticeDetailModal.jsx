import React from 'react';
import './NoticeDetailModal.css';

const NoticeDetailModal = ({ notice, isOpen, onClose }) => {
  if (!isOpen) return null;

  const handleBackdropClick = (e) => {
    if (e.target === e.currentTarget) {
      onClose();
    }
  };

  return (
    <div className="notice-modal-overlay" onClick={handleBackdropClick}>
      <div className="notice-modal-content">
        <div className="notice-modal-header">
          <span className="notice-modal-tag">
            {notice?.type === "A" ? "[공지사항]" : 
             notice?.type === "E" ? "[이벤트]" : "[공지]"}
          </span>
          <button className="notice-modal-close" onClick={onClose}>×</button>
        </div>
        <h2 className="notice-modal-title">{notice?.board_title}</h2>
        <div className="notice-modal-date">
          {new Date(notice?.created_at).toLocaleDateString("ko-KR")}
        </div>
        <div className="notice-modal-body">
          {notice?.board_content}
        </div>
      </div>
    </div>
  );
};

export default NoticeDetailModal;