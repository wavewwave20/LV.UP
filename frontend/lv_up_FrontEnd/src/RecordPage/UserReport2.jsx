import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom"; // 🚀 useLocation 추가
import { fetchReportOptions, reportMatching } from "../api/SmalltalkAPI";

import "./UserReport2.css";
import not_checked from "../assets/imageFile/not_checked_box.png";
import checked from "../assets/imageFile/check.png";

function UserReport2() {
  const location = useLocation();
  const navigate = useNavigate();
  const matchingId = location.state?.matchingId; // 🚀 state에서 matchingId 가져오기

  // 신고 옵션 리스트
  const [reasons, setReasons] = useState([]);
  const [selectedReasonIds, setSelectedReasonIds] = useState([]);
  const [userDescription, setUserDescription] = useState("");

  // 🚀 1) 신고 옵션 불러오기
  useEffect(() => {
    const getReportOptions = async () => {
      try {
        const response = await fetchReportOptions();
        setReasons(response.data);
      } catch (error) {
        console.error("신고 옵션 불러오기 실패:", error);
      }
    };
    getReportOptions();
  }, []);

  useEffect(() => {
    console.log("🚀 matchingId from location.state:", matchingId);
  }, [matchingId]);

  // 🚀 2) 체크박스 선택/해제
  const toggleReason = (reasonId) => {
    setSelectedReasonIds((prev) => (prev.includes(reasonId) ? prev.filter((r) => r !== reasonId) : [...prev, reasonId]));
  };

  // 🚀 3) 신고 요청 보내기
  const handleComplete = async () => {
    if (!matchingId) {
      alert("매칭 ID가 없습니다. 다시 시도해주세요.");
      return;
    }
    if (selectedReasonIds.length === 0) {
      alert("최소 하나의 신고 사유를 선택해주세요.");
      return;
    }

    try {
      // 콘솔에서 실제 전송할 데이터 형식만 확인
      console.log("🚨 신고 요청 데이터:", {
        matchingId,
        reportTypeIds: selectedReasonIds,
        description: userDescription,
      });

      // reportMatching 함수에 각 인자를 따로 넘긴다!
      await reportMatching(matchingId, selectedReasonIds, userDescription);

      alert("신고가 정상적으로 접수되었습니다.");
      navigate("/main"); // 신고 완료 후 메인 페이지로 이동
    } catch (error) {
      console.error("🚨 신고 요청 실패:", error);
      alert("신고 제출 중 오류가 발생했습니다.");
    }
  };

  const handleBack = () => {
    navigate(-1);
  };

  return (
    <div className="overlay">
      <div className="user-report-popup">
        <div className="how-good-text">
          사용자를 신고하고자 하는 <br /> 사유를 선택해주세요
        </div>

        {/* 🚀 신고 사유 체크박스 */}
        <div className="select-reason-container">
          {reasons.map((item) => (
            <div key={item.report_type_id} className="reason-item" onClick={() => toggleReason(item.report_type_id)}>
              <img src={selectedReasonIds.includes(item.report_type_id) ? checked : not_checked} alt="checkbox" />
              <span className={selectedReasonIds.includes(item.report_type_id) ? "selected-text" : "reason-text"}>{item.description}</span>
            </div>
          ))}
        </div>

        {/* 🚀 신고 내용 입력 */}
        <div className="reason-text-area">
          <p className="reason-item1">신고 사유를 남겨주세요</p>
          <p className="reason-item2">남겨주신 사유는 운영정책에 의해 처리됩니다</p>
          <textarea placeholder="여기에 적어주세요" value={userDescription} onChange={(e) => setUserDescription(e.target.value)} />
        </div>

        {/* 🚀 버튼 */}
        <div className="report-button-container">
          <button className="report-cancel-button" onClick={handleBack}>
            취소
          </button>
          <button className="evaluate-complete-button" onClick={handleComplete}>
            신고하기
          </button>
        </div>
      </div>
    </div>
  );
}

export default UserReport2;
