<template>
  <el-container>
    <el-header>
      <el-menu :default-active="activeIndex" class="el-menu" mode="horizontal" @select="handleSelect">
        <el-menu-item>录播间</el-menu-item>
        <el-menu-item index="LiveList">直播列表</el-menu-item>
        <el-menu-item index="VideoList">录播列表</el-menu-item>
        <el-menu-item index="UserCenter">个人中心</el-menu-item>
      </el-menu></el-header>
    <el-container>
      <el-container>
        <el-main>
        </el-main>
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
  name: 'video-room'
}
</script>

<style scoped>
  #head-menu{
    background-color: #545c64;
    text-color: #fff;
    active-text-color: #ffd04b;
    type: flex;
    justify: end;
    align-self: flex-end;
  }
</style>
