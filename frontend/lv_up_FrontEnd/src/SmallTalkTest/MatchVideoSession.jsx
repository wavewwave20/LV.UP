import React, { useState, useEffect } from "react";
import { OpenVidu } from "openvidu-browser";
import axios from "axios";
import SockJS from 'sockjs-client';

const OPENVIDU_SERVER_URL = "YOUR_OPENVIDU_URL";
const OPENVIDU_SECRET = "Alltogether33_";

const MatchVideoSession = () => {
    const [matchResult, setMatchResult] = useState(null);
    const [loading, setLoading] = useState(false);
    const [session, setSession] = useState(null);
    const [publisher, setPublisher] = useState(null);
    const [subscribers, setSubscribers] = useState([]);
    const [sessionId, setSessionId] = useState("");
    const [username, setUsername] = useState("User" + Math.floor(Math.random() * 100));
    const [isMatching, setIsMatching] = useState(false);
    const [socket, setSocket] = useState(null);

    const API_URL = import.meta.env.VITE_API_URL;
    const WS_URL = 'YOUR_WEBSOKET_URL';  // WebSocket URL

    useEffect(() => {
        return () => {
            if (socket) {
                socket.close();
            }
            if (session) {
                session.disconnect();
            }
        };
    }, [socket, session]);

    useEffect(() => {
        return () => {
            if (publisher) {
                publisher.release();
            }
        };
    }, [publisher]);

    useEffect(() => {
        if (publisher) {
            const videoElement = document.getElementById("my-video");
            if (videoElement) {
                publisher.addVideoElement(videoElement);
            }
        }
    }, [publisher]);

    const getSession = async (sessionId) => {
        try {
            let response = await fetch(`${OPENVIDU_SERVER_URL}/openvidu/api/sessions/${sessionId}`, {
                method: "GET",
                headers: { "Authorization": "Basic " + btoa("OPENVIDUAPP:" + OPENVIDU_SECRET) },
            });

            if (response.ok) {
                console.log("✅ 기존 세션이 존재합니다:", sessionId);
                return sessionId;
            }

        } catch (error) {
            console.error("❌ 세션 확인/생성 실패:", error);
            alert(`세션 확인/생성 실패: ${error.message}`);
        }
    };

    const createToken = async (sessionId) => {
        if (!sessionId) return alert("세션 ID가 없습니다.");

        try {
            const response = await fetch(`${OPENVIDU_SERVER_URL}/openvidu/api/sessions/${sessionId}/connection`, {
                method: "POST",
                headers: {
                    "Authorization": "Basic " + btoa("OPENVIDUAPP:" + OPENVIDU_SECRET),
                    "Content-Type": "application/json",
                },
            });

            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

            const jsonResponse = await response.json();
            console.log("✅ 생성된 토큰:", jsonResponse.token);
            return jsonResponse.token;
        } catch (error) {
            console.error("❌ 토큰 생성 실패:", error);
            alert(`토큰 생성 실패: ${error.message}`);
        }
    };

    const joinSession = async (sessionData) => {
        try {
            const sessionId = sessionData.sessionId;
            setSessionId(sessionId);

            const ov = new OpenVidu();
            const newSession = ov.initSession();

            newSession.on("streamCreated", (event) => {
                const subscriber = newSession.subscribe(event.stream, undefined);
                setSubscribers((prev) => [...prev, subscriber]);
            });

            newSession.on("streamDestroyed", (event) => {
                setSubscribers((prev) => prev.filter((sub) => sub !== event.stream.streamManager));
            });

            setSession(newSession);

            const id = await getSession(sessionId);
            const token = await createToken(id);
            if (!token) return;

            await newSession.connect(token, { clientData: username });

            const newPublisher = await ov.initPublisherAsync(undefined, {
                audioSource: true,
                videoSource: true,
                mirror: true,
            });

            setPublisher(newPublisher);
            newSession.publish(newPublisher);
        } catch (error) {
            console.error("❌ WebRTC 세션 참가 실패:", error);
            alert(`WebRTC 참가 실패: ${error.message}`);
        }
    };

    const connectWebSocket = () => {
        // 쿠키에서 토큰 가져오기
        const getCookie = (name) => {
            const value = `; ${document.cookie}`;
            const parts = value.split(`; ${name}=`);
            if (parts.length === 2) return parts.pop().split(';').shift();
        };
        
        const userToken = getCookie('token');
        const yourJwtToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3Mzk4NjE4NjAsInJvbCI6MCwibmlkIjoiaG1VR3JJTk9OWnBJcWtubUhxVU5NIn0.GSEOM3o1-q5NxwqbJV32z1Fu3Ed4DgJ_5DDenBS2px2XEG6pAUM6GYeDNXPHjunvfr86PFWVQu7VLVmb8eO-dg";
        const ws = new WebSocket(`${WS_URL}/match?token=${yourJwtToken}`);  // URL 파라미터로 토큰 전달
        const newWs = new WebSocket(`wss://YOUR_WEBSOKET_URL/ws/match?token=${yourJwtToken}`);


        ws.onopen = () => {
            console.log("✅ WebSocket 연결 성공");
            setSocket(ws);

            // 매칭 요청 메시지 전송
            ws.send(JSON.stringify({
                type: 'MATCH',
                message: "매칭 요청"
            }));
        };

        ws.onmessage = (event) => {
            console.log("✅ 메시지 수신:", event.data);
            try {
                const sessionData = JSON.parse(event.data);
                joinSession(sessionData);
            } catch (error) {
                console.error("메시지 파싱 오류:", error);
            }
        };

        ws.onerror = (error) => {
            console.error("WebSocket 에러:", error);
            handleDisconnect();
        };

        ws.onclose = () => {
            console.log("WebSocket 연결 종료");
            setSocket(null);
            setIsMatching(false);
        };

        return ws;
    };

    const handleDisconnect = () => {
        if (socket) {
            socket.close();
            setSocket(null);
        }
        setIsMatching(false);
    };

    const handleMatchRequest = async () => {
        setLoading(true);
        setMatchResult(null);
        setIsMatching(true);

        try {
            const response = await axios.post(
                `${API_URL}/smalltalk/user/match`,
                null,
                { 
                    params: { 
                        modeId: 5,
                        target: 'A'
                    },
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`
                    }
                }
            );
            
            if (response.data) {
                setMatchResult(response.data);
                handleDisconnect();
                connectWebSocket();
            }
        } catch (error) {
            console.error("매칭 요청 오류:", error);
            setMatchResult("❌ 매칭 요청 실패");
            setIsMatching(false);
            handleDisconnect();
        }

        setLoading(false);
    };

    const handleMatchCancel = async () => {
        try {
            await axios.post(`${API_URL}/smalltalk/user/match/cancel`);
            setIsMatching(false);
            setMatchResult("매칭이 취소되었습니다.");
            
            // WebSocket 연결 해제
            handleDisconnect();
        } catch (error) {
            console.error("매칭 취소 오류:", error);
            alert("매칭 취소에 실패했습니다.");
        }
    };

    return (
        <div style={{ maxWidth: "400px", margin: "50px auto", textAlign: "center" }}>
            <h2>Match & Video Session</h2>
            <button
                onClick={isMatching ? handleMatchCancel : handleMatchRequest}
                disabled={loading}
                style={{
                    width: "100%",
                    padding: "10px",
                    backgroundColor: isMatching ? "#dc3545" : "#007bff",
                    color: "white",
                    border: "none",
                    cursor: "pointer",
                }}
            >
                {loading ? "매칭 요청 중..." : (isMatching ? "매칭 요청 취소" : "매칭 요청")}
            </button>
            {matchResult && (
                <p style={{ marginTop: "20px", fontWeight: "bold" }}>{matchResult}</p>
            )}

            <div>
                <h2>내 비디오</h2>
                <video id="my-video" autoPlay />
            </div>

            <div>
                <h2>참여자</h2>
                {subscribers.map((sub, index) => (
                    <video key={index} autoPlay ref={(el) => el && sub.addVideoElement(el)} />
                ))}
            </div>
        </div>
    );
};

export default MatchVideoSession;
