function InviteCtrl($scope, $http) {

	$scope.data = {};

	$scope.email = '';
	
	$scope.hasOneEmail=false;

	$scope.go = function() {

		$http.post(base_url + 'api/user/invite.json', $scope.data)
		     .success(
		    	function(data, status, headers, config) {
					window.location.href = window.location.protocol + '//'
							+ window.location.host + base_url
							+ 'user/home';
					// });
				}
			)
			.error(
				function(data, status, headers, config) {
			alert(data);
			alert(status);
		});
	}
	
	$scope.delEmail=function(index){
		$scope.data.emails.splice(index, 1);
		if($scope.data.emails.length==0){
			$scope.hasOneEmail=false;
		}
	}
	
	$scope.addEmail=function(){
		//alert('addEmail');
		if($.inArray($scope.email, $scope.data.emails)>-1){
			$scope.existed=true;
			return;
		}
		$http.post(
				base_url + 'api/user/invite-check',
				$scope.email
			).success(function(_data, status, headers, config) {
			//alert(_data);
			if(_data=='false'){
				$scope.data.emails.push($scope.email);
				$scope.email='';
				$scope.existed=false;
				$scope.hasOneEmail=true;
			}else{
				$scope.existed=true;
			}
		});
		
	}

	function reset() {
		//alert("InviteCtrol reset");
		$http({
			method : 'GET',
			url : base_url + 'api/user/invite.json'

		}).success(function(_data, status, headers, config) {
			//alert(_data);
			$scope.data = _data;
		});
	}
	
	reset();

}
