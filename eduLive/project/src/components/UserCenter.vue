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
            <h1>修改昵称</h1>
          </el-col>
        </el-row>
        <el-form ref="usernameForm" :model="usernameForm" :rules="usernameRules" id="username-form">
          <el-form-item label="" required>
            <el-col :span="18">
              <el-form-item id="username-item" prop="nickname">
                <el-input v-model="usernameForm.nickname" placeholder="输入新昵称" prefix-icon="el-icon-edit-outline"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-button type="primary" id="username-button" @click="modifyUsername('usernameForm')">修改昵称</el-button>
            </el-col>
          </el-form-item>
        </el-form>
        <el-row>
          <el-col span="16">
            <h1>修改密码</h1>
          </el-col>
        </el-row>
        <el-form ref="passwordForm" :model="passwordForm" :rules="passwordRules">
          <el-form-item label="" prop="password">
            <el-input v-model="passwordForm.password" placeholder="输入旧密码" type="password" prefix-icon="el-icon-goods"></el-input>
          </el-form-item>
        </el-form>
        <el-form ref="newPasswordForm" :model="newPasswordForm" :rules="newPasswordRules">
          <el-form-item label="" prop="password">
            <el-input v-model="newPasswordForm.password" placeholder="输入新密码" type="password" prefix-icon="el-icon-goods"></el-input>
          </el-form-item>
          <el-form-item label="" prop="confirm">
            <el-input v-model="newPasswordForm.confirm" placeholder="确认新密码" type="password" prefix-icon="el-icon-goods"></el-input>
          </el-form-item>
          <el-form-item label="" required>
            <el-col :span="17">
              <el-form-item id="code-item" prop="code">
                <el-input v-model="usernameForm.code" placeholder="输入验证码" prefix-icon="el-icon-message"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-button type="primary" id="code-button" @click="getCode('newPasswordForm', $event)">获取验证码</el-button>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="modifyPassword('passwordForm','newPasswordForm')">修改密码</el-button>
          </el-form-item>
        </el-form>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Sidebar from '@/components/Sidebar'
import HeaderBar from '@/components/HeaderBar'

export default {
  name: 'UserCenter',
  components: {
    Sidebar,
    HeaderBar
  },
  data () {
    let validateConfirm = (rule, value, callback) => {
      if (value !== this.newPasswordForm.password) {
        callback(new Error('两次新密码不一致'))
      } else {
        callback()
      }
    }
    let validateNickname = (rule, value, callback) => {
      this.$ajax.post(this.rootUrl + '/user/checkNickname.action', this.usernameForm)
        .then((response) => {
          if (response.data === 1) {
            callback(new Error('该昵称已被占用'))
          } else {
            callback()
          }
        })
    }
    let validatePassword = (rule, value, callback) => {
      this.$ajax.post(this.rootUrl + '/user/checkPassword.action', this.passwordForm)
        .then((response) => {
          if (response.data === 0) {
            callback(new Error('旧密码错误'))
          } else {
            callback()
          }
        })
    }
    let validateCode = (rule, value, callback) => {
      this.$ajax.post(this.rootUrl + '/user/checkPassword.action', this.passwordForm)
        .then((response) => {
          if (response.data === 0) {
            callback(new Error('旧密码错误'))
          } else {
            callback()
          }
        })
    }
    return {
      nicknameUrl: '/user/userModifyNickname.action',
      passwordUrl: '/user/userModifyPassword.action',
      usernameForm: {
        nickname: ''
      },
      usernameRules: {
        nickname: [
          { required: true, message: '请填写新昵称', trigger: 'blur' },
          { max: 6, message: '昵称最多6个字符', trigger: 'blur' },
          { validator: validateNickname, trigger: 'blur' }
        ]
      },
      passwordForm: {
        password: ''
      },
      newPasswordForm: {
        password: '',
        confirm: '',
        code: ''
      },
      passwordRules: {
        password: [
          { required: true, message: '请输入现有密码', trigger: 'blur' },
          { validator: validatePassword, trigger: 'blur' }
        ]
      },
      newPasswordRules: {
        password: [
          { required: true, message: '请输入现有密码', trigger: 'blur' },
          { min: 6, max: 16, message: '密码长度应在6-16位', trigger: 'blur' }
        ],
        confirm: [
          { required: true, message: '请确认新密码', trigger: 'blur' },
          { validator: validateConfirm, trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入短信验证码', trigger: 'blur' },
          { validator: validateCode, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    modifyPassword (formName1, formname2) {
      this.$refs[formName1].validate((valid) => {
        if (valid) {
        } else {
          return false
        }
      })
      this.$refs[formname2].validate((valid) => {
        if (valid) {
          let _this = this
          this.$ajax.post(this.rootUrl + _this.passwordUrl, _this.newPasswordForm)
            .then((response) => {
              if (response.data === 'relogin') {
                this.$message.error('请重新登录')
                this.$router.push({path: '/Login'})
              } else if (response.data === 'modifyFail') {
                this.$message.error('修改失败')
              } else if (response.data === 'success') {
                this.$message.success('修改成功')
              } else {
                this.$message.error('系统出错')
              }
            })
        } else {
          return false
        }
      })
    },
    getCode (value, event) {
      let _this = this
      this.$ajax.post(this.rootUrl + '/user/modifySendMessage.action', _this.newPasswordForm)
        .then((response) => {
          if (response.data === 0) {
            this.$message.success('短信发送成功')
          } else if (response.data === 1) {
            this.$message.error('请重新登录')
            this.$router.push({path: '/Login'})
          } else {
            this.$message.error('系统错误')
          }
        })
    },
    modifyUsername (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let _this = this
          this.$ajax.post(this.rootUrl + _this.nicknameUrl, _this.usernameForm)
            .then((response) => {
              if (response.data === 'relogin') {
                this.$message.error('请重新登录')
                this.$router.push({path: '/Login'})
              } else if (response.data === 'modifyFail') {
                this.$message.error('修改失败')
              } else if (response.data === 'nicknameExist') {
                this.$message.error('昵称已存在')
              } else if (response.data === 'success') {
                this.$message.success('修改成功')
              } else {
                this.$message.error('系统出错')
              }
            })
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
  .el-header {
    padding: 0;
  }

  .el-form-item {
    width: 40%;
    margin: 30px auto;
  }

  .el-button {
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

  #username-button {
    width: 100%;
    margin-left: 19%;
  }

  #code-button {
    width: 100%;
    margin-left: 19%;
  }

  #username-item {
    margin: 0;
    width: 100%;
  }

  #code-item {
     margin: 0;
     width: 100%;
   }
  .el-main {
    margin-left: -7%;
    text-align: center;
  }
</style>
