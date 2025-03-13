import React, { useEffect, useRef, useState } from "react";
import { Canvas } from "@react-three/fiber";
import { Color, Euler, Matrix4 } from "three";
import Avatar from "./Avatar";
import Background from "./Background";

// Mediapipe Face Landmarker
import {
  FilesetResolver,
  FaceLandmarker,
  FaceLandmarkerOptions,
  PoseLandmarker,
  PoseLandmarkerOptions,
  PoseLandmarkerResult,
} from "@mediapipe/tasks-vision";

type AvatarCanvasProps = {
  selectedAvatar: number; // 부모로부터 받은 아바타 번호 (null 아님)
  selectedBackground: number; // 부모로부터 받은 배경 번호 (null 아님)
  onAvatarReady?: (stream: MediaStream) => void;
};

const DETECTION_INTERVAL_MS = 66; // 약 15fps 제한

export default function AvatarCanvas({
  selectedAvatar,
  selectedBackground,
  onAvatarReady,
}: AvatarCanvasProps) {
  const videoRef = useRef<HTMLVideoElement | null>(null);
  const canvasRef = useRef<HTMLCanvasElement | null>(null);

  const [headRotation, setHeadRotation] = useState(new Euler(0, 0, 0));
  const [avatarUrl, setAvatarUrl] = useState(
    `YOUR_FRONTEND_URL/assets/Character${selectedAvatar}-1.glb`
  );
  const [canvasStream, setCanvasStream] = useState<MediaStream | null>(null);

  let faceLandmarker: FaceLandmarker | null = null;
  let lastVideoTime = -1;
  let lastDetectionTime = 0;

  // selectedAvatar가 바뀌면 아바타의 기본 표정 URL 재설정
  useEffect(() => {
    setAvatarUrl(
      `YOUR_FRONTEND_URL/assets/Character${selectedAvatar}-1.glb`
    );
  }, [selectedAvatar]);

  // Mediapipe 초기화
  useEffect(() => {
    const predict = async () => {
      if (!videoRef.current || !faceLandmarker) return;

      const nowInMs = Date.now();
      const video = videoRef.current;

      if (nowInMs - lastDetectionTime < DETECTION_INTERVAL_MS) {
        requestAnimationFrame(predict);
        return;
      }
      lastDetectionTime = nowInMs;

      if (lastVideoTime !== video.currentTime) {
        lastVideoTime = video.currentTime;
        const result = faceLandmarker.detectForVideo(video, nowInMs);

        // 머리 회전
        if (
          result.facialTransformationMatrixes &&
          result.facialTransformationMatrixes.length > 0
        ) {
          const matrixArray = result.facialTransformationMatrixes[0].data;
          const matrix4 = new Matrix4().fromArray(matrixArray);
          const newEuler = new Euler().setFromRotationMatrix(matrix4);
          setHeadRotation(newEuler);
        }

        // 블렌드셰이프 (감정에 따른 glb 변경)
        if (result.faceBlendshapes && result.faceBlendshapes.length > 0) {
          const blendshapes = result.faceBlendshapes[0].categories || [];

          const mouthSmileLeft =
            blendshapes.find((b) => b.categoryName === "mouthSmileLeft")
              ?.score ?? 0;
          const mouthSmileRight =
            blendshapes.find((b) => b.categoryName === "mouthSmileRight")
              ?.score ?? 0;
          const smileScore = mouthSmileLeft + mouthSmileRight;

          const browDownLeft =
            blendshapes.find((b) => b.categoryName === "browDownLeft")?.score ??
            0;
          const browDownRight =
            blendshapes.find((b) => b.categoryName === "browDownRight")
              ?.score ?? 0;
          const angryScore = browDownLeft + browDownRight;

          if (smileScore > 0.4) {
            setAvatarUrl(
              `YOUR_FRONTEND_URL/assets/Character${selectedAvatar}-2.glb`
            );
          } else if (angryScore > 0.11) {
            setAvatarUrl(
              `YOUR_FRONTEND_URL/assets/Character${selectedAvatar}-3.glb`
            );
          } else {
            setAvatarUrl(
              `YOUR_FRONTEND_URL/assets/Character${selectedAvatar}-1.glb`
            );
          }
        }
      }
      requestAnimationFrame(predict);
    };

    const initLandmarker = async () => {
      const filesetResolver = await FilesetResolver.forVisionTasks(
        "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision@0.10.0/wasm"
      );
      const options: FaceLandmarkerOptions = {
        baseOptions: {
          modelAssetPath:
            "https://storage.googleapis.com/mediapipe-models/face_landmarker/face_landmarker/float16/1/face_landmarker.task",
          delegate: "GPU",
        },
        numFaces: 1,
        runningMode: "VIDEO",
        outputFaceBlendshapes: true,
        outputFacialTransformationMatrixes: true,
      };

      faceLandmarker = await FaceLandmarker.createFromOptions(
        filesetResolver,
        options
      );
      // Pose
      poseLandmarker = await PoseLandmarker.createFromOptions(
        filesetResolver,
        poseOptions
      );
    }

      if (videoRef.current) {
        videoRef.current.play();
        requestAnimationFrame(predict);
      }
    };

    navigator.mediaDevices
      .getUserMedia({ video: true, audio: false })
      .then((stream) => {
        if (videoRef.current) {
          videoRef.current.srcObject = stream;
          videoRef.current.onloadeddata = initLandmarker;
        }
      })
      .catch((err) => {
        console.error("Could not access webcam:", err);
      });

    return () => {
      // Cleanup if needed
    };
  }, [selectedAvatar]);

  // 캔버스 -> captureStream
  useEffect(() => {
    // (A) Canvas가 실제로 렌더된 뒤 captureStream 시도
    const timer = setTimeout(() => {
      if (canvasRef.current) {
        const stream = canvasRef.current.captureStream(30);
        setCanvasStream(stream);
      } else {
        console.warn("AvatarCanvas: canvasRef is null");
      }
    }, 1000);
    return () => clearTimeout(timer);
  }, []);

  // 부모에 스트림 전달
  useEffect(() => {
    if (!canvasStream || !onAvatarReady) return;
    const videoTrack = canvasStream.getVideoTracks()[0];
    if (!videoTrack) return;
    onAvatarReady(new MediaStream([videoTrack]));
  }, [canvasStream, onAvatarReady]);

  return (
    <div>
      <video ref={videoRef} style={{ display: "none" }} autoPlay />
      <Canvas
        style={{ width: 90, height: 120 }}
        camera={{ fov: 25 }}
        shadows
        onCreated={({ gl }) => {
          gl.setPixelRatio(10);
          canvasRef.current = gl.domElement;
        }}
      >
        <Background backgroundIndex={selectedBackground} />
        <ambientLight intensity={0.5} />
        <pointLight
          position={[10, 10, 10]}
          color={new Color(1, 1, 0)}
          intensity={0.5}
          castShadow
        />
        <pointLight
          position={[-10, 0, 10]}
          color={new Color(1, 0, 0)}
          intensity={0.5}
          castShadow
        />
        <pointLight position={[0, 0, 10]} intensity={0.5} castShadow />

        <Avatar url={avatarUrl} headRotation={headRotation} />
      </Canvas>
    </div>
  );
}
