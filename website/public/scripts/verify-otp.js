$(document).ready(() => {

  $("#otp-btn").click(() => {
    $("#otp-btn").text('Sending...')
    $.post("/report/sendotp", { email: $('input[name="email"]').val() }, (data, status) => {
      console.log(data, status)
      if (status === 'success') {
        $('#alert-success').show(80).text('OTP sent! If not received, refresh page and try again.')
        $('#send-otp-field').hide(80)
        $('#enter-otp-field').show(80)
        $('#submit-btn-field').show(80)
      }
      else {
        $('#alert-danger').show(80).text('OTP could not be sent! Try again.')
        $("#otp-btn").text('Send OTP')
      }
    })
  })

})