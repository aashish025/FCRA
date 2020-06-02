$(document).ready(function(){
	defaultGraph();
	//graphHome();
});

function defaultGraph(){
	$('#notice-div').hide();
	//$('#barGraph1').hide();
	var action = '/FCRA/default-home';
	var params = '';
	
	//var graphType = document.querySelector('input[name = "graphType"]:checked').value;
	
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			initNewNotificationCount(JSON.parse("["+data[1]+"]"));
			initNotification(JSON.parse(data[0]),"1","");
			var barContent=[], appStatYear=[], appStatPending=[], appStatApproved=[], appStatTotal=[], barContentDonorWise=[],barContentAssoWise=[],barContentCountryWise=[],
			barContentSuddenRiseIncomeWise=[], appStatYear1=[], appStatPending1=[], appStatApproved1=[], appStatTotal1=[];
			//alert(graphType);
			 $.each(JSON.parse(data[2]), function(index, item) {
				 var a=[];
		    	 a[0]=item.k;
		    	 a[1]=parseInt(item.v);
		    	 barContent[index]=a;
		    	}); 
			 $.each(JSON.parse(data[3]), function(index, item) {
				appStatYear[index]=item.p1;
				appStatTotal[index]= parseInt(item.p2)+parseInt(item.p3);
				appStatPending[index]=parseInt(item.p2);
				appStatApproved[index]=parseInt(item.p3);
		    	});
			 $.each(JSON.parse(data[4]), function(index, item) {
					appStatYear1[index]=item.p1;
					appStatTotal1[index]= parseInt(item.p2)+parseInt(item.p3);
					appStatPending1[index]=parseInt(item.p2);
					appStatApproved1[index]=parseInt(item.p3);
			    	});
			//if(graphType=='r'){
			 createBarGraph2(appStatYear,appStatPending,appStatApproved,appStatTotal);
				createBarGraph(barContent);
				createAppServiceStatistics(appStatYear1,appStatPending1,appStatApproved1,appStatTotal1);
				showGraph(1);
				//createBarGraphDuplicate(barContent);
			//}
			//graphHome();
		},
		error: function(textStatus,errorThrown){
		}
	});

}

function graphHome(){
	$('#notice-div').hide();
	//$('#barGraph1').hide();
	var action = '/FCRA/graph-home';
	//var graphType = document.querySelector('input[name = "graphType"]:checked').value;
	var params = '';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			var blockYear = data[3]; 
			var blockYearList =JSON.parse(data[7]);
			var barContentDonorWise=[],barContentAssoWise=[],barContentCountryWise=[], blkYears=[],
			barContentSuddenRiseIncomeWise=[], barContentDonorFinYearWise=[], barContentAssoFinYearWise=[],barContentFinYearCountryWise=[];
/*			var count=0;
			while(count<=9){
				blkYears[count] = blockYearList[count];
				count++;
			}*/
			//if(graphType == 'd'){
				var countD=0;
				$.each(JSON.parse(data[0]), function(index, item) {
					 var a=[]; 
			    	 a[0]=item.k;
			    	 a[1]=parseInt(item.v);
			    	 barContentDonorWise[index]=a;
			    	}); 
				$.each(JSON.parse(data[4]), function(index, item){
					var a=[]; var yearName;
					blkYears[countD] = blockYearList[countD++];
					a[0]=item.k;
					a[1]=parseInt(item.v);
					barContentDonorFinYearWise[index]=a;
				});
				createDonorwiseBarGraph(barContentDonorWise, blockYear);
				createDonorFinYearWiseBarGraph(barContentDonorFinYearWise, blkYears);				
			//}
			//else if(graphType == 'c'){
				var countC = 0;
				 $.each(JSON.parse(data[2]), function(index, item) {
					 var a=[];
			    	 a[0]=item.k;
			    	 a[1]=parseInt(item.v);
			    	 barContentCountryWise[index]=a;
			    	}); 	
					$.each(JSON.parse(data[6]), function(index, item){
						var a=[];
						blkYears[countC] = blockYearList[countC++];
						a[0]=item.k;
						a[1]=parseInt(item.v);
						barContentFinYearCountryWise[index]=a;
					});							 
				createCountrywiseBarGraph(barContentCountryWise,blockYear);
				createCountryFinYearwiseBarGraph(barContentFinYearCountryWise,blkYears);				
			//}
			//else if(graphType == 'a'){
				 var countA = 0;	
				 $.each(JSON.parse(data[1]), function(index, item) {
					 var a=[];
			    	 a[0]=item.k;
			    	 a[1]=parseInt(item.v);
			    	 barContentAssoWise[index]=a;
			    	}); 	
					$.each(JSON.parse(data[5]), function(index, item){
						var a=[];
						blkYears[countA] = blockYearList[countA++];//.concat(",").concat(item.v);
						a[0]=item.k;
						a[1]=parseInt(item.v);
						barContentAssoFinYearWise[index]=a;
					});	
				createAssowiseBarGraph(barContentAssoWise,blockYear);
				createAssoFinYearwiseBarGraph(barContentAssoFinYearWise,blkYears);				
			//}
		},
		error: function(textStatus,errorThrown){
		}
	});

}

function initNewNotificationCount(data){	
	$.each(data,function(index,item){		
		if(item.A!="0")
			$('#NB-1').html(item.A);
		if(item.C!="0");
			$('#NB-2').html(item.C);
		if(item.D!="0");
			$('#NB-3').html(item.D);
	});
}
function initNotification(data,notificationType,attachments){	
	var notificationList=data;
	var content='',liContent='';
	if(notificationType=="1"){		
		liClass="notice-li nb-msg";		
	}else if(notificationType=="2"){
		liClass="notice-li nb-crc";		
	}else if(notificationType=="3"){
		liClass="notice-li nb-doc";		
	}	
	content='<ul class="notice-container">';
	$.each(notificationList,function(index,item){
		if(item.notificationStatus=="NEW"){
			var titleContent='<span class="title">'+item.messageTitle+'&nbsp;&nbsp;<img src="img/auth/home/msg-new.gif"/></span>';
		}else{
			var titleContent='<span class="title">'+item.messageTitle+'</span>';
		}		
		attachContent=prepareAttachment(notificationType,attachments,item.notificationId);		 
		content+='<li class="'+liClass+'" id="">'+
					'<div class="notice-bubble" padding-bottom="0">'+
						'<div class="notice-header">'+titleContent+							
							'<span class="actions">&nbsp;&nbsp;'+attachContent+'&nbsp;&nbsp;'+
							'<i class="toggle title-t i glyphicon glyphicon-chevron-down" onclick="javascript:initToggle(this);"></i>&nbsp;</span>'+
						'</div>'+
						'<div class="notice-desc">'+
							'<span class="desc">'+item.messageDetails+'</span>'+	
							'<br/><span class="time"><Strong>Posted on:&nbsp;&nbsp;'+item.enteredOn+'</Strong></span>'+
						'</div>'+
					'</div>'+
				'</li>';
		
	});
	content+='</ul>';	
	$('#notice-div').html(content);
}
function prepareAttachment(notificationType,attachments,notificationId){
	var content='';
	if(notificationType=="1"){
			content='';
	}
	else{	
			var liContent='';
			$.each(attachments,function(index,item){				
				if(notificationId==item.notificationId){
					var rowId=item.rowId;
					liContent+='<li><a href="javascript:getNotificationAttachment(\''+rowId+'\');">Document '+(++index)+'</a></li>';
				}
			});	
			var content='<span class="dropdown">'+
									'<i class="toggle glyphicon glyphicon-paperclip" id="attach"'+ 
								    ' data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"></i>'+
									'<ul class="dropdown-menu" aria-labelledby="attach">'
								    +liContent+							
									'</ul>'+
								'</span>';		
	}	
	return content;
}
function getNotificationAttachment(rowId){
	var url='/FCRA/get-notification-attachment-home';
	$("#attachment-download-form").attr('action', url);
	$("#attachment-download-form #rowId").val(rowId);
	$("#attachment-download-form").submit();
}
function initToggle(obj){	
	$(obj).closest('div .notice-header').siblings('.notice-desc').toggle();
	if($(obj).hasClass('glyphicon glyphicon-chevron-down'))
		$(obj).removeClass('glyphicon glyphicon-chevron-down').addClass('glyphicon glyphicon-chevron-up');
	else
		$(obj).removeClass('glyphicon glyphicon-chevron-up').addClass('glyphicon glyphicon-chevron-down');
}
function pullNotification(val){
	$('#notice-div').show();
	$('#barGraphDiv').hide();
	var params='notificationType='+val;
	var action = '/FCRA/selected-home';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){			
			initNotification(JSON.parse(data[0]),val,JSON.parse(data[1]));
		},
		error: function(textStatus,errorThrown){
		}
	});
}

function createCountrywiseBarGraph(barContentCountryWise,blockYear){
	
	/*$('#barGraphCountrywise').html('');	
    $('#barGraphCountrywise').jqplot([barContentCountryWise], {*/
    	$('#graphCountrywise').html('');	
        $('#graphCountrywise').jqplot([barContentCountryWise], {	
        title: 'Top Country-wise Statistics (Block Year '+blockYear + ')',
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
               // label: 'Year Range',
                labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
                tickRenderer: $.jqplot.CanvasAxisTickRenderer,
                tickOptions: {
                    angle: -30 ,
                    fontSize: '7pt'
                }
            },
            yaxis: {
               // label: 'Registration',
                labelRenderer: $.jqplot.CanvasAxisLabelRenderer
              }
        }
    });
 
}

function createCountryFinYearwiseBarGraph(barContentFinYearCountryWise,blkYears){
	
	/*$('#barGraphCountrywise').html('');	
    $('#barGraphCountrywise').jqplot([barContentCountryWise], {*/
	
    	$('#graphFinCountrywise').html('');	
        $('#graphFinCountrywise').jqplot([barContentFinYearCountryWise], {	
        title: 'Top Country FY-wise Statistics',
        animate: !$.jqplot.use_excanvas,
 	    series:[
	          {pointLabels:{
	              show: true,
	              labels: blkYears,
	              //angle: -30 ,
	              fontSize:'10pt'
	             }}],        
        axes:{
            xaxis:{
                renderer: $.jqplot.CategoryAxisRenderer,
                tickRenderer: $.jqplot.CanvasAxisTickRenderer,
                tickOptions: {
                    angle: -30 ,
                    fontSize: '8pt',
                }
            },
            yaxis: {
               // label: 'Registration',
                labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
                tickOptions: {
                    fontSize: '8pt',
                }
              }
        },
        highlighter: {
	           show: true,
	        	tooltipLocation : 'n',
	        	fadeTooltip : true,
	        	useAxesFormatters:false,
	        	//formatString: '%s, %d',
	            tooltipFormatCallback: function(x, y, x_label, seriesIndex){
	            	switch (seriesIndex) {
	            		case 0:
	                		return + y + "," + x_label;
							break;
	            		case 1:
	                		return + y + "," + x_label;
							break;
	            		case 2:
	                		return + y + "," + x_label;
							break;
	            		case 3:
	                		return + y + "," + x_label;
							break;							
	            		case 4:
	                		return + y + "," + x_label;
							break;
	            		case 5:
	                		return + y + "," + x_label;
							break;							
	            		case 6:
	                		return + y + "," + x_label;
							break;
	            		case 7:
	                		return + y + "," + x_label;
							break;
	            		case 8:
	                		return + y + "," + x_label;
							break;
	            		case 9:
	                		return + y + "," + x_label;
							break;						
	            	}
	            },
	            sizeAdjust: 20,
	          },
	          cursor: {
	            show: false
	          }        
    });
 
}

function createAssowiseBarGraph(barContentAssoWise,blockYear){
	$('#graphAssowise').html('');	
    $('#graphAssowise').jqplot([barContentAssoWise], {
        title: 'Top Association-wise Statistics (Block Year '+blockYear + ')',
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
               // label: 'Year Range',
                labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
                tickRenderer: $.jqplot.CanvasAxisTickRenderer,
                tickOptions: {
                    angle: -30 ,
                    fontSize: '7pt'
                }
            },
            yaxis: {
               // label: 'Registration',
                labelRenderer: $.jqplot.CanvasAxisLabelRenderer
              }
        }
    });
 
}

function createAssoFinYearwiseBarGraph(barContentAssoFinYearWise,blkYears){
	$('#graphFinAssowise').html('');
	   $('#graphFinAssowise').jqplot([barContentAssoFinYearWise], { //plot2 = $.jqplot('barGraph1', [s1, s2], {
		//plot2 = $.jqplot('graphFinAssowise', [barContentAssoFinYearWise], {
	    	title: 'Top Association FY-wise Statistics ',
	        animate: !$.jqplot.use_excanvas,
	        
	       series:[
	                {pointLabels:{
	                   show: true,
	                   labels: blkYears,
	                   //angle: -30 ,
	                   fontSize:'8pt'
	                 }}],
     
	        axes:{
	            xaxis:{
	                renderer: $.jqplot.CategoryAxisRenderer,
	                tickRenderer: $.jqplot.CanvasAxisTickRenderer,
	                tickOptions: {
	                    angle: -30 ,
	                    fontSize: '8pt',
	                }
	            },
	            yaxis: {
	               // label: 'Registration',
	                labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
	                tickOptions: {
	                    fontSize: '8pt',
	                }
	              }
	        },
	          highlighter: {
	           show: true,
	        	tooltipLocation : 'n',
	        	fadeTooltip : true,
	        	useAxesFormatters:false,
	        	//formatString: '%s, %d',
	            tooltipFormatCallback: function(x, y, x_label, seriesIndex){
	            	switch (seriesIndex) {
	            		case 0:
	                		return + y + "," + x_label;
							break;
	            		case 1:
	                		return + y + "," + x_label;
							break;
	            		case 2:
	                		return + y + "," + x_label;
							break;
	            		case 3:
	                		return + y + "," + x_label;
							break;							
	            		case 4:
	                		return + y + "," + x_label;
							break;
	            		case 5:
	                		return + y + "," + x_label;
							break;							
	            		case 6:
  	                		return + y + "," + x_label;
							break;
	            		case 7:
	                		return + y + "," + x_label;
							break;
	            		case 8:
	                		return + y + "," + x_label;
							break;
	            		case 9:
	                		return + y + "," + x_label;
							break;							
	            	}
	            },
	            sizeAdjust: 20,
	          },
	          cursor: {
	            show: false
	          }
	    });

}

function createAssoFinYearwiseBarGraphWorkable(barContentAssoFinYearWise,blkYears){
	$('#graphFinAssowise').html('');
	   $('#graphFinAssowise').jqplot([barContentAssoFinYearWise], {
	    	title: 'Top Association FY-wise Statistics ',
	        animate: !$.jqplot.use_excanvas,
	        
	        seriesDefaults:{
	            //renderer:$.jqplot.BarRenderer, 
	        	renderer:$.jqplot.DateAxisRenderer,
	            pointLabels: { show: true},
	            rendererOptions: {               
	                //varyBarColor: true
	            }        	
	        },
	       series:[
	                {pointLabels:{
	                   show: true,
	                   labels: blkYears,
	                   //labels:[ '2007-2008', '2008-2009', '20009-2010', '2010-2011', '2011-2012','2012-2013','2013-2014','2014-2015','2015-2016','2016-2017'],
	                   angle: -30 ,
	                   fontSize:'10pt'
	                 }}],
	        axes:{
	            xaxis:{
	                renderer: $.jqplot.CategoryAxisRenderer,
	               // label: 'Year Range',
	                labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
	                tickRenderer: $.jqplot.CanvasAxisTickRenderer,
	                tickOptions: {
	                    angle: -30 ,
	                    fontSize: '8pt',
	                    	formatString: ''
	                }
	            },
	            yaxis: {
	               // label: 'Registration',
	                labelRenderer: $.jqplot.CanvasAxisLabelRenderer
	              }
	        },
	          highlighter: {
	            show: true,
	            sizeAdjust: 20
	          },
	          cursor: {
	            show: false
	          }
	    });

}

function createDonorwiseBarGraph(barContentDonorWise,blockYear){
	$('#graphDonorwise').html('');	
    $('#graphDonorwise').jqplot([barContentDonorWise], {
        title: 'Top Donor-wise Statistics (Block Year '+blockYear + ')',
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
               // label: 'Year Range',
                labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
                tickRenderer: $.jqplot.CanvasAxisTickRenderer,
                tickOptions: {
                    angle: -30 ,
                    fontSize: '7pt'
                }
            },
            yaxis: {
               // label: 'Registration',
                labelRenderer: $.jqplot.CanvasAxisLabelRenderer
              }
        }
    });
 
}

function createDonorFinYearWiseBarGraph(barContentDonorFinYearWise,blkYears){
	$('#graphFinDonorwise').html('');	
    $('#graphFinDonorwise').jqplot([barContentDonorFinYearWise], {
        title: 'Top Donor FY-wise Statistics ',
        animate: !$.jqplot.use_excanvas,
 	    series:[
 		          {pointLabels:{
 		              show: true,
 		              labels: blkYears,
 		              //angle: -30 ,
 		              fontSize:'10pt'
 		             }}],        
 	        axes:{
 	            xaxis:{
 	                renderer: $.jqplot.CategoryAxisRenderer,
 	                tickRenderer: $.jqplot.CanvasAxisTickRenderer,
 	                tickOptions: {
 	                    angle: -30 ,
 	                    fontSize: '8pt',
 	                }
 	            },
 	            yaxis: {
 	               // label: 'Registration',
 	                labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
 	                tickOptions: {
 	                    fontSize: '8pt',
 	                }
 	              }
 	        },
 	        highlighter: {
 		           show: true,
 		        	tooltipLocation : 'n',
 		        	fadeTooltip : true,
 		        	useAxesFormatters:false,
 		        	//formatString: '%s, %d',
 		            tooltipFormatCallback: function(x, y, x_label, seriesIndex){
 		            	switch (seriesIndex) {
 		            		case 0:
 		                		return + y + "," + x_label;
 								break;
 		            		case 1:
 		                		return + y + "," + x_label;
 								break;
 		            		case 2:
 		                		return + y + "," + x_label;
 								break;
 		            		case 3:
 		                		return + y + "," + x_label;
 								break;							
 		            		case 4:
 		                		return + y + "," + x_label;
 								break;
 		            		case 5:
 		                		return + y + "," + x_label;
 								break;							
 		            		case 6:
 		                		return + y + "," + x_label;
 								break;
 		            		case 7:
 		                		return + y + "," + x_label;
 								break;
 		            		case 8:
 		                		return + y + "," + x_label;
 								break;
 		            		case 9:
 		                		return + y + "," + x_label;
 								break;						
 		            	}
 		            },
 		            sizeAdjust: 20,
 		          },
 		          cursor: {
 		            show: false
 		          }        
    });
 
}

function createBarGraph(barContent){
	//$('#barGraph2').html('');
	$('#graphRegYearWise').html('');
/*	$('#graphDonorwise').hide();$('#graphFinDonorwise').hide();
	$('#graphCountrywise').hide();$('#graphFinCountrywise').hide();
	$('#graphAssowise').hide();	$('#graphFinAssowise').hide();
	$('#graphRegYearWise').show();$('#graphRegYearWise1').show();*/
	$('#graphRegYearWise').jqplot([barContent], {
        title: 'Year wise Registration Statistics ',
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
               // label: 'Year Range',
                labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
                tickRenderer: $.jqplot.CanvasAxisTickRenderer,
                tickOptions: {
                    angle: -30 ,
                    fontSize: '7pt'
                }
            },
            yaxis: {
               // label: 'Registration',
                labelRenderer: $.jqplot.CanvasAxisLabelRenderer
              }
        },
        grid:{ background: '#ffffff'}
    });
}


function createBarGraph2(ticks,s1,s2,s3){
	$('#graphApplicationwise').html('');
                //plot2 = $.jqplot('graphApplicationwise', [s1, s2], {
               $('#graphApplicationwise').jqplot([s3, s1, s2], {
	            title: 'Application Statistics - Year Wise',
	             animate: !$.jqplot.use_excanvas,
	             seriesDefaults: {
	                renderer:$.jqplot.BarRenderer,
	                rendererOptions: {
		            barPadding: 1
		           },
	                pointLabels: { show: true }
	            },
	            axes: {
	                xaxis: {
	                    renderer: $.jqplot.CategoryAxisRenderer,
	                    ticks: ticks,
	                },
	         yaxis: {
         // label: 'Amount',
          labelRenderer: $.jqplot.CanvasAxisLabelRenderer
        }
	            },
	 series:[
	            {label:'Total Recieved'},{label:'Pending'},{label:'Disposed'}
	            ],
	  legend: {
          show: true,
          location: 'ne',
          placement: 'outsideGrid',   	  
      },
      grid:{ background: '#ffffff'},
      seriesColors: ['#3c88c9', '#da5a56', '#54b554']
    });
	 
}

function createAppServiceStatistics(ticks,s1,s2,s3){
	$('#graphFinApplicationwise').html('');
                //plot2 = $.jqplot('graphApplicationwise', [s1, s2], {
               $('#graphFinApplicationwise').jqplot([s3, s1, s2], {
	            title: 'Application Statistics - Service Wise',
	             animate: !$.jqplot.use_excanvas,
	             seriesDefaults: {
	                renderer:$.jqplot.BarRenderer,
	                rendererOptions: {
		            barPadding: 1
		           },
	                pointLabels: { show: true }
	            },
	            axes: {
	                xaxis: {
	                    renderer: $.jqplot.CategoryAxisRenderer,
    					tickRenderer: $.jqplot.CanvasAxisTickRenderer,
    					//ticks: ticks
	                },
	         yaxis: {
         // label: 'Amount',
          labelRenderer: $.jqplot.CanvasAxisLabelRenderer
        }
	            },
	 series:[
	            {label:'Total Recieved'},{label:'Pending'},{label:'Disposed'}
	            ],
	  legend: {
          show: true,
          location: 'ne',
          placement: 'outsideGrid',
        	  
      },
      grid:{ background: '#ffffff'},
      seriesColors: ['#3c88c9', '#da5a56', '#54b554']
    });
	$("#yaxisDesc").html();
	var content = "";
	var j = 1;
	for(var i = 0; i< ticks.length; i++){
		if(j<2){
				content+= "<span class='label label-default'>"+(i+1)+" - "+ticks[i]+"</span> &nbsp;&nbsp;&nbsp;";
		j++;
		}
		else{
			content+= "<span class='label label-default'>"+(i+1)+" - "+ticks[i]+"</span> &nbsp;&nbsp;&nbsp;";
			content+= "<br>";
			j=1;
		}
	}
	$("#yaxisDesc").append(content);
}
function initBarGraph(){
	$('#notice-div').hide();
	$('#barGraphDiv').show();
}
function showGraph(i){
	$("#yaxisDesc").hide();
	$('.graph2').addClass('hidden');
	switch(i){
	case 1: 
		$('.regStats').removeClass('hidden').addClass('showDiv');
		break;
	case 2:
		$('.donorWiseStats').removeClass('hidden').addClass('showDiv');
		break;
	case 3: 
		$('.countryWiseStats').removeClass('hidden').addClass('showDiv');
		break;
	case 4:
		$('.assoWiseStats').removeClass('hidden').addClass('showDiv');
		break;		
	case 5:
		$('.applicationWiseStats').removeClass('hidden').addClass('showDiv');
		 $("#yaxisDesc").show();
		break;
	}
}
/*function graphDonorWise(){
	$('#graphRegYearWise').hide();$('#graphRegYearWise1').hide();
	$('#graphAssowise').hide();$('#graphCountrywise').hide();
	$('#graphFinAssowise').hide();$('#graphFinCountrywise').hide();
	$('#graphDonorwise').show();$('#graphFinDonorwise').show();
}
function graphCountryWise(){
	$('#graphRegYearWise').hide();$('#graphRegYearWise1').hide();
	$('#graphAssowise').hide();	$('#graphFinAssowise').hide();
	$('#graphDonorwise').hide();$('#graphFinDonorwise').hide();
	$('#graphCountrywise').show();$('#graphFinCountrywise').show();
}
function graphAssoWise(){
	$('#graphRegYearWise').hide();$('#graphRegYearWise1').hide();
	$('#graphDonorwise').hide();$('#graphFinDonorwise').hide();
	$('#graphCountrywise').hide();$('#graphFinCountrywise').hide();
	$('#graphAssowise').show();	$('#graphFinAssowise').show();
}
function registrationGraph(){
	$('#graphRegYearWise').show();$('#graphRegYearWise1').show();
	$('#graphDonorwise').show();$('#graphFinDonorwise').show();
	$('#graphCountrywise').show();$('#graphFinCountrywise').show();
	$('#graphAssowise').show();	$('#graphFinAssowise').show();	
}*/
