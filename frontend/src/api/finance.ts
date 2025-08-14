import request from '../utils/request'

export interface Transaction {
  id: number
  type: number // 1-收入，2-支出
  amount: number
  categoryId: number
  categoryName: string
  categoryIcon: string
  categoryColor: string
  description: string
  transactionDate: string
  paymentMethod: number
  paymentMethodName: string
  remark?: string
  status: number
  createTime: string
}

export interface TransactionCreateDTO {
  type: number
  amount: number
  categoryId: number
  description: string
  transactionDate?: string
  paymentMethod?: number
  remark?: string
}

export interface TransactionQueryDTO {
  page?: number
  size?: number
  type?: number
  categoryId?: number
  startDate?: string
  endDate?: string
  keyword?: string
  paymentMethod?: number
  minAmount?: number
  maxAmount?: number
}

export interface Category {
  id: number
  userId: number
  name: string
  icon: string
  color: string
  type: number // 1-收入分类，2-支出分类
  sortOrder: number
  enabled: number
  createTime: string
  updateTime: string
}

export interface FinanceStats {
  monthIncome: number
  monthExpense: number
  monthBalance: number
  yearIncome: number
  yearExpense: number
  yearBalance: number
  totalAssets: number
  totalLiabilities: number
  netWorth: number
  expenseCategoryStats: CategoryStats[]
  incomeCategoryStats: CategoryStats[]
  weeklyExpenseTrend: DailyStats[]
  weeklyIncomeTrend: DailyStats[]
  paymentMethodStats: PaymentMethodStats[]
}

export interface CategoryStats {
  categoryId: number
  categoryName: string
  categoryIcon: string
  categoryColor: string
  amount: number
  percentage: number
}

export interface DailyStats {
  date: string
  amount: number
}

export interface PaymentMethodStats {
  paymentMethod: number
  paymentMethodName: string
  amount: number
  percentage: number
}

/**
 * 创建交易记录
 */
export function createTransaction(data: TransactionCreateDTO): Promise<Transaction> {
  return request<{ code: number; message: string; data: Transaction}>({
    url: '/api/transaction',
    method: 'POST',
    data
  }).then((res: any) => res.data as Transaction)
}

/**
 * 分页查询交易记录
 */
export function getTransactionPage(params: TransactionQueryDTO): Promise<{ records: Transaction[]; total: number; size: number; current: number; pages: number }> {
  return request<{ code: number; message: string; data: { records: Transaction[]; total: number; size: number; current: number; pages: number } }>({
    url: '/api/transaction/page',
    method: 'GET',
    params
  }).then((res: any) => res.data)
}

/**
 * 获取交易记录详情
 */
export function getTransactionDetail(id: number): Promise<Transaction> {
  return request<{ code: number; message: string; data: Transaction}>({
    url: `/api/transaction/${id}`,
    method: 'GET'
  }).then((res: any) => res.data as Transaction)
}

/**
 * 删除交易记录
 */
export function deleteTransaction(id: number): Promise<void> {
  return request<{ code: number; message: string; data: void}>({
    url: `/api/transaction/${id}`,
    method: 'DELETE'
  }).then((res: any) => res.data as void)
}

/**
 * 获取财务统计
 */
export function getFinanceStats(): Promise<FinanceStats> {
  return request<{ code: number; message: string; data: FinanceStats}>({
    url: '/api/transaction/stats',
    method: 'GET'
  }).then((res: any) => res.data as FinanceStats)
}

/**
 * 获取用户分类列表
 */
export function getUserCategories(type: number): Promise<Category[]> {
  return request<{ code: number; message: string; data: Category[]}>({
    url: '/api/category',
    method: 'GET',
    params: { type }
  }).then((res: any) => res.data as Category[])
}

/**
 * 创建分类
 */
export function createCategory(data: Partial<Category>): Promise<Category> {
  return request<{ code: number; message: string; data: Category}>({
    url: '/api/category',
    method: 'POST',
    data
  }).then((res: any) => res.data as Category)
}

/**
 * 更新分类
 */
export function updateCategory(id: number, data: Partial<Category>): Promise<Category> {
  return request<{ code: number; message: string; data: Category}>({
    url: `/api/category/${id}`,
    method: 'PUT',
    data
  }).then((res: any) => res.data as Category)
}

/**
 * 删除分类
 */
export function deleteCategory(id: number): Promise<void> {
  return request<{ code: number; message: string; data: void}>({
    url: `/api/category/${id}`,
    method: 'DELETE'
  }).then((res: any) => res.data as void)
}
