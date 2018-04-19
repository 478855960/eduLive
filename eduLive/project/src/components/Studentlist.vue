<template>
<div>
  <el-popover  ref="stulist"  placement="top" trigger="click" class="stulist">
  <el-table :data="stuListMsg" height="400">
    <el-table-column  property="phoneNum" label="电话" ref="stu-phone-num" class="stu-phone-num"></el-table-column>
    <el-table-column  property="name" label="姓名" ref="name" class="name"></el-table-column>
    <el-table-column  property="ban-btn" label="操作" ref="ban-btn" class="ban-btn">
      <template slot-scope="scope">
        <el-button type="danger" size="mini"> 禁言</el-button>
      </template>
    </el-table-column>
  </el-table>
</el-popover>
<el-button v-popover:stulist @click="queryStuList">学生列表</el-button>
</div>
</template>

<script>
export default {
  data () {
    return {
      wsObj: null,
      msg: '',
      stuListMsg: '',
      url: '/user/getCurUser.action',
      sessioUser: null
    }
  },
  created () {
    this.init()
    this.$ajax.post(this.rootUrl + this.url).then((response) => {
      this.sessioUser = JSON.parse(response)
    })
  },
  methods: {
    init () {
      // 设置websocket连接
      this.wsObj = new WebSocket('ws://localhost:8080/TeamYiMing/websocket/studentList')
      // 绑定websocket事件
      this.wsObj.onmessage = this.updateMsg
      // 设置样式
      // this.$refs.stulist.width = 600
      // this.$refs.stu-phoneNum.width = 150
      // this.$refs.banBtn.width = 100
    },
    updateMsg (event) {
      this.msg = event.data
      this.stuListMsg = JSON.parse(event.data)
    },
    queryStuList () {
      this.wsObj.send(JSON.stringify(this.sessioUser))
    }
  }
}
</script>

<style>
</style>
