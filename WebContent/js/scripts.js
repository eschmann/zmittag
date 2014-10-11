var zmittagApp = angular.module('zmittagApp', [
    'angular-md5',
    'google-maps'.ns(),
    'ngAnimate',
    'ngAria',
    'ngMaterial'
]);

zmittagApp.controller('mainController', function($scope, $http, md5) {
    $scope.api = "http://172.27.9.66:8080/Zmittag/api/";

    $scope.destinations = [];
    $http.get($scope.api+'groups/list/').success(function(data) {
        $scope.destinations = data;
    });

    $scope.data = {
      maxIndex : 1,
      selectedIndex : 0,
      locked : true
    };

    $scope.user = {
        "name": null,
        "email": null,
        "hash": null
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
        //console.log('geolocation error');
    });
});

zmittagApp.directive('gravatar', function() {
    return {
        restrict: 'AE',
        template: 
            '<span class="user">' +
                '<img src="http://www.gravatar.com/avatar/{{ user.hash }}?s=40" alt="{{ user.name }}">' +
            '</span>',
        replace: true
    };
});


zmittagApp.directive('tfFloat', function() {
    return {
      restrict: 'E',
      replace: true,
      scope : {
        fid : '@?',
        value : '='
      },
      compile : function() {
        return {
          pre : function(scope, element, attrs) {
            // transpose `disabled` flag
            if ( angular.isDefined(attrs.disabled) ) {
              element.attr('disabled', true);
              scope.isDisabled = true;
            }

            // transpose the `label` value
            scope.label = attrs.label || "";
            scope.fid = scope.fid || scope.label;

            // transpose optional `type` and `class` settings
            element.attr('type', attrs.type || "text");
            element.attr('class', attrs.class );
          }
        }
      },
      template:
        '<material-input-group ng-disabled="isDisabled">' +
          '<label for="{{fid}}">{{label}}</label>' +
          '<material-input id="{{fid}}" ng-model="value">' +
        '</material-input-group>'
    };
});