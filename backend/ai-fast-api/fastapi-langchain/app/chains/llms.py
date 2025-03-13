from langchain_openai import ChatOpenAI

# ---------------------------------
# LLM 인스턴스 및 환경 설정
# ---------------------------------
gpt4_llm = ChatOpenAI(
    temperature=0.7,
    model_name="gpt-4o-mini",
    max_tokens=512
)

gpt4_llm_check = ChatOpenAI(
    temperature=0.7,
    model_name="gpt-4o-mini",
    max_tokens=512
)