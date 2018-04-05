<template>
  <el-container>
    <el-header></el-header>
    <el-main>
      <h1>上传教学资源</h1>
      <input type="file" @change="getFile($event)" />
      <button @click="submitForm($event)">上传</button>
      <div v-show="uping==1">正在上传中</div>
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
      uping: 0,
      file: '',
      uploadUrl: 'http://localhost:8080/user/upload.action',
      url: '/user/upload.action',
      uploadData: {},
      headersData: {
        'Access-Control-Allow-Origin': '*'
      }
    }
  },
  methods: {
    submitForm (event) {
      console.log('submit!!')
      event.preventDefault()
      let _this = this
      let zipFormData = new FormData()
      zipFormData.append('file', this.file)
      let config = { headers: { 'Content-Type': 'multipart/form-data' } }
      _this.uping = 1
      // _this.$http.post
      this.$ajax.post(this.rootUrl + _this.url, zipFormData, config)
        .then((response) => {
          _this.uping = 0
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

  .el-button {
    background-color: #37C6C0;
    color: #ffffff;
    margin: 0 auto;
  }

  hr {
    width: 55%;
  }

  .el-button {
    margin-right: 80px;
    width: 100%;
  }

  #codeBtn {
    width: 100%;
  }

  #codeItem {
    margin: 0;
    width: 100%;
  }
</style>
