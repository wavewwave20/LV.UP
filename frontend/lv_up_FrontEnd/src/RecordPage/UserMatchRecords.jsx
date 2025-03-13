import React from "react";
import { useNavigate } from "react-router-dom";
import smile from "../assets/imageFile/smile.png";
import smiling from "../assets/imageFile/smiling.png";
import frown from "../assets/imageFile/frown.png";
import siren from "../assets/imageFile/history_siren.png";
import { Pagination } from "../RecordPage/Pagination";

const UserMatchRecords = ({ 
  records,          // 서버에서 받아온 해당 페이지 데이터만
  loading,          // 로딩 상태
  currentPage,      // 현재 페이지 (부모 state)
  totalPages,       // 전체 페이지 수 (부모 state)
  onPageChange,     // 페이지 변경 함수 (부모의 setState)
}) => {
  const navigate = useNavigate();

  if (loading) {
    return <p className="no-records">기록을 불러오는 중입니다...</p>;
  }

  if (!records || records.length === 0) {
    return <p className="no-records">해당 날짜의 유저 매칭 기록이 없습니다.</p>;
  }

  const handleCardClick = (matchingId) => {
    navigate(`/user-feedback/${matchingId}`);
  };

  const handleReportClick = (e, matchInfo) => {
    e.stopPropagation();
    navigate("/user-report-endmatch", {
      state: { matchingId: matchInfo.matching_id },
    });
  };

  const getEmoticonByScore = (score) => {
    if (score === 100) return smile;
    if (score === 50) return smiling;
    return frown;
  };

  return (
    <>
      {records.map((record) => (
        <div
          key={record.matching_id}
          className="user-match-card"
          onClick={() => handleCardClick(record.matching_id)}
        >
          <img
            className="match-icon"
            src={getEmoticonByScore(record.rating_score)}
            alt="평가 이모지"
          />
          <div className="match-info">
            <div className="match-info-top">
              <span className="match-name">{record.partner_name}</span>
              <span className="match-time">
                {new Date(record.start_at).toLocaleTimeString("ko-KR", {
                  hour: "2-digit",
                  minute: "2-digit",
                  hour12: true,
                })}
              </span>
            </div>
            <div className="match-info-bottom">
              <div className="match-tags">
                <span className="match-mode">
                  #{record.mode_name.replace(" 모드", "")}
                </span>
              </div>
              {record.reported === 0 && (
                <img
                  className="siren-icon"
                  src={siren}
                  alt="신고"
                  onClick={(e) => handleReportClick(e, record)}
                />
              )}
            </div>
          </div>
        </div>
      ))}
      {/* Pagination: 서버에서 받은 totalPages와 현재 currentPage, onPageChange 사용 */}
      <Pagination
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={onPageChange}
      />
    </>
  );
};

export default UserMatchRecords;