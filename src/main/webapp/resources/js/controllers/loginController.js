myModule.controller("loginController", function ($scope, $rootScope, AuthService) {
    $scope.authenticate = function(credentials) {
        AuthService.login(credentials);
    }
    $rootScope.$on('event:auth-loginFailed', function () {
        $scope.loginFailed = true;
    });
});
