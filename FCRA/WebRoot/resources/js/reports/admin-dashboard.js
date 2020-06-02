var barContent=[];
$(document).ready(function(){
	pending();
	
});	

function prepareBarchart(){
	
	$('#chart-div').html('');	
    $('#chart-div').jqplot([barContent], {
        title:'Pending Details',
        animate: !$.jqplot.use_excanvas,
        seriesDefaults:{
            renderer:$.jqplot.BarRenderer,            
            pointLabels: { show: true},
            rendererOptions: {               
                varyBarColor: true
            }
        },
        axes:{
            xaxis:{
                renderer: $.jqplot.CategoryAxisRenderer,
                //label: 'Pending Details',
                labelRenderer: $.jqplot.CanvasAxisLabelRenderer,                
                tickRenderer: $.jqplot.CanvasAxisTickRenderer,
                tickOptions: {
                    angle: -30,  
                    fontSize: '10pt',
                    labelPosition: 'middle'
                }
            },
            yaxis: {
            	label: 'Count',
                labelRenderer: $.jqplot.CanvasAxisLabelRenderer
              }
        }
    });
}

function pending(){
	$('#bar-notify').html('');
	  var action = 'pending-bar-chart-administration-dashboard';	
		$.ajax({
			url: action,
			method:'POST',
			dataType:'json',
			success: function(data){	
				//if(data[0]!=null)
				//	notifyList(JSON.parse(data[0]),'');			
				if(!(data[0]==null || data[0]==""))
					
					preparePendingData(JSON.parse(data[0]));
			},
			error: function(textStatus,errorThrown){
				alert('error');			
			}
		});	
}
function preparePendingData(tableData){
			
	$.each(tableData, function(index, item){
		var a=[];	
		if (item.li == 1)
			item.li = "Mail";
		else if (item.li == 2)
			item.li = "Registration";
		else if (item.li == 3)
			item.li = "Notification";
		a[0]=item.li;
		a[1]=parseInt(item.ld);
			barContent[index]=a;	
			prepareBarchart();
	});		
	
}