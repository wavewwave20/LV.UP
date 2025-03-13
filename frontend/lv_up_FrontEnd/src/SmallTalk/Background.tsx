import React, { useEffect } from "react";
import { useLoader, useThree } from "@react-three/fiber";
import { TextureLoader } from "three";

type BackgroundProps = {
  backgroundIndex: number;
};

export default function Background({ backgroundIndex }: BackgroundProps) {
  // 선택된 번호에 맞게 배경 이미지 URL을 생성합니다.
  const imageUrl = `YOUR_FRONTEND_URL/assets/background${backgroundIndex}.jpg`;
  const texture = useLoader(TextureLoader, imageUrl);
  const { scene } = useThree();

  useEffect(() => {
    // 씬의 배경으로 텍스처를 설정합니다.
    scene.background = texture;
  }, [texture, scene]);

  return null;
}
