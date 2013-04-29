function ResourceCtrl($scope, $http) {

	$scope.res = [];

	$scope.commentContent = '';

	$scope.base_url = base_url;

	$scope.selectedIndex = 0;

	$scope.keyword = '';

	$scope.page = 0;

	$scope.cnt = 0;

	$scope.size = 10;

	$scope.isHasMore = false;

	$scope.viewDetails = function(index) {
		var res_data = $scope.res[index];
		var res_id = res_data.resource.id;
		// alert('resource id@' + res_id);
		// $scope.$apply(function(){
		window.location.href = window.location.protocol + '//'
				+ window.location.host + base_url + 'resource/' + res_id;
		// });
	}

	$scope.like = function(index) {
		var res_data = $scope.res[index];
		var res_id = res_data.resource.id;
		// alert('resource id@' + res_id);

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
		$scope.selectedIndex = index;
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
					$scope.commentContent = '';
					$('#commentModal').modal('hide');
				}).error(function(data, status, headers, config) {
			alert(data);
			alert(status);
		});

	}

	$scope.go = function() {
		// alert($scope.keyword);
		reset();
		search();
		total();
	}

	$scope.more = function() {
		$scope.page++;
		search();
	}

	// alert('$scope.connectionStatus @'+$scope.connectionStatus);

	function search() {

		if ($scope.keyword != '') {
			$http(
					{
						method : 'GET',
						url : base_url + 'api/res/s-' + $scope.keyword + '-'
								+ $scope.page + '-' + $scope.size + '.json'
					}).success(function(data, status, headers, config) {

				for ( var i = 0; i < data.length; i++) {
					$scope.res.push(data[i]);
				}
			});
		}

	}

	function total() {
		if ($scope.keyword != '') {
			$http({
				method : 'GET',
				url : base_url + 'api/res/c-' + $scope.keyword
			}).success(function(data, status, headers, config) {
				$scope.cnt = data;
				// alert($scope.res);
			});
		}
	}

	function reset() {
		// $scope.keyword = '';
		$scope.res = [];
		$scope.page = 0;
		$scope.cnt = 0;
		$scope.size = 10;
		$scope.isHasMore = false;

	}

	$scope.$watch('cnt', function(newValue, oldValue) {
		$scope.isHasMore = $scope.page + 1 < Math.ceil(newValue / $scope.size);
	});
	// reset();

}
