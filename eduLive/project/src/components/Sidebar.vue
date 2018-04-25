<template>
  <el-aside class="aside" width="200px">
    <el-menu
      default-active="2"
      class="el-menu-vertical"
      @open="handleOpen"
      @close="handleClose"
      background-color="#2d3a4b"
      text-color="#fff">
      <el-menu-item index="1">
        <i class="el-icon-location"></i>
        <span slot="title">导航一</span>
      </el-menu-item>
      <el-menu-item index="2">
        <i class="el-icon-menu"></i>
        <span slot="title">导航二</span>
      </el-menu-item>
      <el-menu-item index="3">
        <i class="el-icon-document"></i>
        <span slot="title">导航三</span>
      </el-menu-item>
      <el-menu-item index="4">
        <i class="el-icon-setting"></i>
        <span slot="title">导航四</span>
      </el-menu-item>
    </el-menu>
  </el-aside>
</template>

<script>
export default {
  name: 'sidebar',
  methods: {
    handleOpen (key, keyPath) {
    },
    handleClose (key, keyPath) {
    }
  }
}
</script>

<style scoped>
  .el-aside{
    background-color: #2d3a4b;
    color: #333;
    border: #afb1aa 1px solid;
    border-right: 0;
  }
  .el-menu{
    width: 198px;
    border: 0;
  }
</style>
