<template>
  <el-container>
    <el-main>
      <div id='teacher-Video' ref="teacherVideo"></div>
      <div id="initVideo" ref="initTeacherVideo"></div>
    </el-main>
    <el-footer>
      <el-form :inline="true">
        <el-form-item>
          <el-button type="primary" @click="join()" id="joinBtn" ref="joinBtn">加入</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="leave()" id="leaveBtn" ref="leaveBtn">离开</el-button>
        </el-form-item>
      </el-form>
    </el-footer>
  </el-container>
</template>

<script>
import {AgoraRTC} from '../assets/js/AgoraRTCSDK-2.1.1'
if (!AgoraRTC.checkSystemRequirements()) {
  alert('browser is no support webRTC')
}
AgoraRTC.Logger.error('this is error')
AgoraRTC.Logger.warning('this is warning')
AgoraRTC.Logger.info('this is info')
AgoraRTC.Logger.debug('this is debug')
let client, localStream, camera, microphone
export default {
  data () {
    return {
    }
  },
  methods: {
    getDevices: function getDevices () {
      AgoraRTC.getDevices(function (devices) {
        for (let i = 0; i !== devices.length; ++i) {
          let device = devices[i]
          if (device.kind === 'audioinput') {
            camera = device.id
            break
          }
        }
      })
    },
    join: function () {
      let _this = this
      _this.$refs.teacherVideo.style.visibility = 'visible'
      _this.$refs.initTeacherVideo.style.visibility = 'hidden'
      this.$refs.joinBtn.disabled = true
      if (this.$refs.leaveBtn.disabled === true) {
        this.$refs.leaveBtn.disabled = false
      }
      let appIDinput = 'a86334acf5c04a6aa7a85b66d0767612'
      // 此处理应将教师ID设为频道号，但是暂时无法调试，则固定
      let channelInput = '1000'
      // this.getDevices()
      client = AgoraRTC.createClient({mode: 'interop'})
      client.init(appIDinput, function () {
        client.join(null, channelInput, null, function (uid) {
          microphone = 'default'
          localStream = AgoraRTC.createStream({
            streamID: uid,
            audio: true,
            cameraId: camera,
            microphoneId: microphone,
            video: true,
            screen: false
          })
          localStream.setVideoProfile('720p_3')

          // The user has granted access to the camera and mic.
          localStream.on('accessAllowed', function () {
          })

          // The user has denied access to the camera and mic.
          localStream.on('accessDenied', function () {
          })

          localStream.init(function () {
            localStream.play('teacher-Video')
            client.publish(localStream, function (err) {
              alert('Publish stream failed' + err)
            })

            client.on('stream-published', function (evt) {
              _this.$message.success('发起直播成功！')
            })
          })
        })
      })
      let channelKey = ''
      client.on('error', function (err) {
        if (err.reason === 'DYNAMIC_KEY_TIMEOUT') {
          client.renewChannelKey(channelKey, function () {
          })
        }
      })
      client.on('stream-added', function (evt) {
        let stream = evt.stream
        client.subscribe(stream, function (err) {
          alert('Subscribe stream failed' + err)
        })
      })
      client.on('stream-subscribed', function (evt) {
      })
      client.on('stream-removed', function (evt) {
        let stream = evt.stream
        stream.stop()
        _this.$message.success('关闭摄像头成功')
      })
    },
    leave: function () {
      this.$refs.leaveBtn.disabled = true
      if (this.$refs.joinBtn.disabled === true) {
        this.$refs.joinBtn.disabled = false
      }
      let _this = this
      localStream.close()
      _this.$message.success('关闭摄像头成功')
      client.leave(function () {
      }, function (err) {
        alert(err)
      })
      client.unpublish(localStream, function (err) {
        console.log('Unpublish local stream failed' + err)
      })
      _this.$refs.teacherVideo.style.visibility = 'hidden'
      _this.$refs.initTeacherVideo.style.visibility = 'visible'
    }
  }
}
</script>

<style scoped>
  #teacher-Video{
    margin-top: -30px;
    width: 380px;
    height: 200px;
    background-color: #94d2d0;
    position: absolute;
  }
  #initVideo{
    margin-top: -30px;
    width: 380px;
    height: 200px;
    background-color: #94d2d0;
    position: absolute;
  }
  #joinBtn{
    width: 100%;
  }
  #leaveBtn{
    width: 100%;
  }
  .el-button{
    width: 100%;
  }
  .el-footer{
    margin-top: 170px;
  }
</style>
