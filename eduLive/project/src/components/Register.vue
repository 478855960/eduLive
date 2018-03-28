<template>
  <el-container>
    <el-header></el-header>
    <el-main>
      <h1 style="text-align:center">欢 迎 使 用 硕 学 教 育 平 台</h1>
      <hr>
      <el-form status-icon :rules="rules2" :model="form" ref="form" size="small">
        <el-form-item label="昵称" prop="nickname">
          <el-input placeholder="输入自定义昵称" v-model="form.nickname"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phoneNum">
          <el-input placeholder="输入手机号码" v-model="form.phoneNum"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" placeholder="输入密码" v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPassword">
          <el-input type="password" placeholder="再次输入密码" v-model="form.checkPassword"></el-input>
        </el-form-item>
        <el-form-item label="验证码">
          <el-input placeholder="输入短信验证码" v-model="form.verificationCode"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('form')">注册</el-button>
          <el-button>重置</el-button>
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
</template>

<script>
export default {
  data () {
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
        phoneNum: '',
        password: '',
        checkPassword: '',
        verificationCode: ''
      },
      rules: {
      },
      rules2: {
        password: [
          { validator: validatePass, trigger: 'blur' }
        ],
        checkPassword: [
          { validator: validatePass2, trigger: 'blur' }
        ],
        nickname: [
          { required: true, message: '请输入昵称', trigger: 'blur' },
          { min: 2, max: 8, message: '长度在 2 到 8 个字符', trigger: 'blur' }
        ],
        phoneNum: [
          { required: true, message: '请输入电话号码', trigger: 'blur' },
          { min: 0, max: 13, message: '不得超过13位', trigger: 'blur' }
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
              // this.$refs[formName].resetFields()
              //  跳转到登录页
              this.$router.push({path: '/'})
            })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .el-main{
    width: 500px;
    height: 700px;
  }
  .el-container{
    margin: 0 auto;
    width: 500px;
    height: 715px;
    background-repeat: no-repeat;
  }
  .el-form{
    color: white;
  }
  .body{
    margin: 0px;
  }
  .el-button{
    background-color: #37C6C0;
    width: 222px;
  }
  .el-input{
    color: #e3e3e3;
  }
  hr {
    width: 75%;
  }
  h1 {
    margin: 0 auto;
  }
</style>
