var zmittagApp = angular.module('zmittagApp', [
    'angular-md5',
    'google-maps'.ns()
]);

zmittagApp.controller('mainController', function($scope, $http, md5) {
    $scope.api = "http://172.27.9.66:8080/Zmittag/api/";

    $scope.destinations = [];
    $http.get($scope.api+'groups/list/').success(function(data) {
        $scope.destinations = data;
    });

    $scope.user = {
        "name": null,
        "email": null
    };

    $scope.map = {
        center: {
            latitude: 45,
            longitude: -73
        },
        zoom: 8
    };

    navigator.geolocation.getCurrentPosition(function(position) {
        $scope.map.center = {
            latitude: position.coords.latitude,
            longitude: position.coords.longitude
        };
        $scope.$apply();
    }, function() {
        console.log('geolocation error');
    });

    $scope.test = $http.get($scope.api+'members/list/').success(function(data) {
        $scope.user = {
            name: data[0].name,
            email: md5.createHash(data[0].email)
        }
    });
});

zmittagApp.directive('gravatar', function() {
    return {
        restrict: 'AE',
        template: 
            '<span class="user">' +
                '<img src="http://www.gravatar.com/avatar/{{ user.email }}?s=40" alt="{{ user.name }}">' +
            '</span>',
        replace: true
    };
});