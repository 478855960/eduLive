<template>
  <el-container>
    <el-header>
      <HeaderBar></HeaderBar>
    </el-header>
    <el-container class="main">
      <Sidebar></Sidebar>
      <el-main>
        <el-row>
          <el-col span="16">
            <h1>发起直播</h1>
          </el-col>
        </el-row>
        <el-form ref="titleForm" :model="titleForm" :rules="titleRules">
          <el-form-item label="" prop="roomName">
            <el-input v-model="titleForm.roomName" placeholder="请输入直播主题"></el-input>
          </el-form-item>
          <el-form-item label="" prop="isRecording">
            <el-radio-group v-model="titleForm.isRecording">
              <el-radio-button label="1" border>进行录播</el-radio-button>
              <el-radio-button label="0" border>不进行录播</el-radio-button>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <el-form>
          <el-form-item label="">
            <el-col :span="6">
              <el-button @click="doCoverInput()" type="primary" plain>上传封面文件</el-button>
              <input ref="coverInput" id="cover-input" type="file" @change="getCover($event)"/>
            </el-col>
            <el-col :span="16" class="name-col">
              <el-input id="cover-name-input" :disabled="true" v-bind:placeholder="coverName"></el-input>
            </el-col>
          </el-form-item>
        </el-form>
        <el-form>
          <el-form-item label="">
            <el-col :span="6">
              <el-button @click="doFileInput()" type="primary" plain>上传教学资源</el-button>
              <input ref="fileInput" id="file-input" type="file" @change="getFile($event)"/>
            </el-col>
            <el-col :span="16" class="name-col">
              <el-input id="file-name-input" :disabled="true" v-bind:placeholder="fileName"></el-input>
            </el-col>
          </el-form-item>
        </el-form>
        <el-button type="primary" @click="confirm" id="confirm-button">确定</el-button>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Sidebar from '@/components/Sidebar'
import HeaderBar from '@/components/HeaderBar'

export default {
  components: {
    Sidebar,
    HeaderBar
  },
  name: 'Initiate',
  data () {
    return {
      fileUping: 0,
      coverUping: 0,
      file: '',
      cover: '',
      coverName: '请选择jpg或png格式的图片',
      fileName: '请选择ppt或pptx格式的文件',
      coverUrl: '/liveroom/cover.action',
      fileUrl: '/liveroom/upload.action',
      initiateUrl: '/liveroom/initiate.action',
      uploadData: {},
      headersData: {
        'Access-Control-Allow-Origin': '*'
      },
      titleForm: {
        roomName: '',
        isRecording: ''
      },
      titleRules: {
        roomName: [
          { required: true, message: '请输入直播主题', trigger: 'blur' }
        ],
        isRecording: [
          { required: true, message: '请选择是否录播', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    doCoverInput () {
      this.$refs.coverInput.click()
    },
    doFileInput () {
      this.$refs.fileInput.click()
    },
    submitFile () {
      let _this = this
      let zipFormData = new FormData()
      zipFormData.append('file', this.file)
      let config = {headers: {'Content-Type': 'multipart/form-data'}}
      this.$message.info('教学资源上传中')
      this.$ajax.post(this.rootUrl + _this.fileUrl, zipFormData, config)
        .then((response) => {
          if (response.data === '') {
            this.$message.error('教学资源文件类型错误')
          } else if (response.data === 'failure') {
            this.$message.error('系统错误')
          } else {
            sessionStorage.setItem('pptImages', response.data)
            this.$message.success('上传成功')
            this.doInitiate()
          }
        })
    },
    submitCover () {
      let _this = this
      let coverFormData = new FormData()
      coverFormData.append('cover', this.cover)
      let config = {headers: {'Content-Type': 'multipart/form-data'}}
      this.$message.info('封面上传中')
      this.$ajax.post(this.rootUrl + _this.coverUrl, coverFormData, config)
        .then((response) => {
          if (response.data === '') {
            this.$message.error('系统错误')
          } else if (response.data === 'failure') {
            this.$message.error('封面文件类型错误')
          } else {
            this.submitFile()
          }
        })
    },
    getFile (event) {
      this.file = event.target.files[0]
      this.fileName = event.target.files[0].name
    },
    getCover (event) {
      this.cover = event.target.files[0]
      this.coverName = event.target.files[0].name
    },
    confirm () {
      this.$refs['titleForm'].validate((valid) => {
        if (valid) {
          this.submitCover()
        } else {
          return false
        }
      })
    },
    doInitiate () {
      let _this = this
      this.$ajax.post(this.rootUrl + _this.initiateUrl, _this.titleForm)
        .then((response) => {
          if (response.data === 'success') {
            this.$message.success('开始直播')
            this.$router.push({path: '/Live_Teacher'})
          } else if (response.data === 'failure') {
            this.$message.error('发起失败')
          } else if (response.data === 'relogin') {
            this.$message.error('请登录')
            this.$router.push({path: '/Login'})
          } else {
            this.$message.error('系统错误')
          }
        })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .name-col {
    float: left;
  }
  .el-button {
    float: left;
    margin-left: -2.5%;
  }
  #cover-input {
    opacity: 0;
    filter: alpha(opacity=0);
    position: absolute;
    right: 1000%;
  }
  #file-input {
    opacity: 0;
    filter: alpha(opacity=0);
    position: absolute;
    right: 1000%;
  }
  .el-input {
    width: 70%;
    float: left;
  }
  .el-radio-group {
    float: left;
    width: 90%;
    padding-left: 0;
  }
  .el-radio-button {
    float: left;
  }
  .el-main {
    margin-bottom: 5%;
    margin-left: 2.5%;
    text-align: center;
  }
  .el-header {
    padding: 0;
  }

  .el-form-item {
    width: 40%;
    margin: 30px auto;
  }

  .el-col {
    padding: 1%;
  }

  hr {
    width: 55%;
  }

  #confirm-button {
    float: none;
    margin-left: -10%;
    text-align: center;
    width: 15%;
  }
</style>
