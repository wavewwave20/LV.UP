import React, { useEffect, useState } from "react";
import "./TimeExtension.css";

import coin from "../assets/imageFile/coin.png"; // 작은 코인 아이콘
import coin2 from "../assets/imageFile/coin2.png"; // 기본 코인 이미지
import coinUsed from "../assets/imageFile/coinUsed.png"; // 사용 후 코인
import coin_sound from "../assets/soundtrack/coin_sound.mp3"; // 코인 사운드

import { fetchMyCoin } from "../api/UserAPI";
import { toast } from "react-toastify";

function TimeExtension({
  extensionStartTime,
  extensionDuration,
  onDecision, // (decision, { didUseCoin, message })
  opponentDecision, // { decision: 'yes' | 'no', didUseCoin: boolean }
  opponentMessage, // 상대방이 보내온 텍스트 메시지
  myCoinCount,
  messageToshowCount,
}) {
  // 1) 남은 연장 선택 시간
  const [remainingTime, setRemainingTime] = useState(extensionDuration);

  // 2) 내 코인 개수
  const [myCoin, setMyCoin] = useState(0);

  // 3) 내가 YES를 눌렀을 때 코인 사용 여부
  const [myUsedCoin, setMyUsedCoin] = useState(false);
  // 4) 상대방도 YES 시 코인 사용 여부
  const [opponentUsedCoin, setOpponentUsedCoin] = useState(false);

  // 중복 클릭 방지, 내 결정
  const [myLockedIn, setMyLockedIn] = useState(false);
  const [myDecision, setMyDecision] = useState(null);

  // ** 새로 추가: 코인 소리가 이미 재생되었는지 여부 **
  const [coinSoundPlayed, setCoinSoundPlayed] = useState(false);

  // **새로운 상태: 이전 showCoinUsed 값 기억**
  const [prevShowCoinUsed, setPrevShowCoinUsed] = useState(false);

  // A) 마운트 시 내 코인 조회 (or 부모에서 prop으로 받음)
  useEffect(() => {
<<<<<<< HEAD
    const timer = setInterval(() => {
      setRemainingTime((prevTime) => {
        if (prevTime <= 1) {
          clearInterval(timer);
          onDecision("no");
          navigate("/main");
          return 0;
=======
    if (typeof myCoinCount === "number") {
      setMyCoin(myCoinCount);
    } else {
      const loadMyCoin = async () => {
        try {
          const response = await fetchMyCoin();
          setMyCoin(response.data);
        } catch (error) {
          console.error("내 코인 조회 실패:", error);
>>>>>>> develop
        }
      };
      loadMyCoin();
    }
  }, [myCoinCount]);

  // B) 연장 결정 타이머
  useEffect(() => {
    if (!extensionStartTime) return;

    const timerId = setInterval(() => {
      const elapsed = Math.floor((Date.now() - extensionStartTime) / 1000);
      const remain = extensionDuration - elapsed;

      if (remain <= 0) {
        clearInterval(timerId);
        setRemainingTime(0);
        onDecision("no", { didUseCoin: false, message: "" });
      } else {
        setRemainingTime(remain);
      }
    }, 1000);

    return () => clearInterval(timerId);
  }, [extensionStartTime, extensionDuration, onDecision]);

<<<<<<< HEAD
  // X 버튼 (선택 팝업을 강제로 닫고 싶을 때 쓰는 용도)
  const handleClose = () => {
    console.log("TimeExtension 팝업 닫기 버튼 클릭");
    onDecision("no");
    navigate("/main");
  };

  // “더 이야기 할래요” 버튼
=======
  // C) YES 버튼 클릭 시
>>>>>>> develop
  const handleYes = () => {
    if (myLockedIn) return;
    setMyLockedIn(true);
    setMyDecision("yes");

    const hasCoin = myCoin > 0;
    setMyUsedCoin(hasCoin);
    setOpponentUsedCoin(hasCoin);

    let finalMessage = "";
    if (!messageToshowCount) {
      // 코인 여부
      if (hasCoin) {
        finalMessage = "상대방이 대화를 더 하길 원해요!";
      } else {
        finalMessage =
          "상대방이 더 이야기하고 싶어해요!\n계속 대화하시려면 코인 하나가 필요해요.";
      }
      // 토스트
      toast.info("상대방의 대답을 기다리는 중입니다.", {
        position: "top-center",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        theme: "colored",
      });
    }

    onDecision("yes", {
      didUseCoin: hasCoin,
      message: finalMessage,
    });
  };

  // D) NO 버튼 클릭 시
  const handleNo = () => {
<<<<<<< HEAD
    onDecision("no");
    navigate("/main");
=======
    if (myLockedIn) return;
    setMyLockedIn(true);
    setMyDecision("no");
    onDecision("no", { didUseCoin: false, message: "" });
>>>>>>> develop
  };

  // E) 상대방 결정 수신
  useEffect(() => {
    if (!opponentDecision) return;

    if (opponentDecision.decision === "yes") {
      if (opponentDecision.didUseCoin) {
        setMyUsedCoin(true);
        setOpponentUsedCoin(true);
      } else {
        setMyUsedCoin(false);
        setOpponentUsedCoin(false);
      }
    } else {
      setMyUsedCoin(false);
      setOpponentUsedCoin(false);
    }
  }, [opponentDecision]);

  // F) 남은 시간 표시 포맷
  const formattedTime = `00 : ${String(remainingTime).padStart(2, "0")}`;

  // G) 내가 YES 시 안내문 (상태에 따라)
  let decisionMessage = "";
  if (myDecision === "yes") {
    const oppDec = opponentDecision?.decision;
    if (!oppDec) {
      decisionMessage = "상대방의 대답을 기다리고 있어요";
    } else if (oppDec === "yes") {
      decisionMessage = "상대방이 대화를 더 하길 원해요!";
    } else if (oppDec === "no") {
      decisionMessage = "상대방이 대화를 원치 않아요.";
    }
  }

  // showCoinUsed = 코인 사용 여부(myUsedCoin && opponentUsedCoin)
  const showCoinUsed = myUsedCoin && opponentUsedCoin;
  const coinImageToShow = showCoinUsed ? coinUsed : coin2;

  // H) 코인 소리: showCoinUsed가 처음 false→true가 될 때 + 이전에 재생 안 했으면
  useEffect(() => {
    if (!prevShowCoinUsed && showCoinUsed && !coinSoundPlayed) {
      const audio = new Audio(coin_sound);
      audio.play().catch((error) => {
        console.warn("코인 사운드 재생 실패", error);
      });
      setCoinSoundPlayed(true);
    }
    setPrevShowCoinUsed(showCoinUsed);
  }, [showCoinUsed, prevShowCoinUsed, coinSoundPlayed]);

  return (
    <div className="time-extension-container">
      <div className="time-remaining">{formattedTime}</div>

      <div className="notice-end">
        <p className="time-bold-notice">
          통화가 종료되었어요.
          <br />
          조금 더 이야기 할래요?
        </p>
        <p className="time-small-notice">
          상대방도 동의해야 다시 통화가 연결돼요.
        </p>
      </div>

      <div className="coin-image">
        <img
          src={coinImageToShow}
          alt="coin"
          style={{ width: "240px", height: "240px", marginBottom: "20px" }}
        />
      </div>

      <div className="remain-coins">
        <p className="remained-coin-text">현재 보유 코인</p>
        <div className="remained-coin-box">
          <img src={coin} alt="coin" />
          <div>{myCoin}개</div>
        </div>
      </div>

      <div className="select-choose-extend">
        {/* YES 버튼 */}
        <div
          className="positive-extend"
          onClick={handleYes}
          style={{
            opacity: myLockedIn ? 0.5 : 1,
            pointerEvents: myLockedIn ? "none" : "auto",
          }}
        >
          {/* 코인이 1개 이상 있을 때만 아이콘+숫자를 표시 */}
          {myCoin > 0 && (
            <div className="line1">
              <img src={coin} alt="coin" style={{ width: "20px" }} />
              <span>1</span>
            </div>
          )}
          <p>더 이야기 할래요</p>
        </div>

        {/* NO 버튼 */}
        <div
          className="negative-extend"
          onClick={handleNo}
          style={{
            opacity: myLockedIn ? 0.5 : 1,
            pointerEvents: myLockedIn ? "none" : "auto",
          }}
        >
          <p>나중에 할래요</p>
        </div>
      </div>
    </div>
  );
}

export default TimeExtension;
