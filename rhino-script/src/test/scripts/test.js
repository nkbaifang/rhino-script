
module.exports = {
	
	_test_Response: function() {
		var self = this;
		
		print('Response.STATUS_OK=' + Response.STATUS_OK);
	
		var _obj1 = new Response(404);
		print('typeof _obj1=' + typeof _obj1);
		print('_obj1.status: ' + JSON.stringify(_obj1.status));
		
		_obj1.data = {
			success: true,
			message: 'Finished.'
		};
		_obj1.k = 56.7;
		print('_obj1: ' + JSON.stringify(_obj1));
		
		return _obj1;
	},
	
	show: function(request) {
		
		var self = this;
		
		print('request.data=' + typeof request.data);
		
		var _rsp = new Response(200, 'application/json');
		_rsp.data = {
			time: new Date().getTime(),
			func: 'show'
		};
		
		print('response: ' + JSON.stringify(_rsp));
		
		return _rsp;
	}
}