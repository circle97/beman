import request from '../utils/request'

export interface Reminder {
  id: number
  scheduleId: number
  userId: number
  type: number // 1-邮件，2-短信，3-推送，4-微信，5-钉钉
  reminderTime: string
  advanceMinutes: number
  content: string
  status: number // 1-待发送，2-已发送，3-发送失败，4-已取消
  retryCount: number
  maxRetryCount: number
  sendTime: string
  failReason: string
  createTime: string
  updateTime: string
}

export interface ReminderCreateDTO {
  scheduleId: number
  type: number
  reminderTime: string
  advanceMinutes: number
  content: string
  maxRetryCount: number
}

export interface ReminderQueryDTO {
  page?: number
  size?: number
  scheduleId?: number
  type?: number
  status?: number
  reminderTimeFrom?: string
  reminderTimeTo?: string
  includeCancelled?: boolean
}

export interface ReminderStats {
  pendingCount: number
  sentCount: number
  failedCount: number
  todaySentCount: number
  weekSentCount: number
}

/**
 * 创建提醒
 */
export function createReminder(data: ReminderCreateDTO): Promise<Reminder> {
  return request<{ code: number; message: string; data: Reminder}>({
    url: '/api/reminder',
    method: 'POST',
    data
  }).then((res: any) => res.data as Reminder)
}

/**
 * 更新提醒
 */
export function updateReminder(id: number, data: ReminderCreateDTO): Promise<Reminder> {
  return request<{ code: number; message: string; data: Reminder}>({
    url: `/api/reminder/${id}`,
    method: 'PUT',
    data
  }).then((res: any) => res.data as Reminder)
}

/**
 * 删除提醒
 */
export function deleteReminder(id: number): Promise<void> {
  return request<{ code: number; message: string; data: void}>({
    url: `/api/reminder/${id}`,
    method: 'DELETE'
  }).then((res: any) => res.data as void)
}

/**
 * 获取提醒详情
 */
export function getReminderDetail(id: number): Promise<Reminder> {
  return request<{ code: number; message: string; data: Reminder}>({
    url: `/api/reminder/${id}`,
    method: 'GET'
  }).then((res: any) => res.data as Reminder)
}

/**
 * 获取提醒列表
 */
export function getReminderList(params: ReminderQueryDTO): Promise<Reminder[]> {
  return request<{ code: number; message: string; data: Reminder[]}>({
    url: '/api/reminder/list',
    method: 'GET',
    params
  }).then((res: any) => res.data as Reminder[])
}

/**
 * 获取用户的提醒列表
 */
export function getUserReminders(userId: number): Promise<Reminder[]> {
  return request<{ code: number; message: string; data: Reminder[]}>({
    url: `/api/reminder/user/${userId}`,
    method: 'GET'
  }).then((res: any) => res.data as Reminder[])
}

/**
 * 标记提醒为已发送
 */
export function markAsSent(id: number): Promise<void> {
  return request<{ code: number; message: string; data: void}>({
    url: `/api/reminder/${id}/mark-sent`,
    method: 'POST'
  }).then((res: any) => res.data as void)
}

/**
 * 标记提醒为发送失败
 */
export function markAsFailed(id: number, reason: string): Promise<void> {
  return request<{ code: number; message: string; data: void}>({
    url: `/api/reminder/${id}/mark-failed`,
    method: 'POST',
    params: { reason }
  }).then((res: any) => res.data as void)
}

/**
 * 重试发送提醒
 */
export function retryReminder(id: number): Promise<void> {
  return request<{ code: number; message: string; data: void}>({
    url: `/api/reminder/${id}/retry`,
    method: 'POST'
  }).then((res: any) => res.data as void)
}

/**
 * 取消提醒
 */
export function cancelReminder(id: number): Promise<void> {
  return request<{ code: number; message: string; data: void}>({
    url: `/api/reminder/${id}/cancel`,
    method: 'POST'
  }).then((res: any) => res.data as void)
}

/**
 * 批量创建提醒（从日程创建）
 */
export function createRemindersFromSchedule(
  scheduleId: number, 
  reminderSettings: ReminderCreateDTO[]
): Promise<Reminder[]> {
  return request<{ code: number; message: string; data: Reminder[]}>({
    url: '/api/reminder/batch',
    method: 'POST',
    params: { scheduleId },
    data: reminderSettings
  }).then((res: any) => res.data as Reminder[])
}

/**
 * 获取即将到期的提醒
 */
export function getUpcomingReminders(minutes: number = 30): Promise<Reminder[]> {
  return request<{ code: number; message: string; data: Reminder[]}>({
    url: '/api/reminder/upcoming',
    method: 'GET',
    params: { minutes }
  }).then((res: any) => res.data as Reminder[])
}

/**
 * 获取提醒统计信息
 */
export function getReminderStats(): Promise<ReminderStats> {
  return request<{ code: number; message: string; data: ReminderStats}>({
    url: '/api/reminder/stats',
    method: 'GET'
  }).then((res: any) => res.data as ReminderStats)
}
