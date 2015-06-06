myModule.controller("logsController", function ($scope, $http, $modal, AlertService) {

    $http.get('logs/list').success(function(data){
        $scope.logs = [];
        if (data) {
            for (var i = 0; i < data.length; i++) {
                $scope.logs[i] = {
                    name: data[i]
                };
            }
        }
    });

    $scope.getLogContent = function(log) {
        $http.get('logs/?name=' + encodeURI(log.name)).success(function(data){
            log.logContent = data;
        });
    }
});
