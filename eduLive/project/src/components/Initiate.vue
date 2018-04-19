<template>
  <el-container>
    <el-header></el-header>
    <el-main>
      <h1>发起直播</h1>
      <el-form ref="titleForm" :model="titleForm" :rules="titleRules">
        <el-form-item label="直播主题" prop="roomName">
          <el-input v-model="titleForm.roomName" placeholder="请输入直播主题"></el-input>
        </el-form-item>
        <el-form-item label="是否录播" prop="isRecording">
          <el-radio-group v-model="titleForm.isRecording">
            <el-radio label="1">是</el-radio>
            <el-radio label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <el-form>
        <el-form-item label="上传封面">
          <el-col :span="8">
            <input type="file" @change="getCover($event)"/>
          </el-col>
        </el-form-item>
        <div v-show="coverUping==1">正在上传中</div>
      </el-form>
      <el-form>
        <el-form-item label="上传教学资源">
          <el-col :span="8">
            <input type="file" @change="getFile($event)"/>
          </el-col>
        </el-form-item>
        <div v-show="fileUping==1">正在上传中</div>
      </el-form>
      <el-button type="primary" @click="confirm" id="confirm-button">确定</el-button>
    </el-main>
  </el-container>
</template>

<script>
import ElUpload from 'element-ui/packages/upload/src/index'

export default {
  components: {ElUpload},
  name: 'Initiate',
  data () {
    return {
      fileUping: 0,
      coverUping: 0,
      file: '',
      cover: '',
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
    },
    getCover (event) {
      this.cover = event.target.files[0]
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
  .el-container {
    text-align: center;
  }

  .el-form-item {
    width: 40%;
    margin: 30px auto;
  }

  .el-col {
    padding: 1%;
  }

  .el-button {
    background-color: #37C6C0;
    color: #ffffff;
    margin-left: 80px;
    width: 30%;
    text-align: center;
  }

  hr {
    width: 55%;
  }

  .el-button {
    margin-right: 80px;
    width: 100%;
  }

  #confirm-button {
    width: 15%;
  }
</style>
