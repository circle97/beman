import request from '../utils/request'

export interface Reminder {
  id: number
  scheduleId: number
  userId: number
  type: number
  reminderTime: string
  advanceMinutes: number
  content: string
  status: number
  sendTime: string
  failReason: string
  retryCount: number
  maxRetryCount: number
  createTime: string
  updateTime: string
}

export interface GiftRecommendation {
  id: number
  name: string
  description: string
  imageUrl: string
  minPrice: number
  maxPrice: number
  minAge: number
  maxAge: number
  gender: number
  rating: number
  reason: string
  purchaseUrl: string
  occasions: string
  categories: string
  tags: string
  status: number
  createTime: string
  updateTime: string
}

export interface Schedule {
  id: number
  userId: number
  title: string
  description: string
  type: number // 1-生日，2-纪念日，3-节日，4-约会，5-会议，6-其他
  priority: number // 1-低，2-中，3-高，4-紧急
  startTime: string
  endTime: string
  isAllDay: boolean
  repeatType: number // 0-不重复，1-每天，2-每周，3-每月，4-每年
  repeatEndTime: string
  reminderSettings: string
  tags: string
  location: string
  status: number // 1-正常，2-已完成，3-已取消
  createTime: string
  updateTime: string
}

export interface ScheduleCreateDTO {
  title: string
  description: string
  type: number
  priority: number
  startTime: string
  endTime: string
  isAllDay: boolean
  repeatType: number
  repeatEndTime: string
  reminderSettings: ReminderSetting[]
  tags: string[]
  location: string
}

export interface ReminderSetting {
  type: number // 1-邮件，2-短信，3-推送，4-微信，5-钉钉
  advanceMinutes: number
  content: string
}

export interface ScheduleQueryDTO {
  page?: number
  size?: number
  keyword?: string
  type?: number
  priority?: number
  status?: number
  startTimeFrom?: string
  startTimeTo?: string
  endTimeFrom?: string
  endTimeTo?: string
  dateFrom?: string
  dateTo?: string
  tags?: string[]
  location?: string
  includeRepeating?: boolean
  sortBy?: string
  sortOrder?: string
}

export interface ScheduleStats {
  todayCount: number
  weekCount: number
  monthCount: number
  completedCount: number
  pendingCount: number
  highPriorityCount: number
}

/**
 * 创建日程
 */
export function createSchedule(data: ScheduleCreateDTO): Promise<Schedule> {
  return request<{ code: number; message: string; data: Schedule}>({
    url: '/api/schedule',
    method: 'POST',
    data
  }).then((res: any) => res.data as Schedule)
}

/**
 * 更新日程
 */
export function updateSchedule(id: number, data: ScheduleCreateDTO): Promise<Schedule> {
  return request<{ code: number; message: string; data: Schedule}>({
    url: `/api/schedule/${id}`,
    method: 'PUT',
    data
  }).then((res: any) => res.data as Schedule)
}

/**
 * 删除日程
 */
export function deleteSchedule(id: number): Promise<void> {
  return request<{ code: number; message: string; data: void}>({
    url: `/api/schedule/${id}`,
    method: 'DELETE'
  }).then((res: any) => res.data as void)
}

/**
 * 获取日程详情
 */
export function getScheduleDetail(id: number): Promise<Schedule> {
  return request<{ code: number; message: string; data: Schedule}>({
    url: `/api/schedule/${id}`,
    method: 'GET'
  }).then((res: any) => res.data as Schedule)
}

/**
 * 分页查询日程
 */
export function getSchedulePage(params: ScheduleQueryDTO): Promise<{ records: Schedule[]; total: number; size: number; current: number; pages: number }> {
  return request<{ code: number; message: string; data: { records: Schedule[]; total: number; size: number; current: number; pages: number } }>({
    url: '/api/schedule/page',
    method: 'GET',
    params
  }).then((res: any) => res.data)
}

/**
 * 获取指定日期的日程列表
 */
export function getSchedulesByDate(date: string): Promise<Schedule[]> {
  return request<{ code: number; message: string; data: Schedule[]}>({
    url: `/api/schedule/date/${date}`,
    method: 'GET'
  }).then((res: any) => res.data as Schedule[])
}

/**
 * 获取指定日期范围的日程列表
 */
export function getSchedulesByDateRange(startDate: string, endDate: string): Promise<Schedule[]> {
  return request<{ code: number; message: string; data: Schedule[]}>({
    url: '/api/schedule/range',
    method: 'GET',
    params: { startDate, endDate }
  }).then((res: any) => res.data as Schedule[])
}

/**
 * 获取即将到来的日程
 */
export function getUpcomingSchedules(days: number = 7): Promise<Schedule[]> {
  return request<{ code: number; message: string; data: Schedule[]}>({
    url: '/api/schedule/upcoming',
    method: 'GET',
    params: { days }
  }).then((res: any) => res.data as Schedule[])
}

/**
 * 获取重要日程
 */
export function getImportantSchedules(): Promise<Schedule[]> {
  return request<{ code: number; message: string; data: Schedule[]}>({
    url: '/api/schedule/important',
    method: 'GET'
  }).then((res: any) => res.data as Schedule[])
}

/**
 * 完成日程
 */
export function completeSchedule(id: number): Promise<void> {
  return request<{ code: number; message: string; data: void}>({
    url: `/api/schedule/${id}/complete`,
    method: 'POST'
  }).then((res: any) => res.data as void)
}

/**
 * 取消日程
 */
export function cancelSchedule(id: number): Promise<void> {
  return request<{ code: number; message: string; data: void}>({
    url: `/api/schedule/${id}/cancel`,
    method: 'POST'
  }).then((res: any) => res.data as void)
}

/**
 * 智能识别重要日期
 */
export function identifyImportantDates(text: string): Promise<Schedule[]> {
  return request<{ code: number; message: string; data: Schedule[]}>({
    url: '/api/schedule/identify',
    method: 'POST',
    params: { text }
  }).then((res: any) => res.data as Schedule[])
}

/**
 * 生成重复日程
 */
export function generateRepeatingSchedules(id: number, untilDate: string): Promise<Schedule[]> {
  return request<{ code: number; message: string; data: Schedule[]}>({
    url: `/api/schedule/${id}/generate`,
    method: 'POST',
    params: { untilDate }
  }).then((res: any) => res.data as Schedule[])
}

/**
 * 获取日程统计信息
 */
export function getScheduleStats(): Promise<ScheduleStats> {
  return request<{ code: number; message: string; data: ScheduleStats}>({
    url: '/api/schedule/stats',
    method: 'GET'
  }).then((res: any) => res.data as ScheduleStats)
}

/**
 * 创建分级提醒
 */
export function createTieredReminders(id: number): Promise<Reminder[]> {
  return request<{ code: number; message: string; data: Reminder[]}>({
    url: `/api/schedule/${id}/tiered-reminders`,
    method: 'POST'
  }).then((res: any) => res.data as Reminder[])
}

/**
 * 获取分级提醒配置
 */
export function getReminderConfig(): Promise<any> {
  return request<{ code: number; message: string; data: any}>({
    url: '/api/schedule/reminder-config',
    method: 'GET'
  }).then((res: any) => res.data)
}

/**
 * 发送分级提醒
 */
export function sendTieredReminders(id: number): Promise<void> {
  return request<{ code: number; message: string; data: void}>({
    url: `/api/schedule/${id}/send-reminders`,
    method: 'POST'
  }).then((res: any) => res.data as void)
}

/**
 * 获取礼物推荐
 */
export function getGiftRecommendations(params: {
  targetAge?: number
  targetGender?: number
  occasions?: string[]
  maxBudget?: number
  limit?: number
}): Promise<GiftRecommendation[]> {
  return request<{ code: number; message: string; data: GiftRecommendation[]}>({
    url: '/api/schedule/gift-recommendations',
    method: 'GET',
    params
  }).then((res: any) => res.data as GiftRecommendation[])
}

/**
 * 根据场合获取礼物推荐
 */
export function getGiftRecommendationsByOccasion(occasion: string, limit: number = 10): Promise<GiftRecommendation[]> {
  return request<{ code: number; message: string; data: GiftRecommendation[]}>({
    url: `/api/schedule/gift-recommendations/occasion/${occasion}`,
    method: 'GET',
    params: { limit }
  }).then((res: any) => res.data as GiftRecommendation[])
}
