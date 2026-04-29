import request from './request'

// 前台接口
export function getComments(articleId) {
  return request.get(`/comment/list/${articleId}`)
}

export function addComment(data) {
  return request.post('/comment/add', data)
}

// 后台接口
export function getAdminCommentList(params) {
  return request.get('/comment/admin/list', { params })
}

export function deleteComment(id) {
  return request.delete(`/comment/admin/${id}`)
}

export function approveComment(id) {
  return request.post(`/comment/admin/approve/${id}`)
}