// AIChatRecorder.jsx
import React, { useState, useRef } from "react";
<<<<<<< HEAD
import mike from "../assets/imageFile/mike.png"; // 마이크 이미지 경로

const AIChatRecorder = ({ scenarioId, personalityId, onTranscription, onResponseReceived }) => {
=======
import ScaleLoader from "react-spinners/ScaleLoader";
import mike from "../assets/imageFile/mike.png";
import Cookies from "js-cookie";

const AIChatRecorder = ({ onTranscription, endFlag, onEndChat }) => {
>>>>>>> develop
  const [isRecording, setIsRecording] = useState(false);
  const [transcription, setTranscription] = useState("");
  const [audioURL, setAudioURL] = useState(null);
  const mediaRecorderRef = useRef(null);
  const audioChunksRef = useRef([]);

  // Whisper API를 호출하여 녹음된 음성의 텍스트를 받아옴
  const uploadToWhisper = async (audioBlob) => {
    const formData = new FormData();
    formData.append("file", audioBlob, "audio.wav");
    formData.append("model", "whisper-1");
    formData.append("language", "ko");

    const response = await fetch("https://api.openai.com/v1/audio/transcriptions", {
      method: "POST",
      headers: {
        Authorization: `Bearer ${import.meta.env.VITE_OPENAI_API_KEY}`,
      },
      body: formData,
    });
    const data = await response.json();
    return data.text;
  };

<<<<<<< HEAD
  // 백엔드 API에 전송하는 함수
  const sendMessageToBackend = async (userAnswer) => {
    const payload = {
      ai_scenario_id: scenarioId,
      ai_personality_id: personalityId,
      user_answer: userAnswer,
    };

    try {
      const response = await fetch("/api/smalltalk/ai/conversation/text", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
      const data = await response.json();
      return data;
    } catch (error) {
      console.error("백엔드 요청 에러:", error);
      return null;
    }
  };

  // 녹음을 시작하는 함수
=======
>>>>>>> develop
  const startRecording = async () => {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
      mediaRecorderRef.current = new MediaRecorder(stream);
      mediaRecorderRef.current.ondataavailable = (event) => {
        audioChunksRef.current.push(event.data);
      };
      mediaRecorderRef.current.onstop = async () => {
        // Blob 생성 및 오디오 플레이어에서 재생할 수 있도록 URL 생성
        const audioBlob = new Blob(audioChunksRef.current, { type: "audio/wav" });
        const url = URL.createObjectURL(audioBlob);
        setAudioURL(url);
        audioChunksRef.current = [];

        try {
          // STT 변환 (Whisper API 호출)
          const text = await uploadToWhisper(audioBlob);
          setTranscription(text);

          // 1) ***STT가 완료되면 먼저 부모에게 사용자 말풍선 표시 요청***
          if (onTranscription) {
            onTranscription(text);
          }
<<<<<<< HEAD

          // 2) ***그 후 백엔드에 텍스트 전송***
          const backendResponse = await sendMessageToBackend(text);

          // 3) ***백엔드 응답을 부모에게 전달해 AI 말풍선 표시***
          if (backendResponse && onResponseReceived) {
            onResponseReceived(backendResponse);
          }
=======
>>>>>>> develop
        } catch (error) {
          console.error("STT 변환 중 오류 발생:", error);
          setTranscription("오류가 발생했습니다. 다시 시도해주세요.");
        }
      };
      mediaRecorderRef.current.start(500);
      setIsRecording(true);
    } catch (error) {
      console.error("녹음 시작 에러:", error);
    }
  };
<<<<<<< HEAD

  // 녹음을 종료하는 함수
=======
>>>>>>> develop
  const stopRecording = () => {
    if (mediaRecorderRef.current && isRecording) {
      // 0.5초 더 녹음 후 종료
      setTimeout(() => {
        if (mediaRecorderRef.current) {
          mediaRecorderRef.current.stop();
          setIsRecording(false);
        }
      }, 500); // 0.5초 대기 후 종료
    }
  };

  // 마이크 이미지 클릭 시 호출되는 함수 (토글 동작)
  const handleMicClick = () => {
    if (isRecording) {
      stopRecording();
    } else {
      startRecording();
    }
  };

  return (
    <div className="ai-chat-recorder">
<<<<<<< HEAD
      {/* 마이크 이미지 버튼 */}
      <img className="mike" src={mike} alt="mike" onClick={handleMicClick} style={{ cursor: "pointer" }} />
      {isRecording && <p>녹음 중...</p>}
=======
      {/* ✅ endFlag가 true이면 마이크 버튼 대신 종료하기 버튼 표시 */}
      {endFlag ? (
        <button className="end-chat-button" onClick={onEndChat}>
          종료하기
        </button>
      ) : isRecording ? (
        <div className="mike-spinner-container" onClick={handleMicClick}>
          <ScaleLoader color="#fa400d" height={30} width={5} />
        </div>
      ) : (
        <img
          className="mike"
          src={mike}
          alt="mike"
          onClick={handleMicClick}
          style={{ cursor: "pointer" }}
        />
      )}
>>>>>>> develop
    </div>
  );
};

export default AIChatRecorder;
