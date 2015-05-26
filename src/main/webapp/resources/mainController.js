myModule.controller("mainController", function ($rootScope, $scope, $http, $location, Session, AuthService, USER_ROLES) {

    AuthService.checkAuthenticated([USER_ROLES.all]);

    $rootScope.$on('event:auth-loginConfirmed', function () {
        $scope.user = Session;
    });
    $rootScope.$on('event:auth-logoutSuccess', function () {
        $scope.user = null;
    });

    $scope.isAuthenticated = function() {
        return !!Session.userId;
    }
    $scope.logout = function() {
        AuthService.logout()
    };

});
