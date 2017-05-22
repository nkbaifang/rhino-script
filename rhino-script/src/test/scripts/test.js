
var _array_sum = function(a) {
	var self = this;
	var _sum = 0;
	for ( var i = 0; i < a.length; i++ ) {
		_sum += a[i];
	}
	return _sum;
};

module.exports = {
	
	sum: function(request) {
		var self = this;
		
		print('request=' + JSON.stringify(request));
		
		var _array = request.data || [];
		var _sum = _array_sum(_array);
		
		var _rsp = new Response(200, 'application/json');
		_rsp.data = {
			time: new Date().getTime(),
			func: 'show',
			array: request.data,
			sum: _sum
		};
		
		return _rsp;
	}
}