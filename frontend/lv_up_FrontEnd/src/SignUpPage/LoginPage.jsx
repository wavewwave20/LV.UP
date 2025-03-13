import { useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import "./LoginPage.css";
import KakaoLogin from "../assets/imageFile/KakaoLogin.png";

const clientId =
  import.meta.env.VITE_KAKAO_CLIENT_ID || "188a0e355e8e98a2dfea1fef4cb01d98";
const redirectUri = encodeURIComponent(
  import.meta.env.VITE_KAKAO_REDIRECT_URI ||
    "YOUR_BACKEND_URL/login/oauth2/kakao"
);

function LoginPage() {
  const navigate = useNavigate();
  const location = useLocation(); // ✅ 현재 URL을 감지하여 useEffect 실행

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const registerCompletedParam = urlParams.get("register-completed");

    // URL에 `register-completed` 값이 없으면 이동하지 않음
    if (registerCompletedParam === null) return;

    const registerCompleted = registerCompletedParam === "true"; // Boolean 변환

    if (registerCompleted) {
      navigate("/main", { replace: true }); // 🔥 `replace: true` 사용 → 뒤로 가기 방지
    } else {
      navigate("/customize-profile", { replace: true });
    }
  }, [location.search, navigate]); // ✅ URL이 변경될 때마다 실행되도록 설정

  const handleKakaoLogin = () => {
    window.location.href = `https://kauth.kakao.com/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUri}&response_type=code`;
  };

  return (
    <div>
      <div className="login-container">
        <img src={logo} alt="logo" className="logo" />
        <img
          src={KakaoLogin}
          alt="KakaoLogin"
          className="kakao-login"
          onClick={handleKakaoLogin}
        />
      </div>
    </div>
  );
}

export default LoginPage;
