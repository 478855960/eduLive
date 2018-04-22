// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import router from './router'
import $ from 'jquery'
import VueAwesomeSwiper from 'vue-awesome-swiper'
import 'swiper/dist/css/swiper.css'
import './assets/css/iconfont.css'
import { emoji } from './utils/emoji.js'

Vue.use(VueAwesomeSwiper)
Vue.use(ElementUI)
Vue.config.productionTip = false
Vue.prototype.rootUrl = '/TeamYiMing'
Vue.prototype.emoji = emoji
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>',
  render: h => h(App)
})
