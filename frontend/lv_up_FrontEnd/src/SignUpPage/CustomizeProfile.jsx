import React, { useState, useEffect } from "react";
import "./CustomizeProfile.css";
import SelectInterest from "./SelectInterest";
import SelectAvatar from "./SelectAvatar";
import { useNavigate, useLocation } from "react-router-dom";
import Cookies from "js-cookie";
import NicknameInput from "./NicknameInput";
import { registerUser } from "../api"; // API 호출 함수 가져오기

function CustomizeProfile() {
  const location = useLocation();
  const navigate = useNavigate();

  const [token, setToken] = useState(location.state?.token || "");
  const [selectedInterests, setSelectedInterests] = useState([]); // 관심사 리스트
  const [selectedAvatar, setSelectedAvatar] = useState(null); // 아바타 ID (1, 2, 3)

  // useEffect(() => {
  //   if (token) {
  //     Cookies.set("token", token, {
  //       path: "/",
  //       secure: true,
  //       sameSite: "Strict",
  //     });
  //     console.log("🔐 JWT 토큰이 쿠키에 저장됨:", token);
  //   }
  // }, [token]);

  const handleCompletePage = async () => {
    if (!token) {
      console.error("❌ 토큰 없음, 요청 불가능");
      return;
    }

    if (!selectedAvatar) {
      console.error("❌ 아바타를 선택해주세요.");
      return;
    }

    const requestData = {
<<<<<<< HEAD
      interest: selectedInterests, // 서버에서 요구하는 키 "interest"
      avatar_id: selectedAvatar, // 서버에서 요구하는 키 "avatar_id"
=======
      interest: selectedInterests,
      avatar_id: selectedAvatar,
>>>>>>> develop
    };

    try {
      await registerUser(requestData);
<<<<<<< HEAD
      console.log("✅ 회원가입 완료!");
      navigate("/sign-up-complete");
=======
      navigate("/sign-up-complete", {
        state: {
          nickname,
          isNewUser: true,
        },
      });
>>>>>>> develop
    } catch (err) {
      console.error("❌ 회원가입 오류:", err);
      navigate("/sign-up-complete");
    }
  };

  return (
    <div className="customize_container">
      {/* 닉네임 섹션 */}
      <div className="nickname_section">
        <div className="customize_nickname_title">회원님을 어떻게 부르면 좋을까요?</div>
        <div className="warning_bubble">
          다른 사람에게 불쾌감을 주는 닉네임은 제재되요.
        </div>
<<<<<<< HEAD
        <NicknameInput />
=======
        <NicknameInput onNicknameChange={handleNicknameChange} />
>>>>>>> develop
      </div>

      {/* 관심사 선택 섹션 */}
      <div className="interest_section">
        <SelectInterest
          setSelectedInterests={setSelectedInterests}
          token={token}
        />
      </div>

      {/* 아바타 선택 섹션 */}
      <div className="avatar_section">
        <SelectAvatar setSelectedAvatar={setSelectedAvatar} />
      </div>

      {/* 확인 버튼 섹션 */}
      <div className="button_section">
        <button
          className="next_button"
          onClick={handleCompletePage}
        >
          확인
        </button>
      </div>
    </div>
  );
}

export default CustomizeProfile;
