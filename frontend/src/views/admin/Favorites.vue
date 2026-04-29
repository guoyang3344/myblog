<template>
  <div class="favorites-admin">
    <!-- 统计卡片 -->
    <div class="stats-card card">
      <div class="stat-item">
        <div class="stat-icon">
        </div>
        <div class="stat-info">
          <div class="stat-number">{{ pagination.total }}</div>
          <div class="stat-label">总收藏数</div>
        </div>
      </div>
    </div>

    <!-- 收藏列表 -->
    <div class="table-card card">
      <el-table :data="favorites" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="articleId" label="文章ID" width="120" />
        <el-table-column prop="ip" label="IP地址" width="160" />
        <el-table-column prop="createTime" label="收藏时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
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
import dayjs from 'dayjs'
import { getAdminFavoriteList } from '@/api/favorite'

const loading = ref(false)
const favorites = ref([])

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

onMounted(() => {
  fetchFavorites()
})

function formatDate(date) {
  if (!date) return ''
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

async function fetchFavorites() {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.page,
      pageSize: pagination.pageSize
    }
    
    const res = await getAdminFavoriteList(params)
    if (res.code === 200) {
      favorites.value = res.data.records || res.data
      pagination.total = res.data.total || 0
    }
  } catch (error) {
    console.error('获取收藏列表失败:', error)
  } finally {
    loading.value = false
  }
}

function handleSizeChange(size) {
  pagination.pageSize = size
  pagination.page = 1
  fetchFavorites()
}

function handleCurrentChange(page) {
  pagination.page = page
  fetchFavorites()
}
</script>

<style scoped lang="scss">
.favorites-admin {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.stats-card {
  padding: 1.5rem;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
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

.table-card {
  padding: 1.5rem;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 1.5rem;
}
</style>