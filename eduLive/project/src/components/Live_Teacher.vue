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
          <video id="video" controls></video>
        </el-main>
        <el-container id="container1">
          <el-main>
            <el-card class="box-card" id="content">
            </el-card>
          </el-main>
          <el-footer>
            <el-input
              placeholder="请输入内容"
              v-model="input"
              clearable
              id="msg">
            </el-input>
            <el-button type="primary" icon="el-icon-d-arrow-right" id="btn1"></el-button>
          </el-footer>
        </el-container>
      </el-container>
    </el-container>
  </el-container>
</template>

<script>
/* eslint-disable no-caller */
import CodeEditor from '@/components/CodeEditor'
import SlideDisplay from '@/components/SlideDisplay'
import Whiteboard from '@/components/whiteboard'
export default {
  name: 'Live_Teacher',
  components: {
    CodeEditor,
    Whiteboard,
    SlideDisplay
  },
  data () {
    return {
      input: ''
    }
  },
  method: {
  }
}

navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia
if (navigator.getUserMedia) {
  navigator.getUserMedia({ audio: true, video: { width: 400, height: 350 } },
    function (stream) {
      let video = document.querySelector('video')
      video.src = window.URL.createObjectURL(stream)
      video.onloadedmetadata = function (e) {
        video.play()
      }
    },
    function (err) {
      this.$message('发生错误: ' + err.name)
    }
  )
} else {
  this.$message('警告！该浏览器不支持')
}

let socket = new WebSocket('ws://localhost:8080/TeamYiMing/websocket/server')

$(function () {
  listen()
})

function encodeScript (data) {
  if (data == null || data === '') {
    return ''
  }
  return data.replace('<', '&lt;').replace('>', '&gt;')
}

function emit () {
  let text = encodeScript($('#msg').val())
  let msg = {
    'message': text,
    'color': '#15b8ce',
    'bubbleColor': '#2E2E2E',
    'fontSize': '12',
    'fontType': '黑体'
  }
  msg = JSON.stringify(msg)

  socket.send(msg)
  $('#content').append("<kbd style='color: #" + 'ce181c' + ';float: right; font-size: ' + 12 + ";'>" + text + '</kbd><br/>')
  $('#msg').val('')
}

function listen () {
  socket.onopen = function () {
    $('#content').append('<kbd>Welcome!</kbd></br>')
  }
  socket.onmessage = function (evt) {
    let data = JSON.parse(evt.data)
    $('#content').append("<kbd style='color: " + data.color + ';font-size: ' + data.fontSize + ";margin-top: 10px;'>" + data.message + '</kbd></br>')
  }
  socket.onclose = function (evt) {
    $('#content').append('<kbd>' + 'Close!' + '</kbd></br>')
  }
  socket.onerror = function (evt) {
    $('#content').append('<kbd>' + 'ERROR!' + '</kbd></br>')
  }
}

document.onkeydown = function (event) {
  let e = event || window.event || arguments.callee.caller.arguments[0]
  if (e && e.keyCode === 13) { // enter 键
    emit()
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
  .box-card {
    width: 400px;
  }
  #container1 {
    height:200px;border-top: #1b6d85 2px solid;
  }
</style>
