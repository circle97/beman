import request from '../utils/request'

export interface Post {
  id: number
  title: string
  content: string
  authorName: string
  authorAvatar?: string
  likeCount: number
  commentCount: number
  viewCount: number
  createTime: string
  tags?: string[]
  isAnonymous?: boolean
  contentType?: number
  voiceUrl?: string
}

export interface PostCreateDTO {
  title: string
  content: string
  tags: string[]
  isAnonymous?: boolean
  contentType?: number
  voiceUrl?: string
}

export interface PostQueryDTO {
  page?: number
  size?: number
  tags?: string[]
  keyword?: string
  sortType?: number
  contentType?: number
}

export interface PostPageResult {
  records: Post[]
  total: number
  size: number
  current: number
  pages: number
}

/**
 * 创建帖子
 */
export function createPost(data: PostCreateDTO): Promise<Post> {
  return request<{ code: number; message: string; data: Post}>({
    url: '/api/post',
    method: 'POST',
    data
  }).then((res: any) => res.data as Post)
}

/**
 * 分页查询帖子列表
 */
export function getPostPage(params: PostQueryDTO): Promise<PostPageResult> {
  return request<{ code: number; message: string; data: PostPageResult}>({
    url: '/api/post/page',
    method: 'GET',
    params
  }).then((res: any) => res.data as PostPageResult)
}

/**
 * 获取帖子详情
 */
export function getPostDetail(id: number): Promise<Post> {
  return request<{ code: number; message: string; data: Post}>({
    url: `/api/post/${id}`,
    method: 'GET'
  }).then((res: any) => res.data as Post)
}

/**
 * 点赞帖子
 */
export function likePost(id: number): Promise<void> {
  return request<{ code: number; message: string; data: void}>({
    url: `/api/post/${id}/like`,
    method: 'POST'
  }).then((res: any) => res.data as void)
}

/**
 * 取消点赞
 */
export function unlikePost(id: number): Promise<void> {
  return request<{ code: number; message: string; data: void}>({
    url: `/api/post/${id}/like`,
    method: 'DELETE'
  }).then((res: any) => res.data as void)
}

/**
 * 删除帖子
 */
export function deletePost(id: number): Promise<void> {
  return request<{ code: number; message: string; data: void}>({
    url: `/api/post/${id}`,
    method: 'DELETE'
  }).then((res: any) => res.data as void)
}

/**
 * 获取热门标签
 */
export function getHotTags(): Promise<string[]> {
  return request<{ code: number; message: string; data: string[]}>({
    url: '/api/post/hot-tags',
    method: 'GET'
  }).then((res: any) => res.data as string[])
}
