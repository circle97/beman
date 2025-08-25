import request from '../utils/request'

export interface Report {
  id: number
  reporterId: number
  reporterName: string
  contentType: number
  contentId: number
  reasonType: number
  description?: string
  evidence?: string
  status: number
  result?: string
  handlerId?: number
  handlerName?: string
  handleTime?: string
  priority: number
  createTime: string
  updateTime: string
}

export interface ReportVO {
  id: number
  reporterId: number
  reporterName: string
  contentType: number
  contentId: number
  contentSummary?: string
  reasonType: number
  reasonDescription?: string
  description?: string
  evidence?: string
  status: number
  statusDescription?: string
  result?: string
  handlerId?: number
  handlerName?: string
  handleTime?: string
  priority: number
  priorityDescription?: string
  createTime: string
  updateTime: string
}

export interface ReportDTO {
  contentType: number
  contentId: number
  reasonType: number
  description?: string
  evidence?: string
}

export interface ReportQueryDTO {
  status?: number
  contentType?: number
  priority?: number
  handlerId?: number
  keyword?: string
  page?: number
  size?: number
}

export interface ReportPageResult {
  records: ReportVO[]
  total: number
  size: number
  current: number
  pages: number
}

/**
 * 创建举报
 */
export function createReport(data: ReportDTO): Promise<Report> {
  return request<{ code: number; message: string; data: Report}>({
    url: '/api/report',
    method: 'POST',
    data
  }).then((res: any) => res.data as Report)
}

/**
 * 分页查询举报列表
 */
export function getReportPage(params: ReportQueryDTO): Promise<ReportPageResult> {
  return request<{ code: number; message: string; data: ReportPageResult}>({
    url: '/api/report/page',
    method: 'GET',
    params
  }).then((res: any) => res.data as ReportPageResult)
}

/**
 * 获取举报详情
 */
export function getReportDetail(id: number): Promise<ReportVO> {
  return request<{ code: number; message: string; data: ReportVO}>({
    url: `/api/report/${id}`,
    method: 'GET'
  }).then((res: any) => res.data as ReportVO)
}

/**
 * 处理举报
 */
export function handleReport(id: number, result: string, status: number): Promise<void> {
  return request<{ code: number; message: string; data: void}>({
    url: `/api/report/${id}/handle`,
    method: 'PUT',
    params: { result, status }
  }).then((res: any) => res.data as void)
}

/**
 * 驳回举报
 */
export function rejectReport(id: number, reason: string): Promise<void> {
  return request<{ code: number; message: string; data: void}>({
    url: `/api/report/${id}/reject`,
    method: 'PUT',
    params: { reason }
  }).then((res: any) => res.data as void)
}

/**
 * 关闭举报
 */
export function closeReport(id: number, reason: string): Promise<void> {
  return request<{ code: number; message: string; data: void}>({
    url: `/api/report/${id}/close`,
    method: 'PUT',
    params: { reason }
  }).then((res: any) => res.data as void)
}

/**
 * 获取用户的举报历史
 */
export function getUserReports(userId: number): Promise<ReportVO[]> {
  return request<{ code: number; message: string; data: ReportVO[]}>({
    url: `/api/report/user/${userId}`,
    method: 'GET'
  }).then((res: any) => res.data as ReportVO[])
}

/**
 * 获取内容的举报数量
 */
export function getContentReportCount(contentType: number, contentId: number): Promise<number> {
  return request<{ code: number; message: string; data: number}>({
    url: `/api/report/count/${contentType}/${contentId}`,
    method: 'GET'
  }).then((res: any) => res.data as number)
}

/**
 * 获取待处理举报数量
 */
export function getPendingReportCount(): Promise<number> {
  return request<{ code: number; message: string; data: number}>({
    url: '/api/report/count/pending',
    method: 'GET'
  }).then((res: any) => res.data as number)
}

/**
 * 获取高优先级举报数量
 */
export function getHighPriorityReportCount(): Promise<number> {
  return request<{ code: number; message: string; data: number}>({
    url: '/api/report/count/high-priority',
    method: 'GET'
  }).then((res: any) => res.data as number)
}

/**
 * 批量处理举报
 */
export function batchHandleReports(reportIds: number[], result: string, status: number): Promise<void> {
  return request<{ code: number; message: string; data: void}>({
    url: '/api/report/batch-handle',
    method: 'PUT',
    params: { reportIds, result, status }
  }).then((res: any) => res.data as void)
}
