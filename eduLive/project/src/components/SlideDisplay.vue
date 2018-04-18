<template>
  <div class="swiper">
    <swiper :options="swiperOption">
      <swiper-slide v-for="(item, index) in slides" :key="index"><img :src="item"></swiper-slide>
      <!-- Optional controls -->
      <!-- Optional controls -->
      <div class="swiper-pagination "  slot="pagination"></div>
      <div class="swiper-button-prev swiper-button-black" slot="button-prev"></div>
      <div class="swiper-button-next swiper-button-black" slot="button-next"></div>
    </swiper>
  </div>
</template>
<script>
import { swiper, swiperSlide } from 'vue-awesome-swiper'

export default {
  name: 'SlideDisplay',
  data () {
    return {
      slides: '',
      // 轮播config
      swiperOption: {
        // 如果自行设计了插件，那么插件的一些配置相关参数，也应该出现在这个对象中，如下debugger
        debugger: true,
        // autoplay: 3000,
        prevButton: '.swiper-button-prev',
        nextButton: '.swiper-button-next',
        notNextTick: true,
        // 左右点击
        navigation: {
          nextEl: '.swiper-button-next',
          prevEl: '.swiper-button-prev',
        },
        // 分页器设置
        pagination: {
          el: '.swiper-pagination',
          clickable: true
        },
        slidesPerView: 'auto', // 设置slider容器能够同时显示的slides数量(carousel模式)。可以设置为数字（可为小数，小数不可loop），或者 'auto'则自动根据slides的宽度来设定数量。loop模式下如果设置为'auto'还需要设置另外一个参数loopedSlides。
        centeredSlides: true // <span style="color:rgb(68,68,68);font-family:'microsoft yahei';font-size:13px;">设定为true时，活动块会居中，而不是默认状态下的居左。</span>
      }
    }
  },
  mounted () {
    let pptImages = sessionStorage.getItem('pptImages').split(';')
    this.slides = pptImages
  },
  components: {
    swiper,
    swiperSlide
  }
}
</script>

<style scoped>
.slider {
  height: 300px;
}
</style>
