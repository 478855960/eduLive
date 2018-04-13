<template>
  <el-container>
    <el-header>
      <el-menu :default-active="activeIndex" id="head-menu" class="el-menu" mode="horizontal" @select="handleSelect">
        <el-menu-item>硕学平台</el-menu-item>
        <el-menu-item index="LiveList">直播列表</el-menu-item>
        <el-menu-item index="VideoList">录播列表</el-menu-item>
        <el-menu-item index="UserCenter">个人中心</el-menu-item>
      </el-menu></el-header>
    <el-container>
      <el-container>
        <el-main>
          <el-carousel :interval="4000" type="card" height="360px">
          <el-carousel-item v-for="item in 6" :key="item">
            <h3>{{ item }}</h3>
          </el-carousel-item>
        </el-carousel></el-main>
        <el-footer>
          <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect" router>
            <el-menu-item index="HomePage">首页</el-menu-item>
            <el-menu-item index="help">帮助中心</el-menu-item>
            <el-menu-item index="aboutus">关于我们</el-menu-item>
          </el-menu>
        </el-footer>
      </el-container>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'home-page',
  data () {
    return {
    }
  },
  computed: {
    activeIndex () {
      return this.$route.path.replace('/', '')
    }
  },
  methods: {
    handleSelect (key, keyPath) {
      console.log(this)
      console.log(key, keyPath)
    }
  }
}

</script>

<style scoped>
  .el-header, .el-footer {
    background-color: #B3C0D1;
    color: #333;
    text-align: center;
    line-height: 60px;
  }
  .el-menu {
    display: flex;
  }
  .el-menu-item{
    margin-right: 20px;
    }
  .el-main {
    background-color: #E9EEF3;
    color: #333;
    text-align: center;
    line-height: 160px;
  }

  body > .el-container {
    margin-bottom: 40px;
  }

  .el-container:nth-child(5) .el-aside,
  .el-container:nth-child(6) .el-aside {
    line-height: 260px;
  }

  .el-container:nth-child(7) .el-aside {
    line-height: 320px;
  }

  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }

  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }
  #head-menu{
    background-color: #545c64;
    text-color: #fff;
    active-text-color: #ffd04b;
    type: flex;
    justify: end
  }
</style>
