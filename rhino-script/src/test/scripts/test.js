
module.exports = {
	
	
	show: function(request) {
		
		var self = this;
		
		var _system = system.env;
		print('system=' + typeof _system);
		
		var _rsp = new Response(200, 'application/json');
		_rsp.data = {
			time: new Date().getTime(),
			func: 'show',
			req: request.data
		};
		
		return _rsp;
	}
}