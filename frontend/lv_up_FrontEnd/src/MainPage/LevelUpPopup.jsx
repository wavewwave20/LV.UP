import "./LevelUpPopup.css";
import levelUpStar from "../assets/imageFile/levelup_star.png";
import ticket from "../assets/imageFile/ticket.png";
import coin from "../assets/imageFile/coin.png";

function LevelUpPopup({ onClose, rewardData, level }) {
  const { tickets = 0, coins = 0 } = rewardData || {};

  return (
    <div className="levelup_popup_container">
      <div className="levelup_popup_overlay">
        <div className="levelup_popup_content">
          <img
            className="levelup_popup_star"
            src={levelUpStar}
            alt="레벨업 별"
          />

          <div className="levelup_title">Level.Up!</div>

          {/* ✅ 새로운 레벨 표시 */}
          <div className="level_ellipseDiv">{level}</div>

          {/* ✅ 보상이 하나라도 있으면 보상 표시 */}
          {(tickets > 0 || coins > 0) && (
            <div className="levelup_reward_container">
              {tickets > 0 && (
                <div className="levelup_reward_item">
                  <img
                    className="levelup_ticket_icon"
                    src={ticket}
                    alt="티켓"
                  />
                  <span className="levelup_plus">+</span>
                  <span className="levelup_reward_value">{tickets}</span>
                </div>
              )}

              {coins > 0 && (
                <div className="levelup_reward_item">
                  <img className="levelup_coin_icon" src={coin} alt="코인" />
                  <span className="levelup_plus">+</span>
                  <span className="levelup_reward_value">{coins}</span>
                </div>
              )}
            </div>
          )}

          <button className="levelup_confirm_button" onClick={onClose}>
            확인
          </button>
        </div>
      </div>
    </div>
  );
}

export default LevelUpPopup;
