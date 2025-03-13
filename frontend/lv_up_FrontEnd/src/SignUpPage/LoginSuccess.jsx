import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Cookies from "js-cookie";
import { fetchAdmin } from "../api/UserAPI";
import lvup from "../assets/imageFile/lvup.gif";

function LoginSuccess() {
  const navigate = useNavigate();

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const registerCompleted = params.get("register-completed") === "true";
    const token = params.get("token");

    if (token) {
      Cookies.set("token", token, {
        path: "/",
        secure: true,
        sameSite: "Strict",
      });
    } else {
      console.error("❌ 토큰이 없습니다. 쿠키 저장 불가.");
      return;
    }

    if (registerCompleted) {
      // 🔹 fetchAdmin()이 true/false 반환하므로 바로 사용 가능
      fetchAdmin()
        .then((isAdmin) => {
          if (isAdmin.data === true) {
            navigate("/admin");
          } else {
            navigate("/main");
          }
        })
        .catch((error) => {
          navigate("/main"); // 오류 발생 시 기본적으로 main으로 이동
        });
    } else {
      navigate("/customize-profile", { state: { token } });
    }
  }, [navigate]);

  return (
    <div className="loading-container">
      <img src={lvup} alt="Loading..." className="loading-gif" />
    </div>
  );
}

export default LoginSuccess;
