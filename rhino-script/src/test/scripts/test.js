
module.exports = {

	_test_Object: function() {
		var self = this;
		
		print('typeof Object=' + typeof Object);
		print('typeof Object.prototype=' + typeof Object.prototype);
		
		var _proto = Object.prototype;
		var _str = '\n';
		for ( var i in _proto ) {
			_str += i + ': ' + _proto[i] + '\n';
		}
		print(_str);
		
	},
	
	_test_Response: function() {
		var self = this;
	
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
		
		return self._test_Response();
		
		
		
		print('count=' + JSResponse.count);
		print(typeof JSResponse.constructor.STATUS_OK);
		
		var _rsp = new Response(200, 'application/json');
		_rsp.data = {
			time: new Date().getTime(),
			func: 'show'
		};
		
		print('response: ' + JSON.stringify(_rsp));
		
		return _rsp;
	}
}