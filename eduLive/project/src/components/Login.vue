<template>
  <el-container>
    <el-header></el-header>
    <el-main>
      <h1>欢迎使用硕学教育平台</h1>
      <hr>
      <el-form ref="loginForm" label-width="80px" :rules="loginRules" :model="loginForm">
        <el-form-item label="手机号" prop="phoneNum">
          <el-input v-model="loginForm.phoneNum" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" placeholder="请输入密码" type="password"></el-input>
        </el-form-item>
        <el-form-item label="验证码" required>
          <el-col :span="14">
            <el-form-item prop="code" id="codeItem">
              <el-input v-model="loginForm.code" placeholder="请输入验证码" id="code"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6" @click="refreshCode">
            <s-identify :identify-code="identifyCode"></s-identify>
          </el-col>
          <el-col :span="4">
            <el-button id="codeBtn" @click="refreshCode">换一张</el-button>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" v-on:click="submitForm('loginForm')">登录</el-button>
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
</template>

<script>
import SIdentify from '../plugin/identify'
export default {
  components: {SIdentify},
  name: 'Login',
  data () {
    let validateCode = (rule, value, callback) => {
      if (value !== this.identifyCode) {
        callback(new Error('验证码错误'))
      } else {
        callback()
      }
    }
    return {
      url: '/user/userLogin.action',
      identifyCodes: '1234567890',
      identifyCode: '',
      loginForm: {
        phoneNum: '',
        password: '',
        code: ''
      },
      loginRules: {
        phoneNum: [
          { required: true, message: '请输入手机号', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
          { validator: validateCode, trigger: 'blur' }
        ]
      }
    }
  },
  mounted () {
    this.identifyCode = ''
    this.makeCode(this.identifyCodes, 4)
  },
  methods: {
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let _this = this
          this.$ajax.post(this.rootUrl + _this.url, _this.loginForm)
            .then((response) => {
              if (response.data === 'success') {
                this.$message.success('登录成功')
                this.$router.push({path: '/'})
              } else if (response.data === 'failure') {
                this.$message.error('用户名或密码错误')
              } else {
                this.$message.error('系统错误')
              }
            })
        } else {
          console.log('form not valid')
          return false
        }
      })
    },
    randomNum (min, max) {
      return Math.floor(Math.random() * (max - min) + min)
    },
    refreshCode () {
      this.identifyCode = ''
      this.makeCode(this.identifyCodes, 4)
    },
    makeCode (o, l) {
      for (let i = 0; i < l; i++) {
        this.identifyCode += this.identifyCodes[this.randomNum(0, this.identifyCodes.length)]
      }
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
    width: 30%;
  }

  #codeBtn {
    width: 100%;
  }

  #codeItem {
    margin: 0;
    width: 100%;
  }
</style>
