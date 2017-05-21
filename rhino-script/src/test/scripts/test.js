
module.exports = {
	
	
	show: function(request) {
		
		var self = this;
		
		print('request=' + JSON.stringify(request));
		
		var _rsp = new Response(200, 'application/json');
		_rsp.data = {
			time: new Date().getTime(),
			func: 'show',
			req: request.data
		};
		
		print('response: ' + JSON.stringify(_rsp));
		
		return _rsp;
	}
}