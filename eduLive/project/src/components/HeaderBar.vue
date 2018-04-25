<template>
  <el-header>
    <span class="title">硕学教育平台</span>
    <el-input id="selectLiveRoomInfo" class="search" placeholder="搜索感兴趣的课程"></el-input>
    <el-button class="search-buttom" type="primary" icon="el-icon-search" @click="queryLiveRoom">搜索</el-button>
  </el-header>
</template>

<script>
export default {
  name: 'sidebar',
  methods: {
    handleOpen (key, keyPath) {
    },
    handleClose (key, keyPath) {
    },
    queryLiveRoom: function () {
      this.queryInfo.otherInfo = document.getElementById('selectLiveRoomInfo').value
      alert(this.queryInfo)
      this.$ajax
        .post(this.rootUrl + '/liveroom/queryLiveRoomInfo.action', this.queryInfo)
        .then(response => {
          alert(JSON.parse(response.data))
        })
    }
  }
}
</script>

<style scoped>
  .el-header {
    background-color: #0775c1;
    color: #333;
    height: 100%;
    border: #afb1aa 1px solid;
    border-bottom: 0;
    width: 100%;
    text-align: right;
    padding-right: 1%;
  }
  .search {
    width: 200px;
    margin-top: 0.5%;
  }
  .title {
    font-size: 22px;
    color: #FFFFFF;
    text-align: center;
    font-weight: bold;
    float: left;
    margin-top: 1%;
    padding-left: 1%;
    width: 10%;
  }
</style>
