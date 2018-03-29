<template>
  <el-container>
    <el-main>
      <h1>个人中心</h1>
      <hr/>
      <el-form ref="usernameForm" label-width="95px" :model="usernameForm" :rules="usernameRules">
        <el-form-item label="修改昵称" required>
          <el-col :span="18">
            <el-form-item id="usernameItem" prop="username">
              <el-input v-model="usernameForm.username" placeholder="请输入新昵称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-button type="primary" id="usernameBtn" @click="modifyUsername('usernameForm')">修改昵称</el-button>
          </el-col>
        </el-form-item>
      </el-form>
      <el-form ref="passwordForm" label-width="95px" :model="passwordForm" :rules="passwordRules">
        <el-form-item label="旧密码" prop="current">
          <el-input v-model="passwordForm.current" placeholder="请输入旧密码" type="password"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="new">
          <el-input v-model="passwordForm.new" placeholder="请输入新密码" type="password"></el-input>
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirm">
          <el-input v-model="passwordForm.confirm" placeholder="请确认新密码" type="password" id="code"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="modifyPassword('passwordForm')">修改密码</el-button>
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
</template>

<script>
export default {
  name: 'UserCenter',
  data () {
    var validateConfirm = (rule, value, callback) => {
      if (value !== this.passwordForm.new) {
        callback(new Error('两次新密码不一致'))
      } else {
        callback()
      }
    }
    return {
      identifyCodes: '1234567890',
      identifyCode: '',
      usernameForm: {
        username: ''
      },
      usernameRules: {
        username: [
          { required: true, message: '请填写新昵称', trigger: 'blur' },
          { max: 6, message: '昵称最多6个字符', trigger: 'blur' }
        ]
      },
      passwordForm: {
        current: '',
        new: '',
        confirm: ''
      },
      passwordRules: {
        current: [
          { required: true, message: '请输入现有密码', trigger: 'blur' }
        ],
        new: [
          { required: true, message: '请输入新密码', trigger: 'blur' }
        ],
        confirm: [
          { required: true, message: '请确认新密码', trigger: 'blur' },
          { validator: validateConfirm, trigger: 'blur' }
        ]
      }
    }
  },
  mounted () {
    this.identifyCode = ''
    this.makeCode(this.identifyCodes, 4)
  },
  methods: {
    modifyPassword (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert('login!!')
          // $.ajax({
          //   type: "POST",
          //
          //   data
          // })
        } else {
          console.log('form not valid!!')
          return false
        }
      })
    },
    modifyUsername (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert('修改成功')
          // $.ajax({
          //   type: "POST",
          //
          //   data
          // })
        } else {
          console.log('form not valid!!')
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

  .el-form {
    margin-bottom: 4%;
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

  #usernameBtn {
    width: 100%;
    margin-left: 19%;
  }

  #codeItem {
    margin: 0;
    width: 100%;;
  }

  #usernameItem {
    margin: 0;
    width: 100%;;
  }
</style>
