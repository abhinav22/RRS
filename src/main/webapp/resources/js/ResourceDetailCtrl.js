function ResourceDetailCtrl($scope, $http) {

	$scope.item ={};
	$scope.comments=[];
	
	$scope.commentContent = '';

	$scope.base_url = base_url;

	$scope.viewUserProfile = function(index) {
		var user_id = $scope.comments[index].user.id;

		window.location.href = window.location.protocol + '//'
				+ window.location.host + base_url + 'user/public-profile-' + user_id+'';
		// });
	}

	$scope.like = function() {
		$http({
			method : 'GET',
			url : base_url + 'api/res/' + $scope.item_id + '/like.json'
		}).success(function(data, status, headers, config) {
			$scope.item.liked = true;
			$scope.item.likedCount++;
		});

	}

	$scope.initAddComment = function(index) {
		$scope.selectedIndex = index;
		$('#commentModal').modal('show');
	}

	$scope.comment = function() {
		//alert('save');
		$http.post(base_url + 'api/res/' + $scope.item_id + '/comment.json',
				$scope.commentContent
				).success(
					function(data, status, headers, config) {
						$scope.item.commentedCount++;
						$scope.commentContent = '';
						readComments();//refresh comments.
						$('#commentModal').modal('hide');
					}
				).error(function(data, status, headers, config) {
			alert(data);
			alert(status);
		});
	}

	function reset() {	
		readResource();
		readComments();
	}
	
	function readResource(){
		$http({
			method : 'GET',
			url : base_url + 'api/res/' + $scope.item_id+'/detail.json'
		}).success(function(data, status, headers, config) {
			$scope.item = data;
		});	
		
	}
	
	function readComments(){
		$http({
			method : 'GET',
			url : base_url + 'api/res/' + $scope.item_id+'/comments.json'
		}).success(function(data, status, headers, config) {
			$scope.comments = data;
		});
	}

	reset();
	//alert(item_id);
	//alert($scope.item_id);
}
