let goEasy = new GoEasy({
  appkey: 'BC-2c1b84e7528c4d37a2aec64c26343efe'
})

$(function () {
  listen()
})

function encodeScript (data) {
  if (data == null || data === '') {
    return ''
  }
  return data.replace('<', '&lt;').replace('>', '&gt;')
}

function emit () {
  let text = encodeScript($('#msg').val())
  let userID = 'StudentX'
  let style = "{color: '#15b8ce',fontSize: '12px'}"
  let message = "{'userId': '" + userID + "', 'message': '" + text + "', 'style':'" + style + "'}"
  goEasy.publish({
    channel: 'stu',
    message: message
  })
  $('#msg').val('')
}

function listen () {
  goEasy.subscribe({
    channel: 'teach',
    onMessage: function (data) {
      let result = eval('(' + data.content + ')')
      $('#content').append("<kbd style='color: #" + '15b8ce' + '; font-size: ' + 12 + ";'>" + result.message + '</kbd><br/>')
    }
  })
}

document.onkeydown = function (event) {
  let e = event || window.event || arguments.callee.caller.arguments[0]
  if (e && e.keyCode === 13) { // enter 键
    emit()
  }
}
