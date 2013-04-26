function RecommendedResCtrl($scope, $http) {

	$scope.res = [];

	$scope.like = function(index) {
		var res_data=$scope.res[index];
		var res_id=res_data.resource.id;
		alert('resource id@'+res_id);
		
		$http({
			method : 'GET',
			url : base_url + 'api/res/' + res_id + '/like.json'
		}).success(function(data, status, headers, config) {
			res_data.liked=true;
			res_data.likedCount++;
			
			$scope.res[index]=res_data;
		});

	}

	function reset() {
		$http({
			method : 'GET',
			url : base_url + 'api/res/recommended.json'
		}).success(function(data, status, headers, config) {
			$scope.res = data;
			alert($scope.res);
		});

	}

	// alert('$scope.connectionStatus @'+$scope.connectionStatus);

	reset();

}
