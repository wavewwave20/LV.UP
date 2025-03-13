import React, { useState, useEffect, useRef } from "react";
import "./IntroductionPopup.css";
import { postIntroduction } from "../api"; // API 호출 함수 가져오기

const IntroductionPopup = ({ initialValue, onSave, isOpen }) => {
  const [inputValue, setInputValue] = useState(initialValue);
  const contentRef = useRef(null);

  useEffect(() => {
    setInputValue(initialValue); // 팝업이 열릴 때 기존 내용 유지
  }, [initialValue]);

  const handleSave = async () => {
    const trimmedValue =
      inputValue.trim() || "나를 보여줄 수 있는 내용을 작성해주세요.";

    try {
      await postIntroduction(trimmedValue); // 서버로 전송
      onSave(trimmedValue); // UI 업데이트
    } catch (error) {
      console.error("❌ 한 줄 소개 저장 실패:", error);
    }
  };

  return (
    <div
      className="introduction-popup-container"
      style={{
        maxHeight: isOpen ? `${contentRef.current?.scrollHeight}px` : "0px",
        overflow: "hidden",
        transition: "max-height 0.3s ease-in-out",
      }}
      ref={contentRef}
    >
      <textarea
        className="introduction-popup-textarea"
        value={inputValue}
        onChange={(e) => setInputValue(e.target.value)}
        placeholder="안녕하세요. 전 OOO입니다."
      />
      <div className="introduction-popup-buttons">
        <button onClick={handleSave} className="introduction-popup-button">
          확인
        </button>
      </div>
    </div>
  );
};

export default IntroductionPopup;
