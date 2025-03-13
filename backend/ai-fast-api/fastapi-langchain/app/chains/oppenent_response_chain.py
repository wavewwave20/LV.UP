from textwrap import dedent

# LangChain 관련 모듈
from langchain.prompts import PromptTemplate
from langchain_core.output_parsers import StrOutputParser

import app.chains.llms as llms

# 상대방 응답 생성 체인
opponent_response_template = PromptTemplate(
    input_variables=["conversation_history", "u_name", "op_name"],
    template=dedent("""\
        너는 시나리오 속 NPC캐릭터({op_name})이다.
        시나리오상황에 집중하고, 성격과 나이대를 잘 살려야한다.
        이 상황에서 NPC캐릭터({op_name})가 답변한다면?

        {conversation_history}

        시나리오상황에 집중하고, 성격과 나이대를 잘 살려야한다.
        이 상황에서 NPC캐릭터({op_name})가 답변한다면?
        60글자 이내로 작성하고 대사만 출력해.
    """)
)
opponent_response_chain = opponent_response_template | llms.gpt4_llm | StrOutputParser()