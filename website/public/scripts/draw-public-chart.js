$(document).ready(() => {
  var chartData = $.ajax({
    url: "/chartdata",
    dataType: "json",
    async: false
  }).responseJSON

  // console.log(chartData)

  var ctx = document.getElementById('myChart');
  var myChart = new Chart(ctx, {
    type: 'line',
    data: {
      datasets: [{
        label: '# Received',
        data: chartData.data_received,
        borderWidth: 4,
        borderColor: 'rgba(255, 99, 132, 1)',
        // fill: false,
      },
      {
        label: '# Resolved',
        data: chartData.data_resolved,
        borderWidth: 4,
        borderColor: 'rgba(89, 255, 82, 1)',
        // fill: false,
      }
      ]
    },
    options: {
      scales: {
        xAxes: [{
          type: 'time',
          time: {
            unit: 'day'
          }
        }]
      }
    }
  })

})