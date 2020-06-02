<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/include.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<script type="text/javascript">
		
	</script>
	<script type="text/javascript" src="resources/js/jquery-3.4.1.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery-ui.js"></script>
	<!-- <script type="text/javascript" src="resources/js/auth/home/jquery.home.layout.js"></script> -->
	<script type="text/javascript" src="resources/js/all.js"></script>
	<script type="text/javascript" src="resources/js/stop-watch.js"></script>
	<script type="text/javascript" src="resources/js/auth/home/actions.js"></script>
	<script type="text/javascript" src="resources/js/auth/home/user-notify.js"></script>
	<script type="text/javascript" src="resources/js/bootstrap/all/bootstrap.js"></script>
	<script type="text/javascript" src="resources/js/accordian/accordian.js"></script>
	<script src="resources/js/iframeResizer.min.js" type="text/javascript"></script>
	<!-- <link rel="stylesheet" type="text/css" href="resources/css/auth/home/user-notify.css"> -->
	<link rel="stylesheet" type="text/css" href="resources/css/auth/home/header.css">
	
	<link rel="stylesheet" type="text/css" href="resources/css/accordian/accordian.css">
	<link rel="stylesheet" href="resources/font-awesome-4.3.0/css/font-awesome.min.css" />	
	<link rel="stylesheet" type="text/css" href="resources/css/auth/home/home.css">
	<link rel="stylesheet" href="resources/css/bootstrap/all/bootstrap.css" type="text/css" />
	<link href="resources/css/bootstrap/all/menu.css"  media="screen" rel="stylesheet" type="text/css" />
	
	<script type="text/javascript">
		var menuString = '';
		var proceed = "";
		var userId=null;
		var userName=null;
		var userDesignation=null;
		$(document).ready(function(){
			userId="${userId}";	
			userName="${userName}";			
			userDesignation="${userDesignation}";
//			menuString = "${menuString}";
			//menuString = "<c:out value='${menuString}'/>";
			//alert(menuString.replace('&lt;','<').replace('&gt;', '>').replace('&#039;', '\''));
			//$("#nav-drawer nav").html(menuString.replace('&lt;','<').replace('&gt;', '>').replace('&#039;', '\''));
			 
			//generateMenu("${menuList}", $("#nav-drawer nav"));
			//alert($("#nav-drawer nav").html());
			if(window.name=="body-frame"){
				parent.window.location.href=parent.window.location.href;
			}
			proceed="${proceed}";/*'<s:property value="proceed"/>'*/
			var istL=''/*'<s:property value="istDateTimeL"/>'*/;
			var myL=''/*'<s:property value="myDateTimeL"/>'*/;
			//loadMenu();
			window.setTimeout('loadMenu()',1000);
		});	
		
		function loadMenu(){
	        $(function() {
	            var menu_ul = $('#nav-drawer nav > ul > li > ul'),
	                   menu_a  = $('#nav-drawer nav > ul > li > a');
	            
	            menu_ul.hide();
	        
	            menu_a.click(function(e) {
	                e.preventDefault();
	                if(!$(this).parent().hasClass('active')) {
	                    menu_a.parent().removeClass('active');
	                    menu_ul.filter(':visible').slideUp('normal');
	                    $(this).parent().addClass('active');
	                    $(this).next().stop(true,true).slideDown('normal');
	                } else {
	                    $(this).parent().removeClass('active');
	                    $(this).next().stop(true,true).slideUp('normal');
	                }
	            });
	        });
	    };
		
		function generateMenu(menuList, divObj) {			
			divObj.html('');
			var tempPMenuList = $('<ui/>', {'class':'nav bs-docs-sidenav'});
			$.each(menuList, function(pIndex, pMenu){
				
				var tempPMenu = $('<li/>', {'class':'active'}).append($('<a/>', {text:pMenu.pmenuName}));
				var tempSMenuList = $('<ui/>', {'class':'nav'});
				$.each(pMenu.subMenuList, function(sIndex, sMenu){
					tempSMenuList.append($('<li/>', {}).append('<a/>', {id:sMenu.actionPath, onclick:'javascript:switchContent(\''+sMenu.actionPath+'\');', text:smenuName}));					
				});
				tempPMenu.append(tempSMenuList);
				tempPMenuList.append(tempPMenu);
			});
			divObj.append(tempPMenuList);
		}; 	
	</script>	
	<script type="text/javascript" src="resources/js/auth/home/menu.js"></script>
	<script type="text/javascript" src="resources/js/auth/home/main.js"></script>	
	<title>FCRA Portal</title>
</head>

<body id="">
		<div class="container-fluid">
			<div class="row" style="height:70px;" id="page-head">
			    <div class="col-xs-8" style="vertical-align:middle">
			    	<span><img src="resources/img/auth/home/ashoka-black.png" /></span> 	               
			    	<span style="padding-left:10px;"><img src="resources/img/auth/login/mha-logo.png" /></span>
			    	<span><img src="resources/img/auth/home/seprator.jpg" /></span>
			    	<span><img src="resources/img/auth/home/app-title.png" /></span>
			     	<!-- <span class="RBN">Estates Management Portal </span> -->
			    </div>
                <div class="col-xs-4 pull-right" style="vertical-align:middle; margin-top:25px;">
                	<span class="pull-right">
	                    <span class="glyphicon glyphicon-bell" style="font-size:20px;padding-right:10px;top:8px;"></span>  
	                	<span class="" id="user-icon" style="cursor:pointer;top:20px;" >
	                	<img src="resources/img/auth/home/uIcon.png" /></span>
                	</span>
                </div>
		    </div>
            <div class="row menu-bar" id="menu-bar"  style="line-height:50px;">
				<div class="col-xs-1" style="background-color:#346AC2;text-align:center; color:#FFFFFF;">
                    <span class="glyphicon glyphicon-menu-hamburger rotate" onclick="toggleDrawer();" style="padding-top:10px; padding-bottom:10px;font-size:30px; cursor:pointer;">
                    </span>
				</div>
                <div  class="col-xs-10" id="pageHeading" style="z-index:100;background-color:#4184f3; color:#FFF;box-shadow: 0 2px 5px rgba(0, 0, 0, 0.26); box-sizing: border-box;">
			    	<span style="font-size:24px;">Home Page</span>			    	
                </div>                   
			    <div class="col-xs-1" style="z-index:100;background-color:#4184f3; color:#FFF;">		
			    	<span class="glyphicon glyphicon-home pull-right"  style="padding-top:10px; padding-bottom:10px;font-size:30px; cursor:pointer;">
                    </span> 
                  <!--   onclick="goHome();" -->
			    </div>	    
		    </div>
            <div class="row" id="nav-drawer-row" style="">
            	<div class="col-xs-2 shadow" id="cssmenu" style="width:240px;padding-left:0px;position:fixed; top:120px; left:0; bottom:0; z-index:101;overflow-y:auto;">
		                <!-- 	<nav class="bs-docs-sidebar hidden-print hidden-xs hidden-sm " style="padding-left:0px;">                	
								<ul class="nav bs-docs-sidenav">
				                	<c:forEach items="${menuList}" var="pMenu">
					                	<li class="NV">
				        	                    <a href="#overview"><c:out value="${pMenu.pmenuName}"/></a>
				                                 <ul class="nav">                		
							                		<c:forEach items="${pMenu.subMenuList}" var="sMenu">
							                			<li class=""><a id=<c:out value="${sMenu.actionPath}"/> onclick="javascript:switchContent('<c:out value='${sMenu.actionPath}'/>');"><c:out value="${sMenu.smenuName}"/></a></li>
											      	</c:forEach>
								      			</ul>
			                            </li>
							    	</c:forEach>
							    </ul>                		
		                     </nav>     
		                    -->   
		                   <ul>
			                	<c:forEach items="${menuList}" var="pMenu">
				                	<li class="has-sub">				                							                			
				                			<c:if test="${pMenu.pmenuId=='1'}">
				                				<a href="#overview"><i class="fa fa-th-large fa-lg"></i>&nbsp;&nbsp;<c:out value="${pMenu.pmenuName}"/></a>
				                			</c:if>  
				                			<c:if test="${pMenu.pmenuId=='2'}">
				                				<a href="#overview"><i class="fa fa-database"></i>&nbsp;&nbsp;<c:out value="${pMenu.pmenuName}"/></a>
				                			</c:if>  
				                			<c:if test="${pMenu.pmenuId=='3'}">
				                				<a href="#overview"><i class="fa fa-institution"></i>&nbsp;&nbsp;<c:out value="${pMenu.pmenuName}"/></a>
				                			</c:if>  
				                			<c:if test="${pMenu.pmenuId=='4'}">
				                				<a href="#overview"><i class="fa fa-book fa-lg"></i>&nbsp;&nbsp;<c:out value="${pMenu.pmenuName}"/></a>
				                			</c:if>  
				                			<c:if test="${pMenu.pmenuId=='5'}">
				                				<a href="#overview"><i class="fa fa-gears fa-lg"></i>&nbsp;&nbsp;<c:out value="${pMenu.pmenuName}"/></a>
				                			</c:if>  
				                			<c:if test="${pMenu.pmenuId=='98'}">
				                				<a href="#overview"><i class="fa fa-line-chart"></i>&nbsp;&nbsp;<c:out value="${pMenu.pmenuName}"/></a>
				                			</c:if>  
				                			<c:if test="${pMenu.pmenuId=='99'}">
				                				<a href="#overview"><i class="fa fa-gear fa-lg"></i>&nbsp;&nbsp;<c:out value="${pMenu.pmenuName}"/></a>
				                			</c:if>  	
				                			<c:if test="${pMenu.pmenuId=='6'}">
				                				<a href="#overview"><i class="fa fa-support"></i>&nbsp;&nbsp;<c:out value="${pMenu.pmenuName}"/></a>
				                			</c:if>  										       			
			                			  	<ul class="">                		
						                		<c:forEach items="${pMenu.subMenuList}" var="sMenu">
						                			<li class=""><a id=<c:out value="${sMenu.actionPath}"/> onclick="javascript:switchContent('<c:out value='${sMenu.actionPath}'/>');"><i class="fa fa-caret-right"></i>&nbsp;&nbsp;<c:out value="${sMenu.smenuName}"/></a></li>
										      	</c:forEach>
							      			</ul>
		                            </li>
						    	</c:forEach>
						    </ul>	 
						                	
						   
                </div>
             </div>
             <div class="row" id="content-frame-row">
                <div class="col-xs-2" id="dummy">&nbsp;</div>
            	<div class="col-xs-10" id="content-frame" style="padding-left:0px; padding-right:0px;">
                	<iframe src="resources/notification.html" width="100%" id="body-frame" height="630px" name="body-frame" scrolling="auto"  seamlessly="seamlessly"></iframe>
                </div>
            </div>          
		    <div class="row">
				<div class="col-xs-12">
				</div>
		    </div>
		</div>	
		<form action="" id="logout-form"></form>
		<form action="home" id="home-form">
		 <input type="hidden" name="proceed"></input>
		</form>
        <!--<button type="button" onclick="javascript:calScroll();">CAL</button>-->
        
	</body>
</html>
