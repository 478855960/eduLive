<template>
  <el-container>
    <el-header></el-header>
    <el-main>
      <h1>欢迎使用硕学教育平台</h1>
      <hr>
      <el-form ref="form" label-width="80px">
        <el-form-item label="手机号">
          <el-input v-model="tel" placeholder="请输入手机号" type="tel"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="psw" placeholder="请输入密码" type="password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary">登录</el-button>
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
</template>

<script>
export default {
  name: 'Login'
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .el-form-item {
    width: 40%;
    margin: 10px auto;
  }

  .el-button {
    background-color: #37C6C0;
    margin: 0 auto;
  }

  hr {
    width: 55%;
  }

  .el-button {
    margin-right: 80px;
    width: 30%;
  }
</style>
