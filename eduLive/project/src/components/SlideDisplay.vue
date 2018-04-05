<template>
  <!-- 制作一个框架包裹slider -->
  <div class="slider">
    <!-- 配置slider组件 -->
    <slider :pages="pages" :sliderinit="sliderinit" @slide='slide' @tap='onTap' @init='onInit'>
      <!-- 设置loading,可自定义 -->
      <div slot="loading">loading...</div>
    </slider>
  </div>
</template>

<script>
import slider from 'vue-concise-slider'// import slider components

export default {
  name: 'SlideDisplay',
  data () {
    return {
      //Image list
      pages:[
        {
          html: '<div class="slider1">第1页</div>',
          style: {
            'background': '#1bbc9b'
          }
        },
        {
          html: '<div class="slider2">第2页</div>',
          style: {
            'background': '#4bbfc3'
          }
        },
        {
          html: '<div class="slider3">第3页</div>',
          style: {
            'background': '#7baabe'
          }
        }
      ],
      // Sliding configuration [obj]
      sliderinit: {
        currentPage: 0,
        thresholdDistance: 500,
        thresholdTime: 100,
        autoplay: 1000,
        loop: false,
        direction: 'vertical',
        infinite: 1,
        slidesToScroll: 1,
        timingFunction: 'ease',
        duration: 300
      }
    }
  },
  components: {
    slider
  },
  methods: {
    // Listener event
    slide (data) {
      console.log(data)
    },
    onTap (data) {
      console.log(data)
    },
    onInit (data) {
      console.log(data)
    }
  }
}
</script>

<style scoped>
.slider {
  height: 300px;
}
</style>
