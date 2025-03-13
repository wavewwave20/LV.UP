import OpenAI from "openai";

const openai = new OpenAI({
  apiKey: import.meta.env.VITE_OPENAI_API_KEY,
  dangerouslyAllowBrowser: true,
});

const generateSpeech = async (text, gender) => {
  try {
    const voice = gender === "male" ? "alloy" : "sage";

    const response = await openai.audio.speech.create({
      model: "tts-1",
      voice: voice,
      input: text,
    });

    // ArrayBuffer를 Blob으로 변환
    const audioBlob = new Blob([await response.arrayBuffer()], {
      type: "audio/mp3",
    });

    // Blob을 URL로 변환하여 브라우저에서 재생 가능하게 만듦
    return URL.createObjectURL(audioBlob);
  } catch (error) {
    console.error("TTS 변환 중 오류 발생:", error);
    return null;
  }
};

export default generateSpeech;
