import Vue from 'vue'
import VueRouter from 'vue-router'
import index from '../views/index.vue'
import history from '../views/history'
import apiKeySet from '../views/apiKeySet'
import apiKeyList from '../views/apiKeyList'
import translatePage from '../views/translatePage.vue'
import translatePage2 from '../views/translatePage2.vue'
import login from '../views/login'
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: '翻译功能',
    component: index,
    redirect:"/login",
    children:[
      {
          path: '/translatePage',
          name: '文件翻译',
          component: translatePage,
      },
      {
        path: '/translatePage2',
        name: '录音翻译',
        component: translatePage2,
      }
    ]
  },
  {
    path: '/',
    name: '密钥功能',
    component: index,
    children:[
      {
        path: '/apiKeySet',
        name: '翻译密钥设置',
        component: apiKeySet,
      },
      {
        path: '/apiKeyList',
        name: '翻译密钥查询',
        component: apiKeyList,
      },
    ]
  },
  {
    path: '/',
    name: '数据功能',
    component: index,
    children:[
      {
        path: '/history',
        name: '历史记录',
        component: history,
      }
    ]
  },
  {
    path:'/login',
    name: '登录',
    component:login,
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
