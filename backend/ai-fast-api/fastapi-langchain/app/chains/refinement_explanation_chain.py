from textwrap import dedent

# LangChain 관련 모듈
from langchain.prompts import PromptTemplate
from langchain_core.output_parsers.json import JsonOutputParser

import app.chains.llms as llms

# 스트리밍 평가 체인 (비동기 방식 사용)
refinement_explanation_prompt = PromptTemplate(
    input_variables=["conversation_history", "user_answer", "checklist", "u_name"],
        template=dedent("""\
        당신은 전문 대화 평가 전문가입니다. 제공된 대화 기록을 읽고, 플레이어캐릭터({u_name})의 마지막 답변을 아래의 체크리스트와 채점 루브릭에 따라 평가해 주세요.
        각 항목(1~5)은 이미 평가되어 있으며, 각 항목의 주어진 점수에 더해 상세 평가 설명(근거)를 작성해 주시기 바랍니다.
        또한, 모든 항목의 점수를 바탕으로 전체 평가 점수(평균 또는 가중평균)와 종합 평가 의견을 포함한 JSON 형식의 결과를 출력해 주세요.

        {checklist}

        [출력 형식 (JSON)]
        아래의 JSON 구조와 점수를 그대로 사용하여 상세 평가를 더한 결과를 출력해 주세요.
        {{
        "scores": {{
            "1": {{ "score": {1_score}, "explanation": "<항목 1에 대한 상세 평가 설명>" }},
            "2": {{ "score": {2_score}, "explanation": "<항목 2에 대한 상세 평가 설명>" }},
            "3": {{ "score": {3_score}, "explanation": "<항목 3에 대한 상세 평가 설명>" }},
            "4": {{ "score": {4_score}, "explanation": "<항목 4에 대한 상세 평가 설명>" }},
            "5": {{ "score": {5_score}, "explanation": "<항목 5에 대한 상세 평가 설명>" }}
        }},
        "overall_score": {overall_score},
        "overall_comment": "<종합 평가 의견>"
        }}

        [대화 기록]
        ------------------------------------------------  
        {conversation_history}
        ------------------------------------------------  

        위의 지침과 세부 평가 기준에 따라, 제공된 대화 기록을 읽고 플레이어캐릭터({u_name})의 마지막 답변의 상세 평가 설명을 작성해 주세요.
        각 항목에 대해 구체적인 평가 근거를 제공하고, 전체 평가 점수와 종합 평가 의견을 포함한 JSON 결과를 출력해 주세요.
        각 explanation 항목은 반드시 한국어 존댓말로 작성해 주세요.
    """)
)
refinement_explanation_chain = refinement_explanation_prompt | llms.gpt4_llm_check | JsonOutputParser()
    # template=dedent("""\
    #     당신은 전문 대화 평가 전문가입니다. 제공된 대화 기록을 읽고, {u_name}의 마지막 답변을 평가해.
    #     아래의 체크리스트와 세부 채점 루브릭에 따라 각 항목을 100점 만점으로 평가해.
    #     각 항목에 대해 평가 점수와 그에 따른 상세한 평가 설명(근거)을 작성하고, 
    #     모든 항목의 점수를 바탕으로 전체 평가 점수(평균 혹은 가중평균)와 종합 평가 의견을 포함한 JSON 형식의 결과를 출력해.
    #     {checklist}
        
    #     [출력 형식 (JSON)]
    #     아래의 JSON 구조를 그대로 사용하여 평가 결과를 출력해.
    #     {{
    #     "scores": {{
    #         "1": {{ "score": <0~100 점>, "explanation": "<항목 1에 대한 상세 평가 설명>" }},
    #         "2": {{ "score": <0~100 점>, "explanation": "<항목 2에 대한 상세 평가 설명>" }},
    #         "3": {{ "score": <0~100 점>, "explanation": "<항목 3에 대한 상세 평가 설명>" }},
    #         "4": {{ "score": <0~100 점>, "explanation": "<항목 4에 대한 상세 평가 설명>" }},
    #         "5": {{ "score": <0~100 점>, "explanation": "<항목 5에 대한 상세 평가 설명>" }}
    #     }},
    #     "overall_score": <전체 평가 점수 (0~100)>,
    #     "overall_comment": "<종합 평가 의견>"
    #     }}
                    
    #     [대화 기록]
    #     ------------------------------------------------  
    #     {conversation_history}
    #     ------------------------------------------------  

    #     위의 지침과 세부 평가 기준에 따라, 제공된 대화 기록을 읽고 {u_name}의 마지막 답변을 평가해. 
    #     각 항목에 대해 구체적인 점수와 평가 근거를 제공하고, 전체 평가 점수와 종합 의견을 포함한 JSON 결과를 출력해.
    #     각 explanation 항목은 존댓말로 작성해.
    # """)