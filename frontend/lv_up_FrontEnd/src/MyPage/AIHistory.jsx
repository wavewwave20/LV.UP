import React from "react";
import { useNavigate } from "react-router-dom";
import "./AIHistory.css";
import back from "../assets/imageFile/backButton.png";

export default function AIHistory() {
  const navigate = useNavigate();

  const todayChats = [
    { id: 1, scenario: "새 친구 사귀기", time: "오후 02:20" },
    { id: 2, scenario: "카페에서", time: "오후 01:33" }
  ];

  const pastChats = [
    { id: 3, scenario: "싸피에서", time: "오후 10:38" }
  ];

  const handleBackClick = () => navigate('/mypage');
  const handleChatClick = () => navigate('/ai-talk-result');

  const renderChat = (chat) => (
    <div 
      className="chat_record_card"
      onClick={handleChatClick}
      style={{ cursor: 'pointer' }} 
      key={chat.id}
    >
      <span className="chat_scenario">{chat.scenario}</span>
      <span className="chat_time">{chat.time}</span>
    </div>
  );

  return (
    <div className="ai_history_container">
      <div 
        className="back_button"
        onClick={handleBackClick}
        style={{ cursor: 'pointer' }}
      >
        <img src={back} alt="뒤로 가기" />
      </div>

      <h1 className="ai_history_title">AI 대화 기록</h1>
      <div className="ai_history_line" />
      
      <div className="aihistory_date_section">
        <h2 className="aihistory_date_title">오늘</h2>
        {todayChats.map(renderChat)}
      </div>
      
      <div className="aihistory_date_section">
        <h2 className="aihistory_date_title">01월 18일 토요일</h2>
        {pastChats.map(renderChat)}
      </div>
    </div>
  );
}