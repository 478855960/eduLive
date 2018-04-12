<template>
  <el-container>
    <el-header></el-header>
    <el-main>
      <h1>发起直播</h1>
      <el-form ref="titleForm" :model="titleForm" :rules="titleRules">
        <el-form-item label="直播主题" prop="title">
          <el-input v-model="titleForm.title" placeholder="请输入直播主题"></el-input>
        </el-form-item>
        <el-form-item label="是否录播" prop="record">
          <el-radio-group v-model="titleForm.record">
            <el-radio label="是"></el-radio>
            <el-radio label="否"></el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <el-form>
        <el-form-item label="上传封面">
          <el-col span="8">
            <input type="file" @change="getCover($event)"/>
          </el-col>
          <el-col span="4">
            <el-button type="primary" @click="submitCover($event)">上传</el-button>
          </el-col>
        </el-form-item>
        <div v-show="coverUping==1">正在上传中</div>
      </el-form>
      <el-form>
        <el-form-item label="上传教学资源">
          <el-col span="8">
            <input type="file" @change="getFile($event)"/>
          </el-col>
          <el-col span="4">
            <el-button type="primary" @click="submitFile($event)">上传</el-button>
          </el-col>
        </el-form-item>
        <div v-show="fileUping==1">正在上传中</div>
      </el-form>
      <el-button type="primary" @click="confirm('titleForm')" id="confirm-button">确定</el-button>
      <!--<el-upload :action="uploadUrl" :data="uploadData" :onError="uploadError" :onSuccess="uploadSuccess" :before-upload="before" :headers="headersData">-->
        <!--<el-button>选择文件</el-button>-->
      <!--</el-upload>-->
    </el-main>
  </el-container>
</template>

<script>
import ElUpload from 'element-ui/packages/upload/src/index'

export default {
  components: {ElUpload},
  name: 'Upload',
  data () {
    return {
      fileUping: 0,
      coverUping: 0,
      file: '',
      cover: '',
      coverUrl: '/user/cover.action',
      fileUrl: '/user/upload.action',
      uploadData: {},
      headersData: {
        'Access-Control-Allow-Origin': '*'
      },
      titleForm: {
        title: '',
        record: ''
      },
      titleRules: {
        title: [
          { required: true, message: '请输入直播主题', trigger: 'blur' }
        ],
        record: [
          { required: true, message: '请选择是否录播', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submitFile (event) {
      event.preventDefault()
      let _this = this
      let zipFormData = new FormData()
      zipFormData.append('file', this.file)
      let config = {headers: {'Content-Type': 'multipart/form-data'}}
      _this.fileUping = 1
      // _this.$http.post
      this.$ajax.post(this.rootUrl + _this.fileUrl, zipFormData, config)
        .then((response) => {
          _this.fileUping = 0
          if (response.data === 'success') {
            this.$message.success('上传成功')
          } else if (response.data === 'failure') {
            this.$message.error('上传错误')
          } else {
            this.$message.error('系统错误')
          }
        })
    },
    submitCover (event) {
      event.preventDefault()
      let _this = this
      let coverFormData = new FormData()
      coverFormData.append('cover', this.cover)
      let config = {headers: {'Content-Type': 'multipart/form-data'}}
      _this.coverUping = 1
      // _this.$http.post
      this.$ajax.post(this.rootUrl + _this.coverUrl, coverFormData, config)
        .then((response) => {
          _this.coverUping = 0
          if (response.data === 'success') {
            this.$message.success('上传成功')
          } else if (response.data === 'failure') {
            this.$message.error('上传错误')
          } else {
            this.$message.error('系统错误')
          }
        })
    },
    getFile (event) {
      this.file = event.target.files[0]
    },
    getCover (event) {
      this.cover = event.target.files[0]
    },
    confirm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$router.push({path: '/Live_Teacher'})
        } else {
          return false
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
