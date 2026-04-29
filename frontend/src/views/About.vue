<template>
  <div class="about-page">
    <!-- 导航栏 -->
    <nav class="navbar fade-in">
      <div class="container">
        <div class="logo">
          <router-link to="/">📝 我的博客</router-link>
        </div>
        <div class="nav-links">
          <router-link to="/">首页</router-link>
          <router-link to="/articles">文章</router-link>
          <router-link to="/about" class="active">关于</router-link>
          <router-link to="/admin/login" class="admin-link">后台管理</router-link>
        </div>
      </div>
    </nav>

    <!-- 主体内容 -->
    <main class="main-content">
      <div class="container">
        <!-- 个人介绍卡片 -->
        <section class="profile-section fade-in">
          <div class="profile-card card">
            <div class="profile-avatar">
              <el-avatar :size="120">
                <el-icon :size="60"><User /></el-icon>
              </el-avatar>
            </div>
            <h1 class="profile-name">博主名称</h1>
            <p class="profile-title">全栈开发者 / 技术爱好者</p>
            <p class="profile-desc">
              热爱编程，热爱生活。专注于 Web 开发，喜欢分享技术心得。
              希望通过博客记录成长，与大家一起进步！
            </p>
            <div class="social-links">
              <a href="#" class="social-link">
                <el-icon :size="24"><Document /></el-icon>
                <span>GitHub</span>
              </a>
              <a href="#" class="social-link">
                <el-icon :size="24"><ChatDotRound /></el-icon>
                <span>微信</span>
              </a>
              <a href="#" class="social-link">
                <el-icon :size="24"><Message /></el-icon>
                <span>邮箱</span>
              </a>
            </div>
          </div>
        </section>

        <!-- 技能展示 -->
        <section class="skills-section fade-in">
          <h2 class="section-title">
            <el-icon><Cpu /></el-icon>
            技术栈
          </h2>
          <div class="skills-grid">
            <div class="skill-card card">
              <div class="skill-icon frontend">
                <el-icon :size="40"><Monitor /></el-icon>
              </div>
              <h3>前端开发</h3>
              <div class="skill-tags">
                <el-tag size="small">Vue 3</el-tag>
                <el-tag size="small">TypeScript</el-tag>
                <el-tag size="small">Element Plus</el-tag>
                <el-tag size="small">Vite</el-tag>
                <el-tag size="small">SCSS</el-tag>
              </div>
            </div>
            <div class="skill-card card">
              <div class="skill-icon backend">
                <el-icon :size="40"><Server /></el-icon>
              </div>
              <h3>后端开发</h3>
              <div class="skill-tags">
                <el-tag size="small">Spring Boot</el-tag>
                <el-tag size="small">MyBatis Plus</el-tag>
                <el-tag size="small">MySQL</el-tag>
                <el-tag size="small">Redis</el-tag>
                <el-tag size="small">Java</el-tag>
              </div>
            </div>
            <div class="skill-card card">
              <div class="skill-icon devops">
                <el-icon :size="40"><Tools /></el-icon>
              </div>
              <h3>开发工具</h3>
              <div class="skill-tags">
                <el-tag size="small">Git</el-tag>
                <el-tag size="small">Docker</el-tag>
                <el-tag size="small">Linux</el-tag>
                <el-tag size="small">Nginx</el-tag>
                <el-tag size="small">Maven</el-tag>
              </div>
            </div>
          </div>
        </section>

        <!-- 时间线 -->
        <section class="timeline-section fade-in">
          <h2 class="section-title">
            <el-icon><Timer /></el-icon>
            关于博客
          </h2>
          <div class="timeline">
            <div class="timeline-item">
              <div class="timeline-dot"></div>
              <div class="timeline-content card">
                <h3>2024年 - 博客诞生</h3>
                <p>
                  经过一段时间的准备，我的个人博客终于上线了！
                  这是一个全新的开始，记录技术成长的每一步。
                </p>
              </div>
            </div>
            <div class="timeline-item">
              <div class="timeline-dot"></div>
              <div class="timeline-content card">
                <h3>技术选型</h3>
                <p>
                  前端采用 Vue 3 + Element Plus，后端使用 Spring Boot + MyBatis Plus。
                  简洁高效的技术栈，专注于内容创作。
                </p>
              </div>
            </div>
            <div class="timeline-item">
              <div class="timeline-dot"></div>
              <div class="timeline-content card">
                <h3>未来规划</h3>
                <p>
                  持续分享技术文章，记录学习心得。
                  希望能够帮助到更多的人，也欢迎大家交流讨论！
                </p>
              </div>
            </div>
          </div>
        </section>

        <!-- 联系方式 -->
        <section class="contact-section fade-in">
          <div class="contact-card card">
            <h2 class="section-title">
              <el-icon><Message /></el-icon>
              联系我
            </h2>
            <p class="contact-desc">
              如果你有任何问题或建议，欢迎随时联系我！
            </p>
            <div class="contact-info">
              <div class="contact-item">
                <el-icon :size="24"><Message /></el-icon>
                <span>邮箱: admin@example.com</span>
              </div>
              <div class="contact-item">
                <el-icon :size="24"><Location /></el-icon>
                <span>位置: 中国</span>
              </div>
            </div>
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
</script>

<style scoped lang="scss">
.about-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
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
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 2rem;
}

// 个人介绍
.profile-section {
  margin-bottom: 3rem;
}

.profile-card {
  text-align: center;
  padding: 3rem;
}

.profile-avatar {
  margin-bottom: 1.5rem;
  
  :deep(.el-avatar) {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
  }
}

.profile-name {
  font-size: 2rem;
  font-weight: 700;
  color: #333;
  margin-bottom: 0.5rem;
}

.profile-title {
  color: #667eea;
  font-size: 1.1rem;
  margin-bottom: 1.5rem;
}

.profile-desc {
  color: #666;
  line-height: 1.8;
  max-width: 600px;
  margin: 0 auto 2rem;
}

.social-links {
  display: flex;
  justify-content: center;
  gap: 2rem;
}

.social-link {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  color: #666;
  text-decoration: none;
  transition: color 0.3s ease;
}

.social-link:hover {
  color: #667eea;
}

// 技能展示
.skills-section {
  margin-bottom: 3rem;
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

.skills-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
}

.skill-card {
  padding: 2rem;
  text-align: center;
}

.skill-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 1rem;
  color: white;
}

.skill-icon.frontend {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.skill-icon.backend {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.skill-icon.devops {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.skill-card h3 {
  margin-bottom: 1rem;
  color: #333;
}

.skill-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  justify-content: center;
}

// 时间线
.timeline-section {
  margin-bottom: 3rem;
}

.timeline {
  position: relative;
  padding-left: 30px;
}

.timeline::before {
  content: '';
  position: absolute;
  left: 11px;
  top: 0;
  bottom: 0;
  width: 2px;
  background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
}

.timeline-item {
  position: relative;
  margin-bottom: 2rem;
}

.timeline-dot {
  position: absolute;
  left: -26px;
  top: 1.5rem;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: #667eea;
  border: 3px solid white;
  box-shadow: 0 0 0 3px #667eea;
}

.timeline-content {
  padding: 1.5rem;
}

.timeline-content h3 {
  margin-bottom: 0.5rem;
  color: #667eea;
}

.timeline-content p {
  color: #666;
  line-height: 1.6;
}

// 联系方式
.contact-card {
  text-align: center;
  padding: 2rem;
}

.contact-desc {
  color: #666;
  margin-bottom: 1.5rem;
}

.contact-info {
  display: flex;
  justify-content: center;
  gap: 3rem;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #555;
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
  
  .skills-grid {
    grid-template-columns: 1fr;
  }
  
  .contact-info {
    flex-direction: column;
    gap: 1rem;
  }
}
</style>