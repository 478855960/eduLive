<template>
  <div id="codeEditor">
    <MonacoEditor height="300" language="javascript" :code="code" :editorOptions="options" @mounted="onMounted" @codeChange="onCodeChange">
    </MonacoEditor>
  </div>
</template>

<script>
import MonacoEditor from 'vue-monaco-editor'

export default {
  name: 'CodeEditor',
  components: {
    MonacoEditor
  },
  data () {
    return {
      code: '// Type away! \n',
      options: {
        selectOnLineNumbers: false
      }
    }
  },
  methods: {
    onMounted (editor) {
      this.editor = editor
    },
    onCodeChange (editor) {
    }
  }
}
</script>

<style scoped>

</style>
