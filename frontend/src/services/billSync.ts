/**
 * 账单同步服务
 * 支持从CSV、Excel等格式导入账单数据
 */

export interface BillImportResult {
  success: boolean
  message: string
  importedCount: number
  failedCount: number
  errors: string[]
}

export interface BillData {
  date: string
  amount: number
  type: 'income' | 'expense'
  category: string
  description: string
  account?: string
}

export class BillSyncService {
  /**
   * 从CSV文件导入账单
   */
  async importFromCSV(file: File): Promise<BillImportResult> {
    return new Promise((resolve) => {
      const reader = new FileReader()
      const result: BillImportResult = {
        success: false,
        message: '',
        importedCount: 0,
        failedCount: 0,
        errors: []
      }

      reader.onload = (e) => {
        try {
          const csv = e.target?.result as string
          const lines = csv.split('\n')
          const headers = lines[0].split(',').map(h => h.trim())
          
          // 验证CSV格式
          const requiredHeaders = ['date', 'amount', 'type', 'category', 'description']
          const missingHeaders = requiredHeaders.filter(h => !headers.includes(h))
          
          if (missingHeaders.length > 0) {
            result.message = `缺少必要的列: ${missingHeaders.join(', ')}`
            resolve(result)
            return
          }

          // 解析数据行
          for (let i = 1; i < lines.length; i++) {
            if (!lines[i].trim()) continue
            
            try {
              const values = this.parseCSVLine(lines[i])
              const billData = this.parseBillRow(headers, values)
              
              if (this.validateBillData(billData)) {
                result.importedCount++
              } else {
                result.failedCount++
                result.errors.push(`第${i + 1}行数据验证失败`)
              }
            } catch (error) {
              result.failedCount++
              result.errors.push(`第${i + 1}行解析失败: ${error}`)
            }
          }

          result.success = result.importedCount > 0
          result.message = `成功导入 ${result.importedCount} 条记录，失败 ${result.failedCount} 条`
          
        } catch (error) {
          result.message = `解析CSV文件失败: ${error}`
        }
        
        resolve(result)
      }

      reader.onerror = () => {
        result.message = '读取文件失败'
        resolve(result)
      }

      reader.readAsText(file)
    })
  }

  /**
   * 从Excel文件导入账单
   */
  async importFromExcel(file: File): Promise<BillImportResult> {
    // 这里需要集成Excel解析库，如xlsx
    // 暂时返回模拟结果
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          success: true,
          message: 'Excel导入功能开发中，请使用CSV格式',
          importedCount: 0,
          failedCount: 0,
          errors: []
        })
      }, 1000)
    })
  }

  /**
   * 导出账单为CSV
   */
  exportToCSV(bills: BillData[]): string {
    const headers = ['日期', '金额', '类型', '分类', '描述', '账户']
    const csvContent = [
      headers.join(','),
      ...bills.map(bill => [
        bill.date,
        bill.amount,
        bill.type === 'income' ? '收入' : '支出',
        bill.category,
        bill.description,
        bill.account || ''
      ].join(','))
    ].join('\n')

    return csvContent
  }

  /**
   * 下载CSV文件
   */
  downloadCSV(bills: BillData[], filename: string = '账单数据.csv'): void {
    const csvContent = this.exportToCSV(bills)
    const blob = new Blob(['\ufeff' + csvContent], { type: 'text/csv;charset=utf-8;' })
    const link = document.createElement('a')
    
    if (link.download !== undefined) {
      const url = URL.createObjectURL(blob)
      link.setAttribute('href', url)
      link.setAttribute('download', filename)
      link.style.visibility = 'hidden'
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
    }
  }

  /**
   * 解析CSV行（处理引号内的逗号）
   */
  private parseCSVLine(line: string): string[] {
    const result: string[] = []
    let current = ''
    let inQuotes = false
    
    for (let i = 0; i < line.length; i++) {
      const char = line[i]
      
      if (char === '"') {
        inQuotes = !inQuotes
      } else if (char === ',' && !inQuotes) {
        result.push(current.trim())
        current = ''
      } else {
        current += char
      }
    }
    
    result.push(current.trim())
    return result
  }

  /**
   * 解析账单行数据
   */
  private parseBillRow(headers: string[], values: string[]): BillData {
    const data: any = {}
    
    headers.forEach((header, index) => {
      data[header] = values[index] || ''
    })

    return {
      date: data.date || '',
      amount: parseFloat(data.amount) || 0,
      type: data.type === '收入' || data.type === 'income' ? 'income' : 'expense',
      category: data.category || '',
      description: data.description || '',
      account: data.account || ''
    }
  }

  /**
   * 验证账单数据
   */
  private validateBillData(bill: BillData): boolean {
    if (!bill.date || !bill.description) return false
    if (bill.amount <= 0) return false
    if (!bill.category) return false
    
    // 验证日期格式
    const dateRegex = /^\d{4}-\d{2}-\d{2}$/
    if (!dateRegex.test(bill.date)) return false
    
    return true
  }

  /**
   * 获取支持的导入格式
   */
  getSupportedFormats(): string[] {
    return ['.csv', '.xlsx', '.xls']
  }

  /**
   * 获取CSV模板
   */
  getCSVTemplate(): string {
    const headers = ['date', 'amount', 'type', 'category', 'description', 'account']
    const example = ['2024-01-15', '100.00', 'expense', '餐饮', '午餐', '现金']
    
    return [
      headers.join(','),
      example.join(','),
      '2024-01-16,2000.00,income,工资,月薪,银行卡'
    ].join('\n')
  }
}

export const billSyncService = new BillSyncService()
