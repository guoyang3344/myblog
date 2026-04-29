<template>
  <div class="comments-admin">
    <!-- 搜索和操作栏 -->
    <div class="search-bar card">
      <div class="search-left">
        <el-input
          v-model="searchForm.articleId"
          placeholder="按文章ID搜索（选填）"
          prefix-icon="Search"
          style="width: 250px"
          @keyup.enter="handleSearch"
        />
        <el-button type="primary" @click="handleSearch" style="margin-left: 1rem">
          搜索
        </el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>
    </div>

    <!-- 评论列表 -->
    <div class="table-card card">
      <el-table :data="comments" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="articleId" label="文章ID" width="100" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="content" label="评论内容" min-width="250">
          <template #default="{ row }">
            <div class="content-cell" :title="row.content">
              {{ row.content }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="ip" label="IP地址" width="140" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'warning'" size="small">
              {{ row.status === 1 ? '已审核' : '待审核' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="评论时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.status !== 1"
              type="success"
              link
              @click="approveComment(row)"
            >
              审核通过
            </el-button>
            <el-button type="danger" link @click="deleteComment(row)">
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
import { ElMessageBox, ElMessage } from 'element-plus'
import dayjs from 'dayjs'
import { getAdminCommentList, deleteComment as deleteCommentApi, approveComment as approveCommentApi } from '@/api/comment'

const loading = ref(false)
const comments = ref([])

const searchForm = reactive({
  articleId: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

onMounted(() => {
  fetchComments()
})

function formatDate(date) {
  if (!date) return ''
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

async function fetchComments() {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.page,
      pageSize: pagination.pageSize
    }
    
    if (searchForm.articleId) {
      params.articleId = searchForm.articleId
    }
    
    const res = await getAdminCommentList(params)
    if (res.code === 200) {
      comments.value = res.data
      pagination.total = res.total
    }
  } catch (error) {
    console.error('获取评论列表失败:', error)
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pagination.page = 1
  fetchComments()
}

function resetSearch() {
  searchForm.articleId = ''
  pagination.page = 1
  fetchComments()
}

function handleSizeChange(size) {
  pagination.pageSize = size
  pagination.page = 1
  fetchComments()
}

function handleCurrentChange(page) {
  pagination.page = page
  fetchComments()
}

async function approveComment(row) {
  try {
    const res = await approveCommentApi(row.id)
    if (res.code === 200) {
      ElMessage.success('审核通过')
      fetchComments()
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

function deleteComment(row) {
  ElMessageBox.confirm('确定要删除这条评论吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteCommentApi(row.id)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        fetchComments()
      }
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}
</script>

<style scoped lang="scss">
.comments-admin {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.search-bar {
  padding: 1.5rem;
}

.search-left {
  display: flex;
  align-items: center;
}

.table-card {
  padding: 1.5rem;
}

.content-cell {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 250px;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 1.5rem;
}
</style>