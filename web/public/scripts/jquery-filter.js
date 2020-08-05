$(document).ready(function () {
  var currentUrl = $(location).attr('href')

  if (currentUrl.indexOf('?') === -1) {
    currentUrl += '?pageNo='
  } else {
    index = currentUrl.indexOf('pageNo=')
    if (index === -1) currentUrl += '&pageNo='
    else currentUrl = currentUrl.slice(0, index + 7)
  }

  var items = $('li a.page-link')
  $.each(items, function (i, element) {
    $(element).attr('href', function (i, value) {
      return currentUrl + value
    })
  })

  // let qIndex = currentUrl.indexOf('query=')
  // if (qIndex) {
  //   document.querySelector('#filterModal input[name="query"]').value = currentUrl.slice(qIndex+6,)
  // }
})
