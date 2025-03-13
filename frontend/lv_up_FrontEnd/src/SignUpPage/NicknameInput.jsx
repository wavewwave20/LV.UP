import React, { useState } from "react";
import "./NicknameInput.css";
<<<<<<< HEAD
import { checkNickname } from "../api"; // API 호출 함수 가져오기
=======
import { fetchNickname } from "../api/UserAPI";
>>>>>>> develop

function NicknameInput() {
  const [nickname, setNickname] = useState("");
  const [isAvailable, setIsAvailable] = useState(null);
  const [error, setError] = useState(null);

  const checkNicknameAvailability = async () => {
    if (!nickname.trim()) {
      setError("닉네임을 입력해주세요.");
      setIsAvailable(null);
      return;
    }

    try {
      const response = await fetchNickname(nickname);

      if (response.data === true) {
        setIsAvailable(true);
        setError(null);
<<<<<<< HEAD
        console.log("✅ 사용 가능한 닉네임입니다.");
      } else {
        setIsAvailable(false);
        setError("이미 사용 중인 닉네임입니다.");
        console.log("❌ 닉네임이 중복되었습니다.");
=======
        onNicknameChange(nickname, true); // isVerified true로 전달
      } else {
        setIsAvailable(false);
        setError("이미 사용 중인 닉네임입니다.");
        onNicknameChange("", false); // isVerified false로 전달
>>>>>>> develop
      }
    } catch (err) {
      console.error("❌ 중복 확인 오류:", err);
      setError("중복 확인 중 오류가 발생했습니다.");
      setIsAvailable(null);
    }
  };

  return (
    <div className="nickname_input_container">
      {/* 입력 필드 섹션 */}
      <div className="input_section">
          <input
            type="text"
            placeholder="최대 10자"
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
            className="nickname_field"
          />
          <button 
            onClick={checkNicknameAvailability} 
            className="check_button"
          >
            중복 확인
          </button>
      </div>

      {/* 피드백 메시지 섹션 */}
      <div className="feedback_section">
        {isAvailable === true && (
          <p className="feedback_message success">
            ✅ 사용 가능한 닉네임입니다.
          </p>
        )}
        {isAvailable === false && (
          <p className="feedback_message error">
            ❌ {error}
          </p>
        )}
      </div>
    </div>
  );
}

export default NicknameInput;
