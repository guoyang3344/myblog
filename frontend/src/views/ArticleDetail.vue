<template>
  <div class="article-detail-page">
    <!-- 导航栏 -->
    <nav class="navbar fade-in">
      <div class="container">
        <div class="logo">
          <router-link to="/">📝 我的博客</router-link>
        </div>
        <div class="nav-links">
          <router-link to="/">首页</router-link>
          <router-link to="/articles" class="active">文章</router-link>
          <router-link to="/about">关于</router-link>
          <router-link to="/admin/login" class="admin-link">后台管理</router-link>
        </div>
      </div>
    </nav>

    <!-- 加载中 -->
    <div class="loading-container" v-if="loading">
      <el-icon class="loading-icon" :size="60"><Loading /></el-icon>
      <p>加载中...</p>
    </div>

    <!-- 文章详情 -->
    <main class="main-content fade-in" v-else>
      <div class="container">
        <!-- 返回按钮 -->
        <div class="back-btn" @click="goBack">
          <el-icon><ArrowLeft /></el-icon>
          返回列表
        </div>

        <!-- 文章标题区域 -->
        <article class="article-content">
          <header class="article-header">
            <h1 class="article-title">{{ article.title }}</h1>
            <div class="article-meta">
              <span class="meta-item">
                <el-icon><Calendar /></el-icon>
                {{ formatDate(article.createTime) }}
              </span>
              <span class="meta-item" v-if="article.category">
                <el-icon><Folder /></el-icon>
                {{ article.category }}
              </span>
              <span class="meta-item" v-if="article.tags">
                <el-icon><PriceTag /></el-icon>
                {{ article.tags }}
              </span>
            </div>
            <div class="article-stats">
              <span class="stat-item">
                <el-icon><View /></el-icon>
                {{ article.viewCount || 0 }} 阅读
              </span>
              <span class="stat-item">
                <el-icon><ChatDotRound /></el-icon>
                {{ article.commentCount || 0 }} 评论
              </span>
              <span class="stat-item favorite-btn" @click="handleFavorite">
                <el-icon :class="{ favorited: isFavorited }">
                  <Star v-if="isFavorited" />
                  <Star v-else :style="{ color: '#999' }" />
                </el-icon>
                {{ favoriteCount }} 收藏
              </span>
            </div>
          </header>

          <!-- 文章内容 -->
          <div class="article-body" v-html="article.content">
          </div>
        </article>

        <!-- 评论区域 -->
        <section class="comments-section">
          <h3 class="section-title">
            <el-icon><ChatDotRound /></el-icon>
            评论 ({{ comments.length }})
          </h3>

          <!-- 添加评论 -->
          <div class="comment-form card">
            <h4>发表评论</h4>
            <el-input
              v-model="newComment.nickname"
              placeholder="您的昵称"
              class="input-item"
            />
            <el-input
              v-model="newComment.email"
              placeholder="您的邮箱（选填）"
              class="input-item"
            />
            <el-input
              v-model="newComment.content"
              type="textarea"
              placeholder="说点什么吧..."
              :rows="4"
              class="input-item"
            />
            <el-button type="primary" @click="submitComment" :loading="submitting">
              发表评论
            </el-button>
          </div>

          <!-- 评论列表 -->
          <div class="comments-list" v-if="comments.length > 0">
            <div
              v-for="comment in comments"
              :key="comment.id"
              class="comment-item card"
            >
              <div class="comment-avatar">
                <el-avatar :size="50">
                  {{ comment.nickname?.charAt(0) || 'A' }}
                </el-avatar>
              </div>
              <div class="comment-content">
                <div class="comment-header">
                  <span class="comment-name">{{ comment.nickname || '匿名用户' }}</span>
                  <span class="comment-time">{{ formatDate(comment.createTime) }}</span>
                </div>
                <div class="comment-text">{{ comment.content }}</div>
              </div>
            </div>
          </div>
          <div class="no-comments" v-else>
            <el-empty description="暂无评论，快来抢沙发吧！" />
          </div>
        </section>
      </div>
    </main>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="container">
        <p>&copy; 2024 我的博客. All rights reserved.</p>
        <p class="footer-subtitle">用代码创造价值</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'
import { getArticleDetail } from '@/api/article'
import { getComments, addComment } from '@/api/comment'
import { toggleFavorite, checkFavorite } from '@/api/favorite'

const router = useRouter()
const route = useRoute()

const loading = ref(true)
const article = ref({})
const comments = ref([])
const isFavorited = ref(false)
const favoriteCount = ref(0)
const submitting = ref(false)
const newComment = ref({
  nickname: '',
  email: '',
  content: ''
})

onMounted(async () => {
  const articleId = route.params.id
  if (articleId) {
    await fetchArticleDetail(articleId)
    await fetchComments(articleId)
    await checkIsFavorited(articleId)
  }
  loading.value = false
})

function formatDate(date) {
  if (!date) return ''
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

function goBack() {
  router.go(-1)
}

async function fetchArticleDetail(id) {
  try {
    const res = await getArticleDetail(id)
    if (res.code === 200) {
      article.value = res.data.article
      isFavorited.value = res.data.isFavorited
    }
  } catch (error) {
    console.error('获取文章详情失败:', error)
    ElMessage.error('获取文章详情失败')
  }
}

async function fetchComments(articleId) {
  try {
    const res = await getComments(articleId)
    if (res.code === 200) {
      comments.value = res.data
    }
  } catch (error) {
    console.error('获取评论失败:', error)
  }
}

async function checkIsFavorited(articleId) {
  try {
    const res = await checkFavorite(articleId)
    if (res.code === 200) {
      isFavorited.value = res.data.isFavorited
      favoriteCount.value = res.data.count
    }
  } catch (error) {
    console.error('检查收藏状态失败:', error)
  }
}

async function handleFavorite() {
  try {
    const res = await toggleFavorite(route.params.id)
    if (res.code === 200) {
      isFavorited.value = res.data.isFavorited
      favoriteCount.value = res.data.count
      
      if (isFavorited.value) {
        ElMessage.success('收藏成功！')
      } else {
        ElMessage.info('已取消收藏')
      }
    }
  } catch (error) {
    console.error('收藏操作失败:', error)
    ElMessage.error('操作失败')
  }
}

async function submitComment() {
  if (!newComment.value.nickname?.trim()) {
    ElMessage.warning('请输入您的昵称')
    return
  }
  if (!newComment.value.content?.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }

  submitting.value = true
  try {
    const res = await addComment({
      articleId: route.params.id,
      nickname: newComment.value.nickname.trim(),
      email: newComment.value.email?.trim() || '',
      content: newComment.value.content.trim()
    })
    
    if (res.code === 200) {
      ElMessage.success('评论发表成功！')
      newComment.value = { nickname: '', email: '', content: '' }
      fetchComments(route.params.id)
    }
  } catch (error) {
    console.error('发表评论失败:', error)
    ElMessage.error('发表评论失败')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped lang="scss">
.article-detail-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f8f9fa;
}

// 导航栏
.navbar {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.1);
}

.navbar .container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo a {
  font-size: 1.5rem;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  text-decoration: none;
}

.nav-links {
  display: flex;
  gap: 2rem;
  align-items: center;
}

.nav-links a {
  text-decoration: none;
  color: #555;
  font-weight: 500;
  transition: color 0.3s ease;
  position: relative;
}

.nav-links a::after {
  content: '';
  position: absolute;
  bottom: -5px;
  left: 0;
  width: 0;
  height: 2px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  transition: width 0.3s ease;
}

.nav-links a:hover,
.nav-links a.active {
  color: #667eea;
}

.nav-links a:hover::after,
.nav-links a.active::after {
  width: 100%;
}

.admin-link {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white !important;
  padding: 0.5rem 1rem;
  border-radius: 6px;
}

.admin-link::after {
  display: none;
}

// 加载中
.loading-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #667eea;
}

.loading-icon {
  animation: rotate 1s linear infinite;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

// 主体内容
.main-content {
  flex: 1;
  padding: 2rem 0 4rem;
}

.main-content .container {
  max-width: 900px;
  margin: 0 auto;
  padding: 0 2rem;
}

// 返回按钮
.back-btn {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  color: #667eea;
  font-weight: 500;
  cursor: pointer;
  margin-bottom: 1.5rem;
  transition: color 0.3s ease;
}

.back-btn:hover {
  color: #764ba2;
}

// 文章内容
.article-content {
  background: white;
  border-radius: 16px;
  padding: 2rem;
  margin-bottom: 2rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.article-header {
  border-bottom: 2px solid #f0f0f0;
  padding-bottom: 1.5rem;
  margin-bottom: 2rem;
}

.article-title {
  font-size: 2rem;
  font-weight: 700;
  color: #333;
  margin-bottom: 1rem;
  line-height: 1.4;
}

.article-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 1.5rem;
  margin-bottom: 1rem;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #888;
  font-size: 0.95rem;
}

.article-stats {
  display: flex;
  gap: 2rem;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #666;
  font-size: 0.95rem;
}

.favorite-btn {
  cursor: pointer;
  transition: color 0.3s ease;
}

.favorite-btn:hover {
  color: #667eea;
}

.favorited {
  color: #f59e0b;
}

// 文章正文
.article-body {
  font-size: 1.1rem;
  line-height: 1.8;
  color: #444;
  
  :deep(h1), :deep(h2), :deep(h3), :deep(h4), :deep(h5), :deep(h6) {
    margin: 1.5rem 0 1rem;
    color: #333;
    font-weight: 600;
  }
  
  :deep(p) {
    margin-bottom: 1rem;
    text-align: justify;
  }
  
  :deep(img) {
    max-width: 100%;
    border-radius: 8px;
    margin: 1rem 0;
  }
  
  :deep(code) {
    background: #f5f5f5;
    padding: 0.2rem 0.5rem;
    border-radius: 4px;
    font-size: 0.9em;
    color: #e96900;
  }
  
  :deep(pre) {
    background: #1a1a2e;
    color: #e6e6e6;
    padding: 1.5rem;
    border-radius: 8px;
    overflow-x: auto;
    margin: 1rem 0;
    
    code {
      background: none;
      color: inherit;
      padding: 0;
    }
  }
  
  :deep(blockquote) {
    border-left: 4px solid #667eea;
    padding: 0.5rem 1rem;
    margin: 1rem 0;
    background: #f8f9fa;
    color: #666;
    border-radius: 0 8px 8px 0;
  }
  
  :deep(ul), :deep(ol) {
    padding-left: 1.5rem;
    margin: 1rem 0;
    
    li {
      margin: 0.5rem 0;
    }
  }
  
  :deep(a) {
    color: #667eea;
    text-decoration: none;
    transition: color 0.3s ease;
    
    &:hover {
      color: #764ba2;
      text-decoration: underline;
    }
  }
}

// 评论区域
.comments-section {
  margin-top: 3rem;
}

.section-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 1.5rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

// 评论表单
.comment-form {
  padding: 1.5rem;
  margin-bottom: 2rem;
  
  h4 {
    margin-bottom: 1rem;
    color: #333;
    font-weight: 600;
  }
  
  .input-item {
    margin-bottom: 1rem;
  }
}

// 评论列表
.comments-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.comment-item {
  display: flex;
  gap: 1rem;
  padding: 1.5rem;
}

.comment-content {
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
  color: #999;
  font-size: 0.9rem;
}

.comment-text {
  color: #555;
  line-height: 1.6;
}

.no-comments {
  text-align: center;
  padding: 2rem;
}

// 页脚
.footer {
  background: #1a1a2e;
  color: rgba(255, 255, 255, 0.7);
  padding: 2rem 0;
  text-align: center;
}

.footer .container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
}

.footer-subtitle {
  font-size: 0.9rem;
  margin-top: 0.5rem;
  opacity: 0.6;
}

// 响应式
@media (max-width: 768px) {
  .nav-links {
    display: none;
  }
  
  .article-title {
    font-size: 1.5rem;
  }
  
  .article-meta {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .article-stats {
    flex-wrap: wrap;
    gap: 1rem;
  }
}
</style>