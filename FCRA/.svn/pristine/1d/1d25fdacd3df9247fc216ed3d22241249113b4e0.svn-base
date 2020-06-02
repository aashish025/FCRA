(function($){

	var defaultOptions = {
		sno: "false",
		multiselect: "false",
		recordsinpage: "10",
		title: "",
		width: "1000px",
		dataobject: "",
		dataurl: "",
		datafunction: "",
		checkboxrequired: "false",
		columndetails:[],
		tools:[],
		defaultsortcolumn:"",
		defaultsortorder:"",
		pageonload: ""
	};
	
	$.fn.bootgrid = function(options) {
		var opts = $.extend({}, defaultOptions, options);
		$(this).data('data-options',opts);
		formatgrid($(this), opts);
	};
	
	$.fn.getSelectedRows = function() {
		var objId = $(this).attr('id');
		var selectedRows = [];
		
		$('#'+objId).find('input:checkbox[name="'+objId+'-row-checkbox'+'"]:checked').each(function () {
			var rowData = $(this).parent().parent().data('row-data');
			selectedRows.push(rowData);
		});
		return selectedRows;
	};
	
	$.fn.getCurrentPage = function() {
		var objId = $(this).attr('id');
		var currentPage = parseInt($('#'+objId+'-curpage').val());

		return currentPage;
	};
	
	function prepareGridBody(data, opts, objId)
	{
		var rowClass = '';
		var tbody = $('<tbody/>');
		if(opts['onRowSelect'] != null && opts['onRowSelect'] != undefined)
			rowClass = 'clickable';
		var list = data[opts['dataobject']];
		if(list != null && list != undefined) {
		$.each(data[opts['dataobject']], function(index, rowData){
				
			var headerRow = $('<tr/>', {'class':rowClass}).data('row-data', rowData);
			//alert(headerRow.data('row-data'));
			if(opts.checkboxrequired == true)
				headerRow.append($('<td/>', {css:{'text-align':'center'}}).append($('<input/>', {'type':'checkbox', 'id':objId+'-row-checkbox-'+index, 'name':objId+'-row-checkbox'})));
			$.each(opts.columndetails, function(key, value){
				var formattedString = rowData[value['name']];
				if(value['formatter'] != null && value['formatter'] != undefined && typeof value['formatter'] == 'function')
					formattedString = value['formatter'](rowData);
				headerRow.append($('<td/>', {'html':formattedString}));
			});
			tbody.append(headerRow);
		});
		}
		return tbody;
	}
	
	function addButton(buttonClass, spanClass, id) {
		var buttonObj = $('<button/>', {'type':'button', 'class':buttonClass, 'id':id}).append($('<span/>', {'class':spanClass}));
		
		return buttonObj;
	}
	
	function nextPage(e) {
		var objId = e.data.objectId;
		var currentPage = parseInt($('#'+objId+'-curpage').val());
		var totalPages = parseInt($('#'+objId+'-totpage').text().split(' ')[1]);
		
		var pageNum = currentPage + 1;
		if(pageNum > totalPages)
			pageNum = totalPages;

		if(currentPage != pageNum)
			requestPage(objId, pageNum);
	}
	
	function previousPage(e) {
		var objId = e.data.objectId;
		var currentPage = parseInt($('#'+objId+'-curpage').val());
		var totalPages = parseInt($('#'+objId+'-totpage').text().split(' ')[1]);
		
		var pageNum = currentPage - 1;
		if(pageNum <= 0)
			pageNum = 1;
		
		if(currentPage != pageNum)
			requestPage(objId, pageNum);
	}
	
	function firstPage(e) {
		var objId = e.data.objectId;
		var currentPage = parseInt($('#'+objId+'-curpage').val());
		
		var pageNum =  1;

		if(currentPage != pageNum)
			requestPage(objId, pageNum);
	}
	
	function lastPage(e) {
		var objId = e.data.objectId;
		var currentPage = parseInt($('#'+objId+'-curpage').val());
		var totalPages = parseInt($('#'+objId+'-totpage').text().split(' ')[1]);
		
		var pageNum = totalPages;

		if(currentPage != pageNum)
			requestPage(objId, pageNum);
	}
	
	function requestPage(objId, pageNum) {
		var options = $('#'+objId).data('data-options');
		var spanObj = $('#'+objId+'-column-headings > th > span.sort-column-active');
		var defaultsortcolumn = options.defaultsortcolumn;
		var defaultsortorder = options.defaultsortorder;
		var column=defaultsortcolumn;
		var order=defaultsortorder;
		if(spanObj != null && spanObj != undefined && spanObj.length > 0) {
			column = spanObj.data('column-name');
			if(spanObj.hasClass('glyphicon-sort-by-attributes')) {
				order = 'asc';
			}
			else if(spanObj.hasClass('glyphicon-sort-by-attributes-alt')){
				order = 'desc';
			}
		}
		var tbody1 = preparePage($('#'+objId), options, pageNum, column, order);
		//$('#'+objId).find('tbody').empty();
		//$('#'+objId).find('tbody').html(tbody1.children());
	}
	
	function sortByColumn(e) {
		var objId = e.data.objectId;
		var spanObj = $(e.target);
		var orderByColumn = spanObj.data('column-name');
		var next = null;
		var order = null;
		
		if(spanObj.hasClass('glyphicon-sort')) {
			next = 'glyphicon-sort-by-attributes';
			order = 'asc';
		}
		else if(spanObj.hasClass('glyphicon-sort-by-attributes')) {
			next = 'glyphicon-sort-by-attributes-alt';
			order = 'desc';
		}
		else {
			next = 'glyphicon-sort';
			order = '';
		}
		$('#'+objId+'-column-headings > th > span.clickable').removeClass().addClass('glyphicon glyphicon-sort clickable pull-right');
		spanObj.removeClass().addClass('glyphicon clickable pull-right sort-column-active '+next);
		var currentPage = parseInt($('#'+objId+'-curpage').val());
		var totalPages = parseInt($('#'+objId+'-totpage').text().split(' ')[1]);
		
		var pageNum = currentPage;
		if(pageNum <= 0)
			pageNum = 1;
		
		requestPage(objId, pageNum);
	}
	
	function rowSelection(e) {
		
		var objId = e.data.objectId;
		var target = $(e.target);
		if(target.prop('nodeName') != 'TD')
			return;
		//alert('rowSelection');
		var rowData = target.parent().data('row-data');
		var options = $('#'+objId).data('data-options');
		//alert(rowData);
		$('#'+objId).find('tbody tr').removeClass('warning');
		target.parent().addClass('warning');
		if(options['onRowSelect'] != null && options['onRowSelect'] != undefined)
			options['onRowSelect'](rowData);
	}
	
	function rowChecked(e) {
		var objId = e.data.objectId;
		var target = $(e.target);

		var rowData = target.parent().parent().data('row-data');
		var options = $('#'+objId).data('data-options');
		if(options['onCheckboxSelect'] != null && options['onCheckboxSelect'] != undefined)
			options['onCheckboxSelect'](target.prop('checked'), rowData);
	}
	
	function toggleRowSelection(e) {
		
		var objId = e.data.objectId;
		var target = $(e.target);
		if(target.prop('checked') == true) {
			$('#'+objId+' input:checkbox[name="'+objId+'-row-checkbox'+'"]').prop("checked", true);
		} else {
			$('#'+objId+' input:checkbox[name="'+objId+'-row-checkbox'+'"]').prop("checked", false);
		}
		$('#'+objId+' input:checkbox[name="'+objId+'-row-checkbox'+'"]').change();
	}
	
	function getTableBody($object, opts, pageNum, data) {
		var recordsinpage = opts['recordsinpage'];
		var objId = $object.attr('id');
		var totalRecs = (data['totalrecords']==null)? 0 : data['totalrecords'];
		var totalPages = Math.floor(totalRecs / recordsinpage);
		if(totalRecs % recordsinpage > 0)
			totalPages += 1;
		var tabbody = prepareGridBody(data, opts, objId);
		if(pageNum > totalPages)
			pageNum = totalPages;
		//var from = recordsinpage * (pageNum - 1) + 1;
		var listLength = (data[opts['dataobject']] == null)? 0 : data[opts['dataobject']].length;
		var from = (listLength > 0)?recordsinpage * (pageNum - 1) + 1:0;
		var to = recordsinpage * pageNum;
		if(to > totalRecs)
    		to = totalRecs;
		//alert('recordsinpage :'+recordsinpage+'totalRecs :'+totalRecs+'pageNum :'+pageNum)
		
		var pagingDiv = $object.find('#'+objId+'-paging');
		 
		pagingDiv.html('Viewing '+from+'-'+to+' of ' + totalRecs);
		pagingDiv.append(addButton('btn btn-default btn-xs', 'glyphicon glyphicon-step-backward', objId+'-first'))
			.append(addButton('btn btn-default btn-xs', 'glyphicon glyphicon-backward', objId+'-backward'))
			.append($('<span/>', {'text':'Page'}))
			.append($('<input/>', {'type':'text', 'value':pageNum, 'id':objId+'-curpage'}).attr('size','1'))
			.append($('<span/>', {'id':objId+'-totpage', 'text':'of ' + totalPages}))
			.append(addButton('btn btn-default btn-xs', 'glyphicon glyphicon-forward', objId+'-forward'))
			.append(addButton('btn btn-default btn-xs', 'glyphicon glyphicon-step-forward', objId+'-last'));
			//.append($('<input/>', {'id':objId+'-appIdSearch', 'type':'text', 'placeholder':'Search with appId..'}));
		pagingDiv.parent().parent().clone().appendTo(tabbody);
		$('#'+objId).find('tbody').empty();
		$('#'+objId).find('tbody').html(tabbody.children());
		return tabbody;
	}
	function overlapPageLoad(id, z){
		var spinner='data:image/gif;base64,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';
		$('body').append($('<div/>',{'id':id,css:{width:$(document).width(),height:$(document).outerHeight(),position:'absolute',left:'0',top:'0','z-index':z,background:'rgba(255,255,255,0.9)'}}).append($('<span/>',{css:{position:'relative',left:'50%',top:'44%'}}).append($('<img/>',{src:spinner,width:'60px',height:'60px'}))));
	}
	function showSpinner(id, z){
		overlapPageLoad(id, z);
	}
	function hideSpinner(id){
		$('#'+id).remove();
	}
	function preparePage($object, opts, pageNum, sortColumn, sortOrder)
	{
		var data = null;
		var recordsinpage = opts['recordsinpage'];
		var objId = $object.attr('id');
		//alert(JSON.stringify(opts));
		var tabbody = null;
		if(opts.datafunction != null && opts.datafunction != '')
		{
			data = opts['datafunction'].call(this, pageNum, recordsinpage, sortColumn, sortOrder);	
			tabbody = getTableBody($object, opts, pageNum, data);
		}
		else
		{
			showSpinner(objId+'-spinner', 10);
			var url = opts['dataurl'].split('?');
			var action = url[0];
			var params = 'pageNum='+pageNum+'&recordsPerPage='+recordsinpage+'&sortColumn='+sortColumn+'&sortOrder='+sortOrder+(url[1]==undefined ? '' : '&'+url[1]);
			$.ajax({
				url: action,
				method:'GET',
				data:params,
				dataType:'json',
				success: function(data){
					//alert(data.list);
					var recordsinpage = opts['recordsinpage'];
					
					if(pageNum > 1  && (data.totalRecords <= ((pageNum-1) * recordsinpage))) {
						hideSpinner(objId+'-spinner', 10);
						preparePage($object, opts, pageNum-1, sortColumn, sortOrder);
					} else {
						getTableBody($object, opts, pageNum, {totalrecords:data.totalRecords, propertyList:data.list});
						hideSpinner(objId+'-spinner', 10);
					}
				},
				error: function(textStatus,errorThrown){
					hideSpinner(objId+'-spinner', 10);
					return {totalrecords:0, propertyList:data.propertyList};
				}
			});
		}
		/* *
		var totalRecs = data['totalrecords'];
		var totalPages = Math.floor(totalRecs / recordsinpage);
		if(totalRecs % recordsinpage > 0)
			totalPages += 1;
		var tabbody = prepareGridBody(data, opts, objId);
		if(pageNum > totalPages)
			pageNum = totalPages;
		var from = recordsinpage * (pageNum - 1) + 1;
		var to = recordsinpage * pageNum;
		if(to > data['totalrecords'])
    		to = data['totalrecords'];
		
		var pagingDiv = $object.find('#'+objId+'-paging');
		 
		pagingDiv.html('Viewing '+from+'-'+to+' of ' + data['totalrecords']);
		pagingDiv.append(addButton('btn btn-default btn-xs', 'glyphicon glyphicon-step-backward', objId+'-first'))
			.append(addButton('btn btn-default btn-xs', 'glyphicon glyphicon-backward', objId+'-backward'))
			.append($('<span/>', {'text':'Page'}))
			.append($('<input/>', {'type':'text', 'value':pageNum, 'id':objId+'-curpage'}).attr('size','1'))
			.append($('<span/>', {'id':objId+'-totpage', 'text':'of ' + totalPages}))
			.append(addButton('btn btn-default btn-xs', 'glyphicon glyphicon-forward', objId+'-forward'))
			.append(addButton('btn btn-default btn-xs', 'glyphicon glyphicon-step-forward', objId+'-last'));
		pagingDiv.parent().parent().clone().appendTo(tabbody);
		* */
		return tabbody;
	}
	
	function searchWithApplicationId(){
		 var key=e.keyCode || e.which;
		  if (key==13){
			  alert(1);
		  }	
	}
	
	function formatgrid($object, opts) {
		var numCols = opts.columndetails.length;
		var defaultsortcolumn = opts.defaultsortcolumn;
		var defaultsortorder = opts.defaultsortorder;
		var objId = $object.attr('id');
		var pageonload = opts.pageonload;
		if(pageonload == null || pageonload == "")
			pageonload = 1;
		
		var headerRow = $('<tr/>', {'id':objId+'-column-headings'});
		if(opts.checkboxrequired == true) {
			numCols++;
			headerRow.append($('<th/>', {css:{'text-align':'center'}}).append($('<input/>', {'type':'checkbox', 'id':objId+'-row-checkbox-all'})));
		}
		$.each(opts.columndetails, function(key, value){
			var spanObj = '';
			if(value['sortable'] == true) {
				spanObj = $('<span/>',{'class':'glyphicon glyphicon-sort pull-right clickable'}).data('column-name',value['name']);
			}
			headerRow.append($('<th/>', {'text':value['title']}).append(spanObj));
		});
		var toolsRow = $('<tr/>');
		var toolsCol= $('<td/>', {'colspan':numCols, 'class':'boot-grid-toolbar'});
		toolsRow.append(toolsCol);
		var toolDiv = $('<span/>', {'id':objId+'-tools', 'class':'boot-grid-tools'});
		var pagingDiv = $('<span/>', {'id':objId+'-paging', 'class':'boot-grid-paging'});
		toolsCol.append(toolDiv);
		toolsCol.append(pagingDiv);
		$.each(opts.tools, function(key, value){
			switch(value)
			{
				case 'add' : icon='glyphicon-plus'; break;
				case 'edit' : icon='glyphicon-pencil'; break;
				case 'delete' : icon='glyphicon-trash'; break;
				case 'search' : icon='glyphicon-search'; break;
			}
			toolDiv.append(addButton('btn btn-default btn-xs', 'glyphicon '+icon));
		});
		
		var tableObj = $('<table/>', {'id':objId+'-table', /*'width': opts['width'],*/ 'class':'table table-bordered'});
		var tableHeading = (opts['title']==null || opts['title']=='' || opts['title']==undefined)? null : $('<tr/>').append($('<th/>', {'colspan':numCols, 'text':opts['title']}))
		tableObj.append($('<thead/>', {'style':'background-color: #f3f3f3;'}).append(tableHeading)
							.append(headerRow));
		var tableBody = $('<tbody/>').append(toolsRow);
		tableObj.append(tableBody);
		$object.append($('<small/>')
				.append(tableObj))
			.addClass('table-responsive boot-grid-container');
		var tbody1 = preparePage($object, opts, pageonload, defaultsortcolumn, defaultsortorder);
		$object.find(objId+'-tools').attr('id', '1');
		$object.find(objId+'-paging').attr('id', '2');
		//$object.find('tbody').empty();
		//$object.find('tbody').append(tbody1.children());
		$object.on('change', '#'+objId+'-appIdSearch', {objectId:objId}, function(e){searchWithApplicationId();});
		$object.unbind().on('click', '#'+objId+'-forward', {objectId:objId}, function(e){nextPage(e);});
		$object.on('click', '#'+objId+'-backward', {objectId:objId}, function(e){previousPage(e);});
		$object.on('click', '#'+objId+'-first', {objectId:objId}, function(e){firstPage(e);});
		$object.on('click', '#'+objId+'-last', {objectId:objId}, function(e){lastPage(e);});
		$object.on('click', '#'+objId+'-column-headings > th > span.clickable', {objectId:objId}, function(e){sortByColumn(e);});
		$object.on('click', '#'+objId+'-table > tbody > tr.clickable', {objectId:objId}, function(e){rowSelection(e);});
		$object.on('change', '#'+objId+'-row-checkbox-all', {objectId:objId}, function(e){toggleRowSelection(e);});
		$object.on('change', 'input:checkbox[name="'+objId+'-row-checkbox'+'"]', {objectId:objId}, function(e){rowChecked(e);});
		
	};
}(jQuery));