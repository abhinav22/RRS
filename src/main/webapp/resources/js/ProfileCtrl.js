function ProfileCtrl($scope, $http) {

	$scope.user = {};

	$scope.copy = {};

	$scope.email = {};

	$scope.link = {};

	$scope.phone = {};

	$scope.isEditable = false;

	$scope.isDirty = false;

	function checkDirty() {
		if ($scope.isEditable && (!angular.equals($scope.user, $scope.copy))) {
			$scope.isDirty = true;
		} else {
			$scope.isDirty = false;
		}
	}

	$scope.markEditable = function() {
		$scope.isEditable = true;
		// alert($scope.isEditable);
	}

	$scope.delEmail = function(index) {
		$scope.user.emails.splice(index, 1);
		checkDirty();
	}

	$scope.addEmail = function() {
		$scope.user.emails.push(email);
		checkDirty();
	}

	$scope.delLink = function(index) {
		$scope.user.links.splice(index, 1);
		checkDirty();
	}

	$scope.addLink = function() {
		$scope.user.links.push(link);
		checkDirty();
	}

	$scope.delPhone = function(index) {
		$scope.user.phones.splice(index, 1);
		checkDirty();
	}

	$scope.addPhone = function() {
		$scope.user.phonse.push(phone);
		checkDirty();
	}

	$scope.save = function() {
		$http.post(base_url + 'api/user/me', $scope.user);
		reset();
	}

	$scope.reset = function() {
		$http({
			method : 'GET',
			url : base_url + 'api/user/me.json'
		}).success(function(data, status, headers, config) {
			$scope.user = data;
			angular.copy($scope.user, $scope.copy);
			console.log($scope.user || json);
		});

		$scope.isDirty = false;
		$scope.isEditable = false;
	}

	$scope.reset();
}

function AppCtrl($scope) {
	console.log("app contoller");
}

angular.module("MyApp", []);
