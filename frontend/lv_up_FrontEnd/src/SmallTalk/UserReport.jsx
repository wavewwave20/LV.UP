import React, { useState, useEffect } from "react";
<<<<<<< HEAD
import { useNavigate, useLocation } from "react-router-dom";
import { fetchReportOptions, postReport } from "../api"; // api.js 파일에서 가져옵니다.

import "./UserReport.css";
=======
import { useNavigate } from "react-router-dom";
import { fetchReportOptions, postReport } from "../api/SmalltalkAPI"; // 실제 API 함수
import "./UserReport.css"; // 아래에 있는 CSS 파일을 import
>>>>>>> develop
import not_checked from "../assets/imageFile/not_checked_box.png";
import checked from "../assets/imageFile/check.png";

/**
 * 신고 팝업을 띄우는 컴포넌트
 *
 * @param {function} onClose        - "취소" 버튼 클릭 시 실행할 함수 (ex. 부모 컴포넌트에서 팝업 닫기)
 * @param {function} onLeaveSession - 신고 완료 시 세션 종료 등 후속 작업 (ex. 메인화면으로 이동 등)
 */
function UserReport({ onClose, onLeaveSession }) {
  const navigate = useNavigate();

  // 서버에서 가져올 신고 옵션 리스트
  // [{ report_type_id: number, description: string }, ...] 형태로 가정
  const [reasons, setReasons] = useState([]);

  // 선택된 신고 타입 ID 배열
  const [selectedReasonIds, setSelectedReasonIds] = useState([]);

  // 신고 내용 직접 입력(텍스트 영역)
  const [userDescription, setUserDescription] = useState("");

  /**
   * 1) 컴포넌트가 처음 마운트될 때 신고 옵션 목록을 서버에서 가져옵니다.
   */
  useEffect(() => {
    const getReportOptions = async () => {
      try {
        const response = await fetchReportOptions();
        // response.data 예: [{ report_type_id: 1, description: "비속어 사용" }, ...]
        setReasons(response.data);
      } catch (error) {
        console.error("신고 옵션을 불러오는 데 실패했습니다: ", error);
      }
    };
    getReportOptions();
  }, []);

  /**
   * 2) 체크박스 클릭 시 선택/해제 처리
   */
  const toggleReason = (reasonId) => {
    setSelectedReasonIds((prev) =>
      prev.includes(reasonId)
        ? prev.filter((r) => r !== reasonId)
        : [...prev, reasonId]
    );
  };

  /**
   * 3) 신고 등록(“신고완료”) 버튼 클릭 시
   */
  const handleComplete = async () => {
    // 신고 사유를 하나도 선택하지 않은 경우
    if (selectedReasonIds.length === 0) {
      alert("최소 하나의 신고 사유를 선택해주세요.");
      return;
    }

    try {
      // 서버에 보낼 데이터 구조 (예시)
      const reportData = {
        reportTypeIds: selectedReasonIds, // 예: [1, 2, 3]
        description: userDescription, // 사용자가 입력한 상세 사유
      };

      // console.log("🚨 신고 내용:", reportData);

      // 실제 신고 등록 API 호출
      await postReport(reportData);
      console.log("🚨 신고가 정상 처리되었습니다.");

      // 신고 완료 후 세션 종료 (부모 콜백 or 바로 라우팅)
      if (onLeaveSession) {
        await onLeaveSession();
      }
      navigate("/main"); // 메인 화면 등 원하는 곳으로 이동
    } catch (error) {
      console.error("신고 등록 실패:", error);
      alert("신고 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
    }
  };

  return (
    <div className="overlay">
      <div className="user-report-popup">
        <div className="how-good-text">
          사용자를 신고하고자 하는 <br /> 사유를 선택해주세요
        </div>

        {/* 체크박스 항목 */}
        <div className="select-reason-container">
          {reasons.map((item) => (
            <div
              key={item.report_type_id}
              className="reason-item"
              onClick={() => toggleReason(item.report_type_id)}
            >
              <img
                src={
                  selectedReasonIds.includes(item.report_type_id)
                    ? checked
                    : not_checked
                }
                alt="checkbox"
              />
              <span
                className={
                  selectedReasonIds.includes(item.report_type_id)
                    ? "selected-text"
                    : "reason-text"
                }
              >
                {item.description}
              </span>
            </div>
          ))}
        </div>

        {/* 텍스트 입력 영역 */}
        <div className="reason-text-area">
          <p className="reason-item1">신고 사유를 남겨주세요</p>
          <p className="reason-item2">
            남겨주신 사유는 운영정책에 의해 처리됩니다
          </p>
          <textarea
            placeholder="여기에 적어주세요"
            value={userDescription}
            onChange={(e) => setUserDescription(e.target.value)}
          />
        </div>

        {/* 버튼들 */}
        <div className="report-button-container">
          <button className="report-cancel-button" onClick={onClose}>
            취소
          </button>
          <button className="evaluate-complete-button" onClick={handleComplete}>
            신고완료
          </button>
        </div>
      </div>
    </div>
  );
}

export default UserReport;
