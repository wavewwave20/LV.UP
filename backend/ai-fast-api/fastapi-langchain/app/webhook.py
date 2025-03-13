# app/webhook.py
import os
import httpx
import asyncio
from app.langchain_logic import refinement_explanation_invoke, next_suggested_answer_invoke

async def send_explanation_webhook(params: dict, webhook_url: str) -> None:

    
    
    explanation_result = await asyncio.to_thread(refinement_explanation_invoke, params)
    # result에 turn 추가
    explanation_result["turn"] = params["this_turn"]
    explanation_result["end_flag"] = params["end_flag"]


    print("refinement_explanation_invoke 결과:", explanation_result, flush=True)

    try:
        async with httpx.AsyncClient() as client:
            headers = {
                "Content-Type": "application/json",  # json 매개수를 사용하면 자동 설정되지만, 명시적으로 추가할 수도 있음
                "Accept": "application/json",
                # "Authorization": "Bearer <YOUR_TOKEN>"  # 필요에 따라 토큰 등 추가
            }
            response = await client.post(webhook_url, json=explanation_result, headers=headers)
            print("Webhook 전송 완료, 상태 코드:", response.status_code, flush=True)
    except Exception as e:
        print("Webhook 전송 중 오류 발생:", e, flush=True)


async def send_hint_webhook(params: dict, webhook_url: str) -> None:

    hint_result = await asyncio.to_thread(next_suggested_answer_invoke, params)

    # hint_result(string)를 dict으로 변환
    hint_result_json = {"hint": hint_result, "turn": params["this_turn"]}

    print("next_suggested_answer_invoke 결과:", hint_result_json, flush=True)

    try:
        async with httpx.AsyncClient() as client:
            headers = {
                "Content-Type": "application/json",  # json 매개수를 사용하면 자동 설정되지만, 명시적으로 추가할 수도 있음
                "Accept": "application/json",
                # "Authorization": "Bearer <YOUR_TOKEN>"  # 필요에 따라 토큰 등 추가
            }
            response = await client.post(webhook_url, json=hint_result_json, headers=headers)
            print("Webhook 전송 완료, 상태 코드:", response.status_code, flush=True)
    except Exception as e:
        print("Webhook 전송 중 오류 발생:", e, flush=True)