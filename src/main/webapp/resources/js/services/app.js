var myModule = angular.module("myAppModule", ['ngResource', 'ngRoute', 'angularFileUpload'])
        .constant('USER_ROLES', {
            all: '*',
            admin: 'admin',
            dev: 'dev'
        })
        .constant('AUTH_EVENTS', {
            loginSuccess: 'auth-login-success',
            loginFailed: 'auth-login-failed',
            logoutSuccess: 'auth-logout-success',
            sessionTimeout: 'auth-session-timeout',
            notAuthenticated: 'auth-not-authenticated',
            notAuthorized: 'auth-not-authorized'
        })
        .service('Session', function () {
            this.create = function (email, userRole, userId) {
                this.email = email;
                this.userRole = userRole;
                this.userId = userId;
    };
            this.invalidate = function () {
                this.email = null;
                this.userRole = null;
                this.userId = null;
            };
            return this;
        })
        .factory('AuthService', function ($http, Session) {
            var authService = {};

            authService.login = function (credentials) {
                return $http
                    .post('/login', credentials)
                    .then(function (user) {
                        Session.create(user.id,
                            user.role);
                        return user;
                    });
            };
            isAuthorized = function (authorizedRoles) {
                if (!angular.isArray(authorizedRoles)) {
                    authorizedRoles = [authorizedRoles];
                }

                if (authorizedRoles.indexOf('*') != -1) {
                    return true;
                }
                return authorizedRoles.indexOf(Session.userRole) != -1;
            },

            checkAuthenticated = function (authorizedRoles) {
                $http.get('protected/authentication_check.gif', {
                    ignoreAuthModule: 'ignoreAuthModule'
                }).success(function (data, status, headers, config) {
                    if (!Session.login) {
                        $http.get('/principal')
                            .success((function(data) {
                                Session.create(data.email, data.role, data.userId);
                                if (!isAuthorized(authorizedRoles)) {
                                    // user is not allowed
                                    $rootScope.$broadcast("event:auth-notAuthorized");
                                } else {
                                    $rootScope.$broadcast("event:auth-loginConfirmed");
                                }
                            }))

                        ;
                    }else{
                        if (!isAuthorized(authorizedRoles)) {
                            // user is not allowed
                            $rootScope.$broadcast("event:auth-notAuthorized");
                        } else {
                            $rootScope.$broadcast("event:auth-loginConfirmed");
                        }
                    }
                }).error(function (data, status, headers, config) {
                    if (!isAuthorized(authorizedRoles)) {
                        $rootScope.$broadcast('event:auth-loginRequired', data);
                    }
                });
            }
            return authService;
        })
        .config(function ($routeProvider, USER_ROLES) {
        $routeProvider.when("/login",
            {
                templateUrl: 'resources/views/login.html',
                controller: 'loginController',
                data: {
                    authorizedRoles: [USER_ROLES.all]
                }

            })
            .when("/test",
            {
                templateUrl: 'resources/views/test.html',
                controller: 'testController',
                data: {
                    authorizedRoles: [USER_ROLES.admin]
                }
            });

        })
        .config(function ($httpProvider) {
            var interceptor = ['$rootScope', '$q', 'httpBuffer', function($rootScope, $q, httpBuffer) {
                function success(response) {
                    return response;
                }

                function error(response) {
                    if (response.status === 401 && !response.config.ignoreAuthModule) {
                        var deferred = $q.defer();
                        httpBuffer.append(response.config, deferred);
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
        })
        .run(function ($rootScope, $location, AuthService, Session, USER_ROLES) {
            $rootScope.authenticated = false;

            $rootScope.$on('$routeChangeStart', function (event, next) {
                AuthService.checkAuthenticated(next.data.authorizedRoles);
            });


            // Call when the the client is confirmed
            $rootScope.$on('event:auth-loginConfirmed', function (data) {
                $rootScope.authenticated = true;
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
                $rootScope.authenticated = false;
                if ($location.path() !== "/" && $location.path() !== "" && $location.path() !== "/register" &&
                    $location.path() !== "/activate" && $location.path() !== "/login") {
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