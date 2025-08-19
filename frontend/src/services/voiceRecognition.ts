/**
 * 语音识别服务
 */

export interface VoiceRecognitionResult {
  text: string
  confidence: number
  duration: number
}

export interface ParsedTransaction {
  type: 'income' | 'expense'
  amount: number
  description: string
  category?: string
  date?: string
}

export class VoiceRecognitionService {
  private mediaRecorder: MediaRecorder | null = null
  private audioChunks: Blob[] = []
  private isRecording = false
  private stream: MediaStream | null = null

  async startRecording(): Promise<void> {
    try {
      this.stream = await navigator.mediaDevices.getUserMedia({ audio: true })
      this.mediaRecorder = new MediaRecorder(this.stream)
      this.audioChunks = []
      this.isRecording = true

      this.mediaRecorder.ondataavailable = (event) => {
        if (event.data.size > 0) {
          this.audioChunks.push(event.data)
        }
      }

      this.mediaRecorder.start()
    } catch (error) {
      console.error('启动语音录制失败:', error)
      throw new Error('无法访问麦克风，请检查权限设置')
    }
  }

  stopRecording(): Promise<VoiceRecognitionResult> {
    return new Promise((resolve, reject) => {
      if (!this.mediaRecorder || !this.isRecording) {
        reject(new Error('没有正在进行的录制'))
        return
      }

      this.mediaRecorder.onstop = async () => {
        try {
          const audioBlob = new Blob(this.audioChunks, { type: 'audio/wav' })
          const result = await this.recognizeSpeech(audioBlob)
          resolve(result)
        } catch (error) {
          reject(error)
        } finally {
          this.cleanup()
        }
      }

      this.mediaRecorder.stop()
    })
  }

  private async recognizeSpeech(audioBlob: Blob): Promise<VoiceRecognitionResult> {
    // 模拟识别结果
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    const mockResults = [
      '昨天买花200元',
      '今天工资收入5000元',
      '打车花了35块钱',
      '吃饭消费68元',
      '收到红包100元',
      '买书花了89元',
      '看电影120元',
      '超市购物156元'
    ]
    
    const randomResult = mockResults[Math.floor(Math.random() * mockResults.length)]
    
    return {
      text: randomResult,
      confidence: 0.85 + Math.random() * 0.1,
      duration: 2000 + Math.random() * 1000
    }
  }

  parseTransactionText(text: string): ParsedTransaction {
    const amountMatch = text.match(/(\d+(?:\.\d+)?)/)
    if (!amountMatch) {
      throw new Error('无法识别金额，请重新录制')
    }

    const amount = parseFloat(amountMatch[1])
    const incomeKeywords = ['收入', '工资', '红包', '收到', '赚', '赢', '奖金', '补贴']
    const type = incomeKeywords.some(keyword => text.includes(keyword)) ? 'income' : 'expense'
    
    const description = text.replace(amountMatch[0], '').trim() || (type === 'income' ? '收入' : '支出')

    return { type, amount, description }
  }

  private cleanup(): void {
    this.isRecording = false
    if (this.stream) {
      this.stream.getTracks().forEach(track => track.stop())
      this.stream = null
    }
    this.mediaRecorder = null
    this.audioChunks = []
  }

  getRecordingStatus(): boolean {
    return this.isRecording
  }
}

export const voiceRecognitionService = new VoiceRecognitionService()
