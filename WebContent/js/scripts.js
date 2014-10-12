var zmittagApp = angular.module('zmittagApp', [
    'angular-md5',
    'google-maps'.ns(),
    'ngAnimate',
    'ngAria',
    'ngMaterial',
    'LocalStorageModule',
    'google-maps'.ns()
]);

zmittagApp.config(function (localStorageServiceProvider) {
    localStorageServiceProvider.setPrefix('zmittagApp');
    localStorageServiceProvider.setStorageCookie(0, '/');
});

zmittagApp.controller('mainController', function($scope, $log, $filter, $http, $materialToast, md5, localStorageService) {
    $scope.api = "http://172.27.9.66:8080/Zmittag/api/";

    //$scope.map = {center: {latitude: 40.1451, longitude: -99.6680 }, zoom: 4 };
    $scope.options = {scrollwheel: false, query: "london"};
    $scope.searchbox = {
        template:'searchbox.tpl.html',
        position:'top-center',
        events: "places_changed: 'testing()'"
    };

    $scope.gmaps;
    $scope.search = '';
    $scope.marker = {
        id: 0,
        name: null,
        address: null,
        coords: {
            latitude: null,
            longitude: null
        },
        location: [null, null],
        website: null
    };

    $scope.map = {
        center: {
            latitude: 45,
            longitude: -73
        },
        zoom: 8
    };

    $scope.createRestaurantAndSuggest = function(flag) {
        $http.post($scope.api+'restaurants/add', {
            name: $scope.marker.name,
            latitude: $scope.marker.coords.latitude,
            longitude: $scope.marker.coords.longitude,
            address: $scope.marker.address,
            url: $scope.marker.website,
            tags: []
        }).success(function() {
            if (flag) {
                $http.post($scope.api+'groups/add', {
                    name: $scope.marker.name,
                    member: $scope.user.name,
                    email: $scope.user.email
                }).success(function() {
                    $materialToast.show({
                        template: '<material-toast>Success!</material-toast>',
                        duration: 2000,
                        position: 'top right'
                    });

                    window.setTimeout(function() {
                        window.location.reload(false);
                    }, 800);
                }).error(function() {
                    $materialToast.show({
                        template: '<material-toast>Whoops. Something went wrong.<br>The destination was created but you did not join.</material-toast>',
                        duration: 2000,
                        position: 'top right'
                    });

                    window.setTimeout(function() {
                        window.location.reload(false);
                    }, 800);
                });
            }else {
                $materialToast.show({
                    template: '<material-toast>Success!</material-toast>',
                    duration: 2000,
                    position: 'top right'
                });

                window.setTimeout(function() {
                    window.location.reload(false);
                }, 800);
            }
        }).error(function() {
            $materialToast.show({
                template: '<material-toast>Whoops. Something went wrong.<br>Please try again later.</material-toast>',
                duration: 2000,
                position: 'top right'
            });
        });
    };

    $scope.updateMap = function(input) {
        if (!input) {
            return;
        };
        $scope.marker = input;
        $scope.marker.id = 0;
        $scope.marker.options = {
            title: input.name,
            animation: 'DROP',
            labelContent: $scope.marker.name,
            labelStyle: {
                zIndex: 9999
            }
        };

        $scope.map.zoom = 12;
        $scope.map.center = input.coords;


        // $scope.marker.events = {
        //     click: function(marker, eventName, model, arguments) {
        //         console.log(marker);
        //     }
        // }
        // console.log(input);
    };

    // $scope.testing = function() {
    //     console.log('testing');
    // };

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
        $scope.isActiveValue = id;
    };

    $scope.isActive = function(id) {
        return id == $scope.isActiveValue;
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
        //console.log($scope.user);
    }

    $scope.md5 = function(value) {
        return md5.createHash(value);
    };

    $scope.joinGroup = function(id) {
        $http.post($scope.api+'groups/'+id+'/join', {
            member: $scope.user.name,
            email: $scope.user.email
        }).success(function() {
            $materialToast.show({
                template: '<material-toast>Success!</material-toast>',
                duration: 2000,
                position: 'top right'
            });
        }).error(function() {
            $materialToast.show({
                template: '<material-toast>Whoops. Something went wrong.<br>Please try again later.</material-toast>',
                duration: 2000,
                position: 'top right'
            });
        });
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

    $scope.search = {
        archive: ''
    };

    $scope.hasJoined = function(id) {
        for (var i = 0; i < $scope.destinations.length; i++) {
            if ($scope.destinations[i].id == id) {
                for (var t = 0; t < $scope.destinations[i].memberList.length; t++) {
                    if ($scope.destinations[i].memberList[t].email == $scope.user.email) {
                        return true;
                    };
                };
            };
        };
        return false;
    };
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

zmittagApp.directive('googleplaces', function() {
    return {
        require: 'ngModel',
        link: function(scope, element, attrs, model) {
            var options = {
                types: []
            };

            scope.gmaps = new google.maps.places.Autocomplete(element[0], options);

            google.maps.event.addListener(scope.gmaps, 'place_changed', function() {
                var place = scope.gmaps.getPlace();
                scope.updateMap({
                    name: place.name,
                    address: place.formatted_address,
                    coords: {
                        latitude: place.geometry.location.lat(),
                        longitude: place.geometry.location.lng()
                    },
                    location: [place.geometry.location.lat(), place.geometry.location.lng()],
                    website: place.website
                });

                scope.$apply(function() {
                    model.$setViewValue(element.val());
                    scope.updateMap();
                });
            });
        }
    };
});