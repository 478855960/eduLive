import Vue from 'vue'
import Router from 'vue-router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import Axios from 'axios'
import HelloWorld from '@/components/HelloWorld'
import LiveTeacher from '@/components/Live_Teacher'
import Login from '@/components/Login'
import Register from '@/components/Register'
import UserCenter from '@/components/UserCenter'

Vue.use(Router)
Vue.use(ElementUI)
Vue.prototype.$ajax = Axios

export default new Router({
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: HelloWorld
    },
    {
      path: '/live_teacher',
      name: 'Live_Teacher',
      component: LiveTeacher
    },
    {
      path: '/Login',
      name: 'Login',
      component: Login
    },
    {
      path: '/Register',
      name: 'Register',
      component: Register
    },
    {
      path: '/UserCenter',
      name: 'UserCenter',
      component: UserCenter
    }
  ]
})
