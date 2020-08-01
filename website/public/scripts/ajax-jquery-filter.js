
$(document).ready(function () {
  $('#filterForm').submit(function () {
    $('#filterModal').modal('hide')
    var postData = {}
    var form = $('#filterForm').serializeArray()
    $.each(form, function (i, field) {
      postData[field.name] = field.value
    })

    $.post('http://localhost:4444/admin/dashboard/filter',
      postData,
      function (data, status) {
        console.log('Recieved: ', data)

        // empty the previous posts
        $('#posts').empty()
        // loop over the recieved json data having posts and render them
        data.posts.forEach(post => {
          var temp = ''
          temp += `<a href="/admin/dashboard/${post.caseId}\n">`
          if (post.resolved === true) temp += `<div class="alert alert-success mb-2" id="${post.caseId}">\n`
          else temp += `<div class="alert alert-dark mb-2" id="${post.caseId}">\n`
          temp += `<h5>${data.orgs[post.orgId]}</h5>\n`
          temp += `<p class="mb-0"><b>${post.place},</b> ${post.date}</p>\n`
          temp += '</div>\n'
          temp += '</a>\n'
          // append to the posts container
          $('#posts').append(temp)
        })
      })
  })
})
