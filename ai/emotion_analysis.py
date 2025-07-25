import torch
from transformers import BertTokenizer, BertForSequenceClassification
import json

class EmotionDetector:
    def __init__(self):
        self.tokenizer = BertTokenizer.from_pretrained('bert-base-chinese')
        self.model = BertForSequenceClassification.from_pretrained('./emotion_model')
        self.emotion_labels = [
            'neutral', 'happy', 'sad', 'angry', 'surprised',
            'worried', 'disappointed', '渴望关心', '抱怨', '寻求建议'
        ]

    def analyze(self, text):
        inputs = self.tokenizer(text, return_tensors="pt", padding=True, truncation=True)
        outputs = self.model(**inputs)
        logits = outputs.logits
        probabilities = torch.nn.functional.softmax(logits, dim=1)
        max_prob_idx = torch.argmax(probabilities).item()
        confidence = probabilities[0][max_prob_idx].item()

        return {
            "emotion": self.emotion_labels[max_prob_idx],
            "confidence": round(confidence, 4),
            "suggestion": self._get_suggestion(self.emotion_labels[max_prob_idx])
        }

    def _get_suggestion(self, emotion):
        suggestions = {
            "渴望关心": "可以尝试主动询问对方的感受，例如：'你最近是不是感觉压力很大？'",
            "抱怨": "先倾听并认可对方的感受，避免急于辩解",
            "寻求建议": "提供2-3个具体可行的方案，而非空泛的安慰"
            # 其他情绪建议...
        }
        return suggestions.get(emotion, "保持耐心和理解，继续观察对方的情绪变化")

# API接口
from fastapi import FastAPI
import uvicorn

app = FastAPI()
detector = EmotionDetector()

@app.post("/analyze")
async def analyze_emotion(text: str):
    result = detector.analyze(text)
    return result

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)