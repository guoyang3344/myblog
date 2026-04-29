<template>
  <div class="home">
    <!-- 导航栏 -->
    <nav class="navbar fade-in">
      <div class="container">
        <div class="logo">
          <router-link to="/">📝 我的博客</router-link>
        </div>
        <div class="nav-links">
          <router-link to="/" class="active">首页</router-link>
          <router-link to="/articles">文章</router-link>
          <router-link to="/about">关于</router-link>
          <router-link to="/admin/login" class="admin-link">后台管理</router-link>
        </div>
      </div>
    </nav>

    <!-- 英雄区域 -->
    <section class="hero fade-in">
      <div class="container">
        <div class="hero-content">
          <h1 class="hero-title float">欢迎来到我的博客</h1>
          <p class="hero-subtitle">分享技术，记录生活，传递思想</p>
          <div class="hero-buttons">
            <router-link to="/articles" class="btn-primary">浏览文章</router-link>
            <router-link to="/about" class="btn-outline">关于我</router-link>
          </div>
        </div>
        <div class="hero-image">
          <div class="floating-cards">
            <div class="floating-card card-1">
              <el-icon :size="40"><Document /></el-icon>
              <span>技术分享</span>
            </div>
            <div class="floating-card card-2">
              <el-icon :size="40"><ChatDotRound /></el-icon>
              <span>互动交流</span>
            </div>
            <div class="floating-card card-3">
              <el-icon :size="40"><Star /></el-icon>
              <span>收藏分享</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 最新文章 -->
    <section class="latest-articles fade-in">
      <div class="container">
        <h2 class="section-title">
          <span class="title-icon">🔥</span>
          置顶推荐
        </h2>
        <div class="articles-grid" v-if="topArticles.length > 0">
          <article-card
            v-for="article in topArticles"
            :key="article.id"
            :article="article"
          />
        </div>
        <div class="empty-state" v-else>
          <el-empty description="暂无置顶文章" />
        </div>
        <div class="view-all">
          <router-link to="/articles" class="view-all-link">
            查看全部文章
            <el-icon><ArrowRight /></el-icon>
          </router-link>
        </div>
      </div>
    </section>

    <!-- 统计数据 -->
    <section class="stats-section fade-in">
      <div class="container">
        <div class="stats-grid">
          <div class="stat-item">
            <div class="stat-number">{{ stats.articles }}</div>
            <div class="stat-label">文章数量</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ stats.views }}</div>
            <div class="stat-label">总浏览量</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ stats.comments }}</div>
            <div class="stat-label">评论数量</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ stats.favorites }}</div>
            <div class="stat-label">收藏数量</div>
          </div>
        </div>
      </div>
    </section>

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
import { getTopArticles, getArticleList } from '@/api/article'
import ArticleCard from '@/components/ArticleCard.vue'

const topArticles = ref([])
const stats = ref({
  articles: 0,
  views: 0,
  comments: 0,
  favorites: 0
})

onMounted(async () => {
  await fetchTopArticles()
  await fetchStats()
})

async function fetchTopArticles() {
  try {
    const res = await getTopArticles()
    if (res.code === 200) {
      topArticles.value = res.data
    }
  } catch (error) {
    console.error('获取置顶文章失败:', error)
  }
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
</script>

<style scoped lang="scss">
.home {
  min-height: 100vh;
}

// 导航栏
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
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

// 英雄区域
.hero {
  padding: 8rem 0 6rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

.hero::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 60%);
  animation: rotate 30s linear infinite;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.hero .container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: relative;
  z-index: 1;
}

.hero-content {
  max-width: 500px;
}

.hero-title {
  font-size: 3.5rem;
  font-weight: 700;
  color: white;
  margin-bottom: 1rem;
  text-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

.hero-subtitle {
  font-size: 1.25rem;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 2rem;
  line-height: 1.8;
}

.hero-buttons {
  display: flex;
  gap: 1rem;
}

.btn-outline {
  padding: 12px 24px;
  border: 2px solid rgba(255, 255, 255, 0.5);
  color: white;
  border-radius: 8px;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s ease;
}

.btn-outline:hover {
  background: rgba(255, 255, 255, 0.2);
  border-color: white;
}

.hero-image {
  position: relative;
  width: 500px;
  height: 400px;
}

.floating-cards {
  position: relative;
  width: 100%;
  height: 100%;
}

.floating-card {
  position: absolute;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  color: #667eea;
  font-weight: 600;
}

.card-1 {
  top: 20px;
  left: 50px;
  animation: float 4s ease-in-out infinite;
}

.card-2 {
  top: 150px;
  right: 50px;
  animation: float 5s ease-in-out infinite 1s;
}

.card-3 {
  bottom: 50px;
  left: 100px;
  animation: float 6s ease-in-out infinite 2s;
}

// 最新文章
.latest-articles {
  padding: 5rem 0;
  background: #f8f9fa;
}

.latest-articles .container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
}

.section-title {
  font-size: 2rem;
  font-weight: 700;
  color: #333;
  margin-bottom: 2rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.title-icon {
  font-size: 1.5rem;
}

.articles-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 2rem;
}

.empty-state {
  text-align: center;
  padding: 3rem;
}

.view-all {
  text-align: center;
  margin-top: 3rem;
}

.view-all-link {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  color: #667eea;
  font-weight: 600;
  text-decoration: none;
  font-size: 1.1rem;
  transition: color 0.3s ease;
}

.view-all-link:hover {
  color: #764ba2;
}

// 统计数据
.stats-section {
  padding: 4rem 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stats-section .container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 2rem;
}

.stat-item {
  text-align: center;
  color: white;
}

.stat-number {
  font-size: 3rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
  text-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

.stat-label {
  font-size: 1rem;
  opacity: 0.9;
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
  .hero .container {
    flex-direction: column;
    text-align: center;
  }

  .hero-title {
    font-size: 2.5rem;
  }

  .hero-buttons {
    justify-content: center;
  }

  .hero-image {
    display: none;
  }

  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .stat-number {
    font-size: 2rem;
  }

  .nav-links {
    display: none;
  }
}
</style>