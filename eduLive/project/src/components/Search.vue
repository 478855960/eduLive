<template>
  <el-container>
    <div class="headerbar">
      <HeaderBar></HeaderBar>
    </div>
    <el-container class="main">
      <div class="sidebar">
        <Sidebar></Sidebar>
      </div>
      <el-container class="ec">
        <el-header height="120px" class="ec-header">
          <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
            <el-menu-item index="1" @click="chooseLive=true,chooseRecord=true">综合</el-menu-item>
            <el-menu-item index="2" @click="chooseLive=true,chooseRecord=false">直播</el-menu-item>
            <el-menu-item index="3" @click="chooseLive=false,chooseRecord=true">视频</el-menu-item>
          </el-menu>
        </el-header>
        <el-main>
          <el-collapse v-model="activeNames" @change="handleChange">
            <el-collapse-item title="相关视频" name="1" v-show="chooseRecord">
            </el-collapse-item>
            <el-collapse-item title="相关直播" name="2" v-show="chooseLive">
              <el-row :gutter="20">
                <el-col :span="8" v-for="item in liveRoomList" :key="item.teacherId">
                  <el-card :body-style="{ padding: '14px' }">
                    <div id="all-area">
                      <img :src="item.imgPath" class="image">
                    </div>
                    <span ref="teacherID">{{item.roomName}} {{item.teacherId}}</span>
                    <el-button type="text" class="button" @click="routerToLiveRoom(item.teacherId)">观看直播</el-button>
                  </el-card>
                </el-col>
              </el-row>
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
      activeNames: ['1', '2'],
      chooseLive: true,
      chooseRecord: true,
      liveRoomList: [],
      blackList: {
        teacherId: ''
      }
    }
  },
  created () {
    this.liveRoomList = sessionStorage.getItem('liveRoomList')
  },
  methods: {
    handleSelect (key, keyPath) {
    },
    handleChange (val) {
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
  .ec{
    top:60px;
    left: 200px;
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
