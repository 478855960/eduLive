<template>
  <div class="login-container">
    <el-form autoComplete="on" ref="loginForm" :rules="loginRules" :model="loginForm" label-position="left" label-width="0px"
      class="card-box login-form">
      <h3 class="title">欢迎来到硕学教育平台</h3>
      <el-form-item prop="phoneNum">
        <el-input name="phoneNum" v-model="loginForm.phoneNum" type="text" autoComplete="on" placeholder="输入电话号"
        prefix-icon="el-icon-phone"/>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" v-model="loginForm.password" placeholder="输入密码" prefix-icon="el-icon-goods"></el-input>
      </el-form-item>
      <el-form-item prop="code" id="code-item">
          <el-input v-model="loginForm.code" placeholder="输入验证码" id="code" prefix-icon="el-icon-picture"></el-input>
      </el-form-item>
      <el-row type="flex" :gutter="10" justify="space-around">
        <el-col :span="8">
          <s-identify :identify-code="identifyCode" id="identify"></s-identify>
        </el-col>
        <el-col :span="16">
          <el-button id="code-button" @click="refreshCode" type="primary" class="change-btn">换一张</el-button>
        </el-col>
      </el-row>
      <br>
          <el-form-item>
            <el-button type="primary" v-on:click="submitForm('loginForm')" id="loginBtn">登录</el-button>
          </el-form-item>
    <el-row type="flex" justify="end">
      <el-col :span="15">
      </el-col>
      <el-col :span="9">
        <label class="reg-label" @click="toReg">没有账号?立即注册!</label>
      </el-col>
    </el-row>
    </el-form>
  </div>
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
              if (response.data !== 'failure') {
                this.$message.success('登录成功')
                sessionStorage.setItem('loginUser',JSON.parse(response.data).isStudent)
                sessionStorage.setItem('accessToken', 'Login Successfully')
                this.$router.push({path: '/HomePage'})
              } else if (response.data === 'failure') {
                this.$message.error('用户名或密码错误')
              } else {
                this.$message.error('系统错误')
              }
            })
        } else {
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
    },
    toReg () {
      this.$router.push({path: '/Register'})
    }
  }
}
</script>

<style lang="scss" scoped>
  $bg:#2d3a4b;
  $dark_gray:#889aa4;
  $light_gray:#eee;

  .login-container {
    position: fixed;
    height: 100%;
    width:100%;
    background-color: $bg;
    #identify{
      height: 100%;
      width: 100%;
    }
    #loginBtn{
      width: 100%;
    }
    #code-item{
      vertical-align:middle
    }
    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
    }
    .el-input {
      display: inline-block;
      height: 40px;
      width: 100%;
      background: transparent;
    }
    .tips {
      font-size: 14px;
      color: #fff;
      margin-bottom: 10px;
    }
    .svg-container {
      padding: 6px 5px 6px 15px;
      color: $dark_gray;
      vertical-align: middle;
      width: 30px;
      display: inline-block;
      &_login {
        font-size: 20px;
      }
    }
    .title {
      font-size: 26px;
      font-weight: 400;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
    .login-form {
      position: absolute;
      left: 0;
      right: 0;
      width: 400px;
      padding: 35px 35px 15px 35px;
      margin: 120px auto;
    }
    .el-form-item {
      border: 1px solid rgba(255, 255, 255, 0.1);
      background: rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      color: #454545;
    }
    .show-pwd {
      position: absolute;
      right: 10px;
      top: 7px;
      font-size: 16px;
      color: $dark_gray;
      cursor: pointer;
      user-select:none;
    }
    .thirdparty-button{
      position: absolute;
      right: 35px;
      bottom: 28px;
    }
    .change-btn {
      width: 100%;
    }
    .reg-label {
      color: #eee;
    }
  }
</style>
