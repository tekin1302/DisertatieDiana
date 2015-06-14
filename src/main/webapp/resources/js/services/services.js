myModule
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
    .factory('AuthService', function ($rootScope, $http, Session, $location) {
        var authService = {};

        authService.login = function (credentials) {
            $http({
                method: 'POST',
                url: 'authenticate',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                transformRequest: function(obj) {
                    var str = [];
                    for(var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: {d_username: credentials.d_username, d_password: credentials.d_password}
            }).success(function () {
                $http.get('login/principal').success(function (user) {
                    Session.create(user.username, user.role,
                        user.userId);
                   var redirect = $location.search().redirect;
                    if (redirect) {
                        $location.path($location.search().redirect);
                    } else {
                        $location.path("/");
                    }
                });
            }).error(function(data, status, headers, config){
                if (status == 406) {
                    $rootScope.$broadcast('event:auth-loginFailed');
                }
            });
            /*
             return $http
             .post('authenticate', credentials)
             .then(function (user) {
             Ses sion.create(user.id,
             user.role);
             return user;
             });*/
        };
        authService.isAuthorized = function (authorizedRoles) {
            if (!angular.isArray(authorizedRoles)) {
                authorizedRoles = [authorizedRoles];
            }

            if (authorizedRoles.indexOf('*') != -1) {
                return true;
            }
            return authorizedRoles.indexOf(Session.userRole) != -1;
        },

            authService.permitAll = function(authorizedRoles) {
                if (!angular.isArray(authorizedRoles)) {
                    authorizedRoles = [authorizedRoles];
                }

                return authorizedRoles.indexOf('*') != -1;
            }

        authService.logout = function() {
            $http.get("logout").success(function(){
                $rootScope.$broadcast('event:auth-logoutSuccess');
                Session.invalidate();
                $location.path("/login");
            });
        }

        authService.checkAuthenticated = function (authorizedRoles) {
            $http.get('protected/authentication_check.gif?d=' + new Date().getTime(), {
                ignoreAuthModule: 'ignoreAuthModule'
            }).success(function (data, status, headers, config) {
                if (!Session.userId) {
                    $http.get('login/principal')
                        .success((function(data) {
                            Session.create(data.username, data.role, data.userId);
                            if (!authService.isAuthorized(authorizedRoles)) {
                                // user is not allowed
                                $rootScope.$broadcast("event:auth-notAuthorized");
                            } else {
                                $rootScope.$broadcast("event:auth-loginConfirmed");
                            }
                        }))

                    ;
                }else{
                    if (!authService.isAuthorized(authorizedRoles)) {
                        // user is not allowed
                        $rootScope.$broadcast("event:auth-notAuthorized");
                    } else {
                        $rootScope.$broadcast("event:auth-loginConfirmed");
                    }
                }
            }).error(function (data, status, headers, config) {
                if (!authService.isAuthorized(authorizedRoles)) {
                    $rootScope.$broadcast('event:auth-loginRequired', data);
                }
            });
        }
        return authService;
    })