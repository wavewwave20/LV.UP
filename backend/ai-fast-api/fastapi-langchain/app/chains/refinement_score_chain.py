from textwrap import dedent

# LangChain 관련 모듈
from langchain.prompts import PromptTemplate
from langchain_core.output_parsers.json import JsonOutputParser

import app.chains.llms as llms

# 스트리밍 평가 체인 (동기 방식 사용)
refinement_score_prompt = PromptTemplate(
    input_variables=["conversation_history", "user_answer", "checklist", "u_name"],
    template=dedent("""\
        당신은 전문 대화 평가 전문가입니다. 제공된 대화 기록을 읽고, 플레이어캐릭터({u_name})의 마지막 답변을 아래의 체크리스트와 세부 평가 루브릭에 따라 평가해 주세요.  
        각 항목에 대해 0~100점 사이의 점수를 부여하시고, 평가 결과는 점수(score) 값만 포함하는 JSON 형식으로 출력해 주세요.

        {checklist}

        [출력 형식 (JSON)]
        {{
            "scores": {{
                "1": {{ "score": <0~100>}},
                "2": {{ "score": <0~100>}},
                "3": {{ "score": <0~100>}},
                "4": {{ "score": <0~100>}},
                "5": {{ "score": <0~100>}}
            }}
        }}

        [대화 기록]
        ------------------------------------------------  
        {conversation_history}
        ------------------------------------------------  

        위의 지침과 세부 평가 기준에 따라, 제공된 대화 기록을 읽고 플레이어캐릭터({u_name})의 마지막 답변을 평가해 주세요.  
        각 항목에 대해 점수만을 포함한 JSON 결과를 출력해 주세요.
    """)
)
refinement_score_chain = refinement_score_prompt | llms.gpt4_llm_check | JsonOutputParser()

    # template=dedent("""\
    #     당신은 전문 대화 평가 전문가입니다. 제공된 대화 기록을 읽고, {u_name}의 마지막 답변을 평가해주세요.
    #     아래의 체크리스트와 세부 채점 루브릭에 따라 각 항목을 100점 만점으로 평가해 주세요.
    #     각 항목에 대해 평가 점수는 상세한 평가 설명(근거)가 있는 점수여야 합니다.
    #     각 항목에 대해 평가 점수를 JSON 형식 결과로 출력해 주세요.
    #     {checklist}
                     
    #     [출력 형식 (JSON)]
    #     아래의 JSON 구조를 그대로 사용하여 평가 결과를 출력해 주세요.
    #     {{
    #         "scores": {{
    #             "1": {{ "score": <0~100>}},
    #             "2": {{ "score": <0~100>}},
    #             "3": {{ "score": <0~100>}},
    #             "4": {{ "score": <0~100>}},
    #             "5": {{ "score": <0~100>}}
    #         }}
    #     }}
                    
    #     [대화 기록]
    #     ------------------------------------------------  
    #     {conversation_history}
    #     ------------------------------------------------  

    #     위의 지침과 세부 평가 기준에 따라, 제공된 대화 기록을 읽고 {u_name}의 마지막 답변을 평가해주세요. 
    #     각 5개 항목에 대해 구체적인 점수를 JSON 형식으로 출력해 주십시오.
    #     이후에 각각에 대해 한줄로 왜 그 점수를 줬는지 간단하게 설명해 주세요.
    # """)