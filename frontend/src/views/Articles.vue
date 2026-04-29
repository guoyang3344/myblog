<template>
  <div class="articles-page">
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

    <!-- 页面主体 -->
    <main class="main-content">
      <div class="container">
        <!-- 搜索和筛选 -->
        <div class="search-section fade-in">
          <div class="search-box">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索文章..."
              prefix-icon="Search"
              @keyup.enter="handleSearch"
            >
              <template #append>
                <el-button @click="handleSearch">搜索</el-button>
              </template>
            </el-input>
          </div>
          <div class="category-tags">
            <el-tag
              v-for="cat in categories"
              :key="cat"
              :type="currentCategory === cat ? 'primary' : ''"
              class="category-tag"
              @click="handleCategoryChange(cat)"
            >
              {{ cat }}
            </el-tag>
            <el-tag
              :type="!currentCategory ? 'primary' : ''"
              class="category-tag"
              @click="handleCategoryChange('')"
            >
              全部
            </el-tag>
          </div>
        </div>

        <!-- 文章列表 -->
        <div class="articles-list fade-in">
          <div class="articles-grid" v-if="articles.length > 0">
            <article-card
              v-for="article in articles"
              :key="article.id"
              :article="article"
            />
          </div>
          <div class="empty-state" v-else>
            <el-empty description="暂无文章" />
          </div>
        </div>

        <!-- 分页 -->
        <div class="pagination-section fade-in" v-if="total > 0">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 30, 50]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
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
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getArticleList, getCategories } from '@/api/article'
import ArticleCard from '@/components/ArticleCard.vue'

const router = useRouter()
const route = useRoute()

const articles = ref([])
const categories = ref([])
const searchKeyword = ref('')
const currentCategory = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

onMounted(async () => {
  await fetchCategories()
  await fetchArticles()
})

// 监听路由参数变化
watch(() => route.query, (newQuery) => {
  searchKeyword.value = newQuery.keyword || ''
  currentCategory.value = newQuery.category || ''
  currentPage.value = parseInt(newQuery.page) || 1
  fetchArticles()
}, { immediate: true })

async function fetchCategories() {
  try {
    const res = await getCategories()
    if (res.code === 200) {
      categories.value = res.data
    }
  } catch (error) {
    console.error('获取分类失败:', error)
  }
}

async function fetchArticles() {
  try {
    const params = {
      pageNum: currentPage.value,
      pageSize: pageSize.value
    }
    
    if (searchKeyword.value) {
      params.keyword = searchKeyword.value
    }
    
    if (currentCategory.value) {
      params.category = currentCategory.value
    }
    
    const res = await getArticleList(params)
    if (res.code === 200) {
      articles.value = res.data
      total.value = res.total
    }
  } catch (error) {
    console.error('获取文章列表失败:', error)
  }
}

function handleSearch() {
  currentPage.value = 1
  updateRoute()
}

function handleCategoryChange(category) {
  if (currentCategory.value === category) {
    return
  }
  currentCategory.value = category
  currentPage.value = 1
  updateRoute()
}

function handleSizeChange(size) {
  pageSize.value = size
  currentPage.value = 1
  updateRoute()
}

function handleCurrentChange(page) {
  currentPage.value = page
  updateRoute()
}

function updateRoute() {
  const query = {
    page: currentPage.value
  }
  
  if (searchKeyword.value) {
    query.keyword = searchKeyword.value
  }
  
  if (currentCategory.value) {
    query.category = currentCategory.value
  }
  
  router.push({ path: '/articles', query })
}
</script>

<style scoped lang="scss">
.articles-page {
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

// 主体内容
.main-content {
  flex: 1;
  padding: 2rem 0 4rem;
}

.main-content .container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
}

// 搜索区域
.search-section {
  margin-bottom: 2rem;
}

.search-box {
  margin-bottom: 1.5rem;
}

.category-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.75rem;
}

.category-tag {
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 0.5rem 1rem;
  border-radius: 20px;
}

.category-tag:hover {
  transform: translateY(-2px);
}

// 文章列表
.articles-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 2rem;
}

.empty-state {
  text-align: center;
  padding: 3rem;
}

// 分页
.pagination-section {
  margin-top: 3rem;
  display: flex;
  justify-content: center;
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
  
  .articles-grid {
    grid-template-columns: 1fr;
  }
}
</style>