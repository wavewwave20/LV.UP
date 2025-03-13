from fastapi import APIRouter, BackgroundTasks
from app.models import ConversationTurnRequest, ConversationTurnResponse
from app.langchain_logic import process_conversation_turn
from app.webhook import send_explanation_webhook,send_hint_webhook
from app.chains.checklist import checklist
import os

router = APIRouter()

webhook_url_explanation = os.environ.get("WEBHOOK_URL_EXPLANATION")
webhook_url_hint = os.environ.get("WEBHOOK_URL_HINT")

@router.post("/conversation-turn", response_model=ConversationTurnResponse)
async def conversation_turn(
    request: ConversationTurnRequest,
    background_tasks: BackgroundTasks  # BackgroundTasks 인스턴스 주입
):
    """
    사용자로부터 대화 턴 요청을 받아 LangChain 로직을 통해
    대화 기록, 평가 결과, 상대 답변, 다음 답변 힌트 등을 업데이트하여 반환합니다.
    """

    print(request.dict())
    # 대화 턴 처리 로직 실행 (결과는 dict 형태로 반환)
    result = await process_conversation_turn(
        ai_smalltalk_id=request.ai_smalltalk_id,
        conversation_history=request.conversation_history,
        conversation_records=request.conversation_records,
        mood_score=request.sentiment_score_sum,
        u_name=request.u_name,
        op_name=request.op_name,
        user_input=request.user_input,
        this_turn=request.this_turn
    )

    print("send_explanation_webhook 호출", flush=True)

        

    params = {
        "conversation_history": result["history_without_opponent"],
        "user_answer": request.user_input,
        "checklist": checklist,
        "u_name": request.u_name,
        "this_turn": request.this_turn,
        "end_flag": result["end_flag"],
        "overall_score" : result["overall_score"],
    }

    index = 1
    for score_obj in result["scores"].values():
        params[str(index) + "_score"] = score_obj["score"]
        index += 1




    webhook_url1 = webhook_url_explanation + "/" + str(request.ai_smalltalk_id)
    # send_explanation_webhook 함수를 백그라운드 작업으로 등록
    background_tasks.add_task(send_explanation_webhook, params, webhook_url1)
    
    print("hint 생성 webhook 호출")
    params2 = {
        "conversation_history": result["new_conversation_history"],
        "u_name": request.u_name,
        "op_name": request.op_name,
        "this_turn": request.this_turn,
        "checklist": checklist,
    }
    webhook_url2 = webhook_url_hint + "/" + str(request.ai_smalltalk_id)

    background_tasks.add_task(send_hint_webhook, params2, webhook_url2)
    
    return ConversationTurnResponse(**result)