function create_evisa_graph(evisa_json_data, id_name, graph_label) {

    var bgColors = ['rgba(255, 99, 132, 0.9)',
        'rgba(54, 162, 235, 0.9)',
        'rgba(255, 206, 86, 0.9)',
        'rgba(75, 192, 192, 0.9)',
        'rgba(153, 102, 255, 0.9)',
        'rgba(255, 159, 64, 0.9)',
        'rgba(249, 117, 174, 0.9)',
        'rgba(220, 247, 133, 0.9)',
        'rgba(191, 94, 59, 0.9)',
        'rgba(31, 219, 131, 0.9)',
        'rgba(179, 90, 226, 0.9)',
        'rgba(241, 181, 120, 0.9)'];

    var borderColors = ['rgba(255, 99, 132,1)',
        'rgba(54, 162, 235, 1)',
        'rgba(255, 206, 86, 1)',
        'rgba(75, 192, 192, 1)',
        'rgba(153, 102, 255, 1)',
        'rgba(255, 159, 64, 1)',
        'rgba(249, 117, 174, 1)',
        'rgba(220, 247, 133, 1)',
        'rgba(191, 94, 59, 1)',
        'rgba(31, 219, 131, 1)',
        'rgba(179, 90, 226, 1)',
        'rgba(241, 181, 120, 1)'];

    var ctx = document.getElementById(id_name);
    Chart.Legend.prototype.afterFit = function () {
        this.height = this.height + 20;
    };
    var evisa_chart = new Chart(ctx, {
        type: 'bar',
        responsive: true,
        data: {
            labels: Object.keys(evisa_json_data),
            datasets: [{
                    label: graph_label,
                    data: Object.values(evisa_json_data),
                    backgroundColor: bgColors,
                    borderColor: borderColors,
                    borderWidth: 1
                }]
        },
        options: {
            legend: {
                display: false
            },
            tooltips: {
                callbacks: {
                    label: function (tooltipItem) {
                        return tooltipItem.yLabel;
                    }
                }
            },
            scales: {
                yAxes: [{
                        ticks: {
                            beginAtZero: false
                        }
                    }]
            },
            tooltips: {
                enabled: true
            },
            hover: {
                animationDuration: 1
            },
            animation: {
                duration: 1
            },
            plugins: {
                labels: {
                    render: 'value',
                    overlap: true,
                    fontColor: '#000000',
                    fontSize: 12,
                    arc: false
                }
            }
        }
    });
}// end of create_evisa_graph

function create_total_arrival_departure_barchart(data_list) {

     var backgroundColor_array = ['rgba(214, 237, 45, 0.9)', "rgba(255, 81, 156, 0.9)", "rgba(139, 195, 74, 0.9)",
        "rgba(153, 153, 255, 0.9)", "rgba(244, 67, 54, 0.9)", "rgba(252, 161, 3, 0.9)"];
    var borderColor_array = ['rgba(214, 237, 45, 1 )', "rgba(255, 81, 156, 1)", "rgba(139, 195, 74, 1)",
        "rgba(153, 153, 255, 1)", "rgba(244, 67, 54, 1)", "rgba(252, 161, 3, 1)"];

    var my_dataset_array = [];
    var cou = [];
    var lab = [];
    var outer_lab = [];
    var i = 0;
    $.each(data_list, function (key, value) {
        var my_dataset_array_obj = {};
        outer_lab = [];
        $.each(value, function (key1, value1) {
            cou.push(value1);
            outer_lab.push(key1);
        });
        my_dataset_array_obj['data'] = cou;
        my_dataset_array_obj['label'] = key;
        my_dataset_array_obj['backgroundColor'] = backgroundColor_array[i];
        my_dataset_array_obj['borderColor'] = borderColor_array[i];
        my_dataset_array_obj['borderWidth'] = 1;
        cou = [];
        my_dataset_array.push(my_dataset_array_obj);
        i++;
    });
 
    var barChartData = {
        labels: outer_lab,
        datasets: my_dataset_array

    };

    var chartOptions = {
        responsive: true,
        legend: {
            position: "top"
        },
        title: {
            display: false,
            text: "Chart.js Bar Chart"
        },
        scales: {
            yAxes: [{
                    ticks: {
                        beginAtZero: false
                    }
                }]
        },
        animation: {
            duration: 1
        },
        plugins: {
            labels: {
                render: 'value',
                overlap: true,
                fontColor: '#000000',
                fontSize: 12,
                arc: false
            }
        }
    }


    var ctx = document.getElementById("total_arrival_departure_bar_chart").getContext("2d");
    Chart.Legend.prototype.afterFit = function () {
        this.height = this.height + 20;
    };
    var myBarChart = new Chart(ctx, {
        type: 'bar',
        data: barChartData,
        options: chartOptions
    });

}

function create_voa_barchart(data_list, chart_id) {

    var backgroundColor_array = ['rgba(54, 162, 235, 0.9)', "rgba(255, 81, 156, 0.9)", "rgba(139, 195, 74, 0.9)",
        "rgba(153, 153, 255, 0.9)", "rgba(244, 67, 54, 0.9)", "rgba(252, 161, 3, 0.9)"];
    var borderColor_array = ['rgba(54, 162, 235, 1)', "rgba(255, 81, 156, 1)", "rgba(139, 195, 74, 1)",
        "rgba(153, 153, 255, 1)", "rgba(244, 67, 54, 1)", "rgba(252, 161, 3, 1)"];

    var my_dataset_array = [];
    var cou = [];
    var lab = [];
    var outer_lab = [];
    var i = 0;
    $.each(data_list, function (key, value) {
        var my_dataset_array_obj = {};
        outer_lab = [];
        $.each(value, function (key1, value1) {
            cou.push(value1);
            outer_lab.push(key1);
        });
        my_dataset_array_obj['data'] = cou;
        my_dataset_array_obj['label'] = key;
        my_dataset_array_obj['backgroundColor'] = backgroundColor_array[i];
        my_dataset_array_obj['borderColor'] = borderColor_array[i];
        my_dataset_array_obj['borderWidth'] = 1;
        cou = [];
        my_dataset_array.push(my_dataset_array_obj);
        i++;
    });

    var barChartData = {
        labels: outer_lab,
        datasets: my_dataset_array
    };

    var chartOptions = {
        responsive: true,
        legend: {
            position: "top"
        },
        title: {
            display: false,
            text: "Chart.js Bar Chart"
        },
        scales: {
            yAxes: [{
                    ticks: {
                        beginAtZero: false
                    }
                }]
        },
        animation: {
            duration: 1
        },
        plugins: {
            labels: {
                render: 'value',
                overlap: true,
                fontColor: '#000000',
                fontSize: 12,
                arc: false
            }
        }
    }


    var ctx = document.getElementById(chart_id).getContext("2d");
    Chart.Legend.prototype.afterFit = function () {
        this.height = this.height + 20;
    };
    var myBarChart = new Chart(ctx, {
        type: 'bar',
        data: barChartData,
        options: chartOptions
    });

}

// no need only for remember how to make pie chart
function top_ten_evisa_pie_chart() {
//alert("Ssss");
    var config = {
        type: 'pie',
        data: {
            datasets: [{
                    data: [
                        1259280, 997120, 475387, 413757, 355595, 328937, 319698, 301923, 166834, 161689
                    ],
                    backgroundColor: ['rgba(255, 99, 132,1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(165, 149, 149,1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)',
                        'rgba(81, 246, 255,1)',
                        'rgba(255, 159, 64, 1)',
                        'rgba(249, 117, 174, 1)',
                        'rgba(220, 247, 133, 1)'],
                    label: 'Dataset 1'
                }],
            labels: [
                'UNITED KINGDOM',
                'UNITED STATES OF AMERICA',
                'CHINA',
                'FRANCE',
                'GERMANY',
                'AUSTRALIA',
                'RUSSIAN FEDERATION',
                'CANADA',
                'MALAYSIA',
                'REPUBLIC OF KOREA'
            ]
        },
        options: {
            responsive: true,
            plugins: {
                labels: {
                    render: 'value',
                    overlap: true,
                    fontColor: '#000000',
                    arc: false,
                    position: 'outside',
                    textMargin: 10,
                    outsidePadding: 10,
                }
            }
        }
    };


    // / var ctx =
	// document.getElementById('top_ten_evisa_pie').getContext('2d');
    var ctx = document.getElementById('examplecanvas').getContext('2d');
    var mypiechart = new Chart(ctx, config);


}

// no need only for remember how to make pie chart
function top_five_eservice_chart() {

    var config = {
        type: 'pie',
        data: {
            datasets: [{
                    data: [
                        597039, 588298, 238493, 163259, 112168
                    ],
                    backgroundColor: ['rgba(255, 99, 132,1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(165, 149, 149,1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)',
                        'rgba(81, 246, 255,1)',
                        'rgba(255, 159, 64, 1)',
                        'rgba(249, 117, 174, 1)',
                        'rgba(220, 247, 133, 1)'],
                    label: 'Dataset 1'
                }],
            labels: [
                'Visa Extension',
                'Registration Certificate',
                'Exit Permit',
                'Temporary Registration Permit',
                'Registration Certificate Extension'
            ]
        },
        options: {
            responsive: true,
            plugins: {
                labels: {
                    render: 'value',
                    overlap: true,
                    fontColor: '#000000',
                    arc: false,
                    position: 'outside'
                }
            }
        }
    };


    var ctx = document.getElementById('top_five_e_service').getContext('2d');
    var mypiechart = new Chart(ctx, config);


}// end of top_five_eservice_chart

// no need only for remember how to make pie chart
function total_oci_issued() {

    var config = {
        type: 'pie',
        data: {
            datasets: [{
                    data: [
                        3177517, 162395
                    ],
                    backgroundColor: ['rgba(255, 99, 132,1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(165, 149, 149,1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)',
                        'rgba(81, 246, 255,1)',
                        'rgba(255, 159, 64, 1)',
                        'rgba(249, 117, 174, 1)',
                        'rgba(220, 247, 133, 1)'],
                    label: 'Dataset 1'
                }],
            labels: [
                'Issued by Indian Missions -MEA',
                'Issued by FRROs - BoI , MHA',
            ]
        },
        options: {
            responsive: true,
            plugins: {
                labels: {
                    render: 'value',
                    overlap: true,
                    fontColor: '#000000',
                    fontSize: 18,
                    arc: false
                }
            }
        }
    };

    var ctx = document.getElementById('total_oci_issued').getContext('2d');
    var mypiechart = new Chart(ctx, config);


}// end of total_oci_issued

// no need only for remember how to make pie chart
function top_five_missions_oci() {

    var config = {
        type: 'pie',
        data: {
            datasets: [{
                    data: [531520, 361356, 256999, 224517, 186017],
                    backgroundColor: ['rgba(255, 99, 132,1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(165, 149, 149,1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)',
                        'rgba(81, 246, 255,1)',
                        'rgba(255, 159, 64, 1)',
                        'rgba(249, 117, 174, 1)',
                        'rgba(220, 247, 133, 1)'],
                    label: 'Dataset 1'
                }],
            labels: [
                'London', 'New York', 'San Francisco', 'Birmingham', 'Houston'
            ]
        },
        options: {
            responsive: true,
            plugins: {
                labels: [{
                        render: 'value',
                        overlap: true,
                        fontColor: '#000000',
                        fontSize: 12,
                        arc: false
                    },
                    {
                        render: 'label',
                        overlap: true,
                        position: 'outside',
                        fontColor: '#000000',
                        fontSize: 12,
                        arc: false
                    }]
            }
        }
    };

    var ctx = document.getElementById('top_five_missions_oci').getContext('2d');
    var mypiechart = new Chart(ctx, config);


}// end of total_oci_issued

function create_data_table_2column(table_id, data_list, table_classes, head1, head2) {

    var result = '<table class="' + table_classes + '">';
    result = result + '<thead><tr><th scope="col">' + head1 + '</th><th scope="col">' + head2 + '</th></tr></thead>';
    result = result + '<tbody>';
    Object.keys(data_list).forEach(function (key) {
        result = result + '<tr><td>' + key + '</td><td>' + data_list[key] + '</td></tr>';
    });
    result = result + '</tbody>';

    result = result + '</table>';
    $('#' + table_id).html(result);
    // <td style="cursor: pointer;color: #127fe5;text-decoration: underline;">
}

function create_data_table_5column(table_id, voa_data, table_classes) {



    var labels = [];
    var list_of_data = [];

    var count = 0;
    var count_for_list = 0;

    var result = '<table class="' + table_classes + '">';
    result = result + '<thead><tr><th scope="col">Year</th>';

    $.each(voa_data, function (index, value) {
        result = result + '<th scope="col">' + index + '</th>';
        if (index !== 'UpdateDate') {
            $.each(value, function (index1, value1) {
                count++;
                list_of_data.push(value1);
                labels.push(index1);
            });
            count_for_list = count;
            count = 0;
        }
    });
    result = result + '<th scope="col">Total</th></tr></thead>';
    result = result + '<tbody>';

    var total_count = 0;
    for (var j = 0; j < count_for_list; j++) {
        result = result + '<tr><td>' + labels[j] + '</td>';
        for (var i = j; i < list_of_data.length; i = i + 4) {
            result = result + '<td>' + list_of_data[i] + '</td>';
            total_count = total_count + parseInt(list_of_data[i]);
        }
        result = result + '<td>' + total_count + '</td>';
        total_count = 0;
        result = result + '</tr>';
    }

    result = result + '</tbody>';
    result = result + '</table>';
    $('#' + table_id).html(result);
    // <td style="cursor: pointer;color: #127fe5;text-decoration: underline;">
}

function dropdown_year_wise(data_list, selector_id) {
    var result = "<option selected value=''>All</option>";
    Object.keys(data_list).reverse().forEach(function (key) {
        result = result + '<option value=' + key + '>' + key + '</option>';
    });
    $('#' + selector_id).html(result);
}



function create_total_arrival_departure_barchart(mylabel,a1,a2,a3) {

   var barChartData = {
       labels: mylabel,
       datasets: [
    	   {
    		   data:a1,
    		   label:'Less than 30 days',
    		   backgroundColor:'#54b554',
    		   borderColor:'rgba(214, 237, 45, 1 )',
    		   borderWidth:1
    	   }, {
    		   data:a2,
    		   label:'30 to 60 days',
    		   backgroundColor:'#3c88c9',
    		   borderColor:'rgba(214, 237, 45, 1 )',
    		   borderWidth:1
    	   }, {
    		   data:a3,
    		   label:'More than 60 days',
    		   backgroundColor:'#da5a56',
    		   borderColor:'rgba(214, 237, 45, 1 )',
    		   borderWidth:1
    	   }
    	   
       ]

   };

   var chartOptions = {
       responsive: true,
       legend: {
           position: "top"
       },
       title: {
           display: false,
           text: "Chart.js Bar Chart"
       },
       scales: {
           yAxes: [{
                   ticks: {
                       beginAtZero: false
                   }
               }]
       },
       animation: {
           duration: 1
       },
       plugins: {
           labels: {
               render: 'value',
               overlap: true,
               fontColor: '#000000',
               fontSize: 12,
               arc: false
           }
       }
   }
   
   var ctx = document.getElementById("default_evisa_chart").getContext("2d");
   Chart.Legend.prototype.afterFit = function () {
       this.height = this.height + 20;
   };
   var myBarChart = new Chart(ctx, {
       type: 'bar',
       data: barChartData,
       options: chartOptions
   });
}
function create_total_arrival_departure_barchart1(mylabel,a1,a2,a3) {

   var barChartData = {
       labels: mylabel,
       datasets: [
    	   {
    		   data:a1,
    		   label:'Less than 30 days',
    		   backgroundColor:'#54b554',
    		   borderColor:'rgba(214, 237, 45, 1 )',
    		   borderWidth:1
    	   }, {
    		   data:a2,
    		   label:'30 to 60 days',
    		   backgroundColor:'#3c88c9',
    		   borderColor:'rgba(214, 237, 45, 1 )',
    		   borderWidth:1
    	   }, {
    		   data:a3,
    		   label:'More than 60 days',
    		   backgroundColor:'#da5a56',
    		   borderColor:'rgba(214, 237, 45, 1 )',
    		   borderWidth:1
    	   }
    	   
       ]

   };

   var chartOptions = {
       responsive: true,
       legend: {
           position: "top"
       },
       title: {
           display: false,
           text: "Chart.js Bar Chart"
       },
       scales: {
           yAxes: [{
                   ticks: {
                       beginAtZero: false
                   }
               }]
       },
       animation: {
           duration: 1
       },
       plugins: {
           labels: {
               render: 'value',
               overlap: true,
               fontColor: '#000000',
               fontSize: 12,
               arc: false
           }
       }
   }
   
   var ctx = document.getElementById("default_evisa_chart1").getContext("2d");
   Chart.Legend.prototype.afterFit = function () {
       this.height = this.height + 10;
   };
   var myBarChart = new Chart(ctx, {
       type: 'bar',
       data: barChartData,
       options: chartOptions
   });
}