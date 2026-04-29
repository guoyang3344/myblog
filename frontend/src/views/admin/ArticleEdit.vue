<template>
  <div class="article-edit">
    <!-- 返回按钮 -->
    <div class="back-btn" @click="goBack">
      <el-icon><ArrowLeft /></el-icon>
      返回文章列表
    </div>

    <!-- 表单卡片 -->
    <div class="form-card card">
      <h2 class="form-title">{{ isEdit ? '编辑文章' : '新建文章' }}</h2>

      <el-form
        ref="articleFormRef"
        :model="articleForm"
        :rules="articleRules"
        label-width="100px"
        class="article-form"
      >
        <!-- 标题 -->
        <el-form-item label="文章标题" prop="title">
          <el-input
            v-model="articleForm.title"
            placeholder="请输入文章标题"
            size="large"
          />
        </el-form-item>

        <!-- 摘要 -->
        <el-form-item label="文章摘要" prop="summary">
          <el-input
            v-model="articleForm.summary"
            type="textarea"
            :rows="3"
            placeholder="请输入文章摘要"
          />
        </el-form-item>

        <!-- 内容 -->
        <el-form-item label="文章内容" prop="content">
          <el-input
            v-model="articleForm.content"
            type="textarea"
            :rows="15"
            placeholder="请输入文章内容（支持HTML）"
          />
          <div class="content-tip">
            <el-icon><InfoFilled /></el-icon>
            支持HTML标签，例如：&lt;p&gt;、&lt;h1&gt;、&lt;img&gt;、&lt;code&gt; 等
          </div>
        </el-form-item>

        <!-- 封面图片 -->
        <el-form-item label="封面图片">
          <el-input
            v-model="articleForm.coverImage"
            placeholder="请输入封面图片URL（选填）"
          />
        </el-form-item>

        <!-- 分类和标签 -->
        <div class="form-row">
          <el-form-item label="文章分类" prop="category">
            <el-input
              v-model="articleForm.category"
              placeholder="例如：技术、生活"
              style="width: 200px"
            />
          </el-form-item>

          <el-form-item label="文章标签">
            <el-input
              v-model="articleForm.tags"
              placeholder="多个标签用逗号分隔"
              style="width: 300px"
            />
          </el-form-item>
        </div>

        <!-- 发布状态和置顶 -->
        <div class="form-row">
          <el-form-item label="发布状态">
            <el-radio-group v-model="articleForm.status">
              <el-radio :label="1">发布</el-radio>
              <el-radio :label="0">草稿</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="是否置顶">
            <el-switch v-model="articleForm.isTop" active-text="置顶" inactive-text="不置顶" />
          </el-form-item>
        </div>

        <!-- 提交按钮 -->
        <el-form-item>
          <el-button type="primary" size="large" :loading="submitting" @click="submitForm">
            保存文章
          </el-button>
          <el-button size="large" @click="goBack">
            取消
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getAdminArticleDetail, saveArticle } from '@/api/article'

const router = useRouter()
const route = useRoute()

const articleFormRef = ref(null)
const submitting = ref(false)

const isEdit = computed(() => !!route.params.id)

const articleForm = reactive({
  title: '',
  summary: '',
  content: '',
  coverImage: '',
  category: '',
  tags: '',
  status: 1,
  isTop: 0
})

const articleRules = {
  title: [
    { required: true, message: '请输入文章标题', trigger: 'blur' },
    { min: 2, max: 200, message: '标题长度在 2 到 200 个字符', trigger: 'blur' }
  ],
  summary: [
    { required: true, message: '请输入文章摘要', trigger: 'blur' },
    { min: 10, max: 500, message: '摘要长度在 10 到 500 个字符', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入文章内容', trigger: 'blur' }
  ],
  category: [
    { required: true, message: '请输入文章分类', trigger: 'blur' }
  ]
}

onMounted(async () => {
  if (isEdit.value) {
    await fetchArticleDetail()
  }
})

async function fetchArticleDetail() {
  try {
    const res = await getAdminArticleDetail(route.params.id)
    if (res.code === 200) {
      const article = res.data
      articleForm.title = article.title
      articleForm.summary = article.summary
      articleForm.content = article.content
      articleForm.coverImage = article.coverImage || ''
      articleForm.category = article.category || ''
      articleForm.tags = article.tags || ''
      articleForm.status = article.status
      articleForm.isTop = article.isTop
    }
  } catch (error) {
    ElMessage.error('获取文章详情失败')
    router.push('/admin/articles')
  }
}

function goBack() {
  router.push('/admin/articles')
}

async function submitForm() {
  if (!articleFormRef.value) return
  
  await articleFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        const data = {
          ...articleForm,
          id: isEdit.value ? route.params.id : undefined
        }
        
        const res = await saveArticle(data)
        if (res.code === 200) {
          ElMessage.success(isEdit.value ? '修改成功' : '创建成功')
          router.push('/admin/articles')
        }
      } catch (error) {
        ElMessage.error('保存失败，请稍后重试')
      } finally {
        submitting.value = false
      }
    }
  })
}
</script>

<style scoped lang="scss">
.article-edit {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.back-btn {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  color: #667eea;
  font-weight: 500;
  cursor: pointer;
  transition: color 0.3s ease;
}

.back-btn:hover {
  color: #764ba2;
}

.form-card {
  padding: 2rem;
}

.form-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 2px solid #f0f0f0;
}

.article-form {
  max-width: 900px;
}

.form-row {
  display: flex;
  gap: 2rem;
}

.content-tip {
  margin-top: 0.5rem;
  font-size: 0.85rem;
  color: #999;
  display: flex;
  align-items: center;
  gap: 0.25rem;
}
</style>