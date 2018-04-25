<template>
  <el-container>
    <div class="headerbar">
      <HeaderBar></HeaderBar>
    </div>
    <el-container class="main">
      <div class="sidebar">
        <Sidebar></Sidebar>
      </div>
      <el-container>
        <el-header height="120px" class="ec-header">
          <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
            <el-menu-item index="1">综合</el-menu-item>
            <el-menu-item index="2">直播</el-menu-item>
            <el-menu-item index="3">教师</el-menu-item>
            <el-menu-item index="4">视频</el-menu-item>
          </el-menu>
        </el-header>
        <el-main>
          <el-collapse v-model="activeNames" @change="handleChange">
            <el-collapse-item title="相关教师" name="1">
            </el-collapse-item>
            <el-collapse-item title="相关视频" name="2">
            </el-collapse-item>
            <el-collapse-item title="相关直播" name="3">
            </el-collapse-item>
          </el-collapse>
        </el-main>
      </el-container>
    </el-container>
  </el-container>
</template>

<script>
import Sidebar from '@/components/Sidebar'
import HeaderBar from '@/components/HeaderBar'
export default {
  name: 'search',
  components: {
    Sidebar,
    HeaderBar
  },
  data () {
    return {
      activeIndex: '1',
      activeNames: ['1', '2', '3']
    }
  },
  methods: {
    handleSelect (key, keyPath) {
    },
    handleChange (val) {
    }
  }
}
</script>

<style scoped>
  .sidebar{
    width: 202px;
  }
  .main{
    top:60px;
    left: 0;
    bottom: 0;
    right: 0;
    width: 100%;
    height: 100%;
    position: fixed;
  }
  .header{
    background-color: #0996d1;
    color: #333;
    padding: 10px 10px 10px 900px;
  }
  .ec-header{
    background-color: #e5e5e5;
    padding: 20px;
    margin: 20px 0;
  }
  .el-main{
    background-color: #e5e5e5;
  }
  .search{
    width: 200px;
  }
  .el-menu-item{
    margin: 10px;
  }
  .el-collapse-item{
    margin-bottom: 20px;
  }
</style>
