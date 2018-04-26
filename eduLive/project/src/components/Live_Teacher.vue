<template>
  <el-container>
    <el-header class="header">
      <HeaderBar></HeaderBar>
    </el-header>
    <el-container class="main">
      <div class="sidebar">
        <Sidebar></Sidebar>
      </div>
      <el-container class="ec">
        <div id="test"></div>
        <el-container class="ec_left">
          <el-main>
            <el-tabs v-model="activeTab" type="border-card" @tab-click="switchTab">
              <el-tab-pane label="白板" name="whiteboard">
                <whiteboard>
                </whiteboard>
              </el-tab-pane>
              <el-tab-pane label="PPT" name="ppt">
                <SlideDisplay>
                </SlideDisplay>
              </el-tab-pane>
              <el-tab-pane label="代码" name="code">
                <CodeEditor>
                </CodeEditor>
              </el-tab-pane>
            </el-tabs>
          </el-main>
          <el-footer height="60px">
            <div class="button-group">
              <el-button type="primary">结束直播</el-button>
              <el-button type="success" class="btn">暂停直播</el-button>
              <student-list></student-list>
              <el-button type="info">全员禁言</el-button>
              <el-button  @click="recordStart()">开始录制</el-button>
            </div>
          </el-footer>
        </el-container>
        <el-container direction="vertical" class="ec_right">
          <el-main>
            <Video></Video>
          </el-main>
          <el-container id="container-text">
            <el-main>
              <el-card class="box-card" ref="chatcard">
                <div v-for="item in list" class="text item" :key="item.userId">
                  <p v-bind:style="item.style">{{item.userId}}: <span v-html="emoji(item.message)"></span> </p>
                </div>
              </el-card>
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
                ref="msg">
              </el-input>
              <el-button type="primary" icon="el-icon-d-arrow-right" @click="submit" ref="btn"></el-button>
            </el-footer>
          </el-container>
        </el-container>
      </el-container>
    </el-container>
  </el-container>
</template>

<script>
/* eslint-disable no-caller */
import StudentList from '@/components/Studentlist'
import CodeEditor from '@/components/CodeEditor'
import SlideDisplay from '@/components/SlideDisplay'
import Whiteboard from '@/components/whiteboard'
import Video from '@/components/video'
import vueEmoji from '@/components/emoji'
import Bus from './bus.js'
import Sidebar from '@/components/Sidebar'
import {AgoraRTC} from '../assets/js/AgoraRTCSDK-2.1.1'
import HeaderBar from '@/components/HeaderBar'

export default {
  name: 'Live_Teacher',
  components: {
    StudentList,
    CodeEditor,
    Whiteboard,
    SlideDisplay,
    Video,
    vueEmoji,
    Sidebar,
    HeaderBar
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
      },
      tabSwitchOp: {
        type: '',
        liveroomNum: '',
        phoneNum: '',
        otherInfo: ''
      },
      sessionUser: null,
      tabIndex: 0,
      getCurrentUserUrl: '/user/getCurUser.action',
      activeTab: 'ppt'
    }
  },
  created () {
    this.input = ''
    this.userId = 'Teacher'
    this.list.push({
      userId: this.userId,
      message: '您已进入聊天室',
      style: this.teaStyleObj
    })
    this.goEasy = new GoEasy({
      appkey: 'BC-2c1b84e7528c4d37a2aec64c26343efe'
    })
  },
  methods: {
    handleOpen (key, keyPath) {
    },
    handleClose (key, keyPath) {
    },
    selectEmoji (code) {
      this.input += code
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
        channel: 'teach',
        message: message
      })
      this.list.push({
        userId: this.userId,
        message: text,
        style: this.teaStyleObj
      })
      this.input = ''
    },
    emit () {
      let text = this.encodeScript(this.input)
      let message = "{'userId': '" + this.userId + "', 'message': '" + text + "'}"
      if (text !== '') {
        this.goEasy.publish({
          channel: 'teach',
          message: message
        })
        this.list.push({
          userId: this.userId,
          message: text,
          style: this.teaStyleObj
        })
      }
      this.input = ''
    },
    changeStyle () {
      this.isTeacher = false
    },
    listen () {
      let _list = this.list
      let _stuStyleObj = this.stuStyleObj
      this.goEasy.subscribe({
        channel: 'stu',
        onMessage: function (data) {
          let result = eval('(' + data.content + ')')
          _list.push({
            userId: result.userId,
            message: result.message,
            style: _stuStyleObj
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
    switchTab (tab, e) {
      this.tabIndex = tab.index
      this.tabSwitchOp.otherInfo = this.tabIndex
      Bus.$emit('tabIndex', this.tabSwitchOp)
      if (tab.index === '1') {
        Bus.$emit('pptSwitched', 1)
      }
    },
    recordStart: function () {
      let recordClient, recordStream
      let _this = this
      let appIDinput = 'a86334acf5c04a6aa7a85b66d0767612'
      let channelInput = '10001'
      alert(channelInput)
      recordClient = AgoraRTC.createClient({mode: 'interop'})
      recordClient.init(appIDinput, function () {
        recordClient.join(null, channelInput, null, function (uid) {
          recordStream = AgoraRTC.createStream({
            streamID: uid,
            audio: false,
            video: false,
            screen: true,
            extensionId: 'minllpmhdgpndnkomcoccfekfegnlikg'
          })
          recordStream.setVideoProfile('720p_3')
          // The user has granted access to the camera and mic.
          recordStream.on('accessAllowed', function () {
          })
          // The user has denied access to the camera and mic.
          recordStream.on('accessDenied', function () {
          })
          recordStream.init(function () {
            recordStream.play('test')
            recordClient.publish(recordStream, function (err) {
              alert('Publish stream failed' + err)
            })
            recordClient.on('stream-published', function (evt) {
              _this.$message.success('发起录制成功！')
            })
          })
        })
      })
    }
  },
  mounted () {
    this.listen()
    document.onkeydown = this.enter
    this.$ajax.post(this.rootUrl + this.getCurrentUserUrl)
      .then((response) => {
        this.sessionUser = JSON.parse(response.data)
        this.tabSwitchOp.type = 'tabSwitch'
        this.tabSwitchOp.liveroomNum = this.sessionUser.phoneNum
        this.tabSwitchOp.phoneNum = this.sessionUser.phoneNum
      })
    this.tabSwitchOp.otherInfo = this.tabIndex
    Bus.$emit('tabIndex', this.tabSwitchOp)
    this.activeTab = 'whiteboard'
  }
}

</script>

<style scoped>
  .sidebar{
    width: 202px;
  }
  .main{
  }
  .el-header {
    padding: 0;
  }

  .el-footer {
    background-color: #2d3a4b;
    color: #333;
  }

  .el-main {
    background-color: #e0e0e0;
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
  .el-input{
    margin: 10px auto;
    width: 320px;
  }
  .button-group{
    width: 720px;
    margin: 10px 100px;
  }
  .button-group .el-button{
    margin: 0 10px;
  }
  .box-card{
    width: 400px;
  }

  #container-text{
    height:320px;
    border-top: #afb1aa 1px solid;
  }

   .iconfont {
    cursor: pointer;
    color: #f7793a;
  }
</style>
