myModule.controller("loginController", function ($scope, AuthService) {
    $scope.authenticate = function(credentials) {
        AuthService.login(credentials);
    }
});
