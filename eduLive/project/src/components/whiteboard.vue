<template>
<el-container>
    <el-menu  class="el-menu-vertical-demo" :collapse="isCollapse">
  <el-submenu index="1">
    <template slot="title">
      <i class="el-icon-edit"></i>
      <span slot="title">工具</span>
    </template>
    <el-menu-item-group>
      <span slot="title">画笔</span>
      <el-menu-item index="pen-black" @click="dispatcher('pen-black')">黑</el-menu-item>
      <el-menu-item index="pen-red" @click="dispatcher('pen-red')">红</el-menu-item>
      <el-menu-item index="pen-bule" @click="dispatcher('pen-yellow')">黄</el-menu-item>
      <el-menu-item index="pen-green" @click="dispatcher('pen-green')">绿</el-menu-item>
    </el-menu-item-group>
  </el-submenu>
  
  <el-submenu index="2">
    <template slot="title">
      <i class="el-icon-menu"></i>
      <span slot="title">工具</span>
    </template>
    <el-menu-item-group>
      <span slot="title">图形</span>
      <el-menu-item index="shape-cicle" @click="dispatcher('shape-cicle')">圆</el-menu-item>
      <el-menu-item index="shape-oval" @click="dispatcher('shape-oval')">椭圆</el-menu-item>
      <el-menu-item index="shape-rectangle" @click="dispatcher('shape-rectangle')">矩形</el-menu-item>
    </el-menu-item-group>
  </el-submenu>
  <el-menu-item index="eraser" @click="dispatcher('eraser')">
    <i class="el-icon-close"></i>
    <span slot="title">橡皮</span>
  </el-menu-item>
  <el-menu-item index="text" @click="dispatcher('text')">
    <i class="el-icon-document"></i>
    <span slot="title">文字</span>
  </el-menu-item>
  <el-menu-item index="clear" @click="dispatcher('clear')">
    <i class="el-icon-delete"></i>
    <span slot="title">清空</span>
  </el-menu-item>
</el-menu>
    <canvas @mousedown="dealMouseDown" @mouseup="dealMouseUp" @mousemove="dealMouseMove" id='context' ref='ctx'> </canvas>  
</el-container>

</template>

<script>
export default {
  name: 'Whiteboard',
   data() {
      return {
        isCollapse: true,
        functionPointer: '',
        curPosX: 0,
        curPosY: 0,
        oldPosX: 0,
        oldPosY: 0,
        basePosX: 0,
        basePosY: 0,
        context: null,
        isMouseDown: false,
        pathColor: 'black'
      }
    },
    mounted(){
      
        this.context = this.$refs.ctx.getContext('2d')
        this.basePosX = this.$refs.ctx.offsetLeft
        this.basePosY = this.$refs.ctx.offsetTop
    },
    methods: {
      dispatcher(value){
        let part = value.split('-')
        if(part[0] === 'pen'){
          this.pathColor = part[1]
        }
        if(part[0] === 'clear'){
          this.clear()
        }

      },
      dealMouseMove(event){
        if(this.isMouseDown){
          this.curPosX = event.offsetX * 0.34
          this.curPosY = event.offsetY * 0.34
          // console.log('cur ' + this.curPosX + ' '+ this.curPosY)
          // console.log('old ' + this.oldPosX + ' '+ this.oldPosY)
          this.handDraw(this.curPosX, this.curPosY, this.oldPosX, this.oldPosY)
          this.oldPosX = this.curPosX
          this.oldPosY = this.curPosY
        }  
      },
      dealMouseUp(event){
        this.isMouseDown = false
      },
      dealMouseDown(event){
        this.oldPosX = event.offsetX * 0.34
        this.oldPosY = event.offsetY * 0.34
        this.isMouseDown = true
      },
      handDraw(curx, cury, oldx, oldy){
        this.context.strokeStyle = this.pathColor;
	      this.context.beginPath();
	      this.context.moveTo(oldx,oldy);
  	    this.context.lineTo(curx,cury);
  	    this.context.stroke();
	      this.context.closePath();
      },
      clear(){
        this.context.clearRect(0,0,this.$refs.ctx.width,this.$refs.ctx.height) 
      }      
    }

}
</script>

<style>
  .el-menu{
    width: 8%;
  }
  .el-container {
    width: 100%;
    height: 100%;
  }
  canvas{
    width: 92%;
    height: 100%;
  }
</style>
