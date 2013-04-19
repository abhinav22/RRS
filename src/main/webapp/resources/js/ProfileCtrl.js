function ProfileCtrl($scope, $http){
	
    var user=$scope.user={};
	
	var email={};
	
	var link={};
	
	$scope.delEmail(index){
		user.emails.slice(index);
	}
	
	
	$scope.addEmail(){
		
	}
	
	$scope.delLink(index){
		user.links.slice(index);
	}
	
	$scope.delPhone(index){
		user.phones.slice(index);
	}
	
	$scope.save=function(){
		$http.post(base_url+'/api/user/profile-'+user_id).sucess(
			console.log('ok');
		);
		reset();
	}
	
	$scope.reset=function(){
		$http.get(base_url +'/api/user/profile-'+user_id).success(
			function get(data, status, headers, config){
				user=data;
			}
		);
	}
	
	reset();
}


