$(document).ready(function(){
	getdashboardPiegraph();
	getdashboardServiceStatus();
});
function getdashboardPiegraph(){
	var action = 'status-dash-board-service';
	var params = '';
	$.ajax({
		url: action,
		beforeSend: function() { $('#loading').show()},
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			var i=1,appStatYear=[],appStatPending=[],appStatApproved=[],appStatTotal=[],HosTotalReceived,HosTotalGranted,HosUnderProcess,HosRejected,
			appRegDeg1=[0,0,0,0,0,0,0,0,0,0,0,0],PriTotalReceived,PriTotalGranted,PriUnderProcess,PriRejected,
			appRegDeg2=[0,0,0,0,0,0,0,0,0,0,0,0],RegTotalReceived,RegTotalGranted,RegUnderProcess,RegRejected,
			appRegDeg3=[0,0,0,0,0,0,0,0,0,0,0,0],RenTotalReceived,RenTotalGranted,RenUnderProcess,RenRejected;
			$.each(JSON.parse(data[0]), function(index, item) {
				if(item.p1=='Hospitality'){
				HosTotalReceived=parseInt(item.p2)+parseInt(item.p3)+parseInt(item.p4);
				HosTotalGranted=parseInt(item.p2);
				HosUnderProcess=parseInt(item.p3);
				HosRejected=parseInt(item.p4);
				}
				if(item.p1=='Prior Permission'){
					PriTotalReceived=parseInt(item.p2)+parseInt(item.p3)+parseInt(item.p4);
					PriTotalGranted=parseInt(item.p2);
					PriUnderProcess=parseInt(item.p3);
					PriRejected=parseInt(item.p4);
					}
				if(item.p1=='Registration'){
					RegTotalReceived=parseInt(item.p2)+parseInt(item.p3)+parseInt(item.p4);
					RegTotalGranted=parseInt(item.p2);
					RegUnderProcess=parseInt(item.p3);
					RegRejected=parseInt(item.p4);
					}
				if(item.p1=='Renewal'){
					RenTotalReceived=parseInt(item.p2)+parseInt(item.p3)+parseInt(item.p4);
					RenTotalGranted=parseInt(item.p2);
					RenUnderProcess=parseInt(item.p3);
					RenRejected=parseInt(item.p4);
					}
		    	});
			
			getDashboardPieReg(RegTotalReceived,RegTotalGranted,RegUnderProcess,RegRejected);
			getDashboardPiePri(PriTotalReceived,PriTotalGranted,PriUnderProcess,PriRejected);
			getDashboardPieRen(RenTotalReceived,RenTotalGranted,RenUnderProcess,RenRejected);
			getDashboardPieHos(HosTotalReceived,HosTotalGranted,HosUnderProcess,HosRejected);
		},
		complete: function() { $('#loading').hide(); },
		error: function(textStatus,errorThrown){
		}
	});}
function getdashboardServiceStatus(){
	  $(".bgm-teal").addClass("active");
	  $(".bgm-orange").removeClass("active");
	  $(".bgm-lightgreen").removeClass("active");
	  $(".bgm-cyan").removeClass("active");
	  $("#barGraphDiv11").show();
		$('#barGraphDiv11').html('');
	var action = 'status-dash-board-service';
	var params = '';
	$.ajax({
		url: action,
		beforeSend: function() { $('#loading').show()},
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			var i=1,appStatYear=[],appStatPending=[],appStatApproved=[],appStatTotal=[],HosTotalReceived,HosTotalGranted,HosUnderProcess,HosRejected,
			appRegDeg1=[0,0,0,0,0,0,0,0,0,0,0,0],PriTotalReceived,PriTotalGranted,PriUnderProcess,PriRejected,
			appRegDeg2=[0,0,0,0,0,0,0,0,0,0,0,0],RegTotalReceived,RegTotalGranted,RegUnderProcess,RegRejected,
			appRegDeg3=[0,0,0,0,0,0,0,0,0,0,0,0],RenTotalReceived,RenTotalGranted,RenUnderProcess,RenRejected;
			$.each(JSON.parse(data[0]), function(index, item) {
				if(item.p1=='Hospitality'){
				HosTotalReceived=parseInt(item.p2)+parseInt(item.p3)+parseInt(item.p4);
				HosTotalGranted=parseInt(item.p2);
				HosUnderProcess=parseInt(item.p3);
				HosRejected=parseInt(item.p4);
				}
				if(item.p1=='Prior Permission'){
					PriTotalReceived=parseInt(item.p2)+parseInt(item.p3)+parseInt(item.p4);
					PriTotalGranted=parseInt(item.p2);
					PriUnderProcess=parseInt(item.p3);
					PriRejected=parseInt(item.p4);
					}
				if(item.p1=='Registration'){
					RegTotalReceived=parseInt(item.p2)+parseInt(item.p3)+parseInt(item.p4);
					RegTotalGranted=parseInt(item.p2);
					RegUnderProcess=parseInt(item.p3);
					RegRejected=parseInt(item.p4);
					}
				if(item.p1=='Renewal'){
					RenTotalReceived=parseInt(item.p2)+parseInt(item.p3)+parseInt(item.p4);
					RenTotalGranted=parseInt(item.p2);
					RenUnderProcess=parseInt(item.p3);
					RenRejected=parseInt(item.p4);
					}
				/*$('#tc-'+i).html(parseInt(item.p2)+parseInt(item.p3)+parseInt(item.p4));
				$('#gr-'+i).html(parseInt(item.p2));
				$('#pc-'+i).html(parseInt(item.p3));
				$('#cc-'+i).html(parseInt(item.p4));
				i++;*/
		    	});
			$.each(JSON.parse(data[1]), function(index, item) {
				if(item.p1=='range1'){
					appStatPending[0]= parseInt(item.p2);
					appStatPending[1]= parseInt(item.p3);
					appStatPending[2]= parseInt(item.p4);
					appStatPending[3]= parseInt(item.p5);
				}
				if(item.p1=='range2'){
					appStatApproved[0]= parseInt(item.p2);
					appStatApproved[1]= parseInt(item.p3);
					appStatApproved[2]= parseInt(item.p4);
					appStatApproved[3]= parseInt(item.p5);
				}
				if(item.p1=='range3'){
					appStatTotal[0]= parseInt(item.p2);
					appStatTotal[1]= parseInt(item.p3);
					appStatTotal[2]= parseInt(item.p4);
					appStatTotal[3]= parseInt(item.p5);
				}
		    	});
			$.each(JSON.parse(data[2]), function(index, item) {
				if(item.p2=='0'){
					if(item.p1=='Joint Secretary'){
						appRegDeg1[0]= parseInt(item.p3);
						//alert("appRegDeg1[0]"+appRegDeg1[0]);
					}
					if(item.p1=='Additional Secretary'){
						appRegDeg1[1]= parseInt(item.p3);
					}
					if(item.p1=='Under Secretary'){
						appRegDeg1[2]= parseInt(item.p3);
					}
					if(item.p1=='Deputy Secretary'){
						appRegDeg1[3]= parseInt(item.p3);
					}
					if(item.p1=='Director'){
						appRegDeg1[4]= parseInt(item.p3);
					}
					if(item.p1=='Deputy Director'){
						appRegDeg1[5]= parseInt(item.p3);
					}
					if(item.p1=='Joint Deputy Director'){
						appRegDeg1[6]= parseInt(item.p3);
					}
					if(item.p1=='Section Officer'){
						appRegDeg1[7]= parseInt(item.p3);
					}
					if(item.p1=='Assistant Section Officer'){
						appRegDeg1[8]= parseInt(item.p3);
					}
					if(item.p1=='Senior Secrectariat Assistant'){
						appRegDeg1[9]= parseInt(item.p3);
					}
					if(item.p1=='Personal Assistant'){
						appRegDeg1[10]= parseInt(item.p3);
					}
					if(item.p1=='null'){
						appRegDeg1[11]= parseInt(item.p3);
					}
				}
				if(item.p2=='1'){
					if(item.p1=='Joint Secretary'){
						appRegDeg2[0]= parseInt(item.p3);
					}
					if(item.p1=='Additional Secretary'){
						appRegDeg2[1]= parseInt(item.p3);
					}
					if(item.p1=='Under Secretary'){
						appRegDeg2[2]= parseInt(item.p3);
					}
					if(item.p1=='Deputy Secretary'){
						appRegDeg2[3]=parseInt(item.p3);
					}
					if(item.p1=='Director'){
						appRegDeg2[4]= parseInt(item.p3);
					}
					if(item.p1=='Deputy Director'){
						appRegDeg2[5]= parseInt(item.p3);
					}
					if(item.p1=='Joint Deputy Director'){
						appRegDeg2[6]= parseInt(item.p3);
					}
					if(item.p1=='Section Officer'){
						appRegDeg2[7]= parseInt(item.p3);
					}
					if(item.p1=='Assistant Section Officer'){
						appRegDeg2[8]= parseInt(item.p3);
					}
					if(item.p1=='Senior Secrectariat Assistant'){
						appRegDeg2[9]= parseInt(item.p3);
					}
					if(item.p1=='Personal Assistant'){
						appRegDeg2[10]= parseInt(item.p3);
					}
					if(item.p1=='null'){
						appRegDeg2[11]= parseInt(item.p3);
					}
				}
				if(item.p2=='2'){
					if(item.p1=='Joint Secretary'){
						appRegDeg3[0]= parseInt(item.p3);
						
					}
					if(item.p1=='Additional Secretary'){
						appRegDeg3[1]= parseInt(item.p3);
					}
					if(item.p1=='Under Secretary'){
						appRegDeg3[2]= parseInt(item.p3);
					}
					if(item.p1=='Deputy Secretary'){
						appRegDeg3[3]= parseInt(item.p3);
					}
					if(item.p1=='Director'){
						appRegDeg3[4]= parseInt(item.p3);
					}
					if(item.p1=='Deputy Director'){
						appRegDeg3[5]= parseInt(item.p3);
					}
					if(item.p1=='Joint Deputy Director'){
						appRegDeg3[6]= parseInt(item.p3);
					}
					if(item.p1=='Section Officer'){
						appRegDeg3[7]= parseInt(item.p3);
					}
					if(item.p1=='Assistant Section Officer'){
						appRegDeg3[8]= parseInt(item.p3);
					}
					if(item.p1=='Senior Secrectariat Assistant'){
						appRegDeg3[9]= parseInt(item.p3);
					}
					if(item.p1=='Personal Assistant'){
						appRegDeg3[10]= parseInt(item.p3);
					}
					if(item.p1=='null'){
						appRegDeg3[11]= parseInt(item.p3);
					}
				}
		    	});
		
		
			var ticks = ['MHA', 'IB', 'Cab.Sec', 'Applicant'];
			var ticks1 = ['JS','AS','US','DS','DIR','DD','JDD','SO','ASO','SSA','PA','Not Assigned'];
			createBarGraphRegistration1(ticks,appStatPending,appStatApproved,appStatTotal);
			createBarGraphRegistration2(ticks1,appRegDeg1,appRegDeg2,appRegDeg3);
		},
		complete: function() { $('#loading').hide(); },
		error: function(textStatus,errorThrown){
		}
	});}
function getDashboardPieReg(TotalReceived,TotalGranted,UnderProcess,Rejected) {
	$('#getDashboardPieReg').html('');
	var percTotalGranted =(TotalGranted*100/TotalReceived).toFixed(2);
	var percUnderProcess =(UnderProcess*100/TotalReceived).toFixed(2);
	var percRejected =(Rejected*100/TotalReceived).toFixed(2);
	    var config = {
	        type: 'pie',
	        data: {
	            datasets: [{
	                    data: [
	                    	percTotalGranted,percUnderProcess,percRejected
	                    ],
	                    backgroundColor: [
	                        'rgba(54, 162, 235, 1)',
	                        'rgba(165, 149, 149,1)',
	                        'rgba(220, 247, 133, 1)'],
	                    
	                }],
	            labels: [
	                'Granted '+ '('+TotalGranted+')',
	                'Under Process '+ '('+UnderProcess+')',
	                'Rejected/Closed '+ '('+Rejected+')'
	                
	            ]
	        },
	        options: {
	        	showDatasetLabels : true,
	            responsive: true,
	            legend: {
	            	display: true,
			           position: "top",
			           labels: {
			        	      fontColor: "#333",
			        	      fontSize: 18,
			        	      fontStyle:'bold',
					           fontColor:'black'
			        	    }
			       },
			       'onClick' : function (evt, item) {
	                     //  console.log ('legend onClick', evt);
	                     //  console.log('legd item', item);
	                       jQuery.each(
	                    		   item,
	       						function(i, val) {
	       							
	       							//alert(val._index);
	       							
	       								getdashboardServiceStatus();
	       							
	       							
	       						});
	                       var gss=item.val;
	            //           var data1 = JSON.parse(item);
	                       
	                      //alert("Ssssss"+gss);
	                   },
	            title: {
			           display: true,
			           text: "Registration (Total Application Received :"+TotalReceived+')',
			           fontSize:24,
			           fontStyle:'bold',
			           fontColor:'black'
			       },
			       animation: {
			    	      animateScale: true,
			    	      animateRotate: false
			    	    },
	            plugins: {
	                labels: {
	                    render: "ssss"+'value'+'%gfh',
	                    overlap: true,
	                    fontColor: '#000000',
	                    arc: false,
	                    position: 'outside',
	                    textMargin: 5,
	                    fontSize:30,
	                    outsidePadding: 5,
	                    anchor: 'start',
	                    align: 'start',
	                    fontStyle:'bold',
	                    offset: 1
	                },
	                
			       formatter:function(value,ctx){
			    	 return ctx.chart.data.labels[ctx.dataIndex]+'\n'+value+'%' ;  
			       }
	            },
			    	    layout: {
			                padding:{
			                    left: 30,
			                    right: 0,
			                    top: 0,
			                    bottom: 0,
			                    fontSize:30,
			                }
			            } 
	        
	        }
	    };
	    var ctx = document.getElementById('getDashboardPieReg').getContext('2d');
	    var mypiechart = new Chart(ctx, config);

	}
function getDashboardPiePri(TotalReceived,TotalGranted,UnderProcess,Rejected) {
	var percTotalGranted =(TotalGranted*100/TotalReceived).toFixed(2);
	var percUnderProcess =(UnderProcess*100/TotalReceived).toFixed(2);
	var percRejected =(Rejected*100/TotalReceived).toFixed(2);
	    var config = {
	        type: 'pie',
	        data: {
	            datasets: [{
	                    data: [
	                    	percTotalGranted,percUnderProcess,percRejected
	                    ],
	                    backgroundColor: [
	                        'rgba(54, 162, 235, 1)',
	                        'rgba(165, 149, 149,1)',
	                        'rgba(220, 247, 133, 1)'],
	                    label: 'Dataset 1'
	                }],
	            labels: [
	            	  'Granted '+ '('+TotalGranted+')',
		                'Under Process '+ '('+UnderProcess+')',
		                'Rejected/Closed '+ '('+Rejected+')'
	            ]
	        },
	        options: {
	            responsive: true,
	            legend: {
			           position: "top",
			           labels: {
			        	      fontColor: "#333",
			        	      fontSize: 18,
			        	      fontStyle:'bold',
					           fontColor:'black'
			        	    }
			       },
			       'onClick' : function (evt, item) {
	                     //  console.log ('legend onClick', evt);
	                     //  console.log('legd item', item);
	                       jQuery.each(
	                    		   item,
	       						function(i, val) {
	       							
	       							//alert(val._index);
	       							
	       								getdashboardPriorPermissionServiceStatus();
	       							
	       							
	       						});
	                       var gss=item.val;
	            //           var data1 = JSON.parse(item);
	                       
	                      //alert("Ssssss"+gss);
	                   },
	            title: {
			           display: true,
			           text: "Prior Permission (Total Application Received :"+TotalReceived+')',
			           fontSize:24,
			           fontStyle:'bold',
			           fontColor:'black'
			       },
	            plugins: {
	                labels: {
	                    render: 'value'+'%',
	                    overlap: true,
	                    fontColor: '#000000',
	                    arc: false,
	                    position: 'outside',
	                    textMargin: 5,
	                    outsidePadding: 5,
	                    anchor: 'end',
	                    fontSize:30,
	                    fontStyle:'bold',
	                    align: 'end',
	                    offset: 10
	                }
	            }
	        }
	    };

	    var ctx = document.getElementById('getDashboardPiePri').getContext('2d');
	    var mypiechart = new Chart(ctx, config);

	}
function getDashboardPieRen(TotalReceived,TotalGranted,UnderProcess,Rejected) {
	//alert("ren"+TotalReceived);
	var percTotalGranted =(TotalGranted*100/TotalReceived).toFixed(2);
	var percUnderProcess =(UnderProcess*100/TotalReceived).toFixed(2);
	var percRejected =(Rejected*100/TotalReceived).toFixed(2);
	    var config = {
	        type: 'pie',
	        data: {
	            datasets: [{
	                    data: [
	                    	percTotalGranted,percUnderProcess,percRejected
	                    ],
	                    backgroundColor: [
	                        'rgba(54, 162, 235, 1)',
	                        'rgba(165, 149, 149,1)',
	                        'rgba(220, 247, 133, 1)'],
	                    label: 'Dataset 1'
	                }],
	            labels: [
	            	
	            	  'Granted '+ '('+TotalGranted+')',
		                'Under Process '+ '('+UnderProcess+')',
		                'Rejected/Closed '+ '('+Rejected+')'
	            ]
	        },
	        options: {
	            responsive: true,
	            legend: {
			           position: "top",
			           labels: {
			        	      fontColor: "#333",
			        	      fontSize: 18,
			        	      fontStyle:'bold',
					           fontColor:'black'
			        	    }
			       },
			       'onClick' : function (evt, item) {
                     //  console.log ('legend onClick', evt);
                     //  console.log('legd item', item);
                       jQuery.each(
                    		   item,
       						function(i, val) {
       							
       							//alert(val._index);
       						
       								getdashboardRenewalServiceStatus();
       						
       							
       						});
                       var gss=item.val;
            //           var data1 = JSON.parse(item);
                       
                      //alert("Ssssss"+gss);
                   },
	            title: {
			           display: true,
			           text: "Renewal (Total Application Received :"+TotalReceived+')',
			           fontSize:24,
			           fontStyle:'bold',
			           fontColor:'black'
			       },
	            plugins: {
	                labels: {
	                    render: 'value'+'%',
	                    overlap: true,
	                    fontColor: '#000000',
	                    arc: false,
	                    position: 'outside',
	                    textMargin: 10,
	                    outsidePadding: 10,
	                    anchor: 'end',
	                    fontStyle:'bold',
	                    fontSize:30,
	                    align: 'end',
	                    offset: 10
	                }
	            }
	        }
	    };
	    var ctx = document.getElementById('getDashboardPieRen').getContext('2d');
	    var mypiechart = new Chart(ctx, config);

	}
function getDashboardPieHos(TotalReceived,TotalGranted,UnderProcess,Rejected) {
	//alert("ren");
	var percTotalGranted =(TotalGranted*100/TotalReceived).toFixed(2);
	var percUnderProcess =(UnderProcess*100/TotalReceived).toFixed(2);
	var percRejected =(Rejected*100/TotalReceived).toFixed(2);
	    var config = {
	        type: 'pie',
	        data: {
	            datasets: [{
	                    data: [
	                    	percTotalGranted,percUnderProcess,percRejected
	                    ],
	                    backgroundColor: [
	                    	 'rgba(54, 162, 235, 1)',
		                        'rgba(165, 149, 149,1)',
		                        'rgba(220, 247, 133, 1)'],
	                    label: 'Dataset 1'
	                }],
	            labels: [
	            	  'Granted '+ '('+TotalGranted+')',
		                'Under Process '+ '('+UnderProcess+')',
		                'Rejected/Closed '+ '('+Rejected+')'
	            ]
	        },
	        options: {
	            responsive: true,
	            tooltips: {
					enabled: true,
				},
	            legend: {
			           position: "top",
			           labels: {
			        	      fontColor: "#333",
			        	      fontSize: 18,
			        	      fontStyle:'bold',
					           fontColor:'black'
			        	    }
			       },
			       'onClick' : function (evt, item) {
	                     //  console.log ('legend onClick', evt);
	                     //  console.log('legd item', item);
	                       jQuery.each(
	                    		   item,
	       						function(i, val) {
	       							
	       							//alert(val._index);
	       							
	       								getdashboardHospitalityServiceStatus();
	       							
	       							
	       						});
	                       var gss=item.val;
	            //           var data1 = JSON.parse(item);
	                       
	                      //alert("Ssssss"+gss);
	                   },
	            title: {
			           display: true,
			           text: "Hospitality (Total Application Received :"+TotalReceived+')',
			           fontSize:24,
			           fontStyle:'bold',
			           fontColor:'black'
			       },
	            plugins: {
	            	labels: {
	                    render: 'value'+'%'+'',
	                    overlap: true,
	                    fontColor: '#000000',
	                    arc: false,
	                    position: 'outside',
	                    textMargin: 10,
	                    outsidePadding: 10,
	                    anchor: 'end',
	                    align: 'end',
	                    fontSize:30,
	                    fontStyle:'bold',
	                    offset: 10,
	                    segment: true
	                }
	            }
	        }
	    };
	    var ctx = document.getElementById('getDashboardPieHos').getContext('2d');
	    var mypiechart = new Chart(ctx, config);
	}
function getdashboardPriorPermissionServiceStatus(){
	 $(".bgm-teal").removeClass("active");
	  $(".bgm-orange").addClass("active");
	  $(".bgm-lightgreen").removeClass("active");
	  $(".bgm-cyan").removeClass("active");
	  $("#barGraphDiv11").show();
	  $('#barGraphDiv11').html('');
		var action = 'PriorPermission-dash-board-service';
		var params = '';
		$.ajax({
			url: action,
			beforeSend: function() { $('#loading').show()},
			method:'GET',
			data:params,
			dataType:'json',
			success: function(data){
				var i=1,appStatYear=[],appStatPending=[],appStatApproved=[],appStatTotal=[],
				appRegDeg1=[0,0,0,0,0,0,0,0,0,0,0,0],
				appRegDeg2=[0,0,0,0,0,0,0,0,0,0,0,0],
				appRegDeg3=[0,0,0,0,0,0,0,0,0,0,0,0];
				$.each(JSON.parse(data[0]), function(index, item) {
					if(item.p1=='range1'){
						appStatPending[0]= parseInt(item.p2);
						appStatPending[1]= parseInt(item.p3);
						appStatPending[2]= parseInt(item.p4);
						appStatPending[3]= parseInt(item.p5);
					}
					if(item.p1=='range2'){
						appStatApproved[0]= parseInt(item.p2);
						appStatApproved[1]= parseInt(item.p3);
						appStatApproved[2]= parseInt(item.p4);
						appStatApproved[3]= parseInt(item.p5);
					}
					if(item.p1=='range3'){
						appStatTotal[0]= parseInt(item.p2);
						appStatTotal[1]= parseInt(item.p3);
						appStatTotal[2]= parseInt(item.p4);
						appStatTotal[3]= parseInt(item.p5);
					}
			    	});
				$.each(JSON.parse(data[1]), function(index, item) {
					if(item.p2=='0'){
						if(item.p1=='Joint Secretary'){
							appRegDeg1[0]= parseInt(item.p3);
						}
						if(item.p1=='Additional Secretary'){
							appRegDeg1[1]= parseInt(item.p3);
						}
						if(item.p1=='Under Secretary'){
							appRegDeg1[2]= parseInt(item.p3);
						}
						if(item.p1=='Deputy Secretary'){
							appRegDeg1[3]= parseInt(item.p3);
						}
						if(item.p1=='Director'){
							appRegDeg1[4]= parseInt(item.p3);
						}
						if(item.p1=='Deputy Director'){
							appRegDeg1[5]= parseInt(item.p3);
						}
						if(item.p1=='Joint Deputy Director'){
							appRegDeg1[6]= parseInt(item.p3);
						}
						if(item.p1=='Section Officer'){
							appRegDeg1[7]= parseInt(item.p3);
						}
						if(item.p1=='Assistant Section Officer'){
							appRegDeg1[8]= parseInt(item.p3);
						}
						if(item.p1=='Senior Secrectariat Assistant'){
							appRegDeg1[9]= parseInt(item.p3);
						}
						if(item.p1=='Personal Assistant'){
							appRegDeg1[10]= parseInt(item.p3);
						}
						if(item.p1=='null'){
							appRegDeg1[11]= parseInt(item.p3);
						}
					}
					if(item.p2=='1'){
						if(item.p1=='Joint Secretary'){
							appRegDeg2[0]= parseInt(item.p3);
						}
						if(item.p1=='Additional Secretary'){
							appRegDeg2[1]= parseInt(item.p3);
						}
						if(item.p1=='Under Secretary'){
							appRegDeg2[2]= parseInt(item.p3);
						}
						if(item.p1=='Deputy Secretary'){
							appRegDeg2[3]=parseInt(item.p3);
						}
						if(item.p1=='Director'){
							appRegDeg2[4]= parseInt(item.p3);
						}
						if(item.p1=='Deputy Director'){
							appRegDeg2[5]= parseInt(item.p3);
						}
						if(item.p1=='Joint Deputy Director'){
							appRegDeg2[6]= parseInt(item.p3);
						}
						if(item.p1=='Section Officer'){
							appRegDeg2[7]= parseInt(item.p3);
						}
						if(item.p1=='Assistant Section Officer'){
							appRegDeg2[8]= parseInt(item.p3);
						}
						if(item.p1=='Senior Secrectariat Assistant'){
							appRegDeg2[9]= parseInt(item.p3);
						}
						if(item.p1=='Personal Assistant'){
							appRegDeg2[10]= parseInt(item.p3);
						}
						if(item.p1=='null'){
							appRegDeg2[11]= parseInt(item.p3);
						}
					}
					if(item.p2=='2'){
						if(item.p1=='Joint Secretary'){
							appRegDeg3[0]= parseInt(item.p3);
						}
						if(item.p1=='Additional Secretary'){
							appRegDeg3[1]= parseInt(item.p3);
						}
						if(item.p1=='Under Secretary'){
							appRegDeg3[2]= parseInt(item.p3);
						}
						if(item.p1=='Deputy Secretary'){
							appRegDeg3[3]= parseInt(item.p3);
						}
						if(item.p1=='Director'){
							appRegDeg3[4]= parseInt(item.p3);
						}
						if(item.p1=='Deputy Director'){
							appRegDeg3[5]= parseInt(item.p3);
						}
						if(item.p1=='Joint Deputy Director'){
							appRegDeg3[6]= parseInt(item.p3);
						}
						if(item.p1=='Section Officer'){
							appRegDeg3[7]= parseInt(item.p3);
						}
						if(item.p1=='Assistant Section Officer'){
							appRegDeg3[8]= parseInt(item.p3);
						}
						if(item.p1=='Senior Secrectariat Assistant'){
							appRegDeg3[9]= parseInt(item.p3);
						}
						if(item.p1=='Personal Assistant'){
							appRegDeg3[10]= parseInt(item.p3);
						}
						if(item.p1=='null'){
							appRegDeg3[11]= parseInt(item.p3);
						}
					}
			    	});
				createBarGraphPriorPermission1(appStatYear,appStatPending,appStatApproved,appStatTotal);
				createBarGraphPriorPermission2(appStatYear,appRegDeg1,appRegDeg2,appRegDeg3);
			},
			complete: function() { $('#loading').hide(); },
			error: function(textStatus,errorThrown){
			}
		});}
function getdashboardRenewalServiceStatus(){
	 $(".bgm-teal").removeClass("active");
	  $(".bgm-orange").removeClass("active");
	  $(".bgm-lightgreen").addClass("active");
	  $(".bgm-cyan").removeClass("active");
	  $("#barGraphDiv11").show();
	  $('#barGraphDiv11').html('');
	var action = 'Renewal-dash-board-service';
	var params = '';
	$.ajax({
		url: action,
		beforeSend: function() { $('#loading').show()},
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			var i=1,appStatYear=[],appStatPending=[],appStatApproved=[],appStatTotal=[],appRegDeg1=[0,0,0,0,0,0,0,0,0,0,0,0],
			appRegDeg2=[0,0,0,0,0,0,0,0,0,0,0,0],appRegDeg3=[0,0,0,0,0,0,0,0,0,0,0,0];
			$.each(JSON.parse(data[0]), function(index, item) {
				if(item.p1=='range1'){
					appStatPending[0]= parseInt(item.p2);
					appStatPending[1]= parseInt(item.p3);
					appStatPending[2]= parseInt(item.p4);
					appStatPending[3]= parseInt(item.p5);
				}
				if(item.p1=='range2'){
					appStatApproved[0]= parseInt(item.p2);
					appStatApproved[1]= parseInt(item.p3);
					appStatApproved[2]= parseInt(item.p4);
					appStatApproved[3]= parseInt(item.p5);
				}
				if(item.p1=='range3'){
					appStatTotal[0]= parseInt(item.p2);
					appStatTotal[1]= parseInt(item.p3);
					appStatTotal[2]= parseInt(item.p4);
					appStatTotal[3]= parseInt(item.p5);
				}
		    	});
			$.each(JSON.parse(data[1]), function(index, item) {
				if(item.p2=='0'){
					if(item.p1=='Joint Secretary'){
						appRegDeg1[0]= parseInt(item.p3);
					}
					if(item.p1=='Additional Secretary'){
						appRegDeg1[1]= parseInt(item.p3);
					}
					if(item.p1=='Under Secretary'){
						appRegDeg1[2]= parseInt(item.p3);
					}
					if(item.p1=='Deputy Secretary'){
						appRegDeg1[3]= parseInt(item.p3);
					}
					if(item.p1=='Director'){
						appRegDeg1[4]= parseInt(item.p3);
					}
					if(item.p1=='Deputy Director'){
						appRegDeg1[5]= parseInt(item.p3);
					}
					if(item.p1=='Joint Deputy Director'){
						appRegDeg1[6]= parseInt(item.p3);
					}
					if(item.p1=='Section Officer'){
						appRegDeg1[7]= parseInt(item.p3);
					}
					if(item.p1=='Assistant Section Officer'){
						appRegDeg1[8]= parseInt(item.p3);
					}
					if(item.p1=='Senior Secrectariat Assistant'){
						appRegDeg1[9]= parseInt(item.p3);
					}
					if(item.p1=='Personal Assistant'){
						appRegDeg1[10]= parseInt(item.p3);
					}
					if(item.p1=='null'){
						appRegDeg1[11]= parseInt(item.p3);
					}
				}
				if(item.p2=='1'){
					if(item.p1=='Joint Secretary'){
						appRegDeg2[0]= parseInt(item.p3);
					}
					if(item.p1=='Additional Secretary'){
						appRegDeg2[1]= parseInt(item.p3);
					}
					if(item.p1=='Under Secretary'){
						appRegDeg2[2]= parseInt(item.p3);
					}
					if(item.p1=='Deputy Secretary'){
						appRegDeg2[3]=parseInt(item.p3);
					}
					if(item.p1=='Director'){
						appRegDeg2[4]= parseInt(item.p3);
					}
					if(item.p1=='Deputy Director'){
						appRegDeg2[5]= parseInt(item.p3);
					}
					if(item.p1=='Joint Deputy Director'){
						appRegDeg2[6]= parseInt(item.p3);
					}
					if(item.p1=='Section Officer'){
						appRegDeg2[7]= parseInt(item.p3);
					}
					if(item.p1=='Assistant Section Officer'){
						appRegDeg2[8]= parseInt(item.p3);
					}
					if(item.p1=='Senior Secrectariat Assistant'){
						appRegDeg2[9]= parseInt(item.p3);
					}
					if(item.p1=='Personal Assistant'){
						appRegDeg2[10]= parseInt(item.p3);
					}
					if(item.p1=='null'){
						appRegDeg2[11]= parseInt(item.p3);
					}
				}
				if(item.p2=='2'){
					if(item.p1=='Joint Secretary'){
						appRegDeg3[0]= parseInt(item.p3);
					}
					if(item.p1=='Additional Secretary'){
						appRegDeg3[1]= parseInt(item.p3);
					}
					if(item.p1=='Under Secretary'){
						appRegDeg3[2]= parseInt(item.p3);
					}
					if(item.p1=='Deputy Secretary'){
						appRegDeg3[3]= parseInt(item.p3);
					}
					if(item.p1=='Director'){
						appRegDeg3[4]= parseInt(item.p3);
					}
					if(item.p1=='Deputy Director'){
						appRegDeg3[5]= parseInt(item.p3);
					}
					if(item.p1=='Joint Deputy Director'){
						appRegDeg3[6]= parseInt(item.p3);
					}
					if(item.p1=='Section Officer'){
						appRegDeg3[7]= parseInt(item.p3);
					}
					if(item.p1=='Assistant Section Officer'){
						appRegDeg3[8]= parseInt(item.p3);
					}
					if(item.p1=='Senior Secrectariat Assistant'){
						appRegDeg3[9]= parseInt(item.p3);
					}
					if(item.p1=='Personal Assistant'){
						appRegDeg3[10]= parseInt(item.p3);
					}
					if(item.p1=='null'){
						appRegDeg3[11]= parseInt(item.p3);
					}
				}
		    	});
			createBarGraphRenewal1(appStatYear,appStatPending,appStatApproved,appStatTotal);
			createBarGraphRenewal2(appStatYear,appRegDeg1,appRegDeg2,appRegDeg3);
		},
		complete: function() { $('#loading').hide(); },
		error: function(textStatus,errorThrown){
		}
	});}
function getdashboardHospitalityServiceStatus(){
	 $(".bgm-teal").removeClass("active");
	  $(".bgm-orange").removeClass("active");
	  $(".bgm-lightgreen").removeClass("active");
	  $(".bgm-cyan").addClass("active");
	  $("#barGraphDiv11").show();
	  $('#barGraphDiv11').html('');
	var action = 'Hospitality-dash-board-service';
	var params = '';
	$.ajax({
		url: action,
		beforeSend: function() { $('#loading').show()},
		method:'GET',
		data:params,
		dataType:'json',
		success: function(data){
			var i=1,appStatYear=[],appStatPending=[],appStatApproved=[],appStatTotal=[],appRegDeg1=[0,0,0,0,0,0,0,0,0,0,0,0],
			appRegDeg2=[0,0,0,0,0,0,0,0,0,0,0,0],appRegDeg3=[0,0,0,0,0,0,0,0,0,0,0,0];
			$.each(JSON.parse(data[0]), function(index, item) {
				if(item.p1=='range1'){
					appStatPending[0]= parseInt(item.p2);
					appStatPending[1]= parseInt(item.p3);
					appStatPending[2]= parseInt(item.p4);
					appStatPending[3]= parseInt(item.p5);
				}
				if(item.p1=='range2'){
					appStatApproved[0]= parseInt(item.p2);
					appStatApproved[1]= parseInt(item.p3);
					appStatApproved[2]= parseInt(item.p4);
					appStatApproved[3]= parseInt(item.p5);
				}
				if(item.p1=='range3'){
					appStatTotal[0]= parseInt(item.p2);
					appStatTotal[1]= parseInt(item.p3);
					appStatTotal[2]= parseInt(item.p4);
					appStatTotal[3]= parseInt(item.p5);
				}
		    	});
			$.each(JSON.parse(data[1]), function(index, item) {
				if(item.p2=='0'){
					if(item.p1=='Joint Secretary'){
						appRegDeg1[0]= parseInt(item.p3);
					}
					if(item.p1=='Additional Secretary'){
						appRegDeg1[1]= parseInt(item.p3);
					}
					if(item.p1=='Under Secretary'){
						appRegDeg1[2]= parseInt(item.p3);
					}
					if(item.p1=='Deputy Secretary'){
						appRegDeg1[3]= parseInt(item.p3);
					}
					if(item.p1=='Director'){
						appRegDeg1[4]= parseInt(item.p3);
					}
					if(item.p1=='Deputy Director'){
						appRegDeg1[5]= parseInt(item.p3);
					}
					if(item.p1=='Joint Deputy Director'){
						appRegDeg1[6]= parseInt(item.p3);
					}
					if(item.p1=='Section Officer'){
						appRegDeg1[7]= parseInt(item.p3);
					}
					if(item.p1=='Assistant Section Officer'){
						appRegDeg1[8]= parseInt(item.p3);
					}
					if(item.p1=='Senior Secrectariat Assistant'){
						appRegDeg1[9]= parseInt(item.p3);
					}
					if(item.p1=='Personal Assistant'){
						appRegDeg1[10]= parseInt(item.p3);
					}
					if(item.p1=='null'){
						appRegDeg1[11]= parseInt(item.p3);
					}
				}
				if(item.p2=='1'){
					if(item.p1=='Joint Secretary'){
						appRegDeg2[0]= parseInt(item.p3);
					}
					if(item.p1=='Additional Secretary'){
						appRegDeg2[1]= parseInt(item.p3);
					}
					if(item.p1=='Under Secretary'){
						appRegDeg2[2]= parseInt(item.p3);
					}
					if(item.p1=='Deputy Secretary'){
						appRegDeg2[3]=parseInt(item.p3);
					}
					if(item.p1=='Director'){
						appRegDeg2[4]= parseInt(item.p3);
					}
					if(item.p1=='Deputy Director'){
						appRegDeg2[5]= parseInt(item.p3);
					}
					if(item.p1=='Joint Deputy Director'){
						appRegDeg2[6]= parseInt(item.p3);
					}
					if(item.p1=='Section Officer'){
						appRegDeg2[7]= parseInt(item.p3);
					}
					if(item.p1=='Assistant Section Officer'){
						appRegDeg2[8]= parseInt(item.p3);
					}
					if(item.p1=='Senior Secrectariat Assistant'){
						appRegDeg2[9]= parseInt(item.p3);
					}
					if(item.p1=='Personal Assistant'){
						appRegDeg2[10]= parseInt(item.p3);
					}
					if(item.p1=='null'){
						appRegDeg2[11]= parseInt(item.p3);
					}
				}
				if(item.p2=='2'){
					if(item.p1=='Joint Secretary'){
						appRegDeg3[0]= parseInt(item.p3);
					}
					if(item.p1=='Additional Secretary'){
						appRegDeg3[1]= parseInt(item.p3);
					}
					if(item.p1=='Under Secretary'){
						appRegDeg3[2]= parseInt(item.p3);
					}
					if(item.p1=='Deputy Secretary'){
						appRegDeg3[3]= parseInt(item.p3);
					}
					if(item.p1=='Director'){
						appRegDeg3[4]= parseInt(item.p3);
					}
					if(item.p1=='Deputy Director'){
						appRegDeg3[5]= parseInt(item.p3);
					}
					if(item.p1=='Joint Deputy Director'){
						appRegDeg3[6]= parseInt(item.p3);
					}
					if(item.p1=='Section Officer'){
						appRegDeg3[7]= parseInt(item.p3);
					}
					if(item.p1=='Assistant Section Officer'){
						appRegDeg3[8]= parseInt(item.p3);
					}
					if(item.p1=='Senior Secrectariat Assistant'){
						appRegDeg3[9]= parseInt(item.p3);
					}
					if(item.p1=='Personal Assistant'){
						appRegDeg3[10]= parseInt(item.p3);
					}
					if(item.p1=='null'){
						appRegDeg3[11]= parseInt(item.p3);
					}
				}
		    	});
		
			createBarGraphHospitality1(appStatYear,appStatPending,appStatApproved,appStatTotal);
			createBarGraphHospitality2(appStatYear,appRegDeg1,appRegDeg2,appRegDeg3);
		},
		complete: function() { $('#loading').hide(); },
		error: function(textStatus,errorThrown){
		}
	});}
function createBarGraphRegistration1(mylabel,a1,a2,a3){
	$("#graphApplicationwise").show();
	$("#graphApplicationwise1").show();
	$("#barGraphDiv11").show();
	$('#graphApplicationwise').html('');
	$('#graphApplicationwise1').html('');
	$("#createBarGraphPriorPermission1").hide();
	$("#createBarGraphPriorPermission2").hide();
	$("#createBarGraphRenewal1").hide();
	$("#createBarGraphRenewal2").hide();
	$("#createBarGraphHospitality1").hide();
	$("#createBarGraphHospitality2").hide();
	var mylabel = ['MHA', 'IB', 'Cab.Sec', 'Applicant'];
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
		    		   label:'30 to 90 days',
		    		   backgroundColor:'#3c88c9',
		    		   borderColor:'rgba(214, 237, 45, 1 )',
		    		   borderWidth:1
		    	   }, {
		    		   data:a3,
		    		   label:'More than 90 days',
		    		   backgroundColor:'#da5a56',
		    		   borderColor:'rgba(214, 237, 45, 1 )',
		    		   borderWidth:1
		    	   }
		    	   
		       ]

		   };

		   var chartOptions = {
		       responsive: true,
		       legend: {
		           position: "bottom"
		       },
		       title: {
		           display: true,
		           text: "Pending Registration Applications Statistics - Organization Wise"
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
		               arc: false,
		               fontStyle: "bold"
		           }
		       }
		   }
		   
		   var ctx = document.getElementById("graphApplicationwise").getContext("2d");
		   Chart.Legend.prototype.afterFit = function () {
		       this.height = this.height + 20;
		   };
		   var myBarChart = new Chart(ctx, {
		       type: 'bar',
		       data: barChartData,
		       options: chartOptions
		   });
		}
	function createBarGraphPriorPermission1(ticks,a1,a2,a3){
		$("#graphApplicationwise").hide();
		$("#graphApplicationwise1").hide();
		$("#createBarGraphPriorPermission1").show();
		$("#createBarGraphPriorPermission2").show();
		$('#createBarGraphPriorPermission1').html('');
		$('#createBarGraphPriorPermission2').html('');
		$("#createBarGraphRenewal1").hide();
		$("#createBarGraphRenewal2").hide();
		$("#createBarGraphHospitality1").hide();
		$("#createBarGraphHospitality2").hide();
		var mylabel = ['MHA', 'IB', 'Cab.Sec', 'Applicant'];
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
			    		   label:'30 to 90 days',
			    		   backgroundColor:'#3c88c9',
			    		   borderColor:'rgba(214, 237, 45, 1 )',
			    		   borderWidth:1
			    	   }, {
			    		   data:a3,
			    		   label:'More than 90 days',
			    		   backgroundColor:'#da5a56',
			    		   borderColor:'rgba(214, 237, 45, 1 )',
			    		   borderWidth:1
			    	   }
			    	   
			       ]

			   };

			   var chartOptions = {
			       responsive: true,
			       legend: {
			           position: "bottom"
			       },
			       title: {
			           display: true,
			           text: "Pending Prior Permission Applications Statistics - Organization Wise",
			           //Position:'bottom'
			       },
			       scales: {
			           yAxes: [{
			                   ticks: {
			                       beginAtZero: true
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
			               arc: false,
			               fontStyle: "bold"
			           }
			       }
			   }
			   var ctx = document.getElementById("createBarGraphPriorPermission1").getContext("2d");
			   Chart.Legend.prototype.afterFit = function () {
			       this.height = this.height + 20;
			   };
			   var myBarChart = new Chart(ctx, {
			       type: 'bar',
			       data: barChartData,
			       options: chartOptions
			   });
			}
	function createBarGraphRenewal1(ticks,a1,a2,a3){
		$("#graphApplicationwise").hide();
		$("#graphApplicationwise1").hide();
		$("#createBarGraphPriorPermission1").hide();
		$("#createBarGraphPriorPermission2").hide();
		$("#createBarGraphRenewal1").show();
		$("#createBarGraphRenewal2").show();
		$('#createBarGraphRenewal1').html('');
		$('#createBarGraphRenewal2').html('');
		$("#createBarGraphHospitality1").hide();
		$("#createBarGraphHospitality2").hide();
		var mylabel = ['MHA', 'IB', 'Cab.Sec', 'Applicant'];
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
			    		   label:'30 to 90 days',
			    		   backgroundColor:'#3c88c9',
			    		   borderColor:'rgba(214, 237, 45, 1 )',
			    		   borderWidth:1
			    	   }, {
			    		   data:a3,
			    		   label:'More than 90 days',
			    		   backgroundColor:'#da5a56',
			    		   borderColor:'rgba(214, 237, 45, 1 )',
			    		   borderWidth:1
			    	   }
			    	   
			       ]

			   };
			   var chartOptions = {
			       responsive: true,
			       legend: {
			           position: "bottom"
			       },
			       title: {
			           display: true,
			           text: "Pending Renewal Applications Statistics - Organization Wise"
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
			               arc: false,
			               fontStyle: "bold"
			           }
			       }
			   }
			   var ctx = document.getElementById("createBarGraphRenewal1").getContext("2d");
			   Chart.Legend.prototype.afterFit = function () {
			       this.height = this.height + 20;
			   };
			   var myBarChart = new Chart(ctx, {
			       type: 'bar',
			       data: barChartData,
			       options: chartOptions
			   });

	}
	function createBarGraphHospitality1(ticks,a1,a2,a3){
		$("#graphApplicationwise").hide();
		$("#graphApplicationwise1").hide();
		$("#createBarGraphPriorPermission1").hide();
		$("#createBarGraphPriorPermission2").hide();
		$("#createBarGraphRenewal1").hide();
		$("#createBarGraphRenewal2").hide();
		$('#createBarGraphHospitality1').html('');
		$('#createBarGraphHospitality2').html('');
		$("#createBarGraphHospitality1").show();
		$("#createBarGraphHospitality2").show();
			var mylabel = ['MHA', 'IB', 'Cab.Sec', 'Applicant'];
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
				    		   label:'30 to 90 days',
				    		   backgroundColor:'#3c88c9',
				    		   borderColor:'rgba(214, 237, 45, 1 )',
				    		   borderWidth:1
				    	   }, {
				    		   data:a3,
				    		   label:'More than 90 days',
				    		   backgroundColor:'#da5a56',
				    		   borderColor:'rgba(214, 237, 45, 1 )',
				    		   borderWidth:1
				    	   }
				    	   
				       ]

				   };
				   var chartOptions = {
				       responsive: true,
				       legend: {
				           position: "bottom"
				       },
				       title: {
				           display: true,
				           text: "Pending Hospitality Applications Statistics - Organization Wise"
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
				               arc: false,
				               fontStyle: "bold"
				           }
				       }
				   }
				   var ctx = document.getElementById("createBarGraphHospitality1").getContext("2d");
				   Chart.Legend.prototype.afterFit = function () {
				       this.height = this.height + 20;
				   };
				   var myBarChart = new Chart(ctx, {
				       type: 'bar',
				       data: barChartData,
				       options: chartOptions
				   });
		}
	function createBarGraphRegistration2(ticks,a1,a2,a3){
		var mylabel = ['JS','AS','US','DS','DIR','DD','JDD','SO','ASO','SSA','PA','Not Assigned'];
		   var ctx = document.getElementById("graphApplicationwise1");
	       var chart1 = new Chart(ctx, {
	           type: 'bar',
	           data: {
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
			    		   label:'30 to 90 days',
			    		   backgroundColor:'#3c88c9',
			    		   borderColor:'rgba(214, 237, 45, 1 )',
			    		   borderWidth:1
			    	   }, {
			    		   data:a3,
			    		   label:'More than 90 days',
			    		   backgroundColor:'#da5a56',
			    		   borderColor:'rgba(214, 237, 45, 1 )',
			    		   borderWidth:1
			    	   }
			    	   
			       ]
	           },
	           options:{
			       responsive: true,
			       legend: {
			           position: "bottom"
			       },
			       title: {
			           display: true,
			           text: "Pending Registration Applications Statistics - Designation Wise"
			       },
			       scales: {
			           yAxes: [{
			        	   display: true,
			                   ticks: {
			                       beginAtZero: true,
			                       steps: 200,
			                      // stepSize: 250,
	                                stepValue: 700
	                                
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
			               arc: true,
			               fontStyle: "bold"
			           }
			       }
			   }
	       });
	       ctx.addEventListener('click', function (evt) {
	           var activePoints = chart1.getElementsAtEvent(evt);
	           var activeDataSet = chart1.getDatasetAtEvent(evt);
	          // alert("activePoints"+activePoints);
	          // alert("activeDataSet"+activeDataSet);
	           if (activePoints.length > 0)
	           {
	               var clickedDatasetIndex = activeDataSet[0]._datasetIndex;
	               //alert("clickedDatasetIndex"+clickedDatasetIndex);
	               var clickedElementIndex = activePoints[0]._index;
	               var DegCode = mylabel[clickedElementIndex];
	               var ServiceId="01";
	               var value = chart1.data.datasets[clickedDatasetIndex].data[clickedElementIndex];
	              // alert("value"+value);
	               var params="ServiceId="+ServiceId+"&DegCode="+DegCode+"&clickedDatasetIndex="+clickedDatasetIndex;
	          //    alert(params);
	               var title ='List of Pending Application Of Registration';
	               showReportPendencyReport(params,title);
	           }
	       }, false);
			}
	function createBarGraphPriorPermission2(ticks,a1,a2,a3){
		var mylabel = ['JS','AS','US','DS','DIR','DD','JDD','SO','ASO','SSA','PA','Not Assigned'];
		   var ctx = document.getElementById("createBarGraphPriorPermission2");
	       var chart1 = new Chart(ctx, {
	           type: 'bar',
	           data: {
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
			    		   label:'30 to 90 days',
			    		   backgroundColor:'#3c88c9',
			    		   borderColor:'rgba(214, 237, 45, 1 )',
			    		   borderWidth:1
			    	   }, {
			    		   data:a3,
			    		   label:'More than 90 days',
			    		   backgroundColor:'#da5a56',
			    		   borderColor:'rgba(214, 237, 45, 1 )',
			    		   borderWidth:1
			    	   }
			    	   
			       ]
	           },
	           options:{
			       responsive: true,
			       legend: {
			           position: "bottom"
			       },
			       title: {
			           display: true,
			           text: "Pending Prior Permission Applications Statistics - Designation Wise"
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
			               arc: false,
			               fontStyle: "bold"
			           }
			       }
			   }
	       });
	       ctx.addEventListener('click', function (evt) {
	           var activePoints = chart1.getElementsAtEvent(evt);
	           var activeDataSet = chart1.getDatasetAtEvent(evt);
	           if (activePoints.length > 0)
	           {
	               var clickedDatasetIndex = activeDataSet[0]._datasetIndex;
	               var clickedElementIndex = activePoints[0]._index;
	               var DegCode = mylabel[clickedElementIndex];
	               var ServiceId="02";
	               var value = chart1.data.datasets[clickedDatasetIndex].data[clickedElementIndex];
	               var params="ServiceId="+ServiceId+"&DegCode="+DegCode+"&clickedDatasetIndex="+clickedDatasetIndex;
		            //  alert(params);
	               var title ='List of Pending Application Of Prior permission';
	               showReportPendencyReport(params,title);
	           }
	       }, false);
			}
	function createBarGraphHospitality2(ticks,a1,a2,a3){
		var mylabel = ['JS','AS','US','DS','DIR','DD','JDD','SO','ASO','SSA','PA','Not Assigned'];
		   var ctx = document.getElementById("createBarGraphHospitality2");
	       var chart1 = new Chart(ctx, {
	           type: 'bar',
	           data: {
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
			    		   label:'30 to 90 days',
			    		   backgroundColor:'#3c88c9',
			    		   borderColor:'rgba(214, 237, 45, 1 )',
			    		   borderWidth:1
			    	   }, {
			    		   data:a3,
			    		   label:'More than 90 days',
			    		   backgroundColor:'#da5a56',
			    		   borderColor:'rgba(214, 237, 45, 1 )',
			    		   borderWidth:1
			    	   }
			    	   
			       ]
	           },
	           options:{
			       responsive: true,
			       legend: {
			           position: "bottom"
			       },
			       title: {
			           display: true,
			           text: "Pending Hospitality Applications Statistics - Designation Wise"
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
			               arc: false,
			               fontStyle: "bold"
			           }
			       }
			   }
	       });
	       ctx.addEventListener('click', function (evt) {
	           var activePoints = chart1.getElementsAtEvent(evt);
	           var activeDataSet = chart1.getDatasetAtEvent(evt);
	           if (activePoints.length > 0)
	           {
	               var clickedDatasetIndex = activeDataSet[0]._datasetIndex;
	               var clickedElementIndex = activePoints[0]._index;
	               var DegCode = mylabel[clickedElementIndex];
	               var ServiceId="07";
	               var value = chart1.data.datasets[clickedDatasetIndex].data[clickedElementIndex];
	               var params="ServiceId="+ServiceId+"&DegCode="+DegCode+"&clickedDatasetIndex="+clickedDatasetIndex;
	               var title ='List of Pending Application Of Hospitality';
	               showReportPendencyReport(params,title);
	           }
	       }, false);
			}
	function createBarGraphRenewal2(ticks,a1,a2,a3){
		var mylabel = ['JS','AS','US','DS','DIR','DD','JDD','SO','ASO','SSA','PA','Not Assigned'];
		   var ctx = document.getElementById("createBarGraphRenewal2");
	       var chart1 = new Chart(ctx, {
	           type: 'bar',
	           data: {
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
			    		   label:'30 to 90 days',
			    		   backgroundColor:'#3c88c9',
			    		   borderColor:'rgba(214, 237, 45, 1 )',
			    		   borderWidth:1
			    	   }, {
			    		   data:a3,
			    		   label:'More than 90 days',
			    		   backgroundColor:'#da5a56',
			    		   borderColor:'rgba(214, 237, 45, 1 )',
			    		   borderWidth:1
			    	   }
			    	   
			       ]
	           },
	           options:{
			       responsive: true,
			       legend: {
			           position: "bottom"
			       },
			       title: {
			           display: true,
			           text: "Pending Renewal Applications Statistics - Designation Wise"
			       },
			       scales: {
			    	   pointLabels :{
			               fontStyle: "bold",
			               fontColor: '#000000',
			            }
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
			               arc: false,
			               fontStyle: "bold"
			           }
			       }
			   }
	       });
	       ctx.addEventListener('click', function (evt) {
	           var activePoints = chart1.getElementsAtEvent(evt);
	           var activeDataSet = chart1.getDatasetAtEvent(evt);
	           if (activePoints.length > 0)
	           {
	               var clickedDatasetIndex = activeDataSet[0]._datasetIndex;
	               var clickedElementIndex = activePoints[0]._index;
	               var DegCode = mylabel[clickedElementIndex];
	               var ServiceId="03";
	               var value = chart1.data.datasets[clickedDatasetIndex].data[clickedElementIndex];
	               var params="ServiceId="+ServiceId+"&DegCode="+DegCode+"&clickedDatasetIndex="+clickedDatasetIndex;
	          var title ='List of Pending Application Of Renewal';
	          showReportPendencyReport(params,title);
	           }
	       }, false);
			}

function showReportPendencyReport(params,title) {
	$("#barGraphDiv11").html('');

			$("#barGraphDiv11").bootgrid(
							{
								title : title,
								recordsinpage : '10',
								dataobject : 'propertyList',
								dataurl : 'show-pendency-reg-dash-board-service?' + params,
								columndetails : [

									{
										title : 'Application Id',
										name : 'applicationId'
									},
									{
										title : 'Applicant Name',
										name : 'applicantName',
									},
									
									{
										title : 'Submission Date',
										name : 'serviceYear'
									},
									{
										title : 'Service',
										name : 'submissionDate',
									},
									{
										title : 'User',
										name : 'serviceDesc'
									},
									{
										title : 'Designation',
										name : 'mhaData',
									},
									{
										title : 'Number of Days Since Application Pending',
										name : 'ibData',
									},
										],
							});$('#barGraphDiv11').on('click', 'tbody td', function() {

								  //get textContent of the TD
								//alert("TD cell textContent : "+ this.td);
								getApplicationDetails11(this.textContent);
								  //get the value of the TD using the API 
								// alert("value by API : "+ table.cell({ row: this.parentNode.rowIndex, column : this.cellIndex }).data());
								})
			
	}

function getApplicationDetails11(applicationId) {
	//alert("DDDDDDD");
		var action = 'pull-details-application-tracking';
		//var params = 'applicationId=' +applicationId ;
		var params = 'applicationId=060000022097' ;
	//	alert("params "+params);
		/*var aa = $('#applicationId').val();
		$('#application-id').val(aa);*/
		$.ajax({
			url : action,
			method : 'GET',
			data : params,
			dataType : 'json',

			success : function(data) {
				var status = JSON.parse(data[0]).li;
				var notificationList = JSON.parse(data[1]);
				var applicationDetails = JSON.parse(data[2]);
				var smsDetails = JSON.parse(data[3]);
				var mailDetails =JSON.parse(data[4]);
		        //alert(JSON.stringify(mailDetails));
				//alert(JSON.stringify(data[2]));
						
			/*	$('#application-details').empty();*/
				showNotifications(notificationList);
			//	$("#application-details11").show();
				$('#myModal').modal('show');
				if(status == 'error') {
					//alert("status"+status);
				}
				else {
				//	alert("AAAAAAAA");
					populateForm(applicationDetails,smsDetails,mailDetails);
				}
			},
			error : function(textStatus, errorThrown) {
			}
		});

	}


