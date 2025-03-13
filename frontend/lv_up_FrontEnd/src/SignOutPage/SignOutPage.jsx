import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import backButton from "../assets/imageFile/backButton.png";
import ticket from "../assets/imageFile/ticket.png";
import coin from "../assets/imageFile/coin.png";
import not_checked from "../assets/imageFile/not_checked.png";
import checked from "../assets/imageFile/checked.png";
import "./SignOutPage.css";

function SignOutPage() {
  const location = useLocation();
  const navigate = useNavigate();

  // 예시로 5장의 티켓, 3개의 코인을 보유했다고 가정
  const [tickets, setTickets] = useState(5);
  const [coins, setCoins] = useState(3);

  // 탈퇴 사유 목록
  const reasons = ["통화가 부담스러워요", "어플 내에 버그가 많습니다", "악성유저가 많습니다", "더 이상 이 앱 없이도 잘 대화할 수 있어요"];

  // 선택된 사유 상태
  const [selectedReason, setSelectedReason] = useState("");
  const [isAgreed, setIsAgreed] = useState(false); // 동의 상태
  const [showAlert, setShowAlert] = useState(false); // 경고 메시지 상태

  // 동의 체크박스 클릭 핸들러
  const handleAgreementClick = () => {
    setIsAgreed((prevState) => !prevState); // 상태 토글
    setShowAlert(false); // 알람 메시지 숨기기
  };

  // 회원 탈퇴 버튼 클릭 핸들러
  const handleSignOut = () => {
    if (isAgreed) {
      navigate("/"); // "/"로 이동
    } else {
      setShowAlert(true); // 알람 메시지 표시
    }
  };

  return (
    <div>
      {/* 상단 영역 */}
      <div className="custom-top-area">
        <img className="custom-back-button" src={backButton} alt="backbutton" onClick={() => navigate(-1)} />
        <div className="custom-top-title">회원 탈퇴</div>
      </div>

      {/* 본문 영역 */}
      <div className="main-content">
        {/* 잔여 코인 / 티켓 */}
        <div className="balances">
          <p className="really-exit">정말 탈퇴하시나요?</p>
          <div className="coins">
            <img src={coin} alt="coin" />
            <p>잔여 코인 : {coins} COIN</p>
          </div>
          <div className="tickets">
            <img src={ticket} alt="ticket" />
            <p>잔여 티켓 : {tickets} 장</p>
          </div>
        </div>

        {/* 탈퇴 사유 */}
        <div className="sign-out-reason">
          <p className="select-reason">탈퇴사유 선택</p>
          <div className="reason-list">
            {reasons.map((reason) => (
              <div key={reason} className={`signout-reason-item ${selectedReason === reason ? "selected" : ""}`} onClick={() => setSelectedReason(reason)}>
                {reason}
              </div>
            ))}
          </div>
          <div className="reason-description_signout">
            회원 탈퇴 시 계정 정보 및 보유중인 재화와 대화 기록이 모두 삭제되어 복구가 불가능해요. 
          </div>
        </div>

        {/* 주의사항 동의 */}
        <div className="agreement" onClick={handleAgreementClick}>
          <img src={isAgreed ? checked : not_checked} alt={isAgreed ? "checked" : "not_checked"} />
          <p>위 주의사항을 모두 숙지했고, 탈퇴에 동의합니다.</p>
        </div>

        {/* 경고 메시지 */}
        {showAlert && <p className="alert-message">탈퇴에 동의해야 진행할 수 있습니다.</p>}

        {/* 선택 버튼들 */}
        <div className="select-sign-out">
          <div className="select-no" onClick={() => navigate(-1)}>
            더쓸래요
          </div>
          <div className="select-yes" onClick={handleSignOut}>
            진짜 안녕
          </div>
        </div>
      </div>
    </div>
  );
}

export default SignOutPage;
