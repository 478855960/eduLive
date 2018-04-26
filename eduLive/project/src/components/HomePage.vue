<template>
  <el-container>
    <div class="headerbar">
      <HeaderBar></HeaderBar>
    </div>
    <el-container class="main">
      <div class="sidebar">
        <Sidebar></Sidebar>
      </div>
      <el-main>
        <el-carousel :interval="3000" type="card" indicator-position="none">
          <el-carousel-item v-for="(item, index) in carouselImg" :key="index">
            <img id="carouselImg" v-bind:src="item"/>
          </el-carousel-item>
        </el-carousel>
        <el-row :gutter="20" id="cardRow">
          <el-col :span="8" v-for="item in liveRoomList" :key="item.teacherId">
            <el-card :body-style="{ padding: '14px' }" id="card">
              <div id="all-area">
                <img :src="item.imgPath" class="image">
              </div>
              <span>{{item.roomName}} {{item.teacherId}}</span>
              <el-button type="text" class="button" @click="routerToLiveRoom(item.teacherId)">观看直播</el-button>
            </el-card>
          </el-col>
        </el-row>
        <el-row :gutter="20" id="cardRow">
          <el-col :span="8" v-for="videoItem in videoList" :key="videoItem.teacherId">
            <el-card :body-style="{ padding: '14px' }" id="card">
              <div id="video-area">
                <img :src="videoItem.imgPath" class="image">
              </div>
              <span>{{videoItem.viedoName}} {{videoItem.teacherId}}</span>
              <el-button type="text" class="button">观看录播视频</el-button>
            </el-card>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Sidebar from '@/components/Sidebar'
import HeaderBar from '@/components/HeaderBar'

export default {
  name: 'home-page',
  components: {
    Sidebar,
    HeaderBar
  },
  data () {
    return {
      currentDate: new Date(),
      liveRoomList: [],
      videoList: [],
      queryInfo: {
        otherInfo: ''
      },
      blackList: {
        teacherId: ''
      },
      carouselImg: [
        '/static/carousel/class1.jpg',
        '/static/carousel/class2.jpg',
        '/static/carousel/class3.jpg',
        '/static/carousel/class4.jpg',
        '/static/carousel/class5.jpg'
      ],
      itemPath: '../assets/logo.png'
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
    this.$ajax
      .post(this.rootUrl + '/video//getAllVideoInfo.action', null)
      .then(response => {
        if (response.data === 'failed') {
          this.$message.error('获取录播列表失败！')
        }
        _this.videoList = JSON.parse(response.data)
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
            this.$router.push({path: '/Live_Student'})
          }
        })
    }
  }
}
</script>

<style scoped>
  .headerbar{
    height: 60px;
  }
  .main{
    top: 60px;
    left: 0;
    right: 0;
    bottom: 0;
    width: 100%;
    height: 100%;
    position: fixed;
  }
  .sidebar{
    width: 202px;
  }
  .el-header {
    padding: 0;
  }
  .el-main {
    padding: 0;
  }
  .el-footer {
    background-color: #3b3a36;
    text-align: center;
    center: true;
  }
  .el-footer {
    color: #333;
    text-align: center;
    line-height: 60px;
  }
  .el-menu {
    background-color: #89bdd3;
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
    background-color: #ffffff;
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
  #head-menu {
    background-color: #545c64;
    text-color: #fff;
    active-text-color: #ffd04b;
    type: flex;
    justify: end;
  }
  .row {
    margin-left: 60px;
  }
  #card {
    width: 360px;
    margin-left: 32px;
  }
  .image {
    width: 100%;
    height: 100%;
  }
  #all-area {
    height: 210px;
  }
  #video-area {
    height: 210px;
  }
  .el-carousel {
    margin-top: 2%;
    margin-left: 3%;
    margin-right: 3%;
    height: 360px;
  }
  #carouselImg {
    height: 100%;
  }
</style>
