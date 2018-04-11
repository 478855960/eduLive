<template>
  <el-container>
    <el-header>header</el-header>
    <el-container>
      <el-container class="ec_left">
        <el-main>
            <el-button type="primary" @click="joinLive()">加入</el-button>
            <el-button type="primary">离开</el-button>
        </el-main>
      </el-container>
      <el-container direction="vertical" class="ec_right">
        <el-main>
        </el-main>
        <el-container id="video-area">
          <el-main>
            <div id="video" style="width: 500px;height: 300px;"></div>
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
import {jquery} from '../assets/js/jquery.js'
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
  methods: {
    joinLive () {
      let appID = 'a86334acf5c04a6aa7a85b66d0767612'
      let channel = '1000'
      let client = AgoraRTC.createClient({mode: 'interop'})
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
        alert('coming!!!!!!!!!!!!!!!')
        stream.play('video')
      })
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

  .el-button-group .el-button{
    width: 200px;
    height: 50px;
  }
  .el-input{
    margin: 10px auto;
    width: 350px;
  }
  #video-area{
    height:200px;
    border-top: #1b6d85 2px solid;
  }
</style>
