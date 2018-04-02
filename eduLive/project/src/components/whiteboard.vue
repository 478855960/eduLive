<template>
<el-menu default-active="1-4-1" class="el-menu-vertical-demo" :collapse="isCollapse">
  <el-submenu index="1">
    <template slot="title">
      <i class="el-icon-edit"></i>
      <span slot="title">工具</span>
    </template>
    <el-menu-item-group>
      <span slot="title">画笔</span>
      <el-menu-item index="1-1">黑</el-menu-item>
      <el-menu-item index="1-2">红</el-menu-item>
      <el-menu-item index="1-3">蓝</el-menu-item>
      <el-menu-item index="1-4">绿</el-menu-item>
    </el-menu-item-group>
  </el-submenu>
  
  <el-submenu index="2">
    <template slot="title">
      <i class="el-icon-menu"></i>
      <span slot="title">工具</span>
    </template>
    <el-menu-item-group>
      <span slot="title">图形</span>
      <el-menu-item index="2-1">圆</el-menu-item>
      <el-menu-item index="2-2">椭圆</el-menu-item>
      <el-menu-item index="2-3">矩形</el-menu-item>
    </el-menu-item-group>
  </el-submenu>
  <el-menu-item index="3">
    <i class="el-icon-close"></i>
    <span slot="title">橡皮</span>
  </el-menu-item>
  <el-menu-item index="4">
    <i class="el-icon-document"></i>
    <span slot="title">文字</span>
  </el-menu-item>
  <el-menu-item index="5">
    <i class="el-icon-delete"></i>
    <span slot="title">清空</span>
  </el-menu-item>
</el-menu>

</template>

<script>
export default {
  name: 'Whiteboard',
   data() {
      return {
        isCollapse: true
      };
    },
    methods: {
      
    }

}
</script>

<style>

</style>
