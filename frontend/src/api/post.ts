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
  tags?: string[]
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
export function createPost(data: PostCreateDTO) {
  return request<Post>({
    url: '/api/post',
    method: 'POST',
    data
  })
}

/**
 * 分页查询帖子列表
 */
export function getPostPage(params: PostQueryDTO) {
  return request<PostPageResult>({
    url: '/api/post/page',
    method: 'GET',
    params
  })
}

/**
 * 获取帖子详情
 */
export function getPostDetail(id: number) {
  return request<Post>({
    url: `/api/post/${id}`,
    method: 'GET'
  })
}

/**
 * 点赞帖子
 */
export function likePost(id: number) {
  return request<void>({
    url: `/api/post/${id}/like`,
    method: 'POST'
  })
}

/**
 * 取消点赞
 */
export function unlikePost(id: number) {
  return request<void>({
    url: `/api/post/${id}/like`,
    method: 'DELETE'
  })
}

/**
 * 删除帖子
 */
export function deletePost(id: number) {
  return request<void>({
    url: `/api/post/${id}`,
    method: 'DELETE'
  })
}

/**
 * 获取热门标签
 */
export function getHotTags() {
  return request<string[]>({
    url: '/api/post/hot-tags',
    method: 'GET'
  })
}
