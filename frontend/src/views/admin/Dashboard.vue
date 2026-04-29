<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card card">
        <div class="stat-icon articles">
          <el-icon :size="32"><Document /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-number">{{ stats.articles }}</div>
          <div class="stat-label">文章总数</div>
        </div>
      </div>
      <div class="stat-card card">
        <div class="stat-icon views">
          <el-icon :size="32"><View /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-number">{{ stats.views }}</div>
          <div class="stat-label">总浏览量</div>
        </div>
      </div>
      <div class="stat-card card">
        <div class="stat-icon comments">
          <el-icon :size="32"><ChatDotRound /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-number">{{ stats.comments }}</div>
          <div class="stat-label">评论总数</div>
        </div>
      </div>
      <div class="stat-card card">
        <div class="stat-icon favorites">
          <el-icon :size="32"><Star /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-number">{{ stats.favorites }}</div>
          <div class="stat-label">收藏总数</div>
        </div>
      </div>
    </div>

    <!-- 最新文章和评论 -->
    <div class="content-grid">
      <!-- 最新文章 -->
      <div class="section-card card">
        <div class="section-header">
          <h3 class="section-title">
            <el-icon><Document /></el-icon>
            最新文章
          </h3>
          <el-link :underline="false" @click="goToArticles">
            查看全部
            <el-icon><ArrowRight /></el-icon>
          </el-link>
        </div>
        <div class="article-list" v-if="recentArticles.length > 0">
          <div
            v-for="article in recentArticles"
            :key="article.id"
            class="article-item"
            @click="viewArticle(article.id)"
          >
            <div class="article-info">
              <h4 class="article-title">{{ article.title }}</h4>
              <div class="article-meta">
                <span>{{ formatDate(article.createTime) }}</span>
                <span class="meta-dot">·</span>
                <span>{{ article.viewCount || 0 }} 阅读</span>
              </div>
            </div>
            <div class="article-status" :class="article.status ? 'published' : 'draft'">
              {{ article.status ? '已发布' : '草稿' }}
            </div>
          </div>
        </div>
        <el-empty v-else description="暂无文章" />
      </div>

      <!-- 最新评论 -->
      <div class="section-card card">
        <div class="section-header">
          <h3 class="section-title">
            <el-icon><ChatDotRound /></el-icon>
            最新评论
          </h3>
          <el-link :underline="false" @click="goToComments">
            查看全部
            <el-icon><ArrowRight /></el-icon>
          </el-link>
        </div>
        <div class="comment-list" v-if="recentComments.length > 0">
          <div
            v-for="comment in recentComments"
            :key="comment.id"
            class="comment-item"
          >
            <div class="comment-avatar">
              <el-avatar :size="40">
                {{ comment.nickname?.charAt(0) || 'A' }}
              </el-avatar>
            </div>
            <div class="comment-info">
              <div class="comment-header">
                <span class="comment-name">{{ comment.nickname || '匿名用户' }}</span>
                <span class="comment-time">{{ formatDate(comment.createTime) }}</span>
              </div>
              <p class="comment-content">{{ comment.content }}</p>
            </div>
          </div>
        </div>
        <el-empty v-else description="暂无评论" />
      </div>
    </div>

    <!-- 快捷操作 -->
    <div class="quick-actions card">
      <h3 class="section-title">
        <el-icon><Lightning /></el-icon>
        快捷操作
      </h3>
      <div class="actions-grid">
        <el-button type="primary" size="large" @click="createArticle">
          <el-icon><Plus /></el-icon>
          新建文章
        </el-button>
        <el-button size="large" @click="goToArticles">
          <el-icon><Document /></el-icon>
          文章管理
        </el-button>
        <el-button size="large" @click="goToComments">
          <el-icon><ChatDotRound /></el-icon>
          评论管理
        </el-button>
        <el-button size="large" @click="goToFront">
          <el-icon><House /></el-icon>
          访问前台
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import dayjs from 'dayjs'
import { getArticleList } from '@/api/article'
import { getAdminCommentList } from '@/api/comment'

const router = useRouter()

const stats = ref({
  articles: 0,
  views: 0,
  comments: 0,
  favorites: 0
})

const recentArticles = ref([])
const recentComments = ref([])

onMounted(async () => {
  await fetchStats()
  await fetchRecentArticles()
  await fetchRecentComments()
})

function formatDate(date) {
  if (!date) return ''
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

async function fetchStats() {
  try {
    const res = await getArticleList({ pageNum: 1, pageSize: 1000 })
    if (res.code === 200) {
      const articles = res.data
      stats.value.articles = res.total
      stats.value.views = articles.reduce((sum, a) => sum + (a.viewCount || 0), 0)
      stats.value.comments = articles.reduce((sum, a) => sum + (a.commentCount || 0), 0)
      stats.value.favorites = articles.reduce((sum, a) => sum + (a.likeCount || 0), 0)
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

async function fetchRecentArticles() {
  try {
    const res = await getArticleList({ pageNum: 1, pageSize: 5 })
    if (res.code === 200) {
      recentArticles.value = res.data
    }
  } catch (error) {
    console.error('获取最新文章失败:', error)
  }
}

async function fetchRecentComments() {
  try {
    const res = await getAdminCommentList({ pageNum: 1, pageSize: 5 })
    if (res.code === 200) {
      recentComments.value = res.data
    }
  } catch (error) {
    console.error('获取最新评论失败:', error)
  }
}

function createArticle() {
  router.push('/admin/article/edit')
}

function viewArticle(id) {
  router.push(`/admin/article/edit/${id}`)
}

function goToArticles() {
  router.push('/admin/articles')
}

function goToComments() {
  router.push('/admin/comments')
}

function goToFront() {
  router.push('/')
}
</script>

<style scoped lang="scss">
.dashboard {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

// 统计卡片
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.5rem;
}

.stat-card {
  padding: 1.5rem;
  display: flex;
  align-items: center;
  gap: 1rem;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.stat-icon.articles {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.views {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.comments {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-icon.favorites {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.stat-number {
  font-size: 2rem;
  font-weight: 700;
  color: #333;
}

.stat-label {
  color: #888;
  font-size: 0.95rem;
}

// 内容网格
.content-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.5rem;
}

.section-card {
  padding: 1.5rem;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.section-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #333;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin: 0;
}

// 文章列表
.article-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.article-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.article-item:hover {
  background: #e9ecef;
}

.article-title {
  font-size: 1rem;
  color: #333;
  margin: 0 0 0.5rem 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 300px;
}

.article-meta {
  font-size: 0.85rem;
  color: #888;
}

.meta-dot {
  margin: 0 0.5rem;
}

.article-status {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.85rem;
}

.article-status.published {
  background: rgba(67, 233, 123, 0.1);
  color: #43e97b;
}

.article-status.draft {
  background: rgba(250, 112, 154, 0.1);
  color: #fa709a;
}

// 评论列表
.comment-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.comment-item {
  display: flex;
  gap: 1rem;
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 8px;
}

.comment-info {
  flex: 1;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.comment-name {
  font-weight: 600;
  color: #333;
}

.comment-time {
  font-size: 0.85rem;
  color: #888;
}

.comment-content {
  margin: 0;
  color: #555;
  font-size: 0.95rem;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

// 快捷操作
.quick-actions {
  padding: 1.5rem;
}

.actions-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1rem;
  margin-top: 1rem;
}

.actions-grid :deep(.el-button) {
  height: 50px;
}

// 响应式
@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .content-grid {
    grid-template-columns: 1fr;
  }

  .actions-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }

  .actions-grid {
    grid-template-columns: 1fr;
  }
}
</style>