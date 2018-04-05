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
      <el-menu-item index="shape-circle" @click="dispatcher('shape-circle')">圆</el-menu-item>
      <el-menu-item index="shape-rectangle" @click="dispatcher('shape-rectangle')">矩形</el-menu-item>
      <el-menu-item index="shape-line" @click="dispatcher('shape-line')">直线</el-menu-item>
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
<el-container>
    <div>
      <canvas id='contextBack' ref='ctxback'/>
      <canvas @click="initTextPre"  @mousedown="dealMouseDown" @mouseup="dealMouseUp" @mousemove="dealMouseMove" id='context' ref='ctx' />
    </div>
</el-container>
</el-container>
</template>

<script>
export default {
  name: 'Whiteboard',
  data () {
    return {
      isCollapse: true,
      functionPointer: 'pen',
      curPosX: 0,
      curPosY: 0,
      oldPosX: 0,
      oldPosY: 0,
      basePosX: 0,
      basePosY: 0,
      textPosX: 0,
      textPosY: 0,
      savedText: '',
      context: null,
      contextBack: null,
      isMouseDown: false,
      hasSavedStatus: false,
      setTexted: false,
      pathColor: 'black'
    }
  },
  mounted () {
    let canvasFront = this.$refs.ctx
    let canvasBack = this.$refs.ctxback
    canvasFront.width = 900
    canvasFront.height = 350
    canvasBack.width = 900
    canvasBack.height = 350

    document.onkeydown = this.textDraw
    this.context = this.$refs.ctx.getContext('2d')
    this.contextBack = this.$refs.ctxback.getContext('2d')
    this.basePosX = this.$refs.ctx.offsetLeft
    this.basePosY = this.$refs.ctx.offsetTop
  },
  methods: {
    dispatcher (value) {
      let part = value.split('-')
      this.saveLastText(this.contextBack, this.savedText, this.textPosX, this.textPosY)
      this.clear(this.context)
      this.context.fillStyle = 'black'
      this.textPosX = 0
      this.textPosY = 0
      this.setTexted = false
      if (part[0] === 'pen') {
        this.pathColor = part[1]
        this.functionPointer = 'pen'
      }
      if (part[0] === 'clear') {
        this.clear(this.contextBack)
        this.functionPointer = 'clear'
      }
      if (part[0] === 'shape') {
        this.functionPointer = part[1]
      }
      if (part[0] === 'eraser') {
        this.functionPointer = 'eraser'
      }
      if (part[0] === 'text') {
        this.functionPointer = 'text'
        this.textPosX = 0
        this.textPosY = 0
      }
    },
    dealMouseMove (event) {
      this.curPosX = event.offsetX
      this.curPosY = event.offsetY
      if (this.isMouseDown) {
        if (this.functionPointer === 'pen') {
          this.handDraw(this.curPosX, this.curPosY, this.oldPosX, this.oldPosY)
        } else if (this.functionPointer === 'clear') {

        } else if (this.functionPointer === 'eraser') {
          this.clear(this.context)
          this.context.fillStyle = 'aquamarine'
          this.context.fillRect(this.curPosX, this.curPosY, 50, 50)
          this.contextBack.clearRect(this.curPosX, this.curPosY, 50, 50)
        } else if (this.functionPointer === 'text') {

        } else {
          if (this.functionPointer === 'rectangle') {
            this.clear(this.context)
            let width = this.curPosX - this.oldPosX
            let height = this.curPosY - this.oldPosY
            this.rectangleDraw(this.context, this.oldPosX, this.oldPosY, width, height)
            return
          } else if (this.functionPointer === 'circle') {
            let r = Math.sqrt(Math.pow(this.curPosX - this.oldPosX, 2) + Math.pow(this.curPosY - this.oldPosY, 2))
            this.clear(this.context)
            this.circleDraw(this.context, this.oldPosX, this.oldPosY, r)
            return
          } else if (this.functionPointer === 'line') {
            this.clear(this.context)
            this.lineDraw(this.context, this.curPosX, this.curPosY)
            return
          }
        }
        this.oldPosX = this.curPosX
        this.oldPosY = this.curPosY
      } else {
        if (this.functionPointer === 'eraser') {
          this.clear(this.context)
          this.context.fillStyle = 'aquamarine'
          this.context.fillRect(this.curPosX, this.curPosY, 50, 50)
        }
      }
    },
    dealMouseUp (event) {
      this.isMouseDown = false
      if (this.functionPointer === 'circle') {
        let r = Math.sqrt(Math.pow(this.curPosX - this.oldPosX, 2) + Math.pow(this.curPosY - this.oldPosY, 2))
        this.clear(this.context)
        this.circleDraw(this.contextBack, this.oldPosX, this.oldPosY, r)
      } if (this.functionPointer === 'line') {
        this.clear(this.context)
        this.lineDraw(this.contextBack, this.curPosX, this.curPosY)
      } if (this.functionPointer === 'rectangle') {
        this.clear(this.context)
        let width = this.curPosX - this.oldPosX
        let height = this.curPosY - this.oldPosY
        this.rectangleDraw(this.contextBack, this.oldPosX, this.oldPosY, width, height)
      }
      this.oldPosX = this.curPosX
      this.oldPosY = this.curPosY
    },
    dealMouseDown (event) {
      this.oldPosX = event.offsetX
      this.oldPosY = event.offsetY
      this.isMouseDown = true
    },
    handDraw (curx, cury, oldx, oldy) {
      this.contextBack.strokeStyle = this.pathColor
      this.contextBack.beginPath()
      this.contextBack.moveTo(oldx, oldy)
      this.contextBack.lineTo(curx, cury)
      this.contextBack.stroke()
      this.contextBack.closePath()
    },
    circleDraw (context, downX, downY, r) {
      context.strokeStyle = 'black'
      context.beginPath()
      context.arc(downX, downY, r, 0, 2 * Math.PI, false)
      context.stroke()
    },
    lineDraw (context, curx, cury) {
      context.beginPath()
      context.moveTo(this.oldPosX, this.oldPosY)
      context.lineTo(curx, cury)
      context.stroke()
      context.closePath()
    },
    rectangleDraw (context, downX, downY, width, height) {
      context.beginPath()
      context.rect(downX, downY, width, height)
      context.stroke()
    },
    textDraw (event) {
      if (this.functionPointer === 'text' && this.setTexted) {
        this.clear(this.context)
        this.savedText = this.savedText + String.fromCharCode(event.keyCode)
        this.context.font = '20px Georgia'
        this.context.fillText(this.savedText, this.textPosX, this.textPosY)
      }
    },
    initTextPre (event) {
      this.setTexted = true
      this.contextBack.font = '20px Georgia'
      this.saveLastText(this.contextBack, this.savedText, this.textPosX, this.textPosY)
      this.textPosX = event.offsetX
      this.textPosY = event.offsetY
      this.savedText = ''
    },
    saveLastText (context, value, x, y) {
      context.fillText(value, x, y)
    },
    clear (contex) {
      contex.clearRect(0, 0, this.$refs.ctx.width, this.$refs.ctx.height)
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
    position: absolute;
  }
</style>
