angular.module('app.controllers', [])

.controller('DashCtrl', function($scope) {
    //$scope.uuid = window.device.uuid;
    document.addEventListener("deviceready", function(){
	console.log("device ready");
	$scope.uuid = device.uuid;
	var permanentStorage = window.localStorage;
	window.localStorage.setItem("appKey", "1234567890");
	$scope.$apply();
	
    }, false);

    
    $scope.takePicture = function(){
	navigator.camera.getPicture(
	    function(imageURI){
		var image = document.getElementById('myImage');
		image.src = "data:image/jpeg;base64," + imageURI;
		
	    }, 
	    function(message){
		$scope.image_src = "/link/image-dummy-error.jpg";
		$scope.apply();
	    }, 
	    { quality: 50,
	      destinationType: Camera.DestinationType.DATA_URL
	    });
    },
    $scope.shareTwitter=function(){
	window.plugins.socialsharing.shareViaTwitter('This was shared via twitter');
    },
    $scope.shareFacebook=function(){
	window.plugins.socialsharing.shareViaFacebook('Facebook share', null, 'http://centipair.com');}
})

.controller('FriendsCtrl', function($scope, Friends) {
    $scope.friends = Friends.all();
})

.controller('FriendDetailCtrl', function($scope, $stateParams, Friends) {
    $scope.friend = Friends.get($stateParams.friendId);
    $scope.appKey = window.localStorage.getItem("appKey");
    $scope.message = "Loading Location new sdk";
    navigator.geolocation.getCurrentPosition(
        function(position) {
	    $scope.latitude = position.coords.latitude;
            $scope.longitude = position.coords.longitude;
	    $scope.message = "Location Loaded";
	    $scope.$apply();
        },
        function(error) {
	    console.log(error.code);
	    console.log(error.message);	    
	    $scope.message = error.message;
	    $scope.$apply();
        },
	{timeout: 60000, maximumAge: 75000, enableHighAccuracy: false}
    );
})

.controller('AccountCtrl', function($scope) {
})

.controller('FeedCtrl', function($scope) {
})


 .controller('MapCtrl', function($scope, $ionicLoading) {
     $scope.loading = $ionicLoading.show({
	 content: 'Loading Map...',
	 showBackdrop:false
    });
     $scope.loadMap = function(latitude, longitude){
	 var mapOptions = {
		center: new google.maps.LatLng(latitude, longitude),
		zoom: 16,
		mapTypeId: google.maps.MapTypeId.ROADMAP
	    };
	 var map = new google.maps.Map(document.getElementById("map"),
				       mapOptions);
	 $scope.loading.hide();
     }
     
     navigator.geolocation.getCurrentPosition(
        function(position) {
	    $scope.loadMap(position.coords.latitude, position.coords.longitude);
        },
        function(error) {
	    console.log(error.code);
	    console.log(error.message);	    
	    $scope.message = error.message;
	    $scope.$apply();
        },
	{timeout: 60000, maximumAge: 75000, enableHighAccuracy: false}
    );
 });
