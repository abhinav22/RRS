function RecommendedResCtrl($scope, $http) {

	$scope.res = [];

	$scope.commentContent = '';

	$scope.base_url = base_url;
	
	$scope.selectedIndex=0;
	
	$scope.viewDetails=function(index){
		var res_data = $scope.res[index];
		var res_id = res_data.resource.id;
		//alert('resource id@' + res_id);
		//$scope.$apply(function(){
		window.location.href= window.location.protocol+'//'+window.location.host+base_url+'resource/'+res_id;
		//});
	}

	$scope.like = function(index) {
		var res_data = $scope.res[index];
		var res_id = res_data.resource.id;
		//alert('resource id@' + res_id);

		$http({
			method : 'GET',
			url : base_url + 'api/res/' + res_id + '/like.json'
		}).success(function(data, status, headers, config) {
			res_data.liked = true;
			res_data.likedCount++;

			$scope.res[index] = res_data;
		});

	}
	
	$scope.initAddComment = function(index) {
		$scope.selectedIndex=index;
		$('#commentModal').modal('show');
	}

	$scope.comment = function() {
		var res_data = $scope.res[$scope.selectedIndex];
		var res_id = res_data.resource.id;
		// alert('resource id@'+res_id);

		$http.post(base_url + 'api/res/' + res_id + '/comment.json',
				$scope.commentContent).success(
				function(data, status, headers, config) {
					res_data.commentedCount++;
					$scope.res[$scope.selectedIndex] = res_data;
					$scope.commentContent='';
					$('#commentModal').modal('hide');
				}).error(function(data, status, headers, config) {
			 alert(data);
			 alert(status);
		});

	}

	function reset() {
		$http({
			method : 'GET',
			url : base_url + 'api/res/recommended.json'
		}).success(function(data, status, headers, config) {
			$scope.res = data;
			// alert($scope.res);
		});

	}

	// alert('$scope.connectionStatus @'+$scope.connectionStatus);

	reset();

}
