$(document).ready(function(){
	showGraph(1);
});

function createCountrywiseBarGraph(barContentCountryWise,blockYear){
	
	/*$('#barGraphCountrywise').html('');	
    $('#barGraphCountrywise').jqplot([barContentCountryWise], {*/
    	$('#graphCountrywise').html('');
    	if(barContentCountryWise!=0){
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
    	else{
    		barContentCountryWise = [''];
    		$('#graphCountrywise').jqplot([barContentCountryWise], { title: 'Top Country-wise Statistics (Block Year '+blockYear + ')'});
    	}
}

function createCountryFinYearwiseBarGraph(barContentFinYearCountryWise,blkYears){
	
	/*$('#barGraphCountrywise').html('');	
    $('#barGraphCountrywise').jqplot([barContentCountryWise], {*/
	
    	$('#graphFinCountrywise').html('');	
    	if(barContentFinYearCountryWise!=0){
        $('#graphFinCountrywise').jqplot([barContentFinYearCountryWise], {	
        title: 'Top Country FY-wise Statistics',
        animate: !$.jqplot.use_excanvas,
        seriesDefaults:{
            renderer:$.jqplot.BarRenderer, 
            pointLabels: { show: false, labels: blkYears,fontSize:'10pt'},
            rendererOptions: {               
                varyBarColor: true
            }        	
        },
        /* series:[
	          {pointLabels:{
	              show: true,
	              labels: blkYears,
	              //angle: -30 ,
	              fontSize:'10pt'
	             }}], */       
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
	        	 tooltipFormatCallback: function(x, y, seriesIndex, pointIndex){
		            	return blkYears[x-1] +", "+ y;
		            	
		            },
	            sizeAdjust: 20,
	          },
	          cursor: {
	            show: false
	          }        
    });
    	}
    	else{
    		barContentFinYearCountryWise = [''];
    		$('#graphFinCountrywise').jqplot([barContentFinYearCountryWise], {title: 'Top Country FY-wise Statistics'});
    	}
}

function createAssowiseBarGraph(barContentAssoWise,blockYear){
	$('#graphAssowise').html('');	
	if(barContentAssoWise != 0){
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
	else{
		barContentAssoWise = [''];
		$('#graphAssowise').jqplot([barContentAssoWise], {title: 'Top Association-wise Statistics (Block Year '+blockYear + ')'});
	}
}

function createAssoFinYearwiseBarGraph(barContentAssoFinYearWise,blkYears){
	$('#graphFinAssowise').html('');
	if(barContentAssoFinYearWise.length != 0){
	   $('#graphFinAssowise').jqplot([barContentAssoFinYearWise], { //plot2 = $.jqplot('barGraph1', [s1, s2], {
		//plot2 = $.jqplot('graphFinAssowise', [barContentAssoFinYearWise], {
	    	title: 'Top Association FY-wise Statistics ',
	        animate: !$.jqplot.use_excanvas,
	        seriesDefaults:{
	            renderer:$.jqplot.BarRenderer, 
	            pointLabels: { show: false, labels: blkYears,fontSize:'10pt'},
	            rendererOptions: {               
	                varyBarColor: true
	            }        	
	        },
	       /*series:[
	                {pointLabels:{
	                   show: true,
	                   labels: blkYears,
	                   //angle: -30 ,
	                   fontSize:'8pt'
	                 }}],*/
     
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
	        	 tooltipFormatCallback: function(x, y, seriesIndex, pointIndex){
		            	return blkYears[x-1] +", "+ y;
		            	
		            },
	            sizeAdjust: 20,
	          },
	          cursor: {
	            show: false
	          }
	    });
	}
	else{
		barContentAssoFinYearWise = [''];
		$('#graphFinAssowise').jqplot([barContentAssoFinYearWise], {title: 'Top Association FY-wise Statistics '});
	}
}


function createDonorwiseBarGraph(barContentDonorWise,blockYear){
	$('#graphDonorwise').html('');	
	if(barContentDonorWise.length!=0){
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
	else{
		barContentDonorWise = [''];
		$('#graphDonorwise').jqplot([barContentDonorWise], { title: 'Top Donor-wise Statistics (Block Year '+blockYear + ')'});
	}
}

function createDonorFinYearWiseBarGraph(barContentDonorFinYearWise,blkYears){
	$('#graphFinDonorwise').html('');	
	if(barContentDonorFinYearWise.length!=0){
    $('#graphFinDonorwise').jqplot([barContentDonorFinYearWise], {
        title: 'Top Donor FY-wise Statistics ',
        animate: !$.jqplot.use_excanvas,
        seriesDefaults:{
            renderer:$.jqplot.BarRenderer, 
            pointLabels: { show: false, labels: blkYears,fontSize:'10pt'},
            rendererOptions: {               
                varyBarColor: true
            }        	
        },
        /* series:[
 	            
 		          {pointLabels:{
 		              show: true,
 		              labels: blkYears,
 		              //angle: -30 ,
 		              fontSize:'10pt'
 		             }}],   */     
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
 		            tooltipFormatCallback: function(x, y, seriesIndex, pointIndex){
 		            	return blkYears[x-1] +", "+ y;
 		            	
 		            },
 		            sizeAdjust: 20,
 		          },
 		          cursor: {
 		            show: true
 		          }        
    });
	}
	else{
		barContentDonorFinYearWise = [''];
		$('#graphFinDonorwise').jqplot([barContentDonorFinYearWise], {title: 'Top Donor FY-wise Statistics '});
	}
}

function createBarGraph(barContent){
	//$('#barGraph2').html('');
	$('#graphRegYearWise').html('');
	if(barContent.length!=0){
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
    });}
	else{
		barContent = [''];
		$('#graphRegYearWise').jqplot([barContent], {title: 'Year wise Registration Statistics '});
	}
}


function createBarGraph2(ticks,s1,s2,s3){
	$('#graphApplicationwise').html('');
	//plot2 = $.jqplot('graphApplicationwise', [s1, s2], {
	if(s1.length!=0&&s2.length!=0&&s3.length!=0){
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
	}else{
		s1 = [''];s2 = [''];s3 = [''];
		$('#graphApplicationwise').jqplot([s3, s1, s2], {title: 'Application Statistics - Year Wise'});
	}
               }

function createAppServiceStatistics(ticks,s1,s2,s3){
	$('#graphFinApplicationwise').html('');
	//plot2 = $.jqplot('graphApplicationwise', [s1, s2], {
	if(s1.length!=0&&s2.length!=0&&s3.length!=0){
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
               $("#yaxisDesc").html('');
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
               $("#yaxisDesc").append(content);}
               else{
            	   s1 = [''];s2 = [''];s3 = [''];
           		$('#graphFinApplicationwise').jqplot([s3, s1, s2], {title: 'Application Statistics - Service Wise'});
           	}
               }

function showGraph(i){
	$("#yaxisDesc").hide();
	$('.graph2').addClass('hidden');
	switch(i){
	case 1: 
		getRegStatistics();
		$('.regStats').removeClass('hidden').addClass('showDiv');
		break;
	case 2:
		getDonorStatistics();
		$('.donorWiseStats').removeClass('hidden').addClass('showDiv');
		break;
	case 3: 
		getCountryStatistics();
		$('.countryWiseStats').removeClass('hidden').addClass('showDiv');
		break;
	case 4:
		getAssocicationStatistics();
		$('.assoWiseStats').removeClass('hidden').addClass('showDiv');
		break;		
	case 5:
		getApplicationStatistics();
		$('.applicationWiseStats').removeClass('hidden').addClass('showDiv');
		$("#yaxisDesc").show();
		break;
	}
}

function getRegStatistics(){
if($("#graphRegYearWise").is(':empty')){
	var action = 'reg-graph-dash-board';
	var params = '';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			var barContent=[];
			$.each(JSON.parse(data[0]), function(index, item) {
				var a=[];
				a[0]=item.k;
				a[1]=parseInt(item.v);
				barContent[index]=a;
			}); 
			createBarGraph(barContent);

		},
		error: function(textStatus,errorThrown){
		}
	});
}
}

function getDonorStatistics(){

	if($("#graphDonorwise").is(':empty')||$("#graphFinDonorwise").is(':empty')){
	var action = 'donor-graph-dash-board';
	var params = '';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
		var blockYear = data[1]; 
		var blockYearList =JSON.parse(data[3]);
		var barContentDonorWise=[], blkYears=[],
		barContentDonorFinYearWise=[];
		var countD=0;
		$.each(JSON.parse(data[0]), function(index, item) {
			 var a=[]; 
	    	 a[0]=item.k;
	    	 a[1]=parseInt(item.v);
	    	 barContentDonorWise[index]=a;
	    	}); 
		$.each(JSON.parse(data[2]), function(index, item){
			var a=[]; var yearName;
			blkYears[countD] = item.k;
			a[0]= blockYearList[countD++];
			a[1]=parseInt(item.v);
			barContentDonorFinYearWise[index]=a;
		});
		createDonorwiseBarGraph(barContentDonorWise, blockYear);
		createDonorFinYearWiseBarGraph(barContentDonorFinYearWise, blkYears);		
		},
		error: function(textStatus,errorThrown){
		}
	});
	}
}

function getCountryStatistics(){

	if($("#graphCountrywise").is(':empty')||$("#graphFinCountrywise").is(':empty')){
	var action = 'country-graph-dash-board';
	var params = '';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			var blockYear = data[1]; 
			var blockYearList =JSON.parse(data[3]);
			var barContentCountryWise=[], blkYears=[],
			 barContentFinYearCountryWise=[];
			var countC = 0;
			 $.each(JSON.parse(data[0]), function(index, item) {
				 var a=[];
		    	 a[0]=item.k;
		    	 a[1]=parseInt(item.v);
		    	 barContentCountryWise[index]=a;
		    	}); 	
				$.each(JSON.parse(data[2]), function(index, item){
					var a=[];
					blkYears[countC] = item.k;
					a[0]=blockYearList[countC++];
					a[1]=parseInt(item.v);
					barContentFinYearCountryWise[index]=a;
				});							 
			createCountrywiseBarGraph(barContentCountryWise,blockYear);
			createCountryFinYearwiseBarGraph(barContentFinYearCountryWise,blkYears);			
			},
		error: function(textStatus,errorThrown){
		}
	});
	}
}

function getAssocicationStatistics(){

	if($("#graphAssowise").is(':empty')||$("#graphFinAssowise").is(':empty')){
	var action = 'association-graph-dash-board';
	var params = '';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
				var blockYear = data[1]; 
				var blockYearList =JSON.parse(data[3]);
				var barContentAssoWise=[], blkYears=[],
				barContentAssoFinYearWise=[];
				var countA = 0;	
				$.each(JSON.parse(data[0]), function(index, item) {
					var a=[];
					a[0]=item.k;
					a[1]=parseInt(item.v);
					barContentAssoWise[index]=a;
				}); 	
				$.each(JSON.parse(data[2]), function(index, item){
					var a=[];
					blkYears[countA] = item.k;//.concat(",").concat(item.v);
					a[0]=blockYearList[countA++];
					a[1]=parseInt(item.v);
					barContentAssoFinYearWise[index]=a;
				});	
				createAssowiseBarGraph(barContentAssoWise,blockYear);
				createAssoFinYearwiseBarGraph(barContentAssoFinYearWise,blkYears);	
				
		},
		error: function(textStatus,errorThrown){
		}
	});
	}
}

function getApplicationStatistics(){

	if($("#graphApplicationwise").is(':empty')||$("#graphFinApplicationwise").is(':empty')){
	var action = 'application-graph-dash-board';
	var params = '';
	$.ajax({
		url: action,
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			var  appStatYear=[], appStatPending=[], appStatApproved=[], appStatTotal=[], appStatYear1=[], appStatPending1=[], appStatApproved1=[], appStatTotal1=[];
			$.each(JSON.parse(data[0]), function(index, item) {
				appStatYear[index]=item.p1;
				appStatTotal[index]= parseInt(item.p2)+parseInt(item.p3);
				appStatPending[index]=parseInt(item.p2);
				appStatApproved[index]=parseInt(item.p3);
			});
			$.each(JSON.parse(data[1]), function(index, item) {
				//year = item.p1;
				appStatYear1[index]=item.p1;
				appStatTotal1[index]= parseInt(item.p2)+parseInt(item.p3);
				appStatPending1[index]=parseInt(item.p2);
				appStatApproved1[index]=parseInt(item.p3);
			});
			createBarGraph2(appStatYear,appStatPending,appStatApproved,appStatTotal);
			createAppServiceStatistics(appStatYear1,appStatPending1,appStatApproved1,appStatTotal1);

		},
		error: function(textStatus,errorThrown){
		}
	});
	}
}