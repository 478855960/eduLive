<template>
  <div class="swiper">
    <swiper :options="swiperOption" class="swiper-no-swiping">
      <swiper-slide v-for="(item, index) in slides" :key="index"><img :src="item"></swiper-slide>
      <!-- Optional controls -->
      <!-- Optional controls -->
      <div class="swiper-button-prev swiper-button-black" slot="button-prev" @click="pageMinus()"></div>
      <div class="swiper-button-next swiper-button-black" slot="button-next" @click="pageAdd()"></div>
    </swiper>
  </div>
</template>
<script>
import { swiper, swiperSlide } from 'vue-awesome-swiper'
import Bus from './bus.js'

export default {
  name: 'SlideDisplay',
  data () {
    return {
      sessionUser: null,
      pptWebSocket: null,
      getCurrentUserUrl: '/user/getCurUser.action',
      pageIndex: 0,
      slides: '',
      // 轮播config
      swiperOption: {
        // 如果自行设计了插件，那么插件的一些配置相关参数，也应该出现在这个对象中，如下debugger
        debugger: true,
        // autoplay: 3000,
        grabCursor: false,
        prevButton: '.swiper-button-prev',
        nextButton: '.swiper-button-next',
        // 左右点击
        navigation: {
          nextEl: '.swiper-button-next',
          prevEl: '.swiper-button-prev'
        },
        // 分页器设置
        pagination: {
          el: '.swiper-pagination',
          clickable: true
        },
        // slidesPerView: 'auto', // 设置slider容器能够同时显示的slides数量(carousel模式)。可以设置为数字（可为小数，小数不可loop），或者 'auto'则自动根据slides的宽度来设定数量。loop模式下如果设置为'auto'还需要设置另外一个参数loopedSlides。
        centeredSlides: true // 设定为true时，活动块会居中，而不是默认状态下的居左。
      },
      pageIndexOp: {
        type: '',
        liveroomNum: '',
        phoneNum: '',
        otherInfo: ''
      }
    }
  },
  mounted () {
    let pptImages = sessionStorage.getItem('pptImages').split(';')
    this.slides = pptImages
    this.$ajax.post(this.rootUrl + this.getCurrentUserUrl)
      .then((response) => {
        this.sessionUser = JSON.parse(response.data)
        this.pageIndexOp.type = 'ppt'
        this.pageIndexOp.liveroomNum = this.sessionUser.phoneNum
        this.pageIndexOp.phoneNum = this.sessionUser.phoneNum
      })
    this.pageIndexOp.otherInfo = this.slides[this.pageIndex]
    Bus.$emit('pageIndex', this.pageIndexOp)
  },
  components: {
    swiper,
    swiperSlide
  },
  methods: {
    pageAdd () {
      this.pageIndex++
      this.pageIndexOp.otherInfo = this.slides[this.pageIndex]
      Bus.$emit('pageIndex', this.pageIndexOp)
    },
    pageMinus () {
      this.pageIndex--
      this.pageIndexOp.otherInfo = this.slides[this.pageIndex]
      Bus.$emit('pageIndex', this.pageIndexOp)
    }
  }
}
</script>

<style scoped>
.slider {
  height: 300px;
}
</style>
