(function($){

	var defaultOptions = {
		sno: "false",
		multiselect: "false",
		recordsinpage: "10",
		title: "",
		width: "1000px",
		checkboxrequired: "false",
		columndetails:[],
		tools:[]
	};
	
	$.fn.initLocalgrid = function(options) {
		var opts = $.extend({}, defaultOptions, options);
		$(this).data('data-options',opts);
	};
	
	$.fn.addRowToLocalgrid = function(rowData) {
		var objId = $(this).attr('id');
		var options = $('#'+objId).data('data-options');

		if($(this).children().length == 0)
			formatgrid($(this), options);
		addRow($(this), options, rowData);
	};
	
	$.fn.editRowInLocalgrid = function(rowId, rowData) {
		var objId = $(this).attr('id');
		var opts = $('#'+objId).data('data-options');

		var tbody = $('#'+objId).find('tbody');
		var rowClass = '';
		//alert(tbody.html());
		if(tbody != undefined) {
			var tr = tbody.children(':nth-child('+rowId+')');
			tr.data('row-data', rowData);
			tr.empty();
			$.each(opts.columndetails, function(key, value){
				var formattedString = rowData[value['name']];
				if(value['formatter'] != null && value['formatter'] != undefined && typeof value['formatter'] == 'function')
					formattedString = value['formatter'](rowData);
				tr.append($('<td/>', {'html':formattedString}));
			});
			/*tr.append($('<td/>', {'width':'15px'}).append($('<button/>', {'class':'btn btn-link'}).
				append($('<span/>', {'class':'glyphicon glyphicon-trash'})).on('click', {objectId:objId}, function(e){deleteRow(e);})));*/
		}
	};
	
	$.fn.deleteRowInLocalgrid = function(rowId) {
		var objId = $(this).attr('id');
		var options = $('#'+objId).data('data-options');

		removeRow(objId, rowId);
	};
	
	$.fn.getRowInLocalgrid = function(rowId) {
		var objId = $(this).attr('id');
		var options = $('#'+objId).data('data-options');

		if($(this).children().length == 0)
			formatgrid($(this), options);
		addRow($(this), options, rowData);
	};
	
	$.fn.serializeLocalgrid = function(columnsRequired, columnSeparator, rowSeparator) {
		var tableData = '';
		var tableBody = $(this).find('tbody');
		
		tableBody.find('tr').each(function(){
			var index = $(this).index()+1;
			var rowData = tableBody.children(':nth-child('+index+')').data('row-data');
			//alert(JSON.stringify(rowData));
			$.each(columnsRequired, function(key, value){
				var columnValue = rowData[value];
				if(columnValue == null || columnValue == '' || columnValue == undefined)
					columnValue = ' ';
				tableData += columnValue+columnSeparator;
			});
			tableData += rowSeparator;
		});
		return tableData;

	};
	
	$.fn.addListToLocalgrid = function(dataList) {
		var objId = $(this).attr('id');
		var options = $('#'+objId).data('data-options');
		var rowData = null;
		
		if(dataList != null && dataList.length > 0) {
			if($(this).children().length == 0)
				formatgrid($(this), options);

			var object = $(this);
			$.each(dataList, function(index, rowData){
				addRow(object, options, rowData);
			});
			
		}
	};
	
	$.fn.clearLocalgrid = function() {
		$(this).empty();
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
	
	function deleteRow(e) {
		var button = $(e.target);
		var objId = e.data.objectId;
		removeRow(objId, button.closest('tr').index()+1);
		/*
		button.closest('tr').remove();
		var tbody = $('#'+objId).find('tbody');
		if(tbody.children().length == 0) {
			$('#'+objId).empty();
		}
		*/
	}
	
	function removeRow(objId, rowId) {
		var tbody = $('#'+objId).find('tbody');
		tbody.children(':nth-child('+rowId+')').remove();
		if(tbody.children().length == 0) {
			$('#'+objId).empty();
		}
	}
	
	function addRow($object, opts, rowData) {
		var tbody = $object.find('tbody');
		var objId = $object.attr('id');
		var rowClass = '';

		if(tbody != undefined) {
			if(opts['onRowSelect'] != null && opts['onRowSelect'] != undefined)
				rowClass = 'clickable';
			var headerRow = $('<tr/>', {'class':rowClass}).data('row-data', rowData);
			if(opts.checkboxrequired == true)
				headerRow.append($('<td/>', {css:{'text-align':'center'}}).append($('<input/>', {'type':'checkbox', 'id':'', 'name':objId+'-row-checkbox'})));
			//alert(headerRow.data('row-data'));
			$.each(opts.columndetails, function(key, value){
				//alert(rowData[value['name']]);
				var formattedString = fetchFromObject(rowData, value['name']);//rowData[value['name']];
				if(value['formatter'] != null && value['formatter'] != undefined && typeof value['formatter'] == 'function')
					formattedString = value['formatter'](rowData);
				headerRow.append($('<td/>', {'html':formattedString}));
			});
			/*headerRow.append($('<td/>', {'width':'15px'}).append($('<button/>', {'class':'btn btn-link'}).
				append($('<span/>', {'class':'glyphicon glyphicon-trash'})).on('click', {objectId:objId}, function(e){deleteRow(e);})));*/
			tbody.append(headerRow);
		}
		
	}
	
	function fetchFromObject(obj, prop){
	    //property not found
	    if(typeof obj === 'undefined') return false;
	    
	    //index of next property split
	    var _index = prop.indexOf('.')

	    //property split found; recursive call
	    if(_index > -1){
	        //get object at property (before split), pass on remainder
	        return fetchFromObject(obj[prop.substring(0, _index)], prop.substr(_index+1));
	    }
	    //no split; get property
	    return obj[prop];
	}
	
	function prepareGridBody(data, opts, objId)
	{
		var rowClass = '';
		var tbody = $('<tbody/>');
		if(opts['onRowSelect'] != null && opts['onRowSelect'] != undefined)
			rowClass = 'clickable';
		$.each(data[opts['dataobject']], function(index, rowData){
				
			var headerRow = $('<tr/>', {'class':rowClass}).data('row-data', rowData);
			//alert(headerRow.data('row-data'));
			if(opts.checkboxrequired == true)
				headerRow.append($('<td/>', {css:{'text-align':'center'}}).append($('<input/>', {'type':'checkbox', 'id':objId+'-row-checkbox-'+index, 'name':objId+'-row-checkbox'})));
			$.each(opts.columndetails, function(key, value){
				var formattedString = fetchFromObject(rowData, value['name']);//rowData[value['name']];
				if(value['formatter'] != null && value['formatter'] != undefined && typeof value['formatter'] == 'function')
					formattedString = value['formatter'](rowData);
				headerRow.append($('<td/>', {'html':formattedString}));
			});
			tbody.append(headerRow);
		});
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
		var column='';
		var order='';
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
		//alert('index : '+target.closest('tr').index());
		$('#'+objId).find('tbody tr').removeClass('warning');
		target.parent().addClass('warning');
		if(options['onRowSelect'] != null && options['onRowSelect'] != undefined)
			options['onRowSelect'](target.closest('tr').index()+1, rowData);
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
		//alert('recordsinpage :'+recordsinpage+'totalRecs :'+totalRecs+'pageNum :'+pageNum)
		
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
		$('#'+objId).find('tbody').empty();
		$('#'+objId).find('tbody').html(tabbody.children());
		return tabbody;
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
			var url = opts['dataurl'].split('?');
			var action = url[0];
			var params = 'pageNum='+pageNum+'&recordsPerPage='+recordsinpage+'&sortColumn='+sortColumn+'&sortOrder='+sortOrder+'&'+url[1];
			$.ajax({
				url: action,
				method:'GET',
				data:params,
				dataType:'json',
				success: function(data){
					//alert(data.list);
					getTableBody($object, opts, pageNum, {totalrecords:data.totalRecords, propertyList:data.list});
					
				},
				error: function(textStatus,errorThrown){
					return {totalrecords:0, propertyList:data.propertyList};
				}
			});
		}

		return tabbody;
	}
	
	function formatgrid($object, opts) {
		var numCols = opts.columndetails.length;
		var objId = $object.attr('id');
		
		var headerRow = $('<tr/>', {'id':objId+'-column-headings'});
		if(opts.checkboxrequired == true) {
			numCols++;
			headerRow.append($('<th/>', {css:{'text-align':'center'}}).append($('<input/>', {'type':'checkbox', 'id':objId+'-row-checkbox-all'})));
		}
		$.each(opts.columndetails, function(key, value){
			var spanObj = '';
			/* *
			if(value['sortable'] == true) {
				spanObj = $('<span/>',{'class':'glyphicon glyphicon-sort pull-right clickable'}).data('column-name',value['name']);
			}
			* */
			headerRow.append($('<th/>', {'text':value['title']}).append(spanObj));
		});
		//headerRow.append($('<th/>', {'text':''}));
		/* *
		var toolsRow = $('<tr/>');
		var toolsCol= $('<td/>', {'colspan':numCols+1, 'class':'boot-grid-toolbar'});
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
		* */
		var tableObj = $('<table/>', {'id':objId+'-table', /*'width': opts['width'],*/ 'class':'table table-bordered'});
		var tableHeading = (opts['title']==null || opts['title']=='' || opts['title']==undefined)? null : $('<tr/>').append($('<th/>', {'colspan':numCols, 'text':opts['title']}))
		tableObj.append($('<thead/>', {'style':'background-color: #f3f3f3;'}).append(tableHeading)
							.append(headerRow));
		/* *
		var tableBody = $('<tbody/>').append(toolsRow);
		* */
		var tableBody = $('<tbody/>');
		tableObj.append(tableBody);
		$object.append($('<small/>')
				.append(tableObj))
			.addClass('table-responsive boot-grid-container');
		/* *
		var tbody1 = preparePage($object, opts, 1, '', '');
		$object.find(objId+'-tools').attr('id', '1');
		$object.find(objId+'-paging').attr('id', '2');
		$object.on('click', '#'+objId+'-forward', {objectId:objId}, function(e){nextPage(e);});
		$object.on('click', '#'+objId+'-backward', {objectId:objId}, function(e){previousPage(e);});
		$object.on('click', '#'+objId+'-first', {objectId:objId}, function(e){firstPage(e);});
		$object.on('click', '#'+objId+'-last', {objectId:objId}, function(e){lastPage(e);});
		* */
		$object.unbind().on('click', '#'+objId+'-column-headings > th > span.clickable', {objectId:objId}, function(e){sortByColumn(e);});
		$object.on('click', '#'+objId+'-table > tbody > tr.clickable', {objectId:objId}, function(e){rowSelection(e);});
		$object.on('change', '#'+objId+'-row-checkbox-all', {objectId:objId}, function(e){toggleRowSelection(e);});
		$object.on('change', 'input:checkbox[name="'+objId+'-row-checkbox'+'"]', {objectId:objId}, function(e){rowChecked(e);});
	};
}(jQuery));