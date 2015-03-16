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
            this.create = function (userId, userRole) {
                this.id = sessionId;
                this.userId = userId;
                this.userRole = userRole;
            };
            this.destroy = function () {
                this.id = null;
                this.userId = null;
                this.userRole = null;
            };
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

            checkAuthenticated = function (authorizedRoles) {
                $http.get('protected/authentication_check.gif', {
                    ignoreAuthModule: 'ignoreAuthModule'
                }).success(function (data, status, headers, config) {
                    if (!Session.login) {
                        Account.get(function(data) {
                            Session.create(data.login, data.firstName, data.lastName, data.email, data.roles,data.gender);
                            $rootScope.account = Session;
                            if (!$rootScope.isAuthorized(authorizedRoles)) {
                                // user is not allowed
                                $rootScope.$broadcast("event:auth-notAuthorized");
                            } else {
                                $rootScope.$broadcast("event:auth-loginConfirmed");
                            }
                        });
                    }else{
                        if (!$rootScope.isAuthorized(authorizedRoles)) {
                            // user is not allowed
                            $rootScope.$broadcast("event:auth-notAuthorized");
                        } else {
                            $rootScope.$broadcast("event:auth-loginConfirmed");
                        }
                    }
                }).error(function (data, status, headers, config) {
                    if (!$rootScope.isAuthorized(authorizedRoles)) {
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
            $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

            var interceptor = ['$rootScope', '$q', function (scope, $q) {
                function success(response) {
                    return response;
                }

                function error(response) {

                    return $q.reject(response);
                }

                return function (promise) {
                    return promise.then(success, error);
                }

            }];
            $httpProvider.responseInterceptors.push(interceptor);
        })
        .run(function ($rootScope, $location, AuthService, Session, USER_ROLES) {
            $rootScope.authenticated = false;

            $rootScope.$on('$routeChangeStart', function (event, next) {
                AuthService.checkAuthenticated(next.data.authorizedRoles);
            });
        })

;