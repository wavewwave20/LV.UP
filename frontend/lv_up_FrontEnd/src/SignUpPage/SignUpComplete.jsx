import "./SignUpComplete.css";
import celebrate from "../assets/imageFile/celebrate.png";
import { useNavigate } from "react-router-dom";

function SignUpComplete() {
  const navigate = useNavigate();
<<<<<<< HEAD
  const handleMainPage = () => {
    navigate("/main");
=======
  const location = useLocation();
  const nickname = location.state?.nickname || "User";
  const [isLoading, setIsLoading] = useState(false); // 로딩 상태 추가

  const handleMainPage = async () => {
    try {
      setIsLoading(true); // 로딩 시작
      const response = await getInitialTickets();
      navigate("/main");
    } catch (error) {
      console.error("❌ 티켓 지급 중 오류 발생:", error);
      // 에러가 발생하더라도 메인 페이지로 이동
      navigate("/main");
    } finally {
      setIsLoading(false); // 로딩 종료
    }
>>>>>>> develop
  };

  return (
    <div className="sign-up-complete">
<<<<<<< HEAD
      <img src={celebrate} alt="축하사진" />
      <h2>가입완료!</h2>
      <h2>User님, 환영해요</h2>
      <p>
        친구들과 대화할 수 있는 티켓을 드렸어요 <br />
        미션을 클리어 하시면 더 많은 보상을 드려요!
      </p>
      <button onClick={handleMainPage} className="next-button">
        시작하기
=======
      <img className="celebrate" src={celebrate} alt="축하사진" />
      <div className="signup-title">가입완료!</div>
      <div className="signup-title">가입을 환영해요</div>
      <div className="signup-description">
        친구들과 대화할 수 있는 티켓을 드렸어요 <br />
        미션을 클리어 하시면 더 많은 보상을 드려요!
      </div>
      <button
        onClick={handleMainPage}
        className="signup-next-button"
        disabled={isLoading}
      >
        {isLoading ? "처리 중..." : "시작하기"}
>>>>>>> develop
      </button>
    </div>
  );
}

export default SignUpComplete;
