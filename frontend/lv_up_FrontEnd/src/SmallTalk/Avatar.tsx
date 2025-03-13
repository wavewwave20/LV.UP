import React from "react";
import { useFrame, useGraph } from "@react-three/fiber";
import { useGLTF } from "@react-three/drei";
import * as THREE from "three";
import { Euler } from "three";

<<<<<<< HEAD
// type ArmRotations = {
//   leftUpper: THREE.Quaternion;
//   leftFore: THREE.Quaternion;
//   rightUpper: THREE.Quaternion;
//   rightFore: THREE.Quaternion;
// };

type AvatarProps = {
  /** 아바타 모델 경로 */
  url: string;
  /** 얼굴/머리/척추 회전값 (전역 or 상위 컴포넌트에서 받아옴) */
  headRotation: Euler | undefined;
  /** 팔(상완/하박) 회전 쿼터니언 */
  // armRotations: ArmRotations;
};

/**
 * 아바타 컴포넌트
 * - GLB/GLTF를 로드하고, 
 * - headRotation, armRotations를 반영하여 모델을 업데이트
 */
export default function Avatar({
  url,
  headRotation,
  // armRotations,
}: AvatarProps) {
  // GLTF 모델 로드
  const { scene } = useGLTF(url) as any;
  // 노드(메시, 본 등) 참조
  const { nodes } = useGraph(scene);

  // 모델에 정의된 본(Bone)들 (모델마다 이름이 다를 수 있음)
  // const avatarBones = {
  //   leftUpperArm: nodes.LeftArm as THREE.Bone,
  //   leftForeArm: nodes.LeftForeArm as THREE.Bone,
  //   rightUpperArm: nodes.RightArm as THREE.Bone,
  //   rightForeArm: nodes.RightForeArm as THREE.Bone,
  // };

  // 매 프레임마다 본의 회전값을 갱신
  useFrame(() => {
    // 얼굴(머리/목/척추) 회전
    if (headRotation) {
      // Head
      nodes.Head.rotation.set(headRotation.x, headRotation.y, headRotation.z);

      // Neck (대략적인 보간 예시: 1/5 정도만 회전 반영)
=======
/** Avatar 컴포넌트에 필요한 Props 타입 정의 */
type AvatarProps = {
  /** GLB 파일의 경로 */
  url: string;
  /** 추적된 머리 회전 값 */
  headRotation?: Euler;
};

/**
 * Avatar 컴포넌트
 * - Mediapipe로 추출한 회전값을 받아와, 3D 모델의 Head, Neck 등을 회전시킵니다.
 * - url(문자열)로부터 GLB 파일을 로드합니다.
 */
export default function Avatar({ url, headRotation }: AvatarProps) {
  // (1) GLB 로드
  // useGLTF 훅을 사용해 3D 모델을 불러옵니다.
  // url은 '/assets/Character1-1.glb' 같이 문자열 경로를 그대로 전달받을 수 있습니다.
  const { scene } = useGLTF(url);

  // (2) 모델의 자식 노드(Head, Neck 등)에 접근하기 위해 useGraph 사용
  //    → scene 내부의 모든 노드를 탐색할 수 있게 됩니다.
  const { nodes } = useGraph(scene);

  // (3) 매 프레임마다 회전값을 업데이트
  useFrame(() => {
    if (headRotation) {
      // Head 회전
      nodes.Head.rotation.set(headRotation.x, headRotation.y, headRotation.z);

      // Neck 회전 (Head 회전값의 일부만 적용)
>>>>>>> develop
      nodes.Neck.rotation.set(
        headRotation.x / 5 + 0.3,
        headRotation.y / 5,
        headRotation.z / 5
      );

<<<<<<< HEAD
      // Spine1 (척추 일부, 1/10 정도로 회전 반영)
=======
      // Spine1 회전 (Head 회전값의 일부만 적용)
>>>>>>> develop
      nodes.Spine1.rotation.set(
        headRotation.x / 10,
        headRotation.y / 10,
        headRotation.z / 10
      );
    }

    // 팔 관절(상완/하박) 쿼터니언 보간
    // if (avatarBones) {
    //   avatarBones.leftUpperArm.quaternion.slerp(armRotations.leftUpper, 0.1);
    //   avatarBones.leftForeArm.quaternion.slerp(armRotations.leftFore, 0.1);
    //   avatarBones.rightUpperArm.quaternion.slerp(armRotations.rightUpper, 0.1);
    //   avatarBones.rightForeArm.quaternion.slerp(armRotations.rightFore, 0.1);
    // }
  });

<<<<<<< HEAD
  // 모델의 위치/크기 조정
=======
  // (4) 로드된 scene을 fiber에 primitive 형태로 렌더
  //     position은 대략 아바타가 잘 보이는 곳으로 조정
>>>>>>> develop
  return <primitive object={scene} position={[0, -1.75, 3]} />;
}
