import { Navigate, Outlet } from "react-router-dom";
import { useEffect, useState } from "react";
import { fetchAdmin } from "./api/UserAPI";
function AdminProtectedRoute() {
  const [isAdmin, setIsAdmin] = useState(null); // 관리자 여부
  const [loading, setLoading] = useState(true); // 로딩 상태

  useEffect(() => {
    fetchAdmin()
      .then((response) => {
        setIsAdmin(response.data === true); // `true`면 관리자
      })
      .catch((error) => {
        console.error("❌ 관리자 확인 실패:", error);
        setIsAdmin(false); // 오류 발생 시 관리자 아님
      })
      .finally(() => setLoading(false)); // 로딩 완료
  }, []);

  if (loading) return <p>🔄 관리자 확인 중...</p>; // 로딩 중 메시지 표시

  return isAdmin ? <Outlet /> : <Navigate to="/main" replace />;
}

export default AdminProtectedRoute;
