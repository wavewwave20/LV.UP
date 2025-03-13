import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
<<<<<<< HEAD
import { checkNickname } from "../api"; // 닉네임 중복 확인 API만 사용
=======
import { checkNickname, fetchNickname } from "../api/UserAPI"; // 닉네임 중복 확인 API만 사용
>>>>>>> develop
import "./NicknameEditor.css";
import back from "../assets/imageFile/backButton.png";
import lvup from "../assets/imageFile/lvup.gif";

function NicknameEditor() {
  const [nickname, setNickname] = useState("");
  const [isAvailable, setIsAvailable] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  // 🖊️ 닉네임 입력 핸들러 (최대 10자 제한)
  const handleInputChange = (e) => {
    const value = e.target.value;
    if (value.length <= 10) {
      setNickname(value);
      setIsAvailable(null); // 닉네임이 바뀌면 다시 중복 확인 필요
      setError(null);
    }
  };

  // 🔍 닉네임 중복 확인 (닉네임 자동 저장됨)
  const checkNicknameAvailability = async () => {
    if (!nickname.trim()) {
      setError("닉네임을 입력해주세요.");
      setIsAvailable(null);
      return;
    }

    try {
      const response = await checkNickname(nickname); // 서버로 POST 요청 (닉네임 자동 저장됨)

      if (response.data === true) {
        setIsAvailable(true);
        setError(null);
      } else {
        setIsAvailable(false);
        setError("이미 사용 중인 닉네임입니다.");
      }
    } catch (err) {
      console.error("❌ 중복 확인 오류:", err);
      setError("중복 확인 중 오류가 발생했습니다.");
      setIsAvailable(null);
    }
  };

  // ✅ "확인" 버튼 클릭 시 마이페이지로 이동
  const handleSubmit = () => {
    if (!nickname.trim()) {
      setError("닉네임을 입력해주세요.");
      return;
    }

    if (isAvailable === false) {
      setError("이미 사용 중인 닉네임입니다. 다른 닉네임을 사용해주세요.");
      return;
    }
    fetchNickname(nickname);
    setLoading(true);
    setTimeout(() => {
      setLoading(false);
      navigate("/mypage");
    }, 1000);
  };

  return (
    <div className="nickname_editor_container">
      <div className="back_button" onClick={() => navigate("/mypage")}>
        <img src={back} alt="뒤로가기" />
      </div>
      <h1 className="nickname_title">회원님을 어떻게 부르면 좋을까요?</h1>

      <div className="warning_bubble">
        다른 사람에게 불쾌감을 주는 닉네임은 제재돼요.
      </div>

      <div className="input_container">
        <input
          type="text"
          value={nickname}
          onChange={handleInputChange}
          placeholder="최대 10자"
          className="nickname_input"
        />
        <button
          onClick={checkNicknameAvailability}
          className="nickname-check-button"
        >
          중복 확인
        </button>
      </div>

      {/* ✅ 사용 가능한 닉네임 */}
      {isAvailable === true && (
        <p style={{ color: "green" }}>✅ 사용 가능한 닉네임입니다.</p>
      )}

      {/* ❌ 닉네임 중복 */}
      {isAvailable === false && <p style={{ color: "red" }}>❌ {error}</p>}

      {/* 닉네임 변경 버튼 */}
      <button
        className={`confirm_button ${nickname.trim() && isAvailable ? "active" : ""}`}
        onClick={handleSubmit}
        disabled={!nickname.trim() || !isAvailable} // 닉네임이 없거나 중복확인이 안된 경우 비활성화
      >
        확인
      </button>

      {loading && (
        <div className="loading-overlay">
          <img src={lvup} alt="레벨업" className="loading-gif" />
        </div>
      )}
    </div>
  );
}

export default NicknameEditor;
