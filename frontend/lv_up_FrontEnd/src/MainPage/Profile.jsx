import React, { useState, useEffect } from "react";
import "./Profile.css";
import axios from "axios";
import Cookies from "js-cookie";

const avatarMap = {
  1: "/assets/avatar1.jpg",
  2: "/assets/avatar2.jpg",
  3: "/assets/avatar3.jpg",
  4: "/assets/avatar4.jpg",
  5: "/assets/avatar5.jpg",
};

function Profile({
  nickname,
  level,
  remainExp,
  expForNextLevel,
  currentAvatar,
  onProfileClick,
}) {
  // 🔹 실제 경험치 퍼센트 계산
  const targetExpPercentage = (1 - remainExp / expForNextLevel) * 100;

  // 🔹 애니메이션을 위한 상태 (0에서 목표치까지 증가)
  const [expPercentage, setExpPercentage] = useState(0);
  const [selectedAvatarId, setSelectedAvatarId] = useState(1);

  useEffect(() => {
    if (currentAvatar) {
      setSelectedAvatarId(currentAvatar);
    }
  }, [currentAvatar]);

  // 서버에서 선택된 아바타 ID 가져오기
  useEffect(() => {
    const fetchSelectedAvatar = async () => {
      try {
        const response = await axios.get("/api/users/avatar/selected", {
          headers: {
            Authorization: `Bearer ${Cookies.get("token")}`,
          },
        });

        // currentAvatar가 없을 경우에만 서버에서 가져온 아바타 ID 사용
        if (!currentAvatar) {
          setSelectedAvatarId(response.data.avatarId || response.data || 1);
        }
      } catch (error) {
        console.error("아바타 정보 가져오기 실패:", error);
        // 오류 발생 시 기본 아바타(1)로 설정
        if (!currentAvatar) {
          setSelectedAvatarId(1);
        }
      }
    };

    fetchSelectedAvatar();
  }, [currentAvatar]);

  // 아바타 이미지 가져오기 함수
  const getAvatarImage = (avatarId) => {
    // avatarMap에서 해당 아바타 ID의 이미지 경로를 찾음
    // 없으면 기본값(avatar1.jpg)으로 설정
    return avatarMap[avatarId] || avatarMap[1];
  };

  useEffect(() => {
    let animationFrame;

    // 부드럽게 게이지가 차도록 애니메이션 효과 적용
    const animateExp = () => {
      setExpPercentage((prev) => {
        if (prev < targetExpPercentage) {
          animationFrame = requestAnimationFrame(animateExp);
          return prev + 1; // 1씩 증가하면서 애니메이션 효과
        } else {
          return targetExpPercentage;
        }
      });
    };

    animateExp(); // 애니메이션 시작

    return () => cancelAnimationFrame(animationFrame); // 클린업 함수
  }, [targetExpPercentage]); // targetExpPercentage가 변경될 때마다 실행

  return (
    <div>
      <div className="profile">
        <img
          className="profile-image"
          src={getAvatarImage(selectedAvatarId)}
          alt="profile-image"
          onClick={onProfileClick}
          style={{ cursor: "pointer" }}
        />
        <div className="profile-info">
          {/* 닉네임 표시 */}
          <div className="profile-name">{nickname || "User"}</div>

          <div className="level-box">
            {/* 경험치 게이지 바 */}
            <div className="exp-gauge-bar">
              <div
                className="exp-gauge-fill"
                style={{ width: `${expPercentage}%` }} // 애니메이션 반영
              ></div>
            </div>

            {/* 레벨 표시 */}
            <div className="levels">
              <div className="now-level">Lv.{level}</div>
              <div className="next-level">Lv.{level + 1}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Profile;
