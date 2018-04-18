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
        <el-main>
          <el-card class="box-card" id="content">
            <div v-for="item in list" :key="item" class="text item">
              <p v-bind:style="item.style">{{item.userId}}: <span v-html="emoji(item.message)"></span> </p>
            </div>
          </el-card>
        </el-main>
        <el-container id="container-text">
          <el-main>
          </el-main>
          <el-footer>
            <el-popover
              ref="popover"
              placement="top-start"
              trigger="click">
              <div class="emoji-box">
                <vue-emoji
                  @select="selectEmoji">
                </vue-emoji>
                <span class="pop-arrow arrow"></span>
              </div>
            </el-popover>
            <i class="icon iconfont icon-face" v-popover:popover></i>
            <el-input
              placeholder="请输入内容"
              v-model="input"
              clearable
              id="msg">
            </el-input>
            <el-button type="primary" icon="el-icon-d-arrow-right" @click="submit" id="btn"></el-button>
          </el-footer>
        </el-container>
      </el-container>
    </el-container>
  </el-container>
</template>

<script>
/* eslint-disable no-caller,no-eval,no-undef */

import {AgoraRTC} from '../assets/js/AgoraRTCSDK-2.1.1'
import vueEmoji from '@/components/emoji'
AgoraRTC.Logger.error('this is error')
AgoraRTC.Logger.warning('this is warning')
AgoraRTC.Logger.info('this is info')
AgoraRTC.Logger.debug('this is debug')
export default {
  name: 'Live_Student',
  components: {
    vueEmoji
  },
  data () {
    return {
      input: '',
      goEasy: '',
      list: [],
      userId: '',
      stuStyleObj: {
        color: '#15b8ce',
        fontSize: '12px'
      },
      teaStyleObj: {
        color: '#cc18ce',
        fontSize: '12px'
      }
    }
  },
  created () {
    this.initWebsocket()
    this.input = ''
    this.userId = 'StudentX'
    this.list.push({
      userId: this.userId,
      message: '您已进入聊天室',
      style: this.stuStyleObj
    })
    this.goEasy = new GoEasy({
      appkey: 'BC-2c1b84e7528c4d37a2aec64c26343efe'
    })
    this.listen()
    document.onkeydown = this.enter
  },
  methods: {
    selectEmoji (code) {
      this.input += code
    },
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
    },
    encodeScript (data) {
      if (data == null || data === '') {
        return ''
      }
      return data.replace('<', '&lt;').replace('>', '&gt;')
    },
    submit () {
      let text = this.encodeScript(this.input)
      let message = "{'userId': '" + this.userId + "', 'message': '" + text + "'}"
      this.goEasy.publish({
        channel: 'stu',
        message: message
      })
      this.list.push({
        userId: this.userId,
        message: text,
        style: this.stuStyleObj
      })
      this.input = ''
    },
    emit () {
      let text = this.encodeScript(this.input)
      let message = "{'userId': '" + this.userId + "', 'message': '" + text + "'}"
      if (text !== '') {
        this.goEasy.publish({
          channel: 'stu',
          message: message
        })
        this.list.push({
          userId: this.userId,
          message: text,
          style: this.stuStyleObj
        })
      }
      this.input = ''
    },
    changeStyle () {
      this.isTeacher = false
    },
    listen () {
      let _list = this.list
      let _teaStyleObj = this.teaStyleObj
      this.goEasy.subscribe({
        channel: 'teach',
        onMessage: function (data) {
          let result = eval('(' + data.content + ')')
          _list.push({
            userId: result.userId,
            message: result.message,
            style: _teaStyleObj
          })
        }
      })
    },
    enter (event) {
      let e = event || window.event || arguments.callee.caller.arguments[0]
      if (e && e.keyCode === 13) { // enter 键
        this.emit()
      }
    },
    mounted () {
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
    width: 320px;
  }
  #container-text {
    height:200px;
  }
  #video {
    width: 100%;
    height: 220px;
  }
  .iconfont {
    cursor: pointer;
    color: #f7793a;
  }
</style>
