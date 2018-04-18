<template>
  <div id="codeEditor">
    <div class="cm-container">
      <codemirror :value="code" @change="change" :options="options"></codemirror>
    </div>
    <select name="mode" v-model="mode">
      <option value="javascript">javascript</option>
      <option value="vue">vue</option>
      <option value="c">c</option>
      <option value="java">java</option>
      <option value="python">python</option>
      <option value="html">html</option>
      <option value="css">css</option>
    </select>
  </div>
</template>

<script>
import { codemirror } from 'vue-codemirror-lite'
require('codemirror/mode/javascript/javascript')
require('codemirror/mode/vue/vue')

require('codemirror/addon/hint/show-hint.js')
require('codemirror/addon/hint/html-hint.js')
require('codemirror/addon/hint/css-hint.js')
require('codemirror/addon/hint/show-hint.css')
require('codemirror/addon/hint/javascript-hint.js')

var codes = {
  javascript: 'var component = {\n\tname: "vue-codemirror-lite",\n\tauthor: "Fangxw",\n\trepo: "https://github.com/cnu4/vue-codemirror-lite"\n}\n\n//Press Ctrl-Space to trigger hint',
  vue: '<template>\n<codemirror :value="code"></codemirror>\n</template>\n\n',
  c: '',
  java: '',
  python: '',
  html: '',
  css: ''
}

export default {
  name: 'CodeEditor',
  components: {
    codemirror
  },
  data () {
    return {
      mode: 'javascript'
    }
  },
  computed: {
    code: function () {
      return codes[this.mode]
    },
    options: function () {
      return {
        mode: this.mode,
        tabSize: 2,
        lineNumbers: true,
        lineWrapping: true,
        extraKeys: {'Ctrl-Space': 'autocomplete'}
      }
    }
  },
  methods: {
    change: function (code) {
    }
  }
}
</script>

<style scoped>
  #codeEditor{
    max-width: 720px;
  }
  .cm-container{
    border: #ddd solid 1px;
    margin-bottom: 10px
  }
</style>
