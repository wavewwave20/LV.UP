import React, { useEffect, useState } from "react";
<<<<<<< HEAD
import { useLocation, useNavigate } from "react-router-dom";
import { Session, Publisher, StreamManager, Subscriber } from "openvidu-browser";
import "./RoomComponent.css";
import AvatarCanvas from "./AvatarCanvas";
import TimeExtension from "./TimeExtension";

=======
import "./RoomComponent.css";
import { useLocation, useNavigate } from "react-router-dom";
import ClipLoader from "react-spinners/ClipLoader";

// 하단 아이콘
>>>>>>> develop
import siren from "../assets/imageFile/siren.png";
import mic_on from "../assets/imageFile/mic_on.png";
import mic_off from "../assets/imageFile/mic_off.png";
import lightbulb from "../assets/imageFile/lightbulb.png";

<<<<<<< HEAD
type RoomComponentProps = {
  session: Session;
  publisher: Publisher | null;
  subscribers: StreamManager[];
  onLeaveSession: (reason?: "report") => void;
};
=======
// 컴포넌트
import AvatarCanvas from "./AvatarCanvas";
import TimeExtension from "./TimeExtension";
import UserReport from "./UserReport";

// API
import { fetchSmalltalkTopic, extendSmalltalkMatch } from "../api/SmalltalkAPI";
import { getSelectedAvatar } from "../api/UserAPI";

import { toast } from "react-toastify";
>>>>>>> develop

function RoomComponent({
  session,
  publisher,
  subscribers,
  onLeaveSession,
<<<<<<< HEAD
}: RoomComponentProps) {
=======
  myCoinCount,
  setMyCoinCount,
}) {
>>>>>>> develop
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
<<<<<<< HEAD
  // beginner 모드면 420초(7분), 아니면 300초(5분)
=======

>>>>>>> develop
  const modeParam = queryParams.get("mode") || "free";
  const initialTime = modeParam === "beginner" ? 420 : 15;

<<<<<<< HEAD
  const navigate = useNavigate();

  // 타이머
  const [timer, setTimer] = useState(initialTime);
  // 팝업/스크립트 창 등 상태
  const [isSirenOpen, setIsSirenOpen] = useState(false);
  const [isScriptOpen, setIsScriptOpen] = useState(false);
=======
  // 1) 타이머
  const [sessionStartTime, setSessionStartTime] = useState(null);
  const [timer, setTimer] = useState(initialTime);

  // 2) 연장 관련
  const [extensionStartTime, setExtensionStartTime] = useState(null);
>>>>>>> develop
  const [showExtensionPopup, setShowExtensionPopup] = useState(false);
  const [isMuted, setIsMuted] = useState(false);

<<<<<<< HEAD
  // 연장 결정
  const [myExtensionDecision, setMyExtensionDecision] = useState<string | null>(
    null
  );
  const [otherExtensionDecision, setOtherExtensionDecision] = useState<
    string | null
  >(null);

  // 상대방이 나갔는지 여부
  const [otherUserLeft, setOtherUserLeft] = useState(false);

  // 상대방(첫 번째 subscriber) 정보 (닉네임, 소개)
  const [otherUserInfo, setOtherUserInfo] = useState<{
    nickname: string;
    introduction: string;
  } | null>(null);

  // replaceTrack 중복 실행 방지
  const [didReplace, setDidReplace] = useState(false);

  /**
   * 아바타 캔버스 준비되면 비디오 트랙 교체
   */
  const handleAvatarReady = (stream: MediaStream) => {
=======
  // 3) 오디오 / 스크립트 / 상대방 이탈 여부
  const [isMuted, setIsMuted] = useState(false);
  const [isSirenOpen, setIsSirenOpen] = useState(false);
  const [isScriptOpen, setIsScriptOpen] = useState(false);
  const [otherUserLeft, setOtherUserLeft] = useState(false);

  // 4) 상대방 정보
  const [otherUserInfo, setOtherUserInfo] = useState({
    nickname: "",
    introduction: "",
  });
  const [otherCoinCount, setOtherCoinCount] = useState(0);

  // 5) 내 아바타 트랙 교체 여부
  const [didReplace, setDidReplace] = useState(false);

  // 6) 스크립트(토픽)
  const [displayedTopics, setDisplayedTopics] = useState([]);
  const [currentTopic, setCurrentTopic] = useState(null);

  // 7) 연장 결정
  const [myDecision, setMyDecision] = useState(null);
  const [otherDecision, setOtherDecision] = useState(null);
  const [myYesTimestamp, setMyYesTimestamp] = useState(null);
  const [otherYesTimestamp, setOtherYesTimestamp] = useState(null);
  const [otherDidUseCoin, setOtherDidUseCoin] = useState(false);
  const [opponentExtensionMessage, setOpponentExtensionMessage] = useState("");

  // 8) 내 아바타
  const [selectedAvatar, setSelectedAvatar] = useState(null);
  const [selectedBackground, setSelectedBackground] = useState(null);

  // "첫 메시지" 한 번만 전송, 양쪽 동기화
  const [messageToshowCount, setMessageToshowCount] = useState(false);

  // ─────────────────────────────────────────
  // A) 내 아바타 정보 로드
  // ─────────────────────────────────────────
  useEffect(() => {
    getSelectedAvatar()
      .then(({ data }) => {
        if (data && typeof data.avatarId === "number") {
          setSelectedAvatar(data.avatarId);
          setSelectedBackground(data.avatarId);
        }
      })
      .catch((err) => {
        console.error("아바타 정보 로드 실패:", err);
      });
  }, []);

  // ─────────────────────────────────────────
  // B) 아바타 캔버스 준비되면 replaceTrack
  // ─────────────────────────────────────────
  const handleAvatarReady = (stream) => {
>>>>>>> develop
    if (!publisher || !publisher.stream) return;
    if (didReplace) return;

    const videoTrack = stream.getVideoTracks()[0];
    if (!videoTrack) return;

    publisher
      .replaceTrack(videoTrack, true)
      .then(() => {
<<<<<<< HEAD
=======
        // console.log("아바타 비디오 트랙 replace 성공");
>>>>>>> develop
        setDidReplace(true);
      })
      .catch((err) => {
        console.error("replaceTrack 실패:", err);
      });
  };

<<<<<<< HEAD
  /**
   * 타이머 동작 (1초씩 감소)
   */
  useEffect(() => {
    if (timer <= 0 || otherUserLeft) return;
=======
  // ─────────────────────────────────────────
  // C) 타이머 / extension
  // ─────────────────────────────────────────
  useEffect(() => {
    if (!sessionStartTime || otherUserLeft) return;

>>>>>>> develop
    const interval = setInterval(() => {
      const elapsed = Math.floor((Date.now() - sessionStartTime) / 1000);
      const remain = initialTime - elapsed;
      if (remain <= 0) {
        setTimer(0);
        clearInterval(interval);

        // 양쪽 코인 0 => 바로 종료
        if (myCoinCount === 0 && otherCoinCount === 0) {
          onLeaveSession();
          navigate("/evaluate-user");
        } else {
          // 연장 팝업 열기
          setShowExtensionPopup(true);
          const now = Date.now();
          session
            .signal({ type: "extensionStart", data: String(now) })
            .then(() => {
              setExtensionStartTime(now);
            })
            .catch((err) => {
              console.error("extensionStart 시그널 전송 실패:", err);
            });
          // 음소거
          if (!isMuted) {
            setIsMuted(true);
            publisher?.publishAudio(false);
          }
        }
      } else {
        setTimer(remain);
      }
    }, 1000);
    return () => clearInterval(interval);
  }, [
    sessionStartTime,
    initialTime,
    session,
    isMuted,
    publisher,
    myCoinCount,
    otherCoinCount,
    onLeaveSession,
    navigate,
    otherUserLeft,
  ]);

<<<<<<< HEAD
  /**
   * 타이머가 0이 되는 시점 => 연장 팝업 표시
   * 동시에 마이크 OFF
   */
  useEffect(() => {
    if (timer === 0 && !otherUserLeft) {
      setShowExtensionPopup(true);
      if (!isMuted) {
        setIsMuted(true);
        if (publisher) {
          publisher.publishAudio(false);
        }
      }
=======
  // 내가 처음 publisher가 있을 때 → startTimer 시그널
  useEffect(() => {
    if (!session || !publisher) return;
    if (!sessionStartTime) {
      const startTime = Date.now();
      session
        .signal({ type: "startTimer", data: String(startTime) })
        .then(() => {
          setSessionStartTime(startTime);
        })
        .catch((err) => {
          console.error("startTimer 시그널 전송 실패:", err);
        });
>>>>>>> develop
    }
  }, [session, publisher, sessionStartTime]);

<<<<<<< HEAD
  /**
   * OpenVidu 이벤트 등록
   * - streamCreated: 상대방 영상 들어올 때
   * - streamDestroyed: 상대방 나갈 때
   * - signal: extensionDecision, scriptOpen, userLeft, etc.
   */
=======
  // 상대방 startTimer 수신
  useEffect(() => {
    if (!session) return;
    const handleStartTimer = (event) => {
      const startTime = parseInt(event.data, 10);
      setSessionStartTime(startTime);
    };
    session.on("signal:startTimer", handleStartTimer);
    return () => session.off("signal:startTimer", handleStartTimer);
  }, [session]);

  // 상대방 extensionStart 수신
  useEffect(() => {
    if (!session) return;
    const handleExtensionStart = (event) => {
      const extTime = parseInt(event.data, 10);
      setExtensionStartTime(extTime);
      setShowExtensionPopup(true);
    };
    session.on("signal:extensionStart", handleExtensionStart);
    return () => session.off("signal:extensionStart", handleExtensionStart);
  }, [session]);

  // ─────────────────────────────────────────
  // D) OpenVidu 이벤트 등록
  // ─────────────────────────────────────────
>>>>>>> develop
  useEffect(() => {
    if (!session) return;

    // 새 구독자 스트림
    const onStreamCreated = (event: any) => {
      const subscriber = session.subscribe(event.stream, undefined);

      // 상대방 정보 파싱
      try {
<<<<<<< HEAD
        const dataObj = JSON.parse(event.stream.connection.data);
=======
        const dataStr = event.stream.connection.data;
        const dataObj = JSON.parse(dataStr);
>>>>>>> develop
        if (dataObj.clientData) {
          const userData = JSON.parse(dataObj.clientData);
          if (typeof userData.coinCount === "number") {
            setOtherCoinCount(userData.coinCount);
          }
          setOtherUserInfo({
            nickname: userData.nickname || "",
            introduction: userData.introduction || "",
          });
        }
      } catch (error) {
        console.error("상대방 정보 파싱 실패:", error);
      }

      setSubscribers((prev) => [...prev, subscriber]);
    };

    // 구독자 제거
    const onStreamDestroyed = (event: any) => {
      setSubscribers((prev) =>
        prev.filter((sub) => sub !== event.stream.streamManager)
      );
    };

<<<<<<< HEAD
    // 연장 결정 시그널
    const onSignalExtension = (event: any) => {
      // 내가 보낸 시그널은 무시
      if (event.from.connectionId === session.connection.connectionId) return;
      setOtherExtensionDecision(event.data);
    };

    // 스크립트 요청 시그널
    const onSignalScriptOpen = () => {
=======
    // 스크립트(토픽) 열기 시그널
    const onSignalScriptOpen = (event) => {
      const topic = event.data || "토픽이 없습니다.";
      setCurrentTopic(topic);
>>>>>>> develop
      setIsScriptOpen(true);
      setTimeout(() => setIsScriptOpen(false), 10000);
    };

<<<<<<< HEAD
    // 강제 종료 시그널
    const onSignalUserLeft = (event: any) => {
=======
    // 상대방 유저 나감
    const onSignalUserLeft = (event) => {
>>>>>>> develop
      if (event.from.connectionId === session.connection.connectionId) return;
      setOtherUserLeft(true);
      setShowExtensionPopup(false);
    };

<<<<<<< HEAD
    // 연결 종료
    const onConnectionDestroyed = (event: any) => {
=======
    // 연결 끊어짐
    const onConnectionDestroyed = (event) => {
>>>>>>> develop
      if (event.connection.connectionId !== session.connection.connectionId) {
        setOtherUserLeft(true);
        setShowExtensionPopup(false);
      }
    };

    // firstMsgSent / resetMsgCount
    const handleFirstMsgSent = (event) => {
      if (event.from.connectionId === session.connection.connectionId) return;
      setMessageToshowCount(true);
    };
    const handleResetMsgCount = (event) => {
      if (event.from.connectionId === session.connection.connectionId) return;
      setMessageToshowCount(false);
    };

    session.on("streamCreated", onStreamCreated);
    session.on("streamDestroyed", onStreamDestroyed);
    session.on("signal:scriptOpen", onSignalScriptOpen);
    session.on("signal:userLeft", onSignalUserLeft);
    session.on("connectionDestroyed", onConnectionDestroyed);

<<<<<<< HEAD
    // 정리 함수
=======
    session.on("signal:firstMsgSent", handleFirstMsgSent);
    session.on("signal:resetMsgCount", handleResetMsgCount);

>>>>>>> develop
    return () => {
      session.off("streamCreated", onStreamCreated);
      session.off("streamDestroyed", onStreamDestroyed);
      session.off("signal:scriptOpen", onSignalScriptOpen);
      session.off("signal:userLeft", onSignalUserLeft);
      session.off("connectionDestroyed", onConnectionDestroyed);

      session.off("signal:firstMsgSent", handleFirstMsgSent);
      session.off("signal:resetMsgCount", handleResetMsgCount);
    };
  }, [session, subscribers, setSubscribers]);

<<<<<<< HEAD
  /**
   * 연장 결정 처리 (양쪽 모두 yes => 연장, 아니면 종료)
   */
  useEffect(() => {
    if (!myExtensionDecision || !otherExtensionDecision) return;

    // 둘 다 yes => 연장
    if (myExtensionDecision === "yes" && otherExtensionDecision === "yes") {
      setTimer(initialTime);
      setShowExtensionPopup(false);

      // 마이크 다시 켜기
      setIsMuted(false);
      if (publisher) {
        publisher.publishAudio(true);
      }
=======
  // ─────────────────────────────────────────
  // E) TimeExtension 콜백
  // ─────────────────────────────────────────
  const handleTimeExtensionDecision = (decision, { didUseCoin, message }) => {
    setMyDecision(decision);

    if (decision === "yes") {
      if (!messageToshowCount) {
        setMessageToshowCount(true);
        session
          .signal({ type: "firstMsgSent", data: "" })
          .catch((err) => console.error("firstMsgSent 시그널 실패", err));
      }
      const yesTime = Date.now();
      setMyYesTimestamp(yesTime);

      session
        .signal({
          type: "extensionDecision",
          data: JSON.stringify({
            decision: "yes",
            didUseCoin,
            yesTime,
            message,
          }),
        })
        .catch((err) => {
          console.error("extensionDecision 시그널 전송 실패:", err);
        });
>>>>>>> develop
    } else {
      // NO
      setMyYesTimestamp(null);
      session
        .signal({
          type: "extensionDecision",
          data: JSON.stringify({
            decision: "no",
            didUseCoin: false,
          }),
        })
        .catch((err) => {
          console.error("extensionDecision 시그널 전송 실패:", err);
        });
    }
  };

  // ─────────────────────────────────────────
  // F) 상대방 extensionDecision 수신
  // ─────────────────────────────────────────
  useEffect(() => {
    if (!session) return;

    const handleExtensionSignal = (event) => {
      if (event.from.connectionId === session.connection.connectionId) return;
      try {
        const parsed = JSON.parse(event.data);
        const { decision, didUseCoin, yesTime, message } = parsed;

        if (decision === "yes") {
          setOtherDecision("yes");
          setOtherYesTimestamp(yesTime || null);
          setOtherDidUseCoin(!!didUseCoin);

          if (message) {
            setOpponentExtensionMessage(message);
          }
        } else {
          setOtherDecision("no");
          setOtherYesTimestamp(null);
          setOpponentExtensionMessage("");
          setOtherDidUseCoin(false);
        }
      } catch (err) {
        console.error("extensionDecision 시그널 파싱 실패:", err);
      }
    };

    session.on("signal:extensionDecision", handleExtensionSignal);
    return () => {
      session.off("signal:extensionDecision", handleExtensionSignal);
    };
  }, [session]);

  // (추가) 상대방이 보낸 팝업 메시지 토스트로
  useEffect(() => {
    if (opponentExtensionMessage) {
      toast.info(opponentExtensionMessage, {
        position: "top-center",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        theme: "colored",
      });
    }
  }, [opponentExtensionMessage]);

  // ─────────────────────────────────────────
  // G) 양쪽 모두 결정되었을 때 로직
  // ─────────────────────────────────────────
  useEffect(() => {
    if (!myDecision || !otherDecision) return;

    // (1) 둘 다 YES
    if (myDecision === "yes" && otherDecision === "yes") {
      if (myYesTimestamp && otherYesTimestamp) {
        // 코인 차감 로직
        if (myYesTimestamp < otherYesTimestamp) {
          // 내가 먼저 YES 누름
          if (myCoinCount > 0) {
            setMyCoinCount(myCoinCount - 1);
            extendSmalltalkMatch().catch((err) => {
              console.error("extendSmalltalkMatch() 에러:", err);
            });
          }
        } else {
          // 상대방이 먼저 YES
          if (otherCoinCount === 0) {
            // 상대방 코인이 없으니 내 코인을 차감
            setMyCoinCount(myCoinCount - 1);
            extendSmalltalkMatch().catch((err) => {
              console.error("extendSmalltalkMatch() 에러:", err);
            });
          } else {
            // 상대방 코인 차감
            setOtherCoinCount(otherCoinCount - 1);
            // 여기서 상대방 DB 업데이트 등 로직
          }
        }
      }

      // 타이머 재시작
      const newStart = Date.now();
      session
        .signal({ type: "startTimer", data: String(newStart) })
        .then(() => {
          setSessionStartTime(newStart);
          setTimer(initialTime);
          setShowExtensionPopup(false);

          // 마이크 켜기
          setIsMuted(false);
          publisher?.publishAudio(true);

          // reset messageToshowCount
          setMessageToshowCount(false);
          session
            .signal({ type: "resetMsgCount", data: "" })
            .catch((err) => console.error("resetMsgCount 시그널 실패", err));
        })
        .catch((err) => {
          console.error("연장 startTimer 시그널 실패:", err);
        });
    } else {
      // (2) 한쪽 NO => 종료
      onLeaveSession();
      navigate("/evaluate-user");
    }
<<<<<<< HEAD
    setMyExtensionDecision(null);
    setOtherExtensionDecision(null);
=======

    // 결정값/메시지 초기화
    setMyDecision(null);
    setOtherDecision(null);
    setMyYesTimestamp(null);
    setOtherYesTimestamp(null);
    setOtherDidUseCoin(false);
    setOpponentExtensionMessage("");
>>>>>>> develop
  }, [
    myDecision,
    otherDecision,
    myYesTimestamp,
    otherYesTimestamp,
    myCoinCount,
    otherCoinCount,
    session,
    publisher,
    navigate,
    onLeaveSession,
    initialTime,
    setMyCoinCount,
  ]);

<<<<<<< HEAD
  /**
   * 상대방이 먼저 떠난 경우 => 확인 누르면 메인 이동
   */
  const handleOtherUserLeftConfirm = () => {
    if (publisher && publisher.stream) {
      const tracks = publisher.stream.getMediaStream().getTracks();
      tracks.forEach((track) => track.stop());
    }
    navigate("/main");
  };

  // 마이크 on/off
=======
  // ─────────────────────────────────────────
  // H) 상대방 나감
  // ─────────────────────────────────────────
  const handleOtherUserLeftConfirm = () => {
    onLeaveSession();
    navigate("/main");
  };

  // ─────────────────────────────────────────
  // I) 마이크 on/off
  // ─────────────────────────────────────────
>>>>>>> develop
  const handleMuteToggle = () => {
    if (!publisher) return;
    publisher.publishAudio(isMuted);
    setIsMuted(!isMuted);
  };

<<<<<<< HEAD
  // 신고 버튼 클릭
  const handleSirenClick = () => {
    setIsSirenOpen(true);
  };
  const handleReportAndLeave = async () => {
    if (!session) return;
    // 상대방에게 userLeft 시그널
    await session.signal({ type: "userLeft", data: "" });
    onLeaveSession("report");
  };

  // 스크립트 버튼 클릭
  const handleScriptClick = async () => {
    if (!session) return;
    if (modeParam === "beginner") {
      setIsScriptOpen(!isScriptOpen);
    } else {
      await session.signal({ type: "scriptOpen", data: "" });
      setIsScriptOpen(true);
      setTimeout(() => setIsScriptOpen(false), 10000);
=======
  // ─────────────────────────────────────────
  // J) 신고
  // ─────────────────────────────────────────
  const handleSirenClick = () => {
    setIsSirenOpen(true);
  };

  // ─────────────────────────────────────────
  // K) 스크립트(토픽) 열기
  // ─────────────────────────────────────────
  const handleScriptClick = async () => {
    if (!session) return;
    let selectedTopic = null;
    try {
      const response = await fetchSmalltalkTopic();
      const topics = response.data || [];
      const newTopics = topics.filter((t) => !displayedTopics.includes(t));
      if (newTopics.length > 0) {
        const randomIndex = Math.floor(Math.random() * newTopics.length);
        selectedTopic = newTopics[randomIndex];
        setDisplayedTopics((prev) => [...prev, selectedTopic]);
      } else {
        selectedTopic = "더 이상 표시할 토픽이 없습니다.";
      }
    } catch (err) {
      console.error("토픽 수신 실패:", err);
      selectedTopic = "토픽을 받아오지 못했습니다 :(";
>>>>>>> develop
    }

    setCurrentTopic(selectedTopic);

    try {
      await session.signal({
        type: "scriptOpen",
        data: selectedTopic || "",
      });
    } catch (err) {
      console.error("scriptOpen 시그널 전송 실패:", err);
    }

    setIsScriptOpen(true);
    setTimeout(() => setIsScriptOpen(false), 10000);
  };

<<<<<<< HEAD
  // 연장 팝업 yes/no 결정
  const handleTimeExtensionDecision = async (decision: "yes" | "no") => {
    setMyExtensionDecision(decision);
    if (!session) return;
    await session.signal({
      type: "extensionDecision",
      data: decision,
    });
  };

  // 시간 포맷 (mm:ss)
  const formatTime = (sec: number) => {
=======
  // ─────────────────────────────────────────
  // L) 타이머 표시 포맷
  // ─────────────────────────────────────────
  const formatTime = (sec) => {
>>>>>>> develop
    const m = Math.floor(sec / 60);
    const s = sec % 60;
    return `${String(m).padStart(2, "0")}:${String(s).padStart(2, "0")}`;
  };

<<<<<<< HEAD
  // 첫 구독자(상대방)
  const mainSubscriber = subscribers[0] as Subscriber | undefined;
=======
  const mainSubscriber = subscribers[0] || null;
>>>>>>> develop

  return (
    <div className="room-container">
      {/* 상단 타이머 */}
      <div className="room-top-bar">
        <h2 className="room-timer-text">{formatTime(timer)}</h2>
      </div>

      {/* 상대방 영상 */}
      <div className="main-video-container">
        {mainSubscriber ? (
          <video
            className="main-video"
            ref={(el) => el && mainSubscriber.addVideoElement(el)}
            autoPlay
            playsInline
          />
        ) : (
<<<<<<< HEAD
          <div className="no-user">상대방 없음...</div>
        )}
      </div>

      {/* 상대방 닉네임·소개글 */}
      {otherUserInfo && (
=======
          <div className="no-user">
            <ClipLoader color="#fff" size={100} />
          </div>
        )}
      </div>

      {/* 내 아바타 */}
      <div className="my-video-container">
        {selectedAvatar === null ? (
          <div className="loading-text">아바타 정보를 불러오는 중...</div>
        ) : (
          <AvatarCanvas
            className="my-video"
            onAvatarReady={handleAvatarReady}
            selectedAvatar={selectedAvatar}
            selectedBackground={selectedBackground}
          />
        )}
      </div>

      {/* 상대방 닉네임 / 소개 */}
      {otherUserInfo.nickname && (
>>>>>>> develop
        <div className="user-info-bar">
          <h1>{otherUserInfo.nickname}</h1>
          <p>{otherUserInfo.introduction}</p>
        </div>
      )}

<<<<<<< HEAD
      {/* 내 아바타 비디오 */}
      <div className="my-video-container">
        <AvatarCanvas className="my-video" onAvatarReady={handleAvatarReady} />
      </div>

      {/* 하단 버튼들 */}
      <div className="bottom-bar">
        <button onClick={handleSirenClick} className="bottom-button icon-button">
          <img src={siren} alt="siren" />
        </button>

=======
      {/* 하단 버튼 */}
      <div className="bottom-bar">
        {/* 신고 버튼 */}
        <button
          onClick={handleSirenClick}
          className={`bottom-button icon-button${isSirenOpen ? " active" : ""}`}
        >
          <img src={siren} alt="siren" />
        </button>

        {/* 마이크 버튼 */}
>>>>>>> develop
        <button
          onClick={handleMuteToggle}
          className={`bottom-button icon-button${isMuted ? " active" : ""}`}
        >
          <img src={isMuted ? mic_off : mic_on} alt="mic" />
        </button>

<<<<<<< HEAD
=======
        {/* 스크립트(토픽) 버튼 */}
>>>>>>> develop
        <button
          onClick={handleScriptClick}
          disabled={isScriptOpen}
          className={`bottom-button icon-button${
            isScriptOpen ? " active" : ""
          }`}
        >
          <img src={lightbulb} alt="scripts" />
        </button>
      </div>

      {/* 신고 팝업 */}
      {isSirenOpen && (
<<<<<<< HEAD
        <div className="overlay">
          <div className="siren-popup">
            <p>
              신고하면 통화가 종료됩니다.
              <br />
              계속하시겠습니까?
            </p>
            <button
              style={{ backgroundColor: "red", color: "white", marginRight: 10 }}
              onClick={handleReportAndLeave}
            >
              신고 후 나가기
            </button>
            <button onClick={() => setIsSirenOpen(false)}>취소</button>
          </div>
        </div>
=======
        <UserReport
          onClose={() => setIsSirenOpen(false)}
          onLeaveSession={onLeaveSession}
        />
>>>>>>> develop
      )}

      {/* 스크립트 팝업 */}
      {isScriptOpen && (
        <div className="overlay script-popup-overlay">
          <div className="script-popup">
            {currentTopic ? <p>{currentTopic}</p> : <p>토픽이 없습니다.</p>}
          </div>
        </div>
      )}

      {/* 연장 팝업 */}
      {showExtensionPopup && (
        <TimeExtension
          extensionStartTime={extensionStartTime}
          extensionDuration={30}
          onDecision={handleTimeExtensionDecision}
          opponentDecision={
            otherDecision
              ? { decision: otherDecision, didUseCoin: otherDidUseCoin }
              : null
          }
          opponentMessage={opponentExtensionMessage}
          myCoinCount={myCoinCount}
          messageToshowCount={messageToshowCount}
        />
      )}

      {/* 상대방 나감 → 종료 팝업을 end-popup 형식으로 변경 */}
      {otherUserLeft && (
        <div className="end-popup-overlay">
          <div className="end-popup-content">
            <h2 className="end-chat-title">통화가 종료되었습니다</h2>
            <p className="end-chat-reason">상대방과의 연결이 끊어졌습니다.</p>
            <button
              className="end-chat-button"
              onClick={handleOtherUserLeftConfirm}
            >
              확인
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default RoomComponent;
