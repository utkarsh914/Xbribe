$(document).ready(() => {
  var admin_data = $.ajax({
    url: "/admin/chartdata",
    dataType: "json",
    async: false
  }).responseJSON

  // console.log(admin_data)

  Object.keys(admin_data).forEach(key => {
    var ctx = document.getElementById(key)
    var myChart = new Chart(ctx, {
      type: 'bar',
      data: {
        datasets: [{
          label: '# Received',
          data: admin_data[key].data_received,
          // borderWidth: 4,
          backgroundColor: 'rgba(255, 99, 132, 1)',
          // borderColor: 'rgba(255, 99, 132, 1)',
          // fill: false,
        },
        {
          label: '# Resolved',
          data: admin_data[key].data_resolved,
          // borderWidth: 4,
          backgroundColor: 'rgba(89, 255, 82, 1)',
          // borderColor: 'rgba(89, 255, 82, 1)',
          // fill: false,
        }
        ]
      },
      options: {
        scales: {
          xAxes: [{
            stacked: true,
            type: 'time',
            time: {
              unit: 'day'
            }
          }],
          yAxes: [{
            stacked: true
        }]
        }
      }
    })
  })

})