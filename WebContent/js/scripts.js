var zmittagApp = angular.module('zmittagApp', [
    'angular-md5',
    'google-maps'.ns(),
    'ngAnimate',
    'ngAria',
    'ngMaterial',
    'LocalStorageModule'
]);

zmittagApp.config(function (localStorageServiceProvider) {
    localStorageServiceProvider.setPrefix('zmittagApp');
    localStorageServiceProvider.setStorageCookie(0, '/');
});

zmittagApp.controller('mainController', function($scope, $http, md5, localStorageService) {
    $scope.api = "http://172.27.9.66:8080/Zmittag/api/";

    $scope.destinations = [];
    $http.get($scope.api+'groups/list/').success(function(data) {
        $scope.destinations = data;
    });

    $scope.user = {
        "id": null,
        "name": null,
        "email": '',
        "submitted": false
    };

    if(localStorageService.isSupported) {
        $scope.user = {
            "id": localStorageService.get('user.id'),
            "name": localStorageService.get('user.name'),
            "email": localStorageService.get('user.email') == null ? '' : localStorageService.get('user.email'),
            "submitted": localStorageService.get('user.submitted') == null ? false : true
        };
    }

    $scope.data = {
      maxIndex : 1,
      selectedIndex : 0,
      locked : true
    };

    $scope.isActiveValue = null;

    $scope.setActive = function(id) {
        $scope.isActiveValue == id;
    };

    $scope.isActive = function(id) {
        //console.log(id);
        return id == $scope.isActiveValue ;
    };

    $scope.isValidUser = function() {
        if ($scope.user.name != null && $scope.isValidEmail($scope.user.email)) {
            return true;
        };

        return false;
    }

    $scope.isValidEmail = function(email) {
        return /^([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22))*\x40([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d))*$/.test( email ); 
    };

    $scope.userIsSubmitted = function() {
        return $scope.user.submitted;
    }

    $scope.submitUserCredentials = function() {
        if ($scope.isValidEmail) {
            localStorageService.set('user.id', $scope.user.id);
            localStorageService.set('user.name', $scope.user.name);
            localStorageService.set('user.email', $scope.user.email);
            localStorageService.set('user.submitted', true);
            $scope.user.submitted = true;
        };
        console.log($scope.user);
    }

    $scope.md5 = function(value) {
        return md5.createHash(value);
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
                '<img src="http://www.gravatar.com/avatar/{{ md5(user.email) }}?s=40" alt="{{ user.name }}">' +
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