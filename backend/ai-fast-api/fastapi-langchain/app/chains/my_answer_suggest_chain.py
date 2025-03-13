from textwrap import dedent

# LangChain 관련 모듈
from langchain.prompts import PromptTemplate
from langchain_core.output_parsers import StrOutputParser
from langchain_core.output_parsers.json import JsonOutputParser

import app.chains.llms as llms

# 내 답변 제안 체인
my_answer_suggest_template = PromptTemplate(
    input_variables=["conversation_history", "u_name", "op_name", "checklist"],
    template=dedent("""\
        {conversation_history}

        체크리스트:
        {checklist}

        이 상황에서 NPC캐릭터({op_name})의 마지막 말에 대한 
        플레이어캐릭터({u_name})의 답변을 체크리스트를 참조하여 
        80점에 해당하는 답변을 만들어주세요.
        설명 없이 답변만 만들어주세요.
        100글자 이내로 작성.
                    
        출력 형식(100글자 이내):
        {u_name}: "~~~"
    """)
)
my_answer_suggest_chain = my_answer_suggest_template | llms.gpt4_llm | StrOutputParser()