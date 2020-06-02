$(document).ready(function (){
	initializeRedCategoryList();
	$("#red-flag").validationEngine({promptPosition: 'bottomRight'});
});

function initializeRedCategoryList() {
	$("#red-flag-list").html('');
	$("#red-flag-list").bootgrid(
    		{
    		title:'',
    		recordsinpage:'5',
    		dataobject:'propertyList',    		
    		dataurl:'get-red-flag-category',
    		defaultsortcolumn:'createdDate',
    		defaultsortorder:'desc',
    		columndetails:
			[
				{title:'Category Code', name:'categoryCode',sortable:true},
			
				{title:'Category Description', name:'categoryName', sortable:true},
			
				{title:'Entered On', name:'createdDate',sortable:true}
			],
			onRowSelect:function(rowdata){getRedCategory(rowdata);}
    		});	
	clearForm();
}

function getRedCategory(rowdata){
$('#categoryCode').val(rowdata.categoryCode);
$('#categoryName').val(rowdata.categoryName);
$('#createdDate').val(rowdata.createdDate);
$('#red-flag-list').show();
$('#add-btn').hide();
$('#edit-actions').show();
$("#details-div").hide
$('#table-btn').hide();
$("#form-div").show();	
$('#add-details-btn').hide();
}

function tabledata(){
	$('#red-flag-list').show();
	$('#add-btn').hide();
	$('#edit-actions').hide();
	$('#table-btn').hide();
	$("#form-div").show();	
	$("#add-details-btn").show();
}

function clearForm(){	
	$(document).find('.clear').each(function (){
	$(this).val('');
	});

}

function showaAllTable(){
	$('#red-flag-list').show();
	$('#add-btn').show();
	$('#edit-actions').hide();
	$("#details-div").hide
	$('#table-btn').hide();
	$("#form-div").hide();	
	$('#add-details-btn').hide();
}

function addCategory(){
	$('#sticky-notify').html('');
		if($('#red-flag').validationEngine('validate')){
		$('#bar-notify').html('');
		var params='categoryName='+$('#categoryName').val()+'&requestToken='+$('#requestToken').val();
		$.ajax({
			url: 'add-red-flag-category',
			method:'POST',
			data:params,
			dataType:'json',
			success: function(data){
				notifyList(JSON.parse(data[0]),'');
				var token = JSON.parse(data[1]).li;
				if(token != null && token != '')
				$('#requestToken').val(token);
				initializeRedCategoryList();
			},
			error: function(textStatus,errorThrown){
				alert('error');
				$("#gate-add-button").button('reset');
			}
		});
		clearForm();
		showaAllTable();
	}
}
		
		// Function of Edit
		function editCategory(){
			$('#sticky-notify').html('');
			if($('#red-flag').validationEngine('validate')){
			$('#bar-notify').html('');
			var params = 'categoryCode='+$('#categoryCode').val()+'&categoryName='+$('#categoryName').val()+'&requestToken='+$('#requestToken').val();
			var action = 'edit-red-flag-category';
					$.ajax({
						url: action,
						method:'GET',
						data:params,
						dataType:'json',
						success: function(data){
							notifyList(JSON.parse(data[0]),'');
							var token = JSON.parse(data[1]).li;
							if(token != null && token != '')
							$('#requestToken').val(token);
							initializeRedCategoryList();
						},
						error: function(textStatus,errorThrown){
							alert('error');
							$("#gate-add-button").button('reset');
						}
					});
				clearForm();
				showaAllTable();
			}
		}

		// Function of delete
		
		function deleteCategory(){
			$('#sticky-notify').html('');
			$('#bar-notify').html('');
			if($('#red-flag').validationEngine('validate')){
			var params = 'categoryCode='+$('#categoryCode').val()+'&requestToken='+$('#requestToken').val();
	      	var action = 'delete-red-flag-category';
			$.ajax({
				url: action,
				method:'GET',
				data:params,
				dataType:'json',
				success: function(data){
					notifyList(JSON.parse(data[0]),'');
					var token = JSON.parse(data[1]).li;
					if(token != null && token != '')
					$('#requestToken').val(token);
					initializeRedCategoryList();
				},
				error: function(textStatus,errorThrown){
					alert('error');
					$("#gate-add-button").button('reset');
				}
			});
			clearForm();
			showaAllTable();
		}
		}

