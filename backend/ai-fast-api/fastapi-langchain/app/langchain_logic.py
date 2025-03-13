import json
from typing import Dict, List
from dotenv import load_dotenv

load_dotenv(dotenv_path=".env")

from app.chains.my_answer_suggest_chain import my_answer_suggest_chain
from app.chains.oppenent_response_chain import opponent_response_chain
from app.chains.refinement_explanation_chain import refinement_explanation_chain
from app.chains.refinement_score_chain import refinement_score_chain
from app.chains.checklist import checklist

# 종료 임계치
MOOD_THRESHOLD = 3

def refinement_explanation_invoke(params: Dict) -> Dict:
    result = refinement_explanation_chain.invoke(params)
    print("refinement_explanation_invoke result:", result)
    return result

def next_suggested_answer_invoke(params: Dict) -> Dict:
    result = my_answer_suggest_chain.invoke(params)
    print("next_suggested_answer_invoke result:", result)
    return result

# ---------------------------------
# 대화 턴 처리 함수
# ---------------------------------
async def process_conversation_turn(
    ai_smalltalk_id: int,
    conversation_history: str,
    conversation_records: List[dict],
    mood_score: int,
    u_name: str,
    op_name: str,
    user_input: str,
    this_turn: int
) -> Dict:
    # 사용자 입력 추가
    updated_history = conversation_history + f"\n{u_name}: {user_input}"

    conversation_records.append({
        "turn": this_turn,
        "speaker": u_name,
        "speaker_type": "user",
        "text": user_input,
        "sentiment_score": 0
    })

    
    opponent_answer = ""
    # 상대방 답변 생성
    opponent_answer = opponent_response_chain.invoke({
    "conversation_history": updated_history,
    "u_name": u_name,
    "op_name": op_name
    })

    
    updated_history_added_opponent = updated_history + f"\n{op_name}: {opponent_answer}"


    refinement_score_result = refinement_score_chain.invoke({
        "conversation_history": updated_history,
        "user_answer": user_input,
        "checklist": checklist,
        "u_name": u_name
    })

    # scores를 평균값내어 overall_score 계산, 기본값은 51
    scores = refinement_score_result.get("scores")
    print("점수체점 결과: ", refinement_score_result)
    overall_score = 0
    for score_obj in scores.values():
        overall_score += score_obj["score"]

    overall_score = overall_score / len(scores) if len(scores) > 0 else 51
    print(overall_score, flush=True)

    sentiment_score = 0
    if overall_score < 25:
        sentiment_score = 2
        mood_score += 2
    elif 25 <= overall_score <= 50:
        sentiment_score = 1
        mood_score += 1
    
    end_flag = False
    end_reason = None
    if mood_score >= MOOD_THRESHOLD:
        end_flag = True
        end_reason = f"상대방의 기분 지수({mood_score})가 임계치({MOOD_THRESHOLD})에 도달함."

    # 상대 발화 기록 추가
    conversation_records.append({
        "turn": this_turn,
        "speaker": op_name,
        "speaker_type": "opponent",
        "text": opponent_answer,
        "sentiment_score": sentiment_score
    })

    return {
        "ai_smalltalk_id": ai_smalltalk_id,
        "new_conversation_history": updated_history_added_opponent,
        "history_without_opponent": updated_history,
        "new_conversation_records": conversation_records,
        "op_answer": opponent_answer,
        "sentiment_score_sum": mood_score,
        "u_name": u_name,
        "op_name": op_name,
        "end_flag": end_flag,
        "end_reason": end_reason,
        "this_turn": this_turn,
        "scores": scores,
        "overall_score": overall_score,
    }
