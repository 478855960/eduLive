<template>
  <el-container>
    <el-aside>
      <div id='teacherVideo' style="width: 300px;height: 300px;"></div>
      <div id="studentVideo" style="width: 300px;height: 300px"></div>
      <div class="select">
        <label for="audioSource">Audio source: </label><select id="audioSource"></select>
      </div>
      <div class="select">
        <label for="videoSource">Video source: </label><select id="videoSource"></select>
      </div>
    </el-aside>
    <el-main>
    <el-form size="small">
      <el-form-item label="APP ID:">
        <el-input v-model="form.input" placeholder="请输入内容" ref="appIDInput"></el-input>
      </el-form-item>
      <el-form-item label="Channel:">
        <el-input v-model="form.channel" placeholder="请输入内容" ref="channelInput"></el-input>
      </el-form-item>
      <el-form-item label="Host:">
        <el-checkbox v-model="form.hostchecked" ref="hostCheckbox">备选项</el-checkbox>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="join('form')"  ref="joinBtn">加入</el-button>
        <el-button type="primary" @click="getDevices()">离开</el-button>
      </el-form-item>
    </el-form>
    </el-main>
  </el-container>
</template>

<script>
import {AgoraRTC} from '../assets/js/AgoraRTCSDK-2.1.1'
import {jquery} from '../assets/js/jquery.js'
if (!AgoraRTC.checkSystemRequirements()) {
  alert('browser is no support webRTC')
}
AgoraRTC.Logger.error('this is error')
AgoraRTC.Logger.warning('this is warning')
AgoraRTC.Logger.info('this is info')
AgoraRTC.Logger.debug('this is debug')
let client, localStream, camera, microphone
var audioSelect = document.getElementById('audioSource')
var videoSelect = document.getElementById('videoSource')
export default {
  data () {
    return {
      form: {
        input: '',
        channel: '1000',
        hostchecked: true
      }
    }
  },
  methods: {
    join: function () {
      this.$refs.joinBtn.disabled = true
      this.$refs.hostCheckbox.disabled = true
      let appIDinput = this.$refs.appIDInput.value
      let channelInput = this.$refs.channelInput.value
      let hostCheckBox = this.$refs.hostCheckbox.value
      client = AgoraRTC.createClient({mode: 'interop'})
      client.init(appIDinput, function () {
        client.join(null, channelInput, null, function (uid) {
          if (hostCheckBox) {
            camera = '2f0b505864b76c4e394e71d7c4ea248fad988940680be13798e3a2943fb7bd76'
            microphone = 'default'
            localStream = AgoraRTC.createStream({
              streamID: uid,
              audio: true,
              cameraId: camera,
              microphoneId: microphone,
              video: hostCheckBox,
              screen: false
            })
            if (hostCheckBox) {
              localStream.setVideoProfile('720p_3')
            }

            // The user has granted access to the camera and mic.
            localStream.on('accessAllowed', function () {
            })

            // The user has denied access to the camera and mic.
            localStream.on('accessDenied', function () {
            })

            localStream.init(function () {
              localStream.play('teacherVideo')

              client.publish(localStream, function (err) {
                console(err.toString())
              })

              client.on('stream-published', function (evt) {
                alert('发布成功')
              })
            })
          }
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
          console.log('Subscribe stream failed', err)
        })
      })
      client.on('stream-subscribed', function (evt) {
        let stream = evt.stream
        stream.play('studentVideo')
      })
    },
    getDevices: function getDevices () {
      AgoraRTC.getDevices(function (devices) {
        for (var i = 0; i !== devices.length; ++i) {
          var device = devices[i]
          var option = document.createElement('option')
          option.value = device.deviceId
          if (device.kind === 'audioinput') {
          } else if (device.kind === 'videoinput') {
          } else {
          }
        }
      })
    }
  }
}
</script>

<style scoped>
  .el-input{
    width: 50%
  }
  .el-button{
    width: 27%
  }
  .el-aside{
    width: 50%
  }
  .el-main{
    margin-top: 50px
  }
</style>
