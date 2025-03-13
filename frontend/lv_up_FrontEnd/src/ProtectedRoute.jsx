import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import Cookies from "js-cookie";

const ProtectedRoute = () => {
  const token = Cookies.get("token"); // 🍪 쿠키에서 토큰 가져오기

  return token ? <Outlet /> : <Navigate to="/" replace />;
};

export default ProtectedRoute;
