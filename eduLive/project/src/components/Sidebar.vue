<template>
  <el-aside class="aside" width="200px">
    <el-menu
      class="el-menu-vertical"
      @open="handleOpen"
      @close="handleClose"
      background-color="#2d3a4b"
      text-color="#fff">
      <el-menu-item index="1" @click="routerToHomePage()">
        <i class="el-icon-location"></i>
        <span slot="title">首页</span>
      </el-menu-item>
      <el-menu-item index="2" @click="routerToUserCenter()">
        <i class="el-icon-menu"></i>
        <span slot="title">个人中心</span>
      </el-menu-item>
      <el-menu-item index="4" v-show="user==0" @click="routerToInitiate()">
        <i class="el-icon-document"></i>
        <span slot="title">发起直播</span>
      </el-menu-item>
      <el-menu-item index="3" @click="routerToLogin()">
        <i class="el-icon-document"></i>
        <span slot="title">注销</span>
      </el-menu-item>
    </el-menu>
  </el-aside>
</template>

<script>
export default {
  name: 'sidebar',
  data () {
    return {
      user: [
        ''
      ],
      active: '1'
    }
  },
  methods: {
    handleOpen (key, keyPath) {
    },
    handleClose (key, keyPath) {
    },
    routerToHomePage: function () {
      this.active = '1'
      this.$router.push({path: '/HomePage'})
    },
    routerToUserCenter: function () {
      this.active = '2'
      this.$router.push({path: '/UserCenter'})
    },
    routerToLogin: function () {
      this.$ajax.post(this.rootUrl + '/user/userSignOut.action', null)
        .then((response) => {
          this.$message.success('注销成功！')
          //  跳转到登录页
          this.$router.push({path: '/Login'})
        })
    },
    routerToInitiate: function () {
      this.active = '3'
      this.$router.push({path: '/Initiate'})
    }
  },
  mounted () {
    this.user = sessionStorage.getItem('loginUser')
  }
}
</script>

<style scoped>
  .el-aside{
    background-color: #2d3a4b;
    color: #333;
    border: #afb1aa 1px solid;
    border-right: 0;
    height: 699px;
    top: 60px;
    position: fixed;
  }
  .el-menu{
    width: 198px;
    border: 0;
  }
</style>
