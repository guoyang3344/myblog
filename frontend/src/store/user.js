import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login, getUserInfo } from '@/api/user'

export const useUserStore = defineStore('user', () => {
  const user = ref(null)
  const token = ref(localStorage.getItem('token') || '')

  async function doLogin(username, password) {
    try {
      const res = await login(username, password)
      if (res.code === 200) {
        token.value = res.data.token
        user.value = res.data.user
        localStorage.setItem('token', res.data.token)
        return { success: true, message: '登录成功' }
      } else {
        return { success: false, message: res.message }
      }
    } catch (error) {
      return { success: false, message: '登录失败，请稍后重试' }
    }
  }

  async function fetchUserInfo() {
    if (!token.value) {
      user.value = null
      return
    }
    
    try {
      const res = await getUserInfo()
      if (res.code === 200) {
        user.value = res.data
      } else {
        logout()
      }
    } catch (error) {
      logout()
    }
  }

  function logout() {
    user.value = null
    token.value = ''
    localStorage.removeItem('token')
  }

  return {
    user,
    token,
    doLogin,
    fetchUserInfo,
    logout
  }
})