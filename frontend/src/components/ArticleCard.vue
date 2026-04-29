<template>
  <router-link :to="`/article/${article.id}`" class="article-card card">
    <div class="card-header">
      <span class="category-tag" v-if="article.category">{{ article.category }}</span>
      <span class="top-tag" v-if="article.isTop">置顶</span>
    </div>
    <h3 class="card-title">{{ article.title }}</h3>
    <p class="card-summary">{{ article.summary || '暂无摘要' }}</p>
    <div class="card-tags" v-if="article.tags">
      <span v-for="tag in article.tags.split(',').slice(0, 3)" :key="tag" class="tag-item">
        #{{ tag }}
      </span>
    </div>
    <div class="card-footer">
      <span class="meta-item">
        <el-icon><Calendar /></el-icon>
        {{ formatDate(article.createTime) }}
      </span>
      <div class="stats">
        <span class="stat-item">
          <el-icon><View /></el-icon>
          {{ article.viewCount || 0 }}
        </span>
        <span class="stat-item">
          <el-icon><ChatDotRound /></el-icon>
          {{ article.commentCount || 0 }}
        </span>
        <span class="stat-item">
          <el-icon><Star /></el-icon>
          {{ article.likeCount || 0 }}
        </span>
      </div>
    </div>
  </router-link>
</template>

<script setup>
import { defineProps } from 'vue'
import dayjs from 'dayjs'

const props = defineProps({
  article: {
    type: Object,
    required: true
  }
})

function formatDate(date) {
  if (!date) return ''
  return dayjs(date).format('YYYY-MM-DD')
}
</script>

<style scoped lang="scss">
.article-card {
  display: block;
  text-decoration: none;
  padding: 1.5rem;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.article-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  transform: scaleX(0);
  transform-origin: left;
  transition: transform 0.3s ease;
}

.article-card:hover::before {
  transform: scaleX(1);
}

.card-header {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.category-tag {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 500;
}

.top-tag {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 500;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.8; }
}

.card-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 0.75rem;
  line-height: 1.4;
  transition: color 0.3s ease;
}

.article-card:hover .card-title {
  color: #667eea;
}

.card-summary {
  color: #666;
  line-height: 1.6;
  margin-bottom: 1rem;
  font-size: 0.95rem;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.tag-item {
  color: #667eea;
  font-size: 0.85rem;
  background: rgba(102, 126, 234, 0.1);
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 1rem;
  border-top: 1px solid #f0f0f0;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  color: #999;
  font-size: 0.9rem;
}

.stats {
  display: flex;
  gap: 1rem;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  color: #999;
  font-size: 0.9rem;
  transition: color 0.3s ease;
}

.article-card:hover .stat-item {
  color: #667eea;
}
</style>