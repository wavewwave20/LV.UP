import { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { checkAdmin } from "../api/Admin";
import "./NavigationBar.css";

import homeOn from "../assets/imageFile/home_button_on.png";
import userOn from "../assets/imageFile/user_button_on.png";
import recordOn from "../assets/imageFile/record_button_on.png";
import homeOff from "../assets/imageFile/home_button_off.png";
import userOff from "../assets/imageFile/user_button_off.png";
import recordOff from "../assets/imageFile/record_button_off.png";

function NavigationBar() {
  const navigate = useNavigate();
  const location = useLocation(); // 현재 경로 가져오기
  const [active, setActive] = useState("home");
  const [isAdmin, setIsAdmin] = useState(false);

  // 어드민 체크
  useEffect(() => {
    const checkAdminAuth = async () => {
      try {
        const response = await checkAdmin();
        if (response.status === 200) {
          setIsAdmin(true);
        }
      } catch (error) {
        console.error('어드민 확인 중 오류 발생:', error);
      }
    };
    checkAdminAuth();
  }, []);

  // ✅ 페이지 변경 시 현재 경로에 맞춰 활성화된 버튼 설정
  useEffect(() => {
    const pathToPage = {
      "/main": "home",
      "/record": "record",
      "/mypage": "user",
      "/admin" : "admin",
    };
    setActive(pathToPage[location.pathname] || "home");
  }, [location.pathname]); // 페이지 이동 시 실행

  const handleNavClick = (page) => {
    setActive(page); // 🔥 먼저 상태 변경
    setTimeout(() => {
      // 🔥 상태 변경 후 페이지 이동 (100ms 지연)
      const routes = {
        home: "/main",
        record: "/record",
        user: "/mypage",
        admin: "/admin",
      };
      navigate(routes[page]);
    }, 100);
  };

  return (
    <nav className="nav-bar">
      <ul>
        <li
          className={`nav-item ${active === "home" ? "active" : ""}`}
          onClick={() => handleNavClick("home")}
        >
          <img src={active === "home" ? homeOn : homeOff} alt="홈" />
        </li>
        <li
          className={`nav-item ${active === "record" ? "active" : ""}`}
          onClick={() => handleNavClick("record")}
        >
          <img src={active === "record" ? recordOn : recordOff} alt="기록" />
        </li>
        <li
          className={`nav-item ${active === "user" ? "active" : ""}`}
          onClick={() => handleNavClick("user")}
        >
          <img src={active === "user" ? userOn : userOff} alt="마이페이지" />
        </li>
        {isAdmin && (
          <li
            className={`nav-item ${active === "admin" ? "active" : ""}`}
            onClick={() => handleNavClick("admin")}
          >
            <img src={active === "admin" ? userOn : userOff} alt="관리자" />
          </li>
        )}
      </ul>
    </nav>
  );
}

export default NavigationBar;
