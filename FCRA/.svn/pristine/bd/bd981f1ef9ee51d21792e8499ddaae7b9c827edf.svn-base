
var succMess="Uploaded";
var errMess="Error !!";
$(document).ready(function(){	
	$(document).find('[upload-plugin]').each(function(){	
		prepareUploadPlugin($(this).attr('upload-plugin'));		
	});
});
// Drag and Drop
// Drag and Drop
function prepareUploadPlugin(upId){	
	//uId=$('[upload-plugin="'+upId+'"]').attr('upload-plugin');
	$('[upload-plugin="'+upId+'"]').append('<form action="submit-file-upload-rc" '+ 
		'name="uploadForm" method="post" enctype="multipart/form-data"></form>');
	prepareFileSelector(upId);
	prepareFileList(upId);
}
function prepareFileSelector(upId){	
	$('[up-file-selector="'+upId+'"]').append('<button type="button" class="btn btn-warning title-b"  title="Click to attach files" onclick="javascript:selectFile(\''+upId+'\');" ><span class="glyphicon glyphicon-paperclip"></span></button>'+
	'<input type="file" id="f-'+upId+'"  name="f-'+upId+'[]" '+ 
	' size="50" style="display:none;"></input>');	
	initToolTip();
	$('#f-'+upId).on('change',function(){
		initTable($('[upload-plugin="'+upId+'"]'));	 
	});	
}
function prepareFileList(upId){
	$('[up-file-list="'+upId+'"]').append('<table class="table table-condensed" id="t-'+upId+'"><tbody></tbody></table>');
}

/*function initFilesData(obj){	
	if($(obj).attr('f-reInit-flag')=="true"){
		$(obj).attr('f-file-status',"")  			//errSuccFlag="";
		$(obj).attr('f-id-count',0)				//fileCount=0;	
		$(obj).attr('f-pgs-status',"true");	
		//$(obj).parent().attr('f-current-file',$(obj).parent().data("filesQueue").shift());		//file=filesQueue.shift();		
		$('#delIcon'+$(obj).attr('f-id-count')).hide();
		uploadProcess($(obj).data("filesQueue").shift(),obj);	
	}	
	else{
			window.setTimeout(function(){
    		proceedFile(obj);
    	},1000);
	}	
} */

function initFilesData(obj){	
	if($(obj).attr('f-reInit-flag')=="true"){		
		$(obj).attr('f-file-status',"")  		//errSuccFlag="";
		$(obj).attr('f-id-count',0)				//fileCount=0;	
		$(obj).attr('f-pgs-status',"true");				
		$('#delIcon'+$(obj).attr('f-id-count')).hide();
		uploadProcess($(obj).data("filesQueue").shift(),obj);		
	}	
	else{
			window.setTimeout(function(){
    		proceedFile(obj);
    	},1000);
	}	
}

function proceedFile(obj){	
	$('#pgs'+$(obj).attr('f-id-count')).hide();
	$('#delIcon'+$(obj).attr('f-id-count')).show();	
	if($(obj).attr('f-file-status')=="Success"){
		$(obj).attr('f-file-status',"");	//errSuccFlag="";		
		showStatus("upload successful!","Success",$(obj).attr('f-id-count'));
	}	
	if(!($(obj).data("filesQueue").length==0)){
		$(obj).attr('up-pgs-status',"true");	
		var delTrack;		
		$(obj).attr('f-id-count',parseInt($(obj).attr('f-id-count'))+1);		
		if($(obj).data("resQueue").length>0){			
			for(var k=0;k<$(obj).data("resQueue").length;k++){				
				if($(obj).attr('f-id-count')==$(obj).data("resQueue")[k]){					
					delTrack=0;
					break;
				}
				else					
					delTrack=1;				
			}			
			if(delTrack==0){
					$(obj).data("filesQueue").shift();
					proceedFile(obj);				
			}
			else{						
					$('#delIcon'+$(obj).attr('f-id-count')).hide();
					uploadProcess($(obj).data("filesQueue").shift(),obj);
			}
		}else{											
					$('#delIcon'+$(obj).attr('f-id-count')).hide();					
					uploadProcess($(obj).data("filesQueue").shift(),obj);
		}
		
	}
	else{
			$(obj).attr('up-pgs-status',"false");
	}
}
function uploadProcess(file,obj){	
	if(doValidate(file,obj)=="success"){
		var formData = new FormData();										//var name=file.name;	//var uploadId=fileCount;		
		formData.append('f-'+$(obj).attr('upload-plugin'), file);	
		formData.append('fileName', $(obj).attr('f-fileName'));
	    $.ajax({	    	
	        url: 'upload-attachment-details-home?uploadId='+$(obj).attr('f-id-count'),  									//Server script to process data
	        type: 'POST',	        
	        xhr: function() {  												// Custom XMLHttpRequest
	            var myXhr = $.ajaxSettings.xhr();
	            if(myXhr.upload){ 											// Check if upload property exists            	
	               myXhr.upload.addEventListener('progress',function(e) {            	   
	                	percentDone=(100*e.loaded/e.total);                	
	                   $('progress'+$(obj).attr('f-id-count')).css('width',percentDone);
	                }, false); 												//For handling the progress of the upload
	            }
	            return myXhr;
	        },                      
	        beforeSend: function (e){},										//Ajax events
	        success: function (json){	        	
	        	$('progress'+$(obj).attr('f-id-count')).css('width','100%');
	        	$(obj).attr('f-file-status',"Success");	//errSuccFlag="Success";	        	
	        	window.setTimeout(function(){
	        		proceedFile(obj);
	        	},1000);	        	
	        },
	        error: function (json){},	      
	        data: formData,													// Form data        																
	        cache: false,													//Options to tell jQuery not to process data or worry about content-type.
	        contentType: false,	
	        enctype: 'multipart/form-data',
	        processData: false
	    });
	}
	else{	
			$(obj).attr('f-file-status',"Error");	//errSuccFlag="Error";
			$('#pgs'+$(obj).attr('f-id-count')).hide();
			//window.setTimeout('proceedFile('+obj+')',1000);
			window.setTimeout(function(){
        		proceedFile(obj);
        	},1000);
	}
}

function doValidate(file,obj){	
	var result;		
	if(validateFileExtension(file,$(obj).attr('up-file-types').split(","))==false){		
		showStatus("Only [ "+$(obj).attr('up-file-types').split(",")+" ] is allowed.","Error",$(obj).attr('f-id-count'));
		result="error";
	}
	else{
			if(validateFileSize(file,$(obj).attr('up-file-size'))==false){
				showStatus("Max size is "+$(obj).attr('up-file-size')+" MB.","Error",$(obj).attr('f-id-count'));
				result="error";
			}
			else{				
					result="success";
			}
	}
	return result;
}

function showStatus(message,type,key){	
	$('#fs-mess'+key+'').html('');	
	$('#fs'+key+'').removeClass('hide').show();
	if(type=="Success"){
		$('#fs-mess'+key+'').addClass('text-success');
		$('#fs-mess'+key+'').html(succMess);
	}		
	else if(type=="Error"){
		$('#fs-mess'+key+'').addClass('text-danger');
		$('#fs-mess'+key+'').html(errMess);
		$('#errIcon'+key+'').removeClass('hide').show();		
		$('#errIcon'+key+'').attr('title',message);
		initToolTip();
	}		
	else if(type=="Warning")
		$('#fileStatus'+key+'').addClass('text-warning');
	else if(type=="Info")
		$('#fileStatus'+key+'').addClass('text-info');	
}

function validateFileExtension(file,validType){	
   var flag=0;
   var value=file.name;   
   with(file){
      var ext=value.substring(value.lastIndexOf('.')+1).toLowerCase();
      for(i=0;i<validType.length;i++){
         if(ext==validType[i].toLowerCase()){        	
            flag=0;
            break;
         }
         else{
            flag=1;
         }
      }
      if(flag!=0){         
         return false;
      }
      else{
         return true;
      }
   }
}

function validateFileSize(file,maxSize){  
      if(file!=undefined){
         size = file.size;
      }   
	   if(size!=undefined && size>maxSize) {	      
	      return false;
	   }
	   else{
	      return true;
	   }
}

function initTable(obj){	
	var upObj=$(obj);
	var upFsObj=$('[up-file-selector="'+$(obj).attr('upload-plugin')+'"]');
	var upFlObj=$('[up-file-list="'+$(obj).attr('upload-plugin')+'"]');
	var files =upFsObj.find('input[type=file]')[0].files;																		//$(obj).find('input[type=file]')[0].files.length var len=files.length;	
	if(upObj.find('tbody').html()==''){
		upObj.data("filesQueue",[]);
		upObj.data("resQueue",[]);
		upObj.attr('f-track-log1',0);  												//i=0;				
		upObj.attr('f-max-size',upFsObj.find('input[type=file]')[0].files.length);		//maxSize=len;
		upObj.attr('f-reInit-flag',"true")											//reInitFlag="true";			
	}
	else{				 
			if(upObj.data("filesQueue").length==0)
				upObj.attr('f-reInit-flag',"continue")	//reInitFlag="continue"
			else
				upObj.attr('f-reInit-flag',"false")		// reInitFlag="false";					
			upObj.attr('f-track-log1',upObj.attr('f-log-count'));					//i=logCount;			
			upObj.attr('f-max-size',parseInt(upObj.attr('f-track-log1'))+parseInt(upFsObj.find('input[type=file]')[0].files.length));	//maxSize=i+len;
	}		
	while(upObj.attr('f-track-log1')<upObj.attr('f-max-size')){			
			upObj.attr('f-track-log2',parseInt(upObj.attr('f-max-size'))-parseInt(upObj.attr('f-track-log1'))); //var k=maxSize-i;			
			upObj.attr('f-fileKey',parseInt(upFsObj.find('input[type=file]')[0].files.length)-parseInt(upObj.attr('f-track-log2')));		//var fileKey=len-k;			
			upObj.attr('f-fileSize',getReadableFileSizeString(files[upObj.attr('f-fileKey')].size)); //var fileSize=getReadableFileSizeString(files[fileKey].size);
			upObj.attr('f-fileName',getReadableFileName(files[upObj.attr('f-fileKey')].name)); //var fileName=getReadableFileName(files[fileKey].name);						
			upObj.data("filesQueue").push(files[upObj.attr('f-fileKey')]);	//filesQueue.push(files[upObj.attr('f-fileKey')]);
			
			upObj.find('tbody').append('<tr id=\'delFile-'+upObj.attr('f-track-log1')+'\'><td><b>'+upObj.attr('f-fileName')+'</b> ('+upObj.attr('f-fileSize')+')</td>'+
			'<td  id="fs'+upObj.attr('f-track-log1')+'" class="hide"><font id="fs-mess'+upObj.attr('f-track-log1')+'" class="p-progress"></font> &nbsp;'+
			'<i class="glyphicon glyphicon-info-sign hide title-b"  id="errIcon'+upObj.attr('f-track-log1')+'"></i></td>'+
			'<td width="90px" id=\'pgs'+upObj.attr('f-track-log1')+'\'>'+
			'<div  class="progress progress-striped active"  style="height:10px;">'+
			'<progress'+upObj.attr('f-track-log1')+'  class="progress-bar progress-bar-success"></progress'+upObj.attr('f-track-log1')+'>'+
			'</div></td><td class="delete" style="cursor:pointer;text-align:right;"><i class="glyphicon glyphicon-remove" id="delIcon'+upObj.attr('f-track-log1')+'"></i></td>'+
			'<tr/>');			
			upObj.attr('f-track-log1',parseInt(upObj.attr('f-track-log1'))+1);  //i++;
	}	
	attachDelete(obj);	
	upObj.attr('f-log-count',upObj.attr('f-track-log1')); //	logCount=i;	
	if(upObj.attr('f-reInit-flag')=="true" || upObj.attr('f-reInit-flag')=="continue"){
		initFilesData(obj);
	}
} 
function attachDelete(obj){	
	var upObj=$(obj);
	upObj.find('.delete').on('click',function(){
		initDeleteUpload(this,obj);	 
	});
}
function initDeleteUpload(delObj,parObj){	
	var message=$(delObj).parent().find('td .p-progress').html();
	var value=$(delObj).parent().attr('id');
	var str=value.substring(value.lastIndexOf('-')+1);	
	$(delObj).parent().remove();	
	if(message==succMess){
		//$(obj).parent().remove();
		deleteFile(str,parObj);
	}
	else{			
			$(parObj).data("resQueue").push(str);				//resQueue.push(str);
			/*var val=$(obj).parent().find('td .bar').css('width').substring(0,$(obj).parent().find('td .bar').css('width').lastIndexOf('px'));
			alert(val);
			if(val>0){
				$(obj).parent().remove();
				deleteFile(cacheId,str);
			}
			else{
					$(obj).parent().remove();				
			}	*/		
	} 		
}
function deleteFile(uploadId,obj){
	$(obj).attr('f-action-type',"del");	
	$.ajax({	    	
        url: 'delete-attachment-details-home?uploadId='+uploadId,  			//Server script to process data
        type: 'POST', 
        xhr: function() {  												// Custom XMLHttpRequest
            var myXhr = $.ajaxSettings.xhr();            
            return myXhr;
        },    
        beforeSend: function (e){},										//Ajax events
        success: function (json){  
        	$(obj).attr('f-action-type',"");
        },
        error: function (json){},                																
        cache: false,													//Options to tell jQuery not to process data or worry about content-type.
        contentType: false,
        processData: false
    });
}
function getReadableFileName(name){	
	var str=name.substring(0,name.lastIndexOf('.'));
	var ext=name.substring(name.lastIndexOf('.')+1);
	if(str.length>10){
		return name.substring(0,20)+'.. .'+ext;
	}
	else
		return name;
}

function getReadableFileSizeString(fileSizeInBytes) {
    var i = -1;
    var byteUnits = [' kB', ' MB', ' GB'];
    do {
        fileSizeInBytes = fileSizeInBytes / 1024;
        i++;
    } while (fileSizeInBytes > 1024);
    return Math.max(fileSizeInBytes, 0.1).toFixed(1) + byteUnits[i];
};

function selectFile(upId){	
	$('#f-'+upId).click();
}

