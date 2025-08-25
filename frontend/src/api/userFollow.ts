import request from '../utils/request'

export interface UserFollow {
  id: number
  followerId: number
  followingId: number
  status: number
  followType: number
  remarkName?: string
  createTime: string
  updateTime: string
}

export interface UserFollowVO {
  id: number
  userId: number
  username: string
  nickname?: string
  avatar?: string
  followType: number
  remarkName?: string
  createTime: string
  isMutualFollow?: boolean
  userStatus: number
  lastActiveTime?: string
}

export interface UserFollowDTO {
  followingId: number
  followType?: number
  remarkName?: string
}

export interface UserFollowQueryDTO {
  userId?: number
  queryType?: number
  page?: number
  size?: number
  followType?: number
  keyword?: string
}

export interface UserFollowPageResult {
  records: UserFollowVO[]
  total: number
  size: number
  current: number
  pages: number
}

/**
 * 关注用户
 */
export function followUser(data: UserFollowDTO): Promise<UserFollow> {
  return request<{ code: number; message: string; data: UserFollow}>({
    url: '/api/user-follow',
    method: 'POST',
    data
  }).then((res: any) => res.data as UserFollow)
}

/**
 * 取消关注
 */
export function unfollowUser(followingId: number): Promise<void> {
  return request<{ code: number; message: string; data: void}>({
    url: `/api/user-follow/${followingId}`,
    method: 'DELETE'
  }).then((res: any) => res.data as void)
}

/**
 * 更新关注设置
 */
export function updateFollowSettings(followingId: number, data: UserFollowDTO): Promise<UserFollow> {
  return request<{ code: number; message: string; data: UserFollow}>({
    url: `/api/user-follow/${followingId}`,
    method: 'PUT',
    data
  }).then((res: any) => res.data as UserFollow)
}

/**
 * 分页查询关注列表
 */
export function getFollowingPage(params: UserFollowQueryDTO): Promise<UserFollowPageResult> {
  return request<{ code: number; message: string; data: UserFollowPageResult}>({
    url: '/api/user-follow/following',
    method: 'GET',
    params
  }).then((res: any) => res.data as UserFollowPageResult)
}

/**
 * 分页查询粉丝列表
 */
export function getFollowersPage(params: UserFollowQueryDTO): Promise<UserFollowPageResult> {
  return request<{ code: number; message: string; data: UserFollowPageResult}>({
    url: '/api/user-follow/followers',
    method: 'GET',
    params
  }).then((res: any) => res.data as UserFollowPageResult)
}

/**
 * 查询是否已关注
 */
export function checkIsFollowing(followingId: number): Promise<boolean> {
  return request<{ code: number; message: string; data: boolean}>({
    url: `/api/user-follow/check/${followingId}`,
    method: 'GET'
  }).then((res: any) => res.data as boolean)
}

/**
 * 获取关注数量
 */
export function getFollowingCount(userId: number): Promise<number> {
  return request<{ code: number; message: string; data: number}>({
    url: `/api/user-follow/count/following/${userId}`,
    method: 'GET'
  }).then((res: any) => res.data as number)
}

/**
 * 获取粉丝数量
 */
export function getFollowersCount(userId: number): Promise<number> {
  return request<{ code: number; message: string; data: number}>({
    url: `/api/user-follow/count/followers/${userId}`,
    method: 'GET'
  }).then((res: any) => res.data as number)
}

/**
 * 获取互相关注用户列表
 */
export function getMutualFollowUsers(userId: number): Promise<UserFollowVO[]> {
  return request<{ code: number; message: string; data: UserFollowVO[]}>({
    url: `/api/user-follow/mutual/${userId}`,
    method: 'GET'
  }).then((res: any) => res.data as UserFollowVO[])
}

/**
 * 批量查询关注状态
 */
export function batchCheckFollowStatus(userIds: number[]): Promise<boolean[]> {
  return request<{ code: number; message: string; data: boolean[]}>({
    url: '/api/user-follow/batch-check',
    method: 'POST',
    data: userIds
  }).then((res: any) => res.data as boolean[])
}
