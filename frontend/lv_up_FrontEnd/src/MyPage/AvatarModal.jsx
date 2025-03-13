import React from 'react';
import './AvatarModal.css';

function AvatarModal({ isOpen, onClose, currentAvatar, onSelect }) {
  if (!isOpen) return null;

  const handleOverlayClick = (e) => {
    if (e.target.className === 'avatar-modal-overlay') {
      onClose();
    }
  };

  const avatars = [
    { avatarId: 1, name: "avatar1", src: "/assets/avatar1.jpg" },
    { avatarId: 2, name: "avatar2", src: "/assets/avatar2.jpg" },
    { avatarId: 3, name: "avatar3", src: "/assets/avatar3.jpg" },
    { avatarId: 4, name: "avatar4", src: "/assets/avatar4.jpg" },
    { avatarId: 5, name: "avatar5", src: "/assets/avatar5.jpg" }
  ];

  return (
    <div className="avatar-modal-overlay" onClick={handleOverlayClick}>
      <div className="avatar-modal-content" onClick={(e) => e.stopPropagation()}>
        <h2 className="avatar-modal-title">아바타 선택</h2>
        <div className="avatar-grid">
          {avatars.map((avatar) => (
            <div key={avatar.avatarId} className="avatar-item">
              <img
                src={avatar.src}
                alt={avatar.name}
                onClick={() => onSelect(avatar.avatarId)}
                className={`avatar-image ${currentAvatar === avatar.avatarId ? "selected" : ""}`}
              />
            </div>
          ))}
        </div>
        <button className="modal-close-button" onClick={onClose}>
          확인
        </button>
      </div>
    </div>
  );
}

export default AvatarModal;