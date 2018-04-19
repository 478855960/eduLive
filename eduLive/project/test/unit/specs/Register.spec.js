import Vue from 'vue'
import Register from '@/components/Register'
import ElementUI from 'element-ui'
Vue.use(ElementUI)
describe('Register.vue', () => {
  const Constructor = Vue.extend(Register) // 获得Register组件实例
  const vm = new Constructor().$mount() // 将组件挂载到DOM上
  it('should return 1', () => {
    const sendCodeButton = vm.$el.querySelector('#register-button')
    const clickEvent = new window.Event('click')
    sendCodeButton.dispatchEvent(clickEvent)
    vm._wathcher.run()
    expect(vm.resultMessage.registerResult).to.contain('registerSuccess')
  })
})
