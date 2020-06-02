var handRight='<a href="#" data-toggle="tooltip" title="Next" data-placement="bottom" class="btn btn-default" data-etype="nextButton"><span class="glyphicon glyphicon-hand-right"></span></a>';
var handLeft='<a href="#" data-toggle="tooltip" title="Previous" data-placement="bottom" class="btn btn-default" data-etype="previousButton"><span class="glyphicon glyphicon-hand-left"></span></a>';
var handUp='<a href="#" data-toggle="tooltip" title="Previous" data-placement="bottom" class="btn btn-default" data-etype="previousButton"><span class="glyphicon glyphicon-hand-up"></span></a>';
var handDown='<a href="#" data-toggle="tooltip" title="Previous" data-placement="bottom" class="btn btn-default" data-etype="nextButton"><span class="glyphicon glyphicon-hand-down"></span></a>';
var buttonLeft='<a href="javascript:previous(this);" data-etype="nextIndex">&laquo;</a>';
var buttonRight='<a href="javascript:next(this);" data-etype="previousIndex">&raquo;</a>';
$('document').ready(function(){
	$('body').append('<form name="unloadPagination" id="unloadPagination" style="display:none;" method="get" action="removePage-pagination"></form>');
	$('div[data-toggle="pagination"]').each(function(){
		$(this).pagination($(this).attr('pagination-id'));
		
	});
	$(window).on('beforeunload',function(event){
		//event.preventDefault();
		//event.returnValue = '';
		var id='';
		var first=true;
		$('#unloadPagination input[type="hidden"]').each(function(){
			if(first){
				id=$(this).val();
				first=false;
			}else{
				id=id+','+$(this).val();
			}
			
		});
		parent.remove(id);
		//removeOldPagination(id);
		return '';
	});
	/*window.onBeforeUnload=function(event){
		alert(1);
		event.preventDefault();
		event.returnValue = "\o/";
		var id='';
		var first=true;
		$('#unloadPagination input[type="hidden"]').each(function(){
			if(first){
				id=$(this).val();
				first=false;
			}else{
				id=id+','+$(this).val();
			}
			
		});
		removeOldPagination(id);
	};*/
});
(function($) {
	$.fn.pagination = function(paginationId){
		$(this).attr('pagination-id',paginationId);
		$(this).attr('data-toggle','pagination');
		$(this).attr('data-pagingbuttoncount','1');
		if($('#unloadPagination').find('input[type="hidden"][data-divid="'+$(this).attr('id')+'"]').length>0){
			removeOldPagination($('#unloadPagination').find('input[type="hidden"][data-divid="'+$(this).attr('id')+'"]').val());
			$('#unloadPagination').find('input[type="hidden"][data-divid="'+$(this).attr('id')+'"]').remove();
		}
		$('#unloadPagination').append('<input type="hidden" name="removeId" data-divid="'+$(this).attr('id')+'" value="'+paginationId+'"/>');
		createContainersOnPage($(this));
		$(this).attr('cp','p1').attr('sp','1').attr('ep','1');
		fetchPage(1,$(this));
	};
}( jQuery ));

function createContainersOnPage(obj){
	var toolbar='<div class="row" data-etype="toolbar-row" pagination-id="'+$(obj).attr('pagination-id')+'" >'+
					'<div class="col-xs-11" data-etype="pagination-button-div" pagination-id="'+$(obj).attr('pagination-id')+'" align="left"></div>'+
					'<div class="col-xs-1" data-etype="pagination-refresh-div" pagination-id='+$(obj).attr('pagination-id')+'" align="right">'+
						'<br/><button class="btn btn-primary btn-sm" type="button" onclick="refreshPaginationPlugin(\''+$(obj).attr('pagination-id')+'\')">'+
							'<span class="glyphicon glyphicon-refresh"/>'+
						'</button>'+
					'</div>'+
				'</div>';
	$(obj).append(toolbar);
	var tableDiv='<div class="row" data-etype="table-row" pagination-id="'+$(obj).attr('pagination-id')+'">'+
				'<div class="col-xs-12" data-etype="table-div" pagination-id="'+$(obj).attr('pagination-id')+'" >'+
				'</div></div>';
	$(obj).append(tableDiv);
	return true;
}
function fetchPage(pageNum,pobj){
	var params='pageNum='+pageNum+'&pagingId='+$(pobj).attr('pagination-id');
	$.pullJSON('pullPage-pagination',params,function(data){
		if(data.errorFlag=='false'){
			if(data.updatePlugin==true){
				updatePlugin(data);
			}
			refreshPagination(data.pagingId,pageNum);
			$(pobj).find('div[data-etype="table-div"]').html('<small>'+data.tableContent+'</small>');
			notifyList(data.notifyList);
		}else{
			stickyNotify('Error!','Some error occured. Please try again','e','c',0);
		}
		var s=1;
		var e=0;
		if(data.pagingButtonCount>data.pageCount){
			e=data.pageCount;
		}else{
			e=data.pagingButtonCount;
		}
		generatePagination(s,e+1,pobj,data.pageCount);
		$(pobj).find('a[id="p1"]').closest('li').addClass('active');
	});
}
function updatePlugin(data){
	$('[data-toggle="pagination"][pagination-id="'+data.pagingId+'"]')
	.attr('data-pagingbuttoncount',data.pagingButtonCount)
	.attr('data-pagecount',data.pageCount)
	.attr('data-recordcount',data.recordCount)
	.attr('data-recordperpage',data.recordPerPage);
}
function refreshPagination(paginationId,i){
	var pobj=$('[data-toggle="pagination"][pagination-id="'+paginationId+'"]');
	var pl=parseInt($(pobj).attr('data-pagecount'));
	var p=parseInt($(pobj).attr('data-pagingbuttoncount'));
	var s=0;
	var e=0;
	if(i==$(pobj).attr('sp')){
		if(i!=1){
			s=i-(p-2);
			e=i+2;
			generatePagination(s,e,$(pobj),pl);
		}
	}
	else if(i==$(pobj).attr('ep')){
		if(i!=pl){
			s=i-1;
			e=(i-1)+p;
			generatePagination(s,e,$(pobj),pl);
		}
	}
	$(pobj).find('a[id="'+$(pobj).attr('cp')+'"]').closest('li').addClass('active');
}
function generatePagination(s,e,pobj,pageCount){
	var pc="<ul class=\"pagination pagination-sm\">"+
    		"<li><a style=\"cursor: pointer;\" onclick=\"javascript:refreshPagination('"+$(pobj).attr('pagination-id')+"',"+s+")\"><b>&laquo;</b></a></li>";
	var i=s;
	for(i=s;i<e;i++){
		if(i<=pageCount){
			pc+="<li><a id=\"p"+i+"\" style=\"cursor: pointer;\" onclick=\"javascript:pullPagePagination('"+$(pobj).attr('pagination-id')+"',"+i+")\">"+i+"</a></li>";
		}
	}
	var l=i-1;
	if(l>pageCount){
		l=pageCount;
	}
    pc+="<li><a style=\"cursor: pointer;\" onclick=\"javascript:refreshPagination('"+$(pobj).attr('pagination-id')+"',"+l+")\"><b>&raquo;</b></a></li>"+
    "</ul>";
    $(pobj).attr('sp',s).attr('ep',(e-1));
    $(pobj).find('div[data-etype="pagination-button-div"]').html(pc);
    /*$('#'+id).html(pc);
    $('#'+id).attr('sp',s).attr('ep',(e-1));*/   
}
function pullPagePagination(paginationId,pageNum){
	var pobj=$('[data-toggle="pagination"][pagination-id="'+paginationId+'"]');
	var cp=$(pobj).attr('cp');
	var pf=$(pobj).find('div[data-etype="table-div"]');
	$(pf).html('<small>Loading...</small>');
	var vr=parseInt($(pobj).attr('data-pagingbuttoncount'));
	$(pobj).find('a[id="'+cp+'"]').closest('li').removeClass('active');
	$(pobj).attr('cp','p'+pageNum);
	fetchPage(pageNum,$(pobj));
	$(pobj).find('a[id="p'+pageNum+'"]').closest('li').addClass('active');
	//refreshPagination(paginationId,pageNum);
	
}
function refreshPaginationPlugin(paginationId){
	var pobj=$('[data-toggle="pagination"][pagination-id="'+paginationId+'"]');
	$.getJSON('refresh-pagination','pagingId='+paginationId,function(data){
		updatePlugin(data);
		$(pobj).find('a[id="'+$(pobj).attr('cp')+'"]').click();
	});
}
function removeOldPagination(id){
	$.getJSON('removePage-pagination','removeId='+id,function(data){
	});
}







