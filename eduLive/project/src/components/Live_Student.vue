<template>
  <el-container>
    <el-header class="header">
      <HeaderBar></HeaderBar>
    </el-header>
    <el-container>
      <div class="sidebar">
        <Sidebar></Sidebar>
      </div>
      <el-container>
        <el-container class="ec_left">
          <div v-show="tabIndex==0">
            <canvas id='stuContent' ref='stuContent' />
            <canvas id='tempcanvas' ref='tempcanvas' />
          </div>
          <div v-show="tabIndex==1">
            <img v-bind:src="pptPage" class="image"/>
          </div>
          <div v-show="tabIndex==2" class="cm-container">
            <codemirror v-model="codeMsg" :options="options"></codemirror>
          </div>
        </el-container>
        <el-container direction="vertical" class="ec_right">
          <el-main class="ec_right-main">
            <el-button type="primary" @click="joinLive()">加入</el-button>
            <el-button type="primary">离开</el-button>
            <el-button type="primary" @click="download()">下载教学资源</el-button>
          </el-main>
          <el-main id="video">
            该主播好像还没开播哦···
          </el-main>
          <el-main class="chat-room">
            <el-card class="box-card" id="content">
              <div v-for="item in list" :key="item.style" class="text item">
                <p v-bind:style="item.style">{{item.userId}}: <span v-html="emoji(item.message)"></span> </p>
              </div>
            </el-card>
          </el-main>
          <el-container id="container-text">
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
                id="msg"
                :disabled="isBanned">
              </el-input>
              <el-button type="primary" icon="el-icon-d-arrow-right" @click="submit" id="btn"></el-button>
            </el-footer>
          </el-container>
        </el-container>
      </el-container>
    </el-container>
  </el-container>
</template>

<script>
/* eslint-disable no-caller,no-eval,no-undef */

import {AgoraRTC} from '../assets/js/AgoraRTCSDK-2.1.1'
import vueEmoji from '@/components/emoji'
import { codemirror } from 'vue-codemirror-lite'
import Sidebar from '@/components/Sidebar'
import HeaderBar from '@/components/HeaderBar'
require('codemirror/mode/javascript/javascript')
require('codemirror/mode/vue/vue')

require('codemirror/addon/hint/show-hint.js')
require('codemirror/addon/hint/html-hint.js')
require('codemirror/addon/hint/css-hint.js')
require('codemirror/addon/hint/show-hint.css')
require('codemirror/addon/hint/javascript-hint.js')
AgoraRTC.Logger.error('this is error')
AgoraRTC.Logger.warning('this is warning')
AgoraRTC.Logger.info('this is info')
AgoraRTC.Logger.debug('this is debug')
export default {
  name: 'Live_Student',
  components: {
    vueEmoji,
    codemirror,
    Sidebar,
    HeaderBar
  },
  data () {
    return {
      liveRoomNumForm: {
        liveRoomNum: ''
      },
      downloadUrl: '/liveroom/getSourcePath.action',
      teacherId: '',
      input: '',
      goEasy: '',
      list: [],
      userId: '',
      codeMsg: '',
      isBanned: false,
      stuStyleObj: {
        color: '#15b8ce',
        fontSize: '12px'
      },
      teaStyleObj: {
        color: '#cc18ce',
        fontSize: '12px'
      },
      canvasCtx: null,
      canvasTemp: null,
      url: '/user/getCurUser.action',
      sessioUser: null,
      ws: null,
      wsStulistObj: null,
      wsWhiteboardObj: null,
      WhiteboardData: {
        type: 'pen',
        color: 'black',
        oldX: 0,
        oldY: 0,
        curX: 0,
        curY: 0
      },
      pptPage: '',
      tabIndex: 0
    }
  },
  created () {
    this.initWebsocket()
    this.codeMsg = '//'
    this.input = ''
    this.list.push({
      userId: this.userId,
      message: '您已进入聊天室',
      style: this.stuStyleObj
    })
    this.goEasy = new GoEasy({
      appkey: 'BC-2c1b84e7528c4d37a2aec64c26343efe'
    })
    this.listen()
    document.onkeyup = this.enter
  },
  computed: {
    options: function () {
      return {
        mode: 'javascript',
        tabSize: 2,
        lineWrapping: true,
        extraKeys: {'Ctrl-Space': 'autocomplete'},
        readOnly: true
      }
    }
  },
  mounted () {
    this.initCanvas()
  },
  methods: {
    download () {
      let _this = this
      _this.liveRoomNumForm.liveRoomNum = sessionStorage.getItem('teacherID')
      this.$ajax.post(this.rootUrl + _this.downloadUrl, _this.liveRoomNumForm)
        .then((response) => {
          if (response.data === 'failure' || response.data === '') {
            this.$message.error('系统错误')
          } else {
            window.open(response.data)
          }
        })
    },
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
    toHomePage () {
      this.$router.push({path: '/HomePage'})
    },
    initCanvas () {
      let canvas = this.$refs.stuContent
      let tempCanvas = this.$refs.tempcanvas
      canvas.width = 720
      canvas.height = 350
      tempCanvas.width = 720
      tempCanvas.height = 350
      this.canvasCtx = this.$refs.stuContent.getContext('2d')
      this.canvasTemp = this.$refs.stuContent.getContext('2d')
    },
    initWebsocket () {
      let _this = this
      // 设置websocket连接
      this.$ajax.post(this.rootUrl + this.url).then((response) => {
        this.sessioUser = JSON.parse(response.data)
        this.getUId(this.sessioUser.nickname)
        this.ws = new WebSocket('ws://localhost:8080/TeamYiMing/websocket/codeEditor/' +
            this.sessioUser.isStudent + '/' +
            this.sessioUser.phoneNum + '/' +
            '12112345678')
        this.ws.onmessage = this.changeCode
        this.wsWhiteboardObj = new WebSocket('ws://localhost:8080/TeamYiMing/websocket/whiteboard/' +
            this.sessioUser.isStudent + '/' +
            this.sessioUser.phoneNum + '/' +
            '12112345678')
        this.wsWhiteboardObj.onmessage = this.whiteboardDraw
        this.wsStulistObj = new WebSocket('ws://localhost:8080/TeamYiMing/websocket/studentList/' +
        this.sessioUser.isStudent + '/' +
        this.sessioUser.phoneNum + '/' +
        this.sessioUser.name + '/' +
        '12112345678')
        this.wsStulistObj.onmessage = function (msg) {
          // alert(msg.data)
          if (msg.data === 'inBlacklist') {
            _this.toHomePage()
            return
          } else if (msg.data === 'banned' || msg.data === 'cancelBanned') {
            _this.changeInput(msg)
            return
          }
          let message = JSON.parse(msg.data)
          if (message.type === 'ppt') {
            _this.pptPage = message.otherInfo
            console.log(_this.pptPage)
          } else if (message.type === 'tabSwitch') {
            _this.tabIndex = message.otherInfo
            console.log(_this.tabIndex)
          }
        }
        window.onbeforeunload = function () {
          this.wsWhiteboardObj.close()
          this.wsStulistObj.close()
          this.ws.close()
        }
      })
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
    getUId (nickname) {
      this.userId = nickname
    },
    changeInput (msg) {
      if (msg.data === 'banned') {
        this.isBanned = true
      } else if (msg.data === 'cancelBanned') {
        this.isBanned = false
      }
    },
    changeCode (msg) {
      this.codeMsg = msg.data
    },
    whiteboardDraw (msg) {
      console.log(msg.data)
      let drawData = JSON.parse(msg.data)
      if (drawData.type === 'pen') {
        this.canvasCtx.strokeStyle = drawData.color
        this.canvasCtx.beginPath()
        this.canvasCtx.moveTo(drawData.oldX, drawData.oldY)
        this.canvasCtx.lineTo(drawData.curX, drawData.curY)
        this.canvasCtx.stroke()
        this.canvasCtx.closePath()
      } else if (drawData.type === 'eraser') {
        this.canvasCtx.clearRect(drawData.curX, drawData.curY, 50, 50)
      } else if (drawData.type === 'circle') {
        this.canvasCtx.strokeStyle = 'black'
        let r = Math.sqrt(Math.pow(drawData.curX - drawData.oldX, 2) + Math.pow(drawData.curY - drawData.oldY, 2))
        this.canvasCtx.beginPath()
        this.canvasCtx.arc(drawData.oldX, drawData.oldY, r, 0, 2 * Math.PI, false)
        this.canvasCtx.stroke()
      } else if (drawData.type === 'line') {
        this.canvasCtx.beginPath()
        this.canvasCtx.moveTo(drawData.oldX, drawData.oldY)
        this.canvasCtx.lineTo(drawData.curX, drawData.curY)
        this.canvasCtx.stroke()
        this.canvasCtx.closePath()
      } else if (drawData.type === 'rectangle') {
        this.canvasCtx.beginPath()
        this.canvasCtx.rect(drawData.oldX, drawData.oldY, drawData.curX - drawData.oldX, drawData.curY - drawData.oldY)
        this.canvasCtx.stroke()
      } else if (drawData.type === 'clear') {
        this.canvasCtx.clearRect(0, 0, this.$refs.stuContent.width, this.$refs.stuContent.height)
      } else if (drawData.type === 'text') {
      } else if (drawData.type === 'savetext') {
        this.canvasCtx.font = '20px Georgia'
        this.canvasCtx.fillText(drawData.sentText, drawData.curX, drawData.curY)
      }
    }
  }
}
</script>

<style scoped>
  .sidebar{
    width: 202px;
  }
  .el-header {
    padding: 0;
  }
  .el-footer {
    background-color: #B3C0D1;
    color: #333;
  }
  .el-main {
    background-color: #E9EEF3;
    color: #333;
  }

  .ec_left{
    width: 60%;
    height: 699px;
    border: #afb1aa 1px solid;
  }

  .ec_right{
    width: 40%;
    height: 699px;
    border: #afb1aa 1px solid;
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
  .cm-container{
    border: #ddd solid 1px;
    margin-bottom: 10px
  }
  .codeEditor{
    width: 720px;
  }
  .ec_right-main{
    height: 150px;
  }
  #video{
    height: 160px;
  }
  .chat-room{
    height: 230px;
  }
  #container-text{
    height: 60px;
  }
  canvas{
    position: absolute;
  }
</style>
