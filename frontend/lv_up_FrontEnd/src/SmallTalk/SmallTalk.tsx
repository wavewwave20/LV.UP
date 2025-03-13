// src/components/SmallTalk.js
import React, { useState, useEffect, useRef } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { OpenVidu } from "openvidu-browser";
import Join from "./Join";
import RoomComponent from "./RoomComponent";

// API
import {
  requestSmalltalkMatch,
  cancelSmalltalkMatch,
  connectSmalltalkWebSocket,
  endSmalltalkMatch,
} from "../api/SmalltalkAPI";
import { fetchMyTicket, fetchMyCoin } from "../api/UserAPI";

function SmallTalk() {
  const location = useLocation();
  const navigate = useNavigate();

  const { mode, gender } = location.state || {};
  const modeId = mode;
  const target = gender;

  // OpenVidu 관련
  const [session, setSession] = useState(null);
  const [publisher, setPublisher] = useState(null);
  const [subscribers, setSubscribers] = useState([]);

  // 매칭 상태
  const [ticket, setTicket] = useState(null);

  // 내 정보
  const [myCoinCount, setMyCoinCount] = useState(0);
  const [myUserId, setMyUserId] = useState(123); // 예시 유저ID

  // 매칭 UI 제어
  const [isMatching, setIsMatching] = useState(false);
  const [matchResult, setMatchResult] = useState("");
  const [loading, setLoading] = useState(false);

  // 웹소켓 관련
  const [ws, setWs] = useState(null);
  const [pingInterval, setPingInterval] = useState(null);

  // ─────────────────────────────────────────
  // A) 컴포넌트 마운트 시 티켓/코인 조회
  // ─────────────────────────────────────────
  useEffect(() => {
    // 티켓 조회
    fetchMyTicket()
      .then((res) => {
        // 예: res.data = 1
        setTicket(res.data);
      })
      .catch((err) => console.error("티켓 불러오기 실패:", err));

    // 코인 조회
    fetchMyCoin()
      .then((res) => {
        // 예: res.data = 5 (혹은 { coin: 5 }라면 setMyCoinCount(res.data.coin))
        setMyCoinCount(res.data);
      })
      .catch((err) => console.error("코인 불러오기 실패:", err));
  }, []);

  // ─────────────────────────────────────────
  // B) 매칭 요청
  // ─────────────────────────────────────────
  const handleMatchRequest = async () => {
    if (ticket === null) {
      alert("로그인이 필요합니다.");
      return;
    }
    if (ticket === 0) {
      alert("티켓이 부족합니다.");
      return;
    }

    if (isMatching) {
      await handleCancelMatch();
      return;
    }

    setLoading(true);
    setMatchResult(null);

    try {
      // 1) WebSocket 연결
      const newWs = connectSmalltalkWebSocket({
        onOpen: () => console.log("[WebSocket] connected"),
        onMessage: (event) => {
          // console.log("[Server msg]:", event.data);
          let parsedData;
          try {
            parsedData = JSON.parse(event.data);
          } catch (err) {
            console.warn("서버 메시지 JSON 파싱 실패:", err);
            return;
          }

          if (parsedData.type === "MATCH_SESSION_CLOSED") {
            setMatchResult("상대방이 매칭을 종료했습니다.");
          } else if (parsedData.type === "OPENVIDU_SESSION") {
            setMatchResult(`매칭 완료! 세션 ID: ${parsedData.sessionId}`);
            // 2) 세션 참가
            joinSession(parsedData);
          }
        },
        onClose: () => {
          console.log("[WebSocket] disconnected");
        },
        onError: (err) => console.error("[WebSocket] error:", err),
      });
      setWs(newWs);

      // 2) REST API로 매칭 요청
      const response = await requestSmalltalkMatch(modeId, target);
      setMatchResult(response.data);
      // console.log("매칭 요청 응답:", response.data);

      setIsMatching(true);
    } catch (error) {
      setMatchResult("❌ 매칭 요청 실패");
      console.error("매칭 요청 오류:", error);
    } finally {
      setLoading(false);
    }
  };

  // ─────────────────────────────────────────
  // C) 매칭 취소
  // ─────────────────────────────────────────
  const handleCancelMatch = async () => {
    try {
      setLoading(true);
      setMatchResult("매칭 취소 중...");
      await cancelSmalltalkMatch();

      // WebSocket 정리
      if (ws) {
        ws.close();
        setWs(null);
      }
      if (pingInterval) {
        clearInterval(pingInterval);
        setPingInterval(null);
      }
      setIsMatching(false);
      setMatchResult("매칭이 취소되었습니다.");
    } catch (error) {
      console.error("❌ 매칭 취소 실패:", error);
      setMatchResult("❌ 매칭 취소 실패");
    } finally {
      setLoading(false);
    }
  };

  // ─────────────────────────────────────────
  // D) 세션 참가
  // ─────────────────────────────────────────
  const joinSession = async ({
    sessionId,
    token,
    nickname: remoteNickname,
    introduction: remoteIntro,
  }) => {
    try {
      // 1) 세션 초기화
      const ov = new OpenVidu();
      const mySession = ov.initSession();
      setSession(mySession);

      // 2) 세션 connect
      await mySession.connect(token, {
        clientData: JSON.stringify({
          userId: myUserId,
          coinCount: myCoinCount,
          nickname: remoteNickname,
          introduction: remoteIntro,
        }),
      });

      // 3) 퍼블리셔 (아바타용 더미 트랙)
      const dummyCanvas = document.createElement("canvas");
      dummyCanvas.width = 2;
      dummyCanvas.height = 2;
      const ctx = dummyCanvas.getContext("2d");
      if (ctx) {
        ctx.fillStyle = "black";
        ctx.fillRect(0, 0, 2, 2);
      }
      const dummyStream = dummyCanvas.captureStream(5);
      const dummyTrack = dummyStream.getVideoTracks()[0];

      const newPublisher = await ov.initPublisherAsync(undefined, {
        videoSource: dummyTrack,
        audioSource: true,
        publishVideo: true,
        resolution: "900x1200",
        frameRate: 30,
      });
      await mySession.publish(newPublisher);

      setPublisher(newPublisher);
      // console.log("✅ WebRTC 세션 참가 성공:", sessionId);
      setIsMatching(false);
    } catch (error) {
      console.error("❌ WebRTC 세션 참가 실패:", error);
      alert(`WebRTC 참가 실패: ${error.message}`);
    }
  };

  // ─────────────────────────────────────────
  // E) 세션 나가기 (중복 호출 방지)
  // ─────────────────────────────────────────

  // 1) isEnding: 한 번 끝내기 시작하면 더 이상 진행 X
  const isEnding = useRef(false);

  const handleLeaveSession = async () => {
    if (isEnding.current) {
      console.log("이미 세션 종료 중입니다. 중복 호출 방지");
      return;
    }
    isEnding.current = true;

    try {
      await endSmalltalkMatch();
      console.log("✅ 매칭 종료 알림 성공");
    } catch (error) {
      console.error("❌ 매칭 종료 알림 실패:", error);
    }

    if (session) {
      try {
        if (publisher) {
          session.unpublish(publisher);
          if (publisher.stream?.getMediaStream) {
            publisher.stream
              .getMediaStream()
              .getTracks()
              .forEach((t) => t.stop());
          }
        }
        session.disconnect();
        console.log("✅ 세션 disconnected");
      } catch (err) {
        console.error("세션 종료 오류:", err);
      }
    }

    if (pingInterval) {
      clearInterval(pingInterval);
      setPingInterval(null);
    }
    if (ws) {
      ws.close();
      setWs(null);
    }
  };

  return (
    <div style={{ width: "100%", height: "100%" }}>
      {session ? (
        // 매칭 성공 후 (화상/음성) 통화 방
        <RoomComponent
          session={session}
          publisher={publisher}
          subscribers={subscribers}
          setSubscribers={setSubscribers}
          onLeaveSession={handleLeaveSession} // 세션 종료 콜백
          myCoinCount={myCoinCount}
          setMyCoinCount={setMyCoinCount}
        />
      ) : (
        // 매칭 대기 화면
        <Join
          onMatchRequest={handleMatchRequest}
          onCancelMatch={handleCancelMatch}
          loading={loading}
          isMatching={isMatching}
          matchResult={matchResult}
        />
      )}
    </div>
  );
}

export default SmallTalk;
