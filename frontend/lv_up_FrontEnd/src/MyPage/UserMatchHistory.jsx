import React from "react";
import { useNavigate } from "react-router-dom"; // useNavigate 추가
import "./UserMatchHistory.css";
import back from "../assets/imageFile/backButton.png";
import smile from "../assets/imageFile/smile.png";
import smiling from "../assets/imageFile/smiling.png";
import frown from "../assets/imageFile/frown.png";
import history_siren from "../assets/imageFile/history_siren.png";

export default function UserMatchHistory() {
  const navigate = useNavigate(); // useNavigate 훅 사용

  const iconMap = {
    smile: smile,
    smiling: smiling,
    frown: frown,
  };

  const todayMatches = [
    { id: 1, icon: "smiling", name: "이름", time: "오전 00:00" },
    { id: 2, icon: "smile", name: "이름", time: "오전 00:00" },
    { id: 3, icon: "smiling", name: "이름", time: "오전 00:00" },
  ];

  const pastMatches = [
    { id: 4, icon: "frown", name: "이름", time: "오전 00:00" },
    { id: 5, icon: "smiling", name: "이름", time: "오전 00:00" },
  ];

  const renderMatch = (match) => (
    <div className="user_match_card" key={match.id} style={{ cursor: 'pointer' }}>
      <img className="match_icon" src={iconMap[match.icon]} alt={match.icon} />
      <div className="match_info">
        <span className="match_name">{match.name}</span>
        <span className="match_time">{match.time}</span>
      </div>
      <div className="match_tags">
        <span className="match_mode">#모드</span>
        <span className="match_category">#카테고리</span>
        <span 
        className="match_siren" 
        onClick={() => navigate('/user-report', { state: { userId: match.id } })} // 신고 페이지로 이동하면서 유저 ID 전달
        style={{ cursor: 'pointer' }}
      >
        <img src={history_siren} alt="신고" />
      </span>
      </div>

    </div>
  );

  return (
    <div className="user_match_history_container">
      <div 
        className="back_button"
        onClick={() => navigate('/mypage')}
        style={{ cursor: 'pointer' }}
      >
        <img src={back} alt="Back" />
      </div>
      <h1 className="user_history_title">유저 매칭 기록</h1>
      <div className="user_match_history_line" />
      
      <div className="date_section">
        <h2 className="date_title">오늘</h2>
        {todayMatches.map(renderMatch)}
      </div>
      
      <div className="date_section">
        <h2 className="date_title">01월 18일 토요일</h2>
        {pastMatches.map(renderMatch)}
      </div>
    </div>
  );
}