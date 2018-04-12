<template>
  <el-container>
    <el-header></el-header>
    <el-container>
      <el-container class="ec_left">
        <el-main>
            <el-button type="primary" @click="joinLive()">加入</el-button>
            <el-button type="primary">离开</el-button>
        </el-main>
      </el-container>
      <el-container direction="vertical" class="ec_right">
        <el-main id="video">
          该主播好像还没开播哦···
        </el-main>
        <el-container id="container-text">
          <el-main>
          </el-main>
          <el-footer>
            <el-input
              placeholder="请输入内容"
              v-model="input"
              clearable>
            </el-input>
            <el-button type="primary" icon="el-icon-d-arrow-right"></el-button>
          </el-footer>
        </el-container>
      </el-container>
    </el-container>
  </el-container>
</template>

<script>
import {AgoraRTC} from '../assets/js/AgoraRTCSDK-2.1.1'
AgoraRTC.Logger.error('this is error')
AgoraRTC.Logger.warning('this is warning')
AgoraRTC.Logger.info('this is info')
AgoraRTC.Logger.debug('this is debug')
export default {
  name: 'Live_Student',
  data () {
    return {
      input: ''
    }
  },
  created () {
    this.initWebsocket()
  },
  methods: {
    joinLive () {
      let appID = 'a86334acf5c04a6aa7a85b66d0767612'
      let channel = '1000'
      let client = AgoraRTC.createClient({mode: 'interop'})
      let _this = this
      client.init(appID, function () {
        client.join(null, channel, null, function (uid) {
        })
      })
      client.on('stream-added', function (evt) {
        let stream = evt.stream
        client.subscribe(stream, function (err) {
          alert('Subscribe stream failed' + err)
        })
      })
      client.on('stream-subscribed', function (evt) {
        let stream = evt.stream
        stream.play('video')
      })
      client.on('peer-leave', function (evt) {
        let stream = evt.stream
        stream.stop()
        _this.$message('老师关闭了摄像头')
      })
    },
    initWebsocket () {
      // 设置websocket连接
      this.wsObj = new WebSocket('ws://localhost:8080/TeamYiMing/websocket/studentList')
    }
  }
}
</script>

<style scoped>
  .el-header {
    background-color: #B3C0D1;
    color: #333;
    height: 60px;
  }

  .el-footer {
    background-color: #B3C0D1;
    color: #333;
  }
  .el-aside {
    background-color: #D3DCE6;
    color: #333;
  }

  .el-main {
    background-color: #E9EEF3;
    color: #333;
  }

  .ec_left{
    width: 70%;
    height: 600px;
    border: #1b6d85 2px solid;
  }

  .ec_right{
    width: 30%;
    height: 600px;
    border: #1b6d85 2px solid;
  }

  .el-button-group{
    margin:40px 75px;
  }

  .el-input{
    margin: 10px auto;
    width: 350px;
  }
  #container-text {
    height:200px;
  }
  #video {
    width: 100%;
    height: 220px;

  }
</style>
