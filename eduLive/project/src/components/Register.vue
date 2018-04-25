<template>
  <el-container  class='register-container'>
    <el-header></el-header>
    <el-main class='register-main'>
      <h3 class='register-title'>欢迎使用硕学教育平台</h3>
      <el-form status-icon :rules="rules" :model="form" ref="form" class="register-form">
        <el-form-item prop="nickname" >
          <el-input placeholder="输入自定义昵称" v-model="form.nickname" prefix-icon="el-icon-edit-outline"></el-input>
        </el-form-item>
        <el-form-item  prop="name">
          <el-input placeholder="输入真实姓名" v-model="form.name" prefix-icon="el-icon-edit"></el-input>
        </el-form-item>
        <el-form-item  prop="phoneNum">
          <el-input placeholder="输入手机号码" v-model="form.phoneNum" prefix-icon="el-icon-phone"></el-input>
        </el-form-item>
        <el-form-item  prop="password">
          <el-input type="password" placeholder="输入密码" v-model="form.password" prefix-icon="el-icon-goods"></el-input>
        </el-form-item>
        <el-form-item  prop="checkPassword">
          <el-input type="password" placeholder="再次输入密码" v-model="form.checkPassword" prefix-icon="el-icon-goods"></el-input>
        </el-form-item>
        <el-row :gutter="6" justify="space-around">
          <el-col :span="18">
            <el-form-item prop="verificationCode">
              <el-input placeholder="输入短信验证码" v-model="form.verificationCode" prefix-icon="el-icon-message"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-button type="primary" id="sendCode-button" v-on:click="sendMessage('form',$event)" class="getverifi-btn">获取验证码</el-button>
          </el-col>
        </el-row>
        <el-row :gutter="10" justify="space-around">
          <el-col :span="12">
            <el-button type="primary" @click="submitForm('form')" class="reg-btn">注册</el-button>
          </el-col>
          <el-col :span="12">
            <el-button type="primary" class="reset-btn">重置</el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-main>
  </el-container>
</template>

<script>
export default {
  data () {
    let validateAccount = (rule, value, callback) => {
      this.$ajax.post(this.rootUrl + '/user/checkNickname.action', this.form)
        .then((response) => {
          if (response.data === 1) {
            callback(new Error('该昵称已被占用'))
          } else {
            callback()
          }
        })
    }
    let validatePhoneNum = (rule, value, callback) => {
      this.$ajax.post(this.rootUrl + '/user/checkPhoneNum.action', this.form)
        .then((response) => {
          if (response.data === 1) {
            callback(new Error('该电话号码已被注册'))
          } else {
            callback()
          }
        })
    }
    let validateCode = (rule, value, callback) => {
      this.$ajax.post(this.rootUrl + '/user/isVerificationCodeTrue.action', this.form)
        .then((response) => {
          if (response.data !== 1) {
            callback(new Error('验证码错误'))
          } else {
            callback()
          }
        })
    }
    let validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.form.checkPassword !== '') {
          this.$refs.form.validateField('checkPassword')
        }
        callback()
      }
    }
    let validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.form.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      url: '/user/userRegister.action',
      form: {
        nickname: '',
        name: '',
        phoneNum: '',
        password: '',
        checkPassword: '',
        verificationCode: ''
      },
      rules: {
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { validator: validatePass, trigger: 'blur' }
        ],
        checkPassword: [
          { required: true, message: '请输再次输入密码', trigger: 'blur' },
          { validator: validatePass2, trigger: 'blur' }
        ],
        nickname: [
          { required: true, message: '请输入昵称', trigger: 'blur' },
          { min: 2, max: 8, message: '长度在 2 到 8 个字符', trigger: 'blur' },
          { validator: validateAccount, trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入真实姓名', trigger: 'blur' },
          { min: 2, max: 8, message: '长度在 2 到 8 个字符', trigger: 'blur' }
        ],
        phoneNum: [
          { required: true, message: '请输入电话号码', trigger: 'blur' },
          { min: 0, max: 13, message: '不得超过13位', trigger: 'blur' },
          { validator: validatePhoneNum, trigger: 'blur' }
        ],
        verificationCode: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
          { validator: validateCode, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let _this = this
          this.$ajax.post(this.rootUrl + _this.url, _this.form)
            .then((response) => {
              this.$message.success('注册成功！')
              //  跳转到登录页
              this.$router.push({path: '/Login'})
            })
        } else {
          return false
        }
      })
    },
    sendMessage: function (value, event) {
      this.$refs[value].validate((valid) => {
        let _this = this
        this.$ajax.post(this.rootUrl + '/user/sendMessage.action', _this.form)
          .then((response) => {
            this.$message.success('短信发送成功！')
          })
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .register-container {
    margin: 0 auto;
    background-color: #2d3a4b;
  }
  .register-main {
    width: 440px;
    height: 699px;
    margin: 0 auto;
  }
  .el-form-item {
      border: 1px solid rgba(255, 255, 255, 0.1);
      background: rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      color: #454545;
    }
  #sendCode-button {
    margin-top: 0px;
  }
  .register-title {
    font-size: 26px;
    font-weight: 400;
    color: #eee;
    margin: 0px auto 40px auto;
    text-align: center;
    font-weight: bold;
  }
  input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: #889aa4;
      height: 47px;
    }
  .el-input {
      display: inline-block;
      height: 30px;
      width: 100%;
      background: transparent;
    }
    .reset-btn {
      width: 100%;
      border: 20px;
    }
    .reg-btn {
      width: 100%;
      border: 20px;
    }
    .getverifi-btn {
      width: 100%;
      border: 20px;
      padding-left: 0px;
      padding-right: 0px;
    }
</style>
