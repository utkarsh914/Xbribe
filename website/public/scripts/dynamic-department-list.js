$(document).ready(() => {
  var jsonData = $.ajax({
    url: "/getministries",
    dataType: "json",
    xhrFields: { withCredentials: true },
    async: false
  }).responseJSON;

  var data = jsonData.data
  let initialSelection = $('select[name="ministryId"]').val()
  var deps = getDeps(initialSelection)
  let l = deps.length
  for (let i=0; i<l; i++) {
    $('select[name="department"]').append(`<option class="w3-input w3-border" value="${deps[i]}">${deps[i]}</option>`)
  }
  // on changing the selected ministry
  $('select[name="ministryId"]').change(function() {
    $('select[name="department"]').empty()
    deps = getDeps($(this).val())
    console.log(deps)
    let l = deps.length
    for (let i=0; i<l; i++) {
      $('select[name="department"]').append(`<option class="w3-input w3-border" value="${deps[i]}">${deps[i]}</option>`)
    }
  })

  function getDeps (id) {
    for (let i=0; i<data.length; i++) {
      let ministry = data[i]
      if (ministry.ministryId === id) {
        return  ministry.departments
      }
    }
    return null
  }
})