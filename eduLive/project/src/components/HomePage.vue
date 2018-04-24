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
      <el-input ref="selectLiveRoomInfo" class="search" placeholder="搜索感兴趣的课程"></el-input>
      <el-button class="search-buttom" type="primary" icon="el-icon-search" @click="queryLiveRoom">搜索</el-button>
      <el-carousel :interval="4000" type="card" height="360px">
        <el-carousel-item v-for="item in 6" :key="item">
          <h3>{{ item }}</h3>
        </el-carousel-item>
      </el-carousel>
      <el-row :gutter="20">
      <el-col :span="8" v-for="item in liveRoomList">
        <el-card :body-style="{ padding: '14px' }">
          <div id="all-area">
            <img :src="item.imgPath" class="image">
          </div>
          <span ref="teacherID">{{item.roomName}} {{item.teacherId}}</span>
          <el-button type="text" class="button" @click="routerToLiveRoom(item.teacherId)">观看直播</el-button>
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
  name: 'home-page',
  data () {
    return {
      currentDate: new Date(),
      liveRoomList: [],
      queryInfo: {
        info: ''
      },
      blackList: {
        teacherId: ''
      }
    }
  },
  computed: {
    activeIndex () {
      return this.$route.path.replace('/', '')
    }
  },
  mounted () {
    // alert('hhh')
    let _this = this
    this.$ajax
      .post(this.rootUrl + '/liveroom//getAllLiveRoomInfo.action', null)
      .then(response => {
        if (response.data === 'failed') {
          this.$message.error('获取直播列表失败！')
        }
        _this.liveRoomList = JSON.parse(response.data)
      })
  },
  methods: {
    handleSelect (key, keyPath) {
      console.log(this)
      console.log(key, keyPath)
    },
    routerToLiveRoom: function (teacherid) {
      this.blackList.teacherId = teacherid
      let _this = this
      this.$ajax
        .post(this.rootUrl + '/liveroom/queryStatus.action', _this.blackList)
        .then(response => {
          if (response.data === 0) {
            this.$message.error('您已被拉黑，无法进入该直播间！')
          } else {
            sessionStorage.setItem('teacherID', teacherid)
            alert(sessionStorage.getItem('teacherID'))
            this.$router.push({path: '/Live_Student'})
          }
        })
    },
    queryLiveRoom: function () {
      let _this = this
      this.$nextTick(() => {
        alert(_this.$refs.selectLiveRoomInfo.value)
      })
      this.$ajax
        .post(this.rootUrl + '/liveroom/queryLiveRoomInfo.action', {'otherInfo': 'c'})
        .then(response => {
          alert(JSON.parse(response.data))
        })
    }
  }
}
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

.image{
  width: 100%;
  height: 100%;
}
#all-area{
  height: 210px;
}
.el-card{
  width:350px;
}
</style>

/root/java/jdk1.8.0_141/include
