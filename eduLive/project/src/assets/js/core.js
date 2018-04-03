function encodeScript (data) {
  if (data == null || data == '') {
    return ''
  }
  return data.replace('<', '&lt;').replace('>', '&gt;')
}
