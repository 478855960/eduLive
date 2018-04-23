<template>
  <el-container>
    <el-header></el-header>
    <el-container>
      <el-container class="ec_left">
        <el-main>
          <el-tabs type="border-card">
            <el-tab-pane label="PPT">
              <SlideDisplay>
              </SlideDisplay>
            </el-tab-pane>
            <el-tab-pane label="代码">
              <CodeEditor>
              </CodeEditor>
            </el-tab-pane>
            <el-tab-pane label="白板">
              <whiteboard>
              </whiteboard>
            </el-tab-pane>
          </el-tabs>
        </el-main>
        <el-footer height="150px">
          <student-list></student-list>
          <el-button-group>
            <el-button type="primary" plain>结束直播</el-button>
            <el-button type="success" plain>暂停直播</el-button>
            <el-button type="success" plain>学生列表</el-button>
            <el-button type="info" plain>全员禁言</el-button>
          </el-button-group>
        </el-footer>
      </el-container>
      <el-container direction="vertical" class="ec_right">
        <el-main>
          <Video></Video>
        </el-main>
        <el-container id="container-text">
          <el-main>
            <el-card class="box-card" ref="chatcard">
              <div v-for="item in list" class="text item">
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
</template>

<script>
/* eslint-disable no-caller */
import StudentList from '@/components/StudentList'
import CodeEditor from '@/components/CodeEditor'
import SlideDisplay from '@/components/SlideDisplay'
import Whiteboard from '@/components/whiteboard'
import Video from '@/components/video'
import vueEmoji from '@/components/emoji'

export default {
  name: 'Live_Teacher',
  components: {
    StudentList,
    CodeEditor,
    Whiteboard,
    SlideDisplay,
    Video,
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
    }
  },
  mounted () {
    this.listen()
    document.onkeydown = this.enter
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
    height: 650px;
    border: #1b6d85 2px solid;
  }

  .ec_right{
    width: 30%;
    height: 650px;
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
    width: 320px;
  }
  .box-card{
    width: 400px;
  }
  #container-text{
    height:320px;
    border-top: #1b6d85 2px solid;
  }
   .iconfont {
    cursor: pointer;
    color: #f7793a;
  }
</style>
