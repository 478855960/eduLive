<template>
<div id='showstu'>
  <el-popover  ref="stulist"  placement="top" trigger="click" class="stulist">
  <el-table :data="stuListMsg" height="400">
    <el-table-column  property="phoneNum" label="电话" ref="stu-phone-num" class="stu-phone-num" width='100'></el-table-column>
    <el-table-column  property="name" label="姓名" ref="name" class="name" width="80"></el-table-column>
    <el-table-column  property="ban-btn" label="操作" ref="ban-btn" class="ban-btn" width='180'>
      <template slot-scope="scope">
        <el-row :gutter="10">
          <el-col :span="12">
            <el-button type="danger" size="mini" @click="handleBan(scope.row)" v-if="scope.row.banned === false"> 禁言</el-button>
            <el-button type="danger" size="mini" @click="handleBan(scope.row)" v-else> 解除禁言</el-button>
          </el-col>
          <el-col :span="12"><el-button type="danger" size="mini" @click="handleBlacklist(scope.row)"> 拉黑</el-button></el-col>
        </el-row>
      </template>
    </el-table-column>
  </el-table>
</el-popover>
<el-button v-popover:stulist @click="queryStuList" type='success'>学生列表</el-button>
</div>
</template>

<script>
export default {
  data () {
    return {
      wsObj: null,
      msg: '',
      stuListMsg: [],
      sessioUser: null,
      url: '/user/getCurUser.action',
      teacherOp: {
        type: '',
        liveroomNum: '',
        phoneNum: '',
        otherInfo: ''
      }
    }
  },
  created () {
    this.init()
    this.$ajax.post(this.rootUrl + this.url).then((response) => {
      this.sessioUser = JSON.parse(response.data)
      this.wsObj = new WebSocket('ws://localhost:8080/TeamYiMing/websocket/studentList/' +
      this.sessioUser.isStudent + '/' +
      this.sessioUser.phoneNum + '/' +
      this.sessioUser.name + '/' +
      '12112345678')
      this.wsObj.onmessage = this.updateMsg
      window.onbeforeunload = function () {
        let whiteboardWs = this.$store.getters.getWhiteBoardWebsocket
        whiteboardWs.close()
        this.wsObj.close()
      }
    })
  },
  mounted () {
  },
  methods: {
    init () {

    },
    updateMsg (event) {
      this.msg = event.data
      this.stuListMsg = JSON.parse(event.data)
    },
    queryStuList () {
      this.teacherOp.type = 'query'
      this.teacherOp.phoneNum = this.sessioUser.phoneNum
      this.teacherOp.liveroomNum = this.sessioUser.phoneNum
      this.wsObj.send(JSON.stringify(this.teacherOp))
    },
    handleBan (row) {
      if (row.banned === false) {
        this.teacherOp.type = 'banStu'
        this.teacherOp.phoneNum = row.phoneNum
        this.teacherOp.liveroomNum = this.sessioUser.phoneNum
        this.wsObj.send(JSON.stringify(this.teacherOp))
      } else {
        this.teacherOp.type = 'cancelBanStu'
        this.teacherOp.phoneNum = row.phoneNum
        this.teacherOp.liveroomNum = this.sessioUser.phoneNum
        this.wsObj.send(JSON.stringify(this.teacherOp))
      }
    },
    handleBlacklist (row) {
      this.teacherOp.type = 'addBlacklist'
      this.teacherOp.phoneNum = row.phoneNum
      this.teacherOp.liveroomNum = this.sessioUser.phoneNum
      this.wsObj.send(JSON.stringify(this.teacherOp))
    }
  }
}
</script>

<style>
  #showstu{
    display: inline-block;
  }
  .el-button{
    margin: 0 20px;
  }
</style>
