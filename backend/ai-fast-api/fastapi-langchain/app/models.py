from pydantic import BaseModel
from typing import List, Optional

# 요청(Request) 모델
class ConversationTurnRequest(BaseModel):

    ai_smalltalk_id: int

    # 지금까지 진행된 전체 대화 (문자열 형태)
    conversation_history: str
    
    # 대화 기록(JSON 형태의 list)
    conversation_records: Optional[List[dict]]
    
    # 현재까지 누적된 mood_score
    sentiment_score_sum: int
    
    # 내 이름, 상대방 이름
    u_name: str
    op_name: str
    
    # 사용자가 이번 턴에 입력한 답변
    user_input: str

    this_turn: int

# 응답(Response) 모델
class ConversationTurnResponse(BaseModel):

    ai_smalltalk_id: int

    # 새로 업데이트된 대화 히스토리
    new_conversation_history: str
    
    # 새로 업데이트된 대화 기록
    new_conversation_records: List[dict]
    
    # 상대방의 새 답변
    op_answer: str
    
    # 상대방 감정 분석 점수
    sentiment_score_sum: int

    u_name: str
    op_name: str
    
    # 다음에 내가 어떻게 답변하면 좋을지 힌트
    next_suggested_answer: Optional[str] = None
    
    # 대화를 종료해야 하는지 여부
    end_flag: bool = False
    
    # 종료 사유
    end_reason: Optional[str] = None
