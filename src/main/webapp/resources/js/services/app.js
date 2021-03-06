var myModule = angular.module("myAppModule", ['ngResource', 'ngRoute', 'angularFileUpload', 'ui.bootstrap'])
        .config(function ($routeProvider, USER_ROLES) {
        $routeProvider.when("/login",
            {
                templateUrl: 'resources/views/login.html',
                controller: 'loginController',
                data: {
                    authorizedRoles: [USER_ROLES.all]
                }

            })
            .when("/home",
            {
                templateUrl: 'resources/views/home.html',
                controller: 'homeController',
                data: {
                    authorizedRoles: [USER_ROLES.ROLE_USER]
                }

            })
            .when("/error",{
                templateUrl:'resources/views/error.html',
                data:{
                    authorizedRoles:[USER_ROLES.all]
                }
            }).when("/history/:path",{
                templateUrl: 'resources/views/history.html',
                controller: 'historyController',
                data: {
                    authorizedRoles: [USER_ROLES.ROLE_USER]
                },
                resolve: {
                    files: function($http, $route) {
                        var path = encodeURI($route.current.params.path);
                        return $http.get('fileHistory/list?filePath=' + path).then(function(response){
                            return response.data;
                        });
                    }
                }
            }).when("/logs",{
                templateUrl: 'resources/views/logs.html',
                controller: 'logsController',
                data: {
                    authorizedRoles: [USER_ROLES.ROLE_USER]
                }
            }).when("/",{
                templateUrl: 'resources/views/home.html',
                controller: 'homeController',
                data: {
                    authorizedRoles: [USER_ROLES.ROLE_USER]
                }
            });

        })
        .config(["$httpProvider",function($httpProvider) {
            $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

            var interceptor = ['$rootScope', '$q', function($rootScope, $q) {
                function success(response) {
                    return response;
                }

                function error(response) {
                    console.log("response: "+ response.status);
                    if (response.status === 401 && !response.config.ignoreAuthModule) {
                        var deferred = $q.defer();
                        $rootScope.$broadcast('event:auth-loginRequired', response);
                        return deferred.promise;
                    } else if (response.status === 403 && !response.config.ignoreAuthModule) {
                        $rootScope.$broadcast('event:auth-notAuthorized', response);
                    }
                    // otherwise, default behaviour
                    return $q.reject(response);
                }

                return function(promise) {
                    return promise.then(success, error);
                };

            }];
            $httpProvider.responseInterceptors.push(interceptor);
        }])
        .run(function ($rootScope, $location, AuthService, Session, USER_ROLES) {

            $rootScope.$on('$routeChangeStart', function (event, next) {
                if (!AuthService.permitAll(next.data.authorizedRoles)) {
                    AuthService.checkAuthenticated(next.data.authorizedRoles);
                }
            });


            // Call when the the client is confirmed
            $rootScope.$on('event:auth-loginConfirmed', function (data) {
                if ($location.path() === "/login") {
                    var search = $location.search();
                    if (search.redirect !== undefined) {
                        $location.path(search.redirect).search('redirect', null).replace();
                    } else {
                        $location.path('/').replace();
                    }
                }
            });

            // Call when the 401 response is returned by the server
            $rootScope.$on('event:auth-loginRequired', function (rejection) {
                Session.invalidate();
                if ($location.path() !== "/login" && $location.path() !== "/register") {
                    var redirect = $location.path();
                    $location.path('/login').search('redirect', redirect).replace();
                }
            });

            // Call when the 403 response is returned by the server
            $rootScope.$on('event:auth-notAuthorized', function (rejection) {
                $rootScope.errorMessage = 'errors.403';
                $location.path('/error').replace();
            });

            // Call when the user logs out
            $rootScope.$on('event:auth-loginCancelled', function () {
                $location.path('');
            });
        })

;