function PublicProfileCtrl($scope, $http) {

	$scope.user = {};
	$scope.connectionStatus='none';
	var profile_id=$scope.profileId;
	

	$scope.sendConnection = function() {
		$http(
				{
					method : 'GET',
					url : base_url + 'api/user/' + user_id + '/connection-'
							+ profile_id + '/send'
				}).success(function(data, status, headers, config) {
					getConnectionStatus();
		});

	}

	$scope.acceptConnection = function() {
		$http(
				{
					method : 'GET',
					url : base_url + 'api/user/' + user_id + '/connection-'
							+ profile_id + '/accept'
				}).success(function(data, status, headers, config) {
					getConnectionStatus();
		});

	}
	
	$scope.ignoreConnection = function() {
		$http(
				{
					method : 'GET',
					url : base_url + 'api/user/' + user_id + '/connection-'
							+ profile_id + '/ignore'
				}).success(function(data, status, headers, config) {
					getConnectionStatus();
		});

	}

	function getConnectionStatus() {
		$http(
				{
					method : 'GET',
					url : base_url + 'api/user/' + user_id + '/connection-'
							+ profile_id + '/status'
				}).success(function(data, status, headers, config) {
					$scope.connectionStatus = data;
			//console.log($scope.connectionStatus);
		});
	}

	function reset() {
		$http({
			method : 'GET',
			url : base_url + 'api/user/' + profile_id + '/profile.json'
		}).success(function(data, status, headers, config) {
			$scope.user = data;
			if ($scope.user.avatarUrl) {
				$('#avatar').attr('src', base_url + $scope.user.avatarUrl);
			}		
			
		});

	}

	//alert('$scope.connectionStatus @'+$scope.connectionStatus);
	
	reset();
	getConnectionStatus();
	
}
