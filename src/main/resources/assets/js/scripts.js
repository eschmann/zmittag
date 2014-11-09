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

zmittagApp.controller('mainController', function($scope, $log, $filter, $http, $mdToast, md5, localStorageService) {
	
	var hostAndPort = location.protocol+'//'+location.hostname+(location.port ? ':'+location.port: '');
    //var hostAndPort = 'http://178.62.175.20:8080';
    var api = hostAndPort + "/zmittag/api/";

    //
    //  Tabbed Nav
    //

    $scope.nav = {
        current: 0,
        maxIndex: 1,
        locked: true
    };

    //
    //  Current User
    //

    $scope.user = {
        "id": null,
        "name": null,
        "email": null,
        "submitted": false
    };

    if(localStorageService.isSupported) {
        $scope.user = {
            "id": localStorageService.get('user.id'),
            "name": localStorageService.get('user.name'),
            "email": localStorageService.get('user.email'),
            "submitted": localStorageService.get('user.submitted') == null ? false : true
        };
    }

    $scope.md5 = function(value) {
        return md5.createHash(value);
    };

    $scope.isValidUser = function() {
        if ($scope.user.name != null && $scope.isValidEmail($scope.user.email)) { return true; };
        return false;
    }

    $scope.isValidEmail = function(email) {
        return /^([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22))*\x40([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d))*$/.test( email ); 
    };

    // persist user info in localstorage
    $scope.submitUserCredentials = function() {
        if ($scope.isValidEmail) {
            localStorageService.set('user.id', $scope.user.id);
            localStorageService.set('user.name', $scope.user.name);
            localStorageService.set('user.email', $scope.user.email);
            localStorageService.set('user.submitted', true);
            $scope.user.submitted = true;
        };
    }

    //
    //  Current Suggestions
    //

    $scope.destinations = [];
    $http.get(api+'groups/list/').success(function(data) {
        $scope.destinations = data;
    });

    // Check if current user joined a suggestion
    $scope.hasJoined = function(id) {
        for (var i = 0; i < $scope.destinations.length; i++) {
            if ($scope.destinations[i].id == id) {
                if (!$scope.destinations[i].memberList) { continue; };
                for (var t = 0; t < $scope.destinations[i].memberList.length; t++) {
                    if ($scope.destinations[i].memberList[t].email == $scope.user.email) {
                        return true;
                    };
                };
            };
        };
        return false;
    };

    // OnClick Events
    $scope.isActiveValue = null;
    $scope.setActive = function(id) {
        $scope.isActiveValue = id;
    };

    $scope.isActive = function(id) {
        return id == $scope.isActiveValue;
    };

    $scope.joinGroup = function(id) {
        $http.post(api+'groups/'+id+'/join', {
            member: $scope.user.name,
            email: $scope.user.email
        }).success(function() {
            triggerToast('Success!', true);
        }).error(function() {
            triggerToast('Whoops. Something went wrong.<br>Please try again later.');
        });
    };

    //
    //  Recommendations
    //

    $scope.recommendations = [];
    $http.get(api+'restaurants/bestRating').success(function(data){
        $scope.recommendations = data;
    });

    //
    //  Tags
    //
    
    $scope.tags = [];
    $http.get(api+'tags/list').success(function(data){
        $scope.tags = data;
    });

    //
    //  Toasts
    //

    function triggerToast(text, reload, duration, position) {
        reload = typeof reload != 'undefined' ? reload : false;
        duration = typeof duration != 'undefined' ? duration : 2000;
        position = typeof position != 'undefined' ? position : 'top right';

        $mdToast.show({
            template: '<md-toast>'+text+'</md-toast>',
            duration: duration,
            position: position
        });

        if (reload) { window.setTimeout(function() { window.location.reload(false); }, 800); };
    }

    //
    //  Archive
    //

    $scope.archive = [];
    $http.get(api+'restaurants/list/').success(function(data) {
        $scope.archive = data;
    });

    // $scope.suggestGroup = function() {
    //     $http.post(api+'groups/add', {
    //         name: $scope.marker.name,
    //         member: $scope.user.name,
    //         email: $scope.user.email
    //     }).success(function() {
    //         triggerToast('Success!', true);
    //     }).error(function() {
    //         triggerToast('Whoops. Something went wrong.<br>The destination was created but you did not join.', true);
    //     });
    // };

    // $scope.joinGroup = function(id) {
    //     $http.post(api+'groups/'+id+'/join', {
    //         member: $scope.user.name,
    //         email: $scope.user.email
    //     }).success(function() {
    //         triggerToast('Success!', true);
    //     }).error(function() {
    //         triggerToast('Whoops. Something went wrong.<br>Please try again later.');
    //     });
    // };




    // TODO:

    // $scope.options = {scrollwheel: false, query: "london"};
    // $scope.searchbox = {
    //     template:'searchbox.tpl.html',
    //     position:'top-center',
    //     events: "places_changed: 'testing()'"
    // };

    // $scope.gmaps;
    // $scope.search = {
    //     archive: null
    // };

    // $scope.marker = {
    //     id: 0,
    //     name: null,
    //     address: null,
    //     coords: {
    //         latitude: null,
    //         longitude: null
    //     },
    //     location: [null, null],
    //     website: null
    // };

    // $scope.map = {
    //     center: {
    //         latitude: 45,
    //         longitude: -73
    //     },
    //     zoom: 8
    // };

    // $scope.createRestaurantAndSuggest = function(flag) {
    //     $http.post(api+'restaurants/add', {
    //         name: $scope.marker.name,
    //         latitude: $scope.marker.coords.latitude,
    //         longitude: $scope.marker.coords.longitude,
    //         address: $scope.marker.address,
    //         url: $scope.marker.website,
    //         tags: []
    //     }).success(function() {
    //         if (flag) {
    //             $http.post(api+'groups/add', {
    //                 name: $scope.marker.name,
    //                 member: $scope.user.name,
    //                 email: $scope.user.email
    //             }).success(function() {
    //                 triggerToast('Success!', true);
    //             }).error(function() {
    //                 triggerToast('Whoops. Something went wrong.<br>The destination was created but you did not join.', true);
    //             });
    //         }else {
    //             triggerToast('Success!', true);
    //         }
    //     }).error(function() {
    //         triggerToast('Whoops. Something went wrong.<br>Please try again later.');
    //     });
    // };

    // $scope.updateMap = function(input) {
    //     if (!input) { return; };

    //     $scope.marker = input;
    //     $scope.marker.id = 0;
    //     $scope.marker.options = {
    //         title: input.name,
    //         animation: 'DROP',
    //         labelContent: $scope.marker.name,
    //         labelStyle: {
    //             zIndex: 9999
    //         }
    //     };

    //     $scope.map.zoom = 12;
    //     $scope.map.center = input.coords;
    // };

    // navigator.geolocation.getCurrentPosition(function(position) {
    //     $scope.map.center = {
    //         latitude: position.coords.latitude,
    //         longitude: position.coords.longitude
    //     };
    //     $scope.$apply();
    // }, function() {
    //     //console.log('geolocation error');
    // });

    // $scope.searchRestaurants = function(pattern) {
    //     $http.post(api+'restaurants/searchByName', {
    //         searchPattern: pattern
    //     }).success(function(data) {
    //         $scope.search.results = data;
    //     }).error(function(){
    //         $scope.search.results = [];
    //     });     
    // };

    // $scope.tagFilteredRestaurants = [];
    // $scope.searchTagChanged = function(tag) {
    //     $http.post(api+'restaurants/ensureTag', {
    //         searchTag: tag
    //     }).success(function(data) {
    //         $scope.tagFilteredRestaurants = data;
    //     });     
    // };
});

//
//  Gravatar Icon
//

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

//
//  Google Maps Search
//

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