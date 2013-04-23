function ProfileCtrl($scope, $http) {

	$scope.user = {};

	$scope.copy = {};

	$scope.email = {};

	$scope.link = {};

	$scope.phone = {};

	$scope.isEditable = false;

	$scope.isDirty = false;

	$scope.emailTypes = [ 'Private', 'Work' ];
	$scope.linkTypes = [ 'Google Plus', 'Twitter', 'Facebook' ];
	$scope.phoneTypes = [ 'Home', 'Office' ];	
	$scope.salutationLines=[ 'NONE', 'FIRSTNAME',  'FIRSTNAME_AND_LASTNAME','FIRSTNAME_FIRST_CHAR_OF_LASTNAME'];

	function checkDirty() {
		if ($scope.isEditable && (!angular.equals($scope.user, $scope.copy))) {
			$scope.isDirty = true;
		} else {
			$scope.isDirty = false;
		}
	}
	
	$scope.setName=function(){
		$('#nameModal').modal('hide');
		checkDirty();
	}

	$scope.markEditable = function() {
		$scope.isEditable = true;
		// alert($scope.isEditable);
	}

	$scope.delEmail = function(index) {
		$scope.user.emails.splice(index, 1);
		checkDirty();
	}

	$scope.initAddEmail = function() {
		$scope.email = {
			type : '',
			content : '',
			verified : false
		};
		$('#emailModal').modal('show');
	}

	$scope.addEmail = function() {
		$scope.user.emails.push($scope.email);
		$('#emailModal').modal('hide');
		checkDirty();
	}

	$scope.delLink = function(index) {
		$scope.user.links.splice(index, 1);
		checkDirty();
	}

	$scope.initAddLink = function() {
		$scope.link = {
			type : '',
			content : ''
		};
		$('#linkModal').modal('show');
	}

	$scope.addLink = function() {
		$scope.user.links.push($scope.link);
		$('#linkModal').modal('hide');
		checkDirty();
	}

	$scope.delPhone = function(index) {
		$scope.user.phones.splice(index, 1);
		checkDirty();
	}

	$scope.initAddPhone = function() {
		$scope.phone = {
			type : '',
			content : ''
		};
		$('#phoneModal').modal('show');
	}

	$scope.addPhone = function() {
		$scope.user.phones.push($scope.phone);
		$('#phoneModal').modal('hide');
		checkDirty();
	}

	$scope.save = function() {
		$http.post(base_url + 'api/user/me.json', angular.toJson($scope.user))
		     .success(
				function(data, status, headers, config) {
					reset();
				}
			)
			.error(
				function(data, status, headers, config) {
					alert(data);
					alert(status);
				}
			);

	}

	$scope.cancel = function() {
		angular.copy($scope.copy, $scope.user);
		$scope.isDirty = false;
		$scope.isEditable = false;
	}

	function reset() {
		$http({
			method : 'GET',
			url : base_url + 'api/user/me.json'
		}).success(function(data, status, headers, config) {
			$scope.user = data;
			angular.copy($scope.user, $scope.copy);
			if ($scope.user.avatarUrl) {
				$('#avatar').attr('src', base_url + $scope.user.avatarUrl);
			}
			// alert('avatar url@'+$scope.user.avatarUrl);
			// alert('avatar src@'+$('#avatar').attr('src'));
		});

		$scope.isDirty = false;
		$scope.isEditable = false;
	}

	reset();
}

