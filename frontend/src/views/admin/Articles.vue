<template>
  <div class="articles-admin">
    <!-- 搜索和操作栏 -->
    <div class="search-bar card">
      <div class="search-left">
        <el-input
          v-model="searchForm.keyword"
          placeholder="搜索文章标题..."
          prefix-icon="Search"
          style="width: 300px"
          @keyup.enter="handleSearch"
        />
        <el-select
          v-model="searchForm.category"
          placeholder="选择分类"
          clearable
          style="width: 150px; margin-left: 1rem"
          @change="handleSearch"
        >
          <el-option
            v-for="cat in categories"
            :key="cat"
            :label="cat"
            :value="cat"
          />
        </el-select>
        <el-button type="primary" @click="handleSearch" style="margin-left: 1rem">
          搜索
        </el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>
      <div class="search-right">
        <el-button type="primary" @click="createArticle">
          <el-icon><Plus /></el-icon>
          新建文章
        </el-button>
      </div>
    </div>

    <!-- 文章列表 -->
    <div class="table-card card">
      <el-table :data="articles" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" min-width="250">
          <template #default="{ row }">
            <div class="title-cell">
              <span class="title-text" :title="row.title">{{ row.title }}</span>
              <el-tag v-if="row.isTop" type="danger" size="small">置顶</el-tag>
              <el-tag v-if="row.status === 0" type="warning" size="small">草稿</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column prop="viewCount" label="浏览量" width="100" />
        <el-table-column prop="commentCount" label="评论" width="80" />
        <el-table-column prop="likeCount" label="收藏" width="80" />
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="viewArticle(row.id)">
              查看
            </el-button>
            <el-button type="primary" link @click="editArticle(row.id)">
              编辑
            </el-button>
            <el-button type="danger" link @click="deleteArticle(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import dayjs from 'dayjs'
import { getArticleList, getCategories, deleteArticle as deleteArticleApi } from '@/api/article'

const router = useRouter()

const loading = ref(false)
const articles = ref([])
const categories = ref([])

const searchForm = reactive({
  keyword: '',
  category: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

onMounted(async () => {
  await fetchCategories()
  await fetchArticles()
})

function formatDate(date) {
  if (!date) return ''
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

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
  loading.value = true
  try {
    const params = {
      pageNum: pagination.page,
      pageSize: pagination.pageSize
    }
    
    if (searchForm.keyword) {
      params.keyword = searchForm.keyword
    }
    
    if (searchForm.category) {
      params.category = searchForm.category
    }
    
    const res = await getArticleList(params)
    if (res.code === 200) {
      articles.value = res.data
      pagination.total = res.total
    }
  } catch (error) {
    console.error('获取文章列表失败:', error)
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pagination.page = 1
  fetchArticles()
}

function resetSearch() {
  searchForm.keyword = ''
  searchForm.category = ''
  pagination.page = 1
  fetchArticles()
}

function handleSizeChange(size) {
  pagination.pageSize = size
  pagination.page = 1
  fetchArticles()
}

function handleCurrentChange(page) {
  pagination.page = page
  fetchArticles()
}

function createArticle() {
  router.push('/admin/article/edit')
}

function viewArticle(id) {
  router.push(`/article/${id}`)
}

function editArticle(id) {
  router.push(`/admin/article/edit/${id}`)
}

function deleteArticle(row) {
  ElMessageBox.confirm(`确定要删除文章「${row.title}」吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteArticleApi(row.id)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        fetchArticles()
      }
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}
</script>

<style scoped lang="scss">
.articles-admin {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.search-bar {
  padding: 1.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-left {
  display: flex;
  align-items: center;
}

.table-card {
  padding: 1.5rem;
}

.title-cell {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.title-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 200px;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 1.5rem;
}
</style>