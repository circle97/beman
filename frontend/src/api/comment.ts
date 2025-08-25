import request from '../utils/request'

export interface Comment {
  id: number
  postId: number
  content: string
  authorId: number
  authorName: string
  authorAvatar?: string
  parentId?: number
  replyUserId?: number
  replyUserName?: string
  likeCount: number
  isAnonymous: boolean
  status: number
  createTime: string
  isLiked?: boolean
  children?: Comment[]
  childrenCount?: number
}

export interface CommentCreateDTO {
  postId: number
  content: string
  parentId?: number
  replyUserId?: number
  isAnonymous?: boolean
}

export interface CommentQueryDTO {
  postId?: number
  parentId?: number
  page?: number
  size?: number
  sortType?: number
}

export interface CommentPageResult {
  records: Comment[]
  total: number
  size: number
  current: number
  pages: number
}

/**
 * 创建评论
 */
export function createComment(data: CommentCreateDTO): Promise<Comment> {
  return request<{ code: number; message: string; data: Comment}>({
    url: '/api/comment',
    method: 'POST',
    data
  }).then((res: any) => res.data as Comment)
}

/**
 * 分页查询评论列表
 */
export function getCommentPage(params: CommentQueryDTO): Promise<CommentPageResult> {
  return request<{ code: number; message: string; data: CommentPageResult}>({
    url: '/api/comment/page',
    method: 'GET',
    params
  }).then((res: any) => res.data as CommentPageResult)
}

/**
 * 获取评论详情
 */
export function getCommentDetail(id: number): Promise<Comment> {
  return request<{ code: number; message: string; data: Comment}>({
    url: `/api/comment/${id}`,
    method: 'GET'
  }).then((res: any) => res.data as Comment)
}

/**
 * 点赞评论
 */
export function likeComment(id: number): Promise<void> {
  return request<{ code: number; message: string; data: void}>({
    url: `/api/comment/${id}/like`,
    method: 'POST'
  }).then((res: any) => res.data as void)
}

/**
 * 取消点赞评论
 */
export function unlikeComment(id: number): Promise<void> {
  return request<{ code: number; message: string; data: void}>({
    url: `/api/comment/${id}/like`,
    method: 'DELETE'
  }).then((res: any) => res.data as void)
}

/**
 * 删除评论
 */
export function deleteComment(id: number): Promise<void> {
  return request<{ code: number; message: string; data: void}>({
    url: `/api/comment/${id}`,
    method: 'DELETE'
  }).then((res: any) => res.data as void)
}

/**
 * 获取帖子的评论数量
 */
export function getCommentCount(postId: number): Promise<number> {
  return request<{ code: number; message: string; data: number}>({
    url: `/api/comment/count/${postId}`,
    method: 'GET'
  }).then((res: any) => res.data as number)
}
