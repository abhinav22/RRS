function PublicProfileCtrl($scope, $http) {

	$scope.user = {};
	$scope.connectionStatus='none';
	var profile_id=$scope.profileId;
	
	$scope.isMySelf=false;
	
	function checkMySelf(){
//		alert('(user_id==profile_id)'+(user_id==profile_id));
//		alert('(user_id===profile_id)'+(user_id===profile_id));
		return $scope.isMyself=(user_id==profile_id);
	}
	
	$scope.sendConnection = function() {
		$http(
				{
					method : 'GET',
					url : base_url + 'api/user/' + user_id + '/connection-'
							+ profile_id + '/send'
				}).success(function(data, status, headers, config) {
			$scope.connectionStatus = data;
		});

	}

	$scope.acceptConnection = function() {
		$http(
				{
					method : 'GET',
					url : base_url + 'api/user/' + user_id + '/connection-'
							+ profile_id + '/accept'
				}).success(function(data, status, headers, config) {
			$scope.connectionStatus = data;
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
			//alert($scope.connectionStatus);
		});
	}

	function reset() {
		$http({
			method : 'GET',
			url : base_url + 'api/user/' + user_id + '/profile.json'
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
	checkMySelf();
	
}
