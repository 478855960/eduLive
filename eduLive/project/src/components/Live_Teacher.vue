<template>
  <el-container>
    <el-header>header</el-header>
    <el-container>
      <el-container class="ec_left">
        <el-main>
          <el-tabs type="border-card">
            <el-tab-pane label="PPT">PPT</el-tab-pane>
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
        <el-container style="height:200px;border-top: #1b6d85 2px solid;">
          <el-main></el-main>
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
import CodeEditor from '@/components/CodeEditor'
import Whiteboard from '@/components/whiteboard'
export default {
  name: 'Live_Teacher',
  components: {
    CodeEditor,
    Whiteboard
  },
  data () {
    return {
      input: ''
    }
  },
  method: {}
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
</style>
