import request from './request'

// 前台接口
export function toggleFavorite(articleId) {
  return request.post('/favorite/toggle', { articleId })
}

export function checkFavorite(articleId) {
  return request.get(`/favorite/check/${articleId}`)
}

// 后台接口
export function getAdminFavoriteList(params) {
  return request.get('/favorite/admin/list', { params })
}