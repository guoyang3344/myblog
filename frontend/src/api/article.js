import request from './request'

// 前台接口
export function getArticleList(params) {
  return request.get('/article/list', { params })
}

export function getArticleDetail(id) {
  return request.get(`/article/${id}`)
}

export function getTopArticles() {
  return request.get('/article/top')
}

export function getCategories() {
  return request.get('/article/categories')
}

// 后台接口
export function getAdminArticleList(params) {
  return request.get('/article/admin/list', { params })
}

export function getAdminArticleDetail(id) {
  return request.get(`/article/admin/${id}`)
}

export function saveArticle(data) {
  return request.post('/article/admin/save', data)
}

export function deleteArticle(id) {
  return request.delete(`/article/admin/${id}`)
}