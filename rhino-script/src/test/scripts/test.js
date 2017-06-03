
module.exports = {
	
	sum: function(request) {
		var self = this;
		print('request=' + JSON.stringify(request));
		
		var _ng = ng;
		print('ng=' + JSON.stringify(_ng));
		_ng.data = {
			start: 0,
			count: 15,
			type: 'todo'
		};
		print('ng=' + JSON.stringify(_ng));
		print('version=' + version);
		
		
		print('file=' + typeof _ng.file);
		var file = _ng.file;
		var _list = file.list('/');
		print('files: ' + JSON.stringify(_list));
		
		var _rsp = new Response(200, 'application/json');
		_rsp.data = {
			time: new Date().getTime(),
			func: 'show',
			array: request.data
		};
		
		return _rsp;
	}
};
