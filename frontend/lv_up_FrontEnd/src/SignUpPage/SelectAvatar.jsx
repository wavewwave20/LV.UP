import { useState } from "react";
import "./SelectAvatar.css";

function SelectAvatar({ setSelectedAvatar }) {
  const [localSelectedAvatar, setLocalSelectedAvatar] = useState(null);

  const avatars = [
    { id: 1, src: "/assets/avatar1.jpg", name: "avatar1" },
    { id: 2, src: "/assets/avatar2.jpg", name: "avatar2" },
    { id: 3, src: "/assets/avatar3.jpg", name: "avatar3" },
    { id: 4, src: "/assets/avatar4.jpg", name: "avatar4" },
    { id: 5, src: "/assets/avatar5.jpg", name: "avatar5" },
  ];

  const handleAvatarSelect = (avatarName) => {
    const avatarMap = {
      avatar1: 1,
      avatar2: 2,
      avatar3: 3,
      avatar4: 4,
      avatar5: 5,
    };

    const avatarId = avatarMap[avatarName];

    setLocalSelectedAvatar(avatarId);
    setSelectedAvatar(avatarId);
  };

  return (
    <div className="select-avatar-container">
      <h1 className="avatar-select-bar">아바타 선택</h1>
      <div className="avatar-explain">
        스몰톡에서 상대방에게 보이는 아바타를 설정할 수 있어요.
      </div>
      <div className="avatar-list">
        {avatars.map((avatar) => (
          <img
            key={avatar.id}
            src={avatar.src}
            alt={avatar.name}
            onClick={() => handleAvatarSelect(avatar.name)}
            className={`avatar-image ${
              localSelectedAvatar === avatar.id ? "selected" : ""
            }`}
          />
        ))}
      </div>
    </div>
  );
}

export default SelectAvatar;
