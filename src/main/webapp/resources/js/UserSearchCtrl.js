function UserSearchCtrl($scope, $http) {

	$scope.data = [];

	$scope.commentContent = '';

	$scope.base_url = base_url;

	$scope.selectedIndex = 0;

	$scope.keyword = '';

	$scope.page = 0;

	$scope.cnt = 0;

	$scope.size = 10;

	$scope.isHasMore = false;

	$scope.viewDetails = function(index) {

		window.location.href = window.location.protocol + '//'
				+ window.location.host + base_url + 'user/public-profile-' + $scope.data[index].id;
		// });
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


	function search() {
		if ($scope.keyword != '') {
			$http(
					{
						method : 'GET',
						url : base_url + 'api/user/s-' + $scope.keyword + '-'
								+ $scope.page + '-' + $scope.size + '.json'
					}).success(function(data, status, headers, config) {

				for ( var i = 0; i < data.length; i++) {
					$scope.data.push(data[i]);
				}
			});
		}

	}

	function total() {
		if ($scope.keyword != '') {
			$http({
				method : 'GET',
				url : base_url + 'api/user/c-' + $scope.keyword
			}).success(function(data, status, headers, config) {
				$scope.cnt = data;
				// alert($scope.data);
			});
		}
	}

	function reset() {
		// $scope.keyword = '';
		$scope.data = [];
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
