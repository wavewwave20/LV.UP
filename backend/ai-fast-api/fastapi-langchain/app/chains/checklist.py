from textwrap import dedent

# ---------------------------------
# 평가 체크리스트 (원문 그대로 혹은 요약)
# ---------------------------------
checklist = dedent("""\
### Important Guidelines and Evaluation Criteria Before Scoring

1. **Use Consistent Evaluation Criteria**  
   - All evaluators should refer to the detailed criteria below when scoring.
   - **Example Score Ranges:**  
     - **0–24:** Very Poor – Almost completely fails to meet the requirements.  
     - **25-50:** Poor – Only partially meets the requirements; overall effectiveness is lacking.  
     - **50–75:** Average – Meets the basic requirements but has room for improvement.  
     - **76-100:** Excellent – Fully meets all criteria and demonstrates outstanding quality.

2. **Detailed Evaluation Items and Examples**

---

#### 1) Understanding the User's Emotions and Intentions
- **Detailed Criteria:**  
  a. Does the response express appropriate, sometimes dramatic, emotions in line with the conversation content?  
  b. Does it sufficiently consider the listener’s perspective regarding the topic?  
  c. Is the conversation style appropriate for the situation and atmosphere?  

- **Evaluation Examples:**  
  - **0–24:** Very Poor – No attempt is made to understand the user's emotions or intentions; emotional expression is either completely absent or inappropriately distorted.  
  - **25-50:** Poor – There is some attempt at emotional expression and understanding, but it is very limited and hardly considers the listener’s perspective or context.  
  - **50–75:** Average – Basic emotional expression and attempts at understanding are present; however, subtle nuances or adjustments for context are lacking.  
  - **76-100:** Excellent – The user’s emotions and intentions are accurately understood, with responses featuring dramatic, contextually appropriate emotional expression that fully reflects the listener’s perspective.

---

#### 2) Engaging with the Listener and Guiding the Conversation
- **Detailed Criteria:**  
  a. Does the response continuously consider the listener's perspective on the topic?  
  b. Are the listener’s feedback and reactions promptly and effectively incorporated into the conversation?  

- **Evaluation Examples:**  
  - **0–24:** Very Poor – There is no interaction with the listener; the conversation is entirely one-sided with no acknowledgment of feedback.  
  - **25-50:** Poor – Some acknowledgment of the listener's feedback exists, but it is rarely integrated, resulting in a predominantly one-sided dialogue.  
  - **50–75:** Average – The listener’s feedback is occasionally incorporated; however, the conversation flow may feel somewhat awkward and inconsistent.  
  - **76-100:** Excellent – The conversation naturally incorporates the listener’s feedback and reactions, ensuring a smooth, fully interactive dialogue.

---

#### 3) Tailoring the Conversation to the Situation and Atmosphere
- **Detailed Criteria:**  
  a. Is the context and atmosphere of the conversation adequately considered through appropriate vocabulary and expressions?  

- **Evaluation Examples:**  
  - **0–24:** Very Poor – The context or atmosphere is completely ignored, with the use of inappropriate or confusing vocabulary and expressions.  
  - **25-50:** Poor – There is minimal reflection of the situation or atmosphere, leading to poor word choices that negatively affect the conversation flow.  
  - **50–75:** Average – There is a basic consideration of the context, though some expressions may not perfectly match the intended atmosphere.  
  - **76-100:** Excellent – Every response perfectly aligns with the situation and atmosphere, employing natural and highly effective vocabulary and expressions.

---

#### 4) Structuring the Purpose and Content of the Conversation
- **Detailed Criteria:**  
  a. Is the intent and goal of the conversation clearly conveyed?  
  b. Are the main points clearly summarized and communicated?  
  c. Is the content organized in a way that captures the listener’s interest?  

- **Evaluation Examples:**  
  - **0–24:** Very Poor – The conversation’s intent, goals, and main points are completely absent, resulting in confusing and disorganized content.  
  - **25-50:** Poor – Some attempt is made to convey the purpose, but the summary of key points and overall structure is very weak, hindering comprehension.  
  - **50–75:** Average – The basic purpose and main points are communicated, though the content lacks clarity and systematic organization.  
  - **76-100:** Excellent – The conversation’s purpose and main points are very clearly communicated, with content structured in an engaging and effective manner.

---

#### 5) Maintaining Politeness, a Positive Tone, and Respectful Behavior
- **Detailed Criteria:**  
  a. Are appropriate emotional expressions (including dramatic ones when needed) used?  
  b. Is the listener’s perspective respected and are their opinions carefully considered?  
  c. Are responses appropriately tailored to the situation and context?  
  d. Is a fair, non-judgmental attitude maintained throughout the conversation?  

- **Evaluation Examples:**  
  - **0–24:** Very Poor – Politeness is completely lacking, with a negative tone and aggressive attitude pervading the conversation.  
  - **25-50:** Poor – There is only a minimal attempt at politeness; negative expressions or judgmental remarks frequently appear, adversely affecting the tone.  
  - **50–75:** Average – A generally polite demeanor is maintained, though occasional expressions or judgments may undermine the intended tone.  
  - **76-100:** Excellent – The conversation consistently exhibits impeccable politeness and a positive tone, fully respecting the listener’s perspective and engaging in a fair, non-judgmental manner.
""")

# 【채점 진행 전 주의사항 및 평가 기준】  
# 1. **일관된 평가 기준 사용**  
#    - 모든 평가자는 아래 제시된 세부 기준을 참고하여 채점합니다.  
#    - 예시:  
#      - **0~24점:** 매우 부족 - 항목의 요구사항을 거의 충족하지 못함.  
#      - **25~49점:** 부족 - 요구사항의 일부만 충족하며, 전반적인 효과가 미흡함.  
#      - **50~74점:** 보통 - 기본 요구사항은 충족하지만, 개선의 여지가 있음.  
#      - **75~89점:** 우수 - 대부분의 기준을 잘 충족하며, 대화의 질이 높음.  
#      - **90~100점:** 매우 우수 - 모든 기준을 완벽하게 충족하며, 뛰어난 대화 진행.

# 2. **세부 평가 항목 및 예시**  
#    - 각 항목별로 아래의 세부 기준을 참고하여 평가하세요.

# 【체크리스트 및 세부 평가 기준】  
# 1) **사용자의 감정이나 의도를 파악하려고 노력하는가?**  
#    - **세부 기준:**  
#      a. 대화 내용에 맞는 감정을 극적으로 표현하는가?  
#      b. 대화 주제에 대한 청자의 입장을 충분히 고려하는가?  
#      c. 상황과 분위기에 맞는 대화 방식을 사용하는가?  
#    - **평가 예시:**  
#      - 0~24점: 사용자의 감정이나 의도 파악 시도조차 보이지 않으며, 감정 표현이 전혀 없거나 오히려 부적절하게 왜곡됨.
#      - 25~49점: 감정 표현이나 의도 파악의 시도가 있으나, 매우 제한적이며 청자의 입장이나 상황을 거의 고려하지 않음.
#      - 50~74점: 기본적인 감정 표현과 의도 파악은 있으나, 미묘한 감정의 변화나 상황에 따른 세심한 표현이 부족함.
#      - 75~89점: 대화 전반에서 감정과 의도의 파악이 잘 이루어지며, 대부분 상황과 청자의 입장을 반영하지만, 일부 세부 표현에서 개선의 여지가 있음.
#      - 90~100점: 사용자의 감정과 의도를 정확히 파악하여, 상황에 맞는 극적인 감정 표현과 청자의 입장을 완벽하게 반영한 답변을 제공함.

# 2) **청자와 상호작용하며 대화를 이끄는가?**  
#    - **세부 기준:**  
#      a. 대화 주제에 대한 청자의 입장을 지속적으로 고려하는가?  
#      b. 청자의 반응과 피드백을 실시간으로 수용하여 대화에 반영하는가?  
#    - **평가 예시:**  
#      - 0~24점: 청자와의 상호작용이 전혀 이루어지지 않으며, 청자의 피드백이나 반응에 대해 무관심하게 대화를 진행함.
#      - 25~49점: 청자의 반응을 일부 인지하지만, 거의 반영하지 못하고 일방적으로 대화를 주도하는 모습이 두드러짐.
#      - 50~74점: 청자의 일부 피드백은 반영하나, 대화의 흐름이 다소 어색하고 일관성이 떨어지며, 상호작용이 제한적임.
#      - 75~89점: 청자의 피드백과 반응을 적절히 수용하여 대화를 원활하게 이끌지만, 약간의 미세한 조정이 필요한 경우가 있음.
#      - 90~100점: 청자의 피드백에 즉각적이고 효과적으로 반응하며, 자연스럽게 대화의 흐름을 이끌어가면서 청자와 완벽한 상호작용을 보여줌.

# 3) **상황과 분위기에 알맞게 대화 하는가?**  
#    - **세부 기준:**  
#      a. 대화가 진행되는 상황 및 분위기를 충분히 고려하여 적절한 어휘와 표현을 사용하는가?  
#    - **평가 예시:**  
#      - 0~24점: 대화의 상황이나 분위기를 전혀 고려하지 않고, 부적절하거나 혼란스러운 어휘와 표현을 사용함.
#      - 25~49점: 상황이나 분위기에 대한 반영이 극히 미흡하여, 어휘 선택과 표현이 부적절해 대화의 흐름에 부정적인 영향을 줌.
#      - 50~74점: 기본적인 상황 반영은 있으나, 일부 표현이 어울리지 않거나 부적절하여 전체 분위기를 완전히 살리지 못함.
#      - 75~89점: 대화의 상황과 분위기를 대부분 잘 반영하며, 어휘와 표현이 적절하나 약간의 부자연스러움이 존재함.
#      - 90~100점: 모든 발언이 상황과 분위기에 완벽히 부합하며, 어휘와 표현 사용이 매우 자연스럽고 효과적임.

# 4) **대화의 목적과 내용을 효과적으로 구성하는가?**  
#    - **세부 기준:**  
#      a. 대화의 의도와 목표를 명확하게 전달하는가?  
#      b. 논점을 분명하게 요약하여 전달하는가?  
#      c. 청자가 흥미를 가질만한 주제와 내용을 효과적으로 구성하는가?  
#    - **평가 예시:**  
#      - 0~24점: 대화의 의도나 목표, 논점 전달이 전혀 이루어지지 않고, 내용 구성에 큰 혼란과 부재가 있음.
#      - 25~49점: 일부 대화의 목적이나 목표가 전달되지만, 논점 요약과 내용 구성이 매우 부족하여 청자의 이해를 돕지 못함.
#      - 50~74점: 대화의 기본적인 목적과 논점은 전달되지만, 내용 구성에서 명확성이나 체계적인 정리가 부족함.
#      - 75~89점: 대화의 목적과 논점이 명확하게 전달되며, 내용 구성 또한 상당히 효과적이지만, 약간의 보완이 필요한 부분이 있음.
#      - 90~100점: 대화의 목적이 매우 명확하며, 논점이 효과적으로 요약되고, 청자의 흥미를 끌어내는 내용 구성이 돋보임.

# 5) **대화 중 예의를 지키고, 긍정적인 말투와 태도를 유지했는가?**  
#    - **세부 기준:**  
#      a. 대화 내용에 적절한 감정 표현(극적인 표현 포함)을 사용하는가?  
#      b. 청자의 입장을 존중하며, 화자의 주장을 경청하는가?  
#      c. 상황과 맥락을 충분히 고려하여 응답하는가?  
#      d. 화자의 이야기를 평가하거나 판단하지 않고, 공정한 태도를 유지하는가?  
#    - **평가 예시:**  
#      - 0~24점: 예의가 전혀 지켜지지 않고, 부정적인 말투와 공격적인 태도로 대화에 참여함.
#      - 25~49점: 예의를 갖추려는 시도가 미흡하며, 부정적인 표현이나 판단적인 언급이 자주 나타나 대화 분위기를 해침.
#      - 50~74점: 대체로 예의를 지키려는 모습이 보이나, 일부 표현이나 판단에서 미묘한 문제가 발생하여 개선의 여지가 있음.
#      - 75~89점: 대부분의 상황에서 예의를 잘 지키며, 긍정적인 말투와 태도를 유지하지만, 소수의 상황에서 약간의 개선이 필요한 부분이 있음.
#      - 90~100점: 모든 상황에서 예의 바른 태도와 긍정적인 말투를 유지하며, 상대방의 의견을 완벽하게 존중하고 공정하게 대응함.