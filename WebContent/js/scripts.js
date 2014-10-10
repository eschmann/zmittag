var zmittagApp = angular.module('zmittagApp', []);

zmittagApp.controller('mainController', function($scope) {
    $scope.message = "Hello world";
    $scope.destinations = [{"name": "Basilico"}, {"name": "Pickwick"}, {"name": "Thai place"}];
    $scope.user = {
        "name": "Max Muster",
        "email": "max@muster.ch"
    };
});

zmittagApp.directive('gravatar', function() {
    return {
        restrict: 'AE',
        template: '<span class="user">' +
                    '<img src="http://www.gravatar.com/avatar/{ md5(user.email) }?s=30" alt="{ user.name }">' +
                '</span>',
        replace: true
    };
});