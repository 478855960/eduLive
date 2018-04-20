<template>
  <elcontainer>
    <el-header>
      <el-menu :default-active="activeIndex" class="el-menu" mode="horizontal" @select="handleSelect"  text-color="#3b3a36" active-text-color="#3b3a36" router>
        <el-menu-item index="HomePage">硕学平台</el-menu-item>
        <el-menu-item index="LiveList">直播列表</el-menu-item>
        <el-menu-item index="VideoList">录播列表</el-menu-item>
        <el-menu-item index="UserCenter">个人中心</el-menu-item>
        <el-menu-item index="Login" >登陆</el-menu-item>
        <el-menu-item index="Register" >注册</el-menu-item>
        <el-menu-item index="Initiate" >发起直播</el-menu-item>
      </el-menu>
    </el-header>
    <el-main>
      <el-input class="search" v-model="input" placeholder="搜索感兴趣的课程"></el-input>
      <el-button class="search-buttom" type="primary" icon="el-icon-search">搜索</el-button>
      <el-carousel :interval="4000" type="card" height="360px">
        <el-carousel-item v-for="item in 6" :key="item">
          <h3>{{ item }}</h3>
        </el-carousel-item>
      </el-carousel>

      <el-row>
  <el-col :span="8" v-for="(o, index) in 2" :key="o" :offset="index > 0 ? 2 : 0">
    <el-card :body-style="{ padding: '0px' }">
      <img src="../assets/logo.png" class="image">
      <div style="padding: 14px;margin-top:20px">
        <span>好吃的汉堡</span>
        <div class="bottom clearfix">
          <time class="time">{{ currentDate }}</time>
          <el-button type="text" class="button">操作按钮</el-button>
        </div>
      </div>
    </el-card>
  </el-col>
</el-row>

    </el-main>
    <el-footer>
      <el-row>
        <el-col :span="4"></el-col>
        <el-col :span="20">
      <el-breadcrumb separator="/">
     <el-breadcrumb-item>首页</el-breadcrumb-item>
    <el-breadcrumb-item>帮助中心</el-breadcrumb-item>
    <el-breadcrumb-item>关于我们</el-breadcrumb-item>
    <el-breadcrumb-item>意见反馈</el-breadcrumb-item>
    </el-breadcrumb>
        </el-col>
      </el-row>
    </el-footer>
  </elcontainer>
</template>

<script>
export default {
  name: "home-page",
  data() {
    return {
      currentDate: new Date()
    };
  },
  computed: {
    activeIndex() {
      return this.$route.path.replace("/", "");
    }
  },
  mounted() {
    // alert('hhh')
    this.$ajax
      .post(this.rootUrl + "/liveroom//getAllLiveRoomInfo.action", null)
      .then(response => {
        alert(response.data)
      });
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(this);
      console.log(key, keyPath);
    }
  }
};
</script>

<style scoped>
.el-header {
  background-color: #89bdd3;
}
.el-footer {
  background-color: #3b3a36;
  text-align: center;
  center: true;
}
.el-header,
.el-footer {
  color: #333;
  text-align: center;
  line-height: 60px;
}
.el-menu {
  background-color: #89bdd3;
}
.search {
  width: 200px;
  margin-top: 1px;
  margin-left: 900px;
}
.search-buttom {
  height: 40px;
  margin: 10px 5px 15px;
  background-color: #89bdd3;
  border: none;
}
.el-menu-item {
  margin-right: 20px;
}
.login {
  margin-left: 550px;
}
.el-main {
  background-color: #e9eef3;
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
  background-color: #b3c2bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #c0dfd9;
}
#head-menu {
  background-color: #545c64;
  text-color: #fff;
  active-text-color: #ffd04b;
  type: flex;
  justify: end;
}
.el-card{
  /* width: 25%;
  height: 500px; */
}
</style>
