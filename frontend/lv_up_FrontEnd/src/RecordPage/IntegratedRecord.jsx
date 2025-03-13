import React, { useState, useEffect } from "react";
import Calendar from "react-calendar";
import { useNavigate } from "react-router-dom";
import "react-calendar/dist/Calendar.css";
import "./IntegratedRecord.css";
<<<<<<< HEAD

import smile from "../assets/imageFile/smile.png";
import smiling from "../assets/imageFile/smiling.png";
import frown from "../assets/imageFile/frown.png";
import siren from "../assets/imageFile/history_siren.png";
=======
>>>>>>> develop
import NavigationBar from "../MainPage/NavigationBar";
import UserMatchRecords from "./UserMatchRecords";
import AITalkRecords from "./AITalkRecords";
import { fetchMatchHistory } from "../api/SmalltalkAPI";
import { fetchAIHistory } from "../api/AITalkAPI";
import StatisticsModal from "./Statistics";
import { fetchAIHistoryByMonth } from "../api/AITalkAPI";
import { fetchMatchHistoryByMonth } from "../api/SmalltalkAPI";

// 더미 데이터
const DUMMY_DATA = [
  {
    matching_id: 1,
    matching_time: "2025-02-10T09:20:00",
    mode_name: "마이크패스 모드",
    interest_name: "헬스",
    partner_name: "김철수",
    ratingScore: 100
  },
  {
    matching_id: 2,
    matching_time: "2025-02-10T10:30:00",
    mode_name: "중급 모드",
    interest_name: "게임",
    partner_name: "이영희",
    ratingScore: 50
  },
  {
    matching_id: 3,
    matching_time: "2025-02-10T14:15:00",
    mode_name: "자유 모드",
    interest_name: "음악",
    partner_name: "박지민",
    ratingScore: 0
  },
  {
    matching_id: 4,
    matching_time: "2025-02-11T11:20:00",
    mode_name: "초보 모드",
    interest_name: "요리",
    partner_name: "최유진",
    ratingScore: 100
  },
  {
    matching_id: 5,
    matching_time: "2025-02-11T15:45:00",
    mode_name: "자유 모드",
    interest_name: "영화",
    partner_name: "정민수",
    ratingScore: 50
  }
];

function IntegratedRecord() {
  const [date, setDate] = useState(new Date()); // 달력에서 선택된 날짜
  const [activeTab, setActiveTab] = useState("matching");
  const [loading, setLoading] = useState(true);
<<<<<<< HEAD
  const ITEMS_PER_PAGE = 3;

  const formatDate = (date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  };

  const formatDisplayDate = (date) => {
    return date.toLocaleDateString("ko-KR", {
      month: "long",
      day: "numeric",
    });
  };

  const getEmoticonByScore = (score) => {
    if (score === 100) return smile;
    if (score === 50) return smiling;
    return frown;
  };

  useEffect(() => {
    // 더미 데이터 필터링 및 페이지네이션 처리
    const selectedDate = formatDate(date);
    const filteredData = DUMMY_DATA.filter(record => 
      record.matching_time.startsWith(selectedDate)
    );
    
    setMatchRecords(filteredData);
    setLoading(false);
  }, [date]);
=======

  // ✅ 서버 페이지네이션을 위한 state
  const [matchingPage, setMatchingPage] = useState(0);
  const [aiPage, setAiPage] = useState(0);

  // ✅ 서버로부터 받아온 데이터 & totalPages
  const [matchRecords, setMatchRecords] = useState(null);
  const [matchTotalPages, setMatchTotalPages] = useState(0);

  const [aiRecords, setAiRecords] = useState(null);
  const [aiTotalPages, setAiTotalPages] = useState(0);

  const [isModalOpen, setIsModalOpen] = useState(false);

  // 마커 날짜 배열
  const [markedDates, setMarkedDates] = useState([]);

  const navigate = useNavigate();

  const formatDate = (dateObj) => {
    const year = dateObj.getFullYear();
    const month = String(dateObj.getMonth() + 1).padStart(2, "0");
    const day = String(dateObj.getDate()).padStart(2, "0");
    return `${year}${month}${day}`; // YYYYMMDD
  };

  // ✅ 실제로 서버에 요청하여 페이징된 데이터를 받아오는 함수 (예시)
  const loadRecords = async () => {
    try {
      setLoading(true);
      const formattedDate = formatDate(date);

      if (activeTab === "matching") {
        // (예) 매칭 페이지 API 호출
        const response = await fetchMatchHistory(matchingPage, formattedDate);
        // 서버가 { content: [...], total_pages: ... } 형태라면 아래처럼 사용
        setMatchRecords(response.data.content);
        setMatchTotalPages(response.data.total_pages);
      } else {
        // ✅ AI 페이지 API 호출
        const response = await fetchAIHistory(aiPage, formattedDate);

        // (중요) 서버가 배열 형태로 반환 -> 그냥 response.data 사용
        setAiRecords(response.data);

        // total_pages가 따로 없다면, 임의로 1로 두거나
        // 혹은 클라이언트에서 직접 length로 계산하는 식으로 처리
        setAiTotalPages(1); // 또는 0 등으로 임시 세팅하거나
      }
    } catch (error) {
      console.error("기록 로딩 실패:", error);
    } finally {
      setLoading(false);
    }
  };

  // 날짜, 탭, 현재 페이지(matchingPage, aiPage)가 바뀔 때마다 loadRecords
  useEffect(() => {
    loadRecords();
  }, [date, activeTab, matchingPage, aiPage]);

  // 탭 전환 시, 페이지를 0으로 초기화
  const handleTabChange = (tab) => {
    if (tab !== activeTab) {
      setMatchingPage(0);
      setAiPage(0);
    }
    setActiveTab(tab);
  };
>>>>>>> develop

  const handleDateChange = (newDate) => {
    setDate(newDate);
    // 날짜가 바뀌면 페이지도 초기화
    setMatchingPage(0);
    setAiPage(0);
  };

  // 처음 페이지 로드시에 실행
  useEffect(() => {
    handleMonthChange({ activeStartDate: date });
  }, [activeTab]);

  const handleMonthChange = async (newDate) => {
    try {
      setLoading(true);
      const formattedDate = formatDate(newDate.activeStartDate);

      // 매칭 페이지인 경우
      if (activeTab === "matching") {
        const response = await fetchMatchHistoryByMonth(formattedDate);

        setMarkedDates(response?.data.dates || []);
      } else {
        // AI 페이지인 경우
        const response = await fetchAIHistoryByMonth(formattedDate);

        setMarkedDates(response?.data.dates || []);
      }
    } catch (error) {
      console.error("월별 로딩 실패: ", error);
    } finally {
      setLoading(false);
    }
  };

  // 마커 추가
  const addMarkers = ({ date }) => {
    const formattedTileDate = formatDate(date);
    if (markedDates.includes(formattedTileDate)) {
      return (
        <div className="marker-container">
          <div className="marker-circle"></div>
        </div>
      );
    }
    return null;
  };

  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  const formatDisplayDate = (dateObj) => {
    return dateObj.toLocaleDateString("ko-KR", {
      month: "long",
      day: "numeric",
    });
  };

  // 현재 페이지의 데이터만 가져오기
  const getCurrentPageData = () => {
    const startIndex = currentPage * ITEMS_PER_PAGE;
    return matchRecords.slice(startIndex, startIndex + ITEMS_PER_PAGE);
  };

  const totalPages = Math.ceil(matchRecords.length / ITEMS_PER_PAGE);

  return (
    <div className="record-container">
      <h2 className="record-title">매칭 기록</h2>

      <Calendar
        onChange={handleDateChange}
        value={date}
        className="custom-calendar"
        calendarType="gregory"
        showFixedNumberOfWeeks={true}
        onActiveStartDateChange={handleMonthChange}
        tileContent={addMarkers}
      />

      <p className="selected-date">
        <strong>{formatDisplayDate(date)}</strong>
      </p>

      <div className="record-tabs">
        <button
          className={`tab-button ${activeTab === "matching" ? "active" : ""}`}
          onClick={() => handleTabChange("matching")}
        >
          유저 매칭 기록
        </button>
        <button
          className={`tab-button ${activeTab === "ai" ? "active" : ""}`}
          onClick={() => handleTabChange("ai")}
        >
          AI 대화 기록
        </button>
        <button className="tab-button" onClick={openModal}>
          통계
        </button>
      </div>

      <StatisticsModal isOpen={isModalOpen} onClose={closeModal} date={date} />

      <div className="records-section">
<<<<<<< HEAD
        {/* <h3 className="record-category-title">유저 매칭 기록</h3> */}
        
        {loading ? (
          <p className="no-records">기록을 불러오는 중입니다...</p>
        ) : matchRecords.length > 0 ? (
          <>
            {getCurrentPageData().map((record) => (
              <div key={record.matching_id} className="user-match-card">
                <img
                  className="match-icon"
                  src={getEmoticonByScore(record.ratingScore)}
                  alt="평가 이모지"
                />
                <div className="match-info">
                  <div className="match-info-top">
                    <span className="match-name">{record.partner_name}</span>
                    <img className="siren-icon" src={siren} alt="신고" />
                  </div>
                  <div className="match-info-bottom">
                    <div className="match-tags">
                      <span className="match-mode">#{record.mode_name.replace(' 모드', '')}</span>
                    </div>
                    <span className="match-time">
                      {new Date(record.matching_time).toLocaleTimeString('ko-KR', {
                        hour: '2-digit',
                        minute: '2-digit',
                        hour12: true
                      })}
                    </span>
                  </div>
                </div>
              </div>
            ))}
            {totalPages > 1 && (
              <div className="pagination">
                <button 
                  onClick={() => setCurrentPage(prev => Math.max(0, prev - 1))}
                  disabled={currentPage === 0}
                >
                  이전
                </button>
                <span>{currentPage + 1} / {totalPages}</span>
                <button 
                  onClick={() => setCurrentPage(prev => Math.min(totalPages - 1, prev + 1))}
                  disabled={currentPage === totalPages - 1}
                >
                  다음
                </button>
              </div>
            )}
          </>
=======
        {activeTab === "matching" ? (
          <UserMatchRecords
            records={matchRecords}
            loading={loading}
            currentPage={matchingPage} // 부모의 페이지 state
            totalPages={matchTotalPages} // 부모가 서버에서 받은 전체 페이지 수
            onPageChange={setMatchingPage} // 부모 state 업데이트 함수
          />
>>>>>>> develop
        ) : (
          <AITalkRecords
            records={aiRecords}
            loading={loading}
            currentPage={aiPage}
            totalPages={aiTotalPages}
            onPageChange={setAiPage}
          />
        )}
      </div>

      <NavigationBar />
    </div>
  );
}

export default IntegratedRecord;