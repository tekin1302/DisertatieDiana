myModule.controller("historyController", function ($scope, $http, $modal, AlertService, files) {
    $scope.historyItems = files;

    $scope.currentVersion = files[0];

    $scope.applyVersion = function() {
        AlertService.showSuccess('sm', 'Versiunea a fost aplicata cu succes!', $scope, null);
    }

    $scope.getHistoryItemClass = function(item) {
        if ($scope.historyVersion && $scope.historyVersion.id === item.id) {
            return "history-item-selected";
        }
    }
    $scope.selectHistoryItem = function(item) {
        $scope.historyVersion = item;
    }
    $scope.selectHistoryItem(files[1]);

    $scope.historyItems2 = [
        {id: 1, date: '13-02-2015 10:44:23', author: 'Vasile Bordea'},
        {id: 2, date: '13-02-2015 11:13:22', author: 'Vasile Bordea'},
        {id: 3, date: '13-02-2015 12:30:02', author: 'Diana Diaconu'},
        {id: 4, date: '13-02-2015 13:04:55', author: 'Diana Diaconu'},
        {id: 5, date: '13-02-2015 18:15:15', author: 'Vasile Bordea'},
        {id: 6, date: '14-02-2015 17:00:01', author: 'Ana Popescu'},
        {id: 7, date: '14-02-2015 17:05:59', author: 'Vasile Bordea'},
        {id: 8, date: '14-02-2015 18:44:55', author: 'Ana Popescu'},
        {id: 9, date: '14-02-2015 10:44:23', author: 'Vasile Bordea'},
        {id: 10, date: '15-02-2015 11:44:23', author: 'Ana Popescu'},
        {id: 11, date: '13-03-2015 10:44:23', author: 'Vasile Bordea'},
        {id: 12, date: '13-03-2015 11:13:22', author: 'Vasile Bordea'},
        {id: 13, date: '13-03-2015 12:30:02', author: 'Diana Diaconu'},
        {id: 14, date: '13-03-2015 13:04:55', author: 'Diana Diaconu'},
        {id: 15, date: '13-03-2015 18:15:15', author: 'Vasile Bordea'},
        {id: 16, date: '14-03-2015 17:00:01', author: 'Ana Popescu'},
        {id: 17, date: '14-03-2015 17:05:59', author: 'Vasile Bordea'},
        {id: 18, date: '14-03-2015 18:44:55', author: 'Ana Popescu'},
        {id: 19, date: '14-03-2015 10:44:23', author: 'Vasile Bordea'},
        {id: 20, date: '15-03-2015 11:44:23', author: 'Ana Popescu'},
        {id: 21, date: '13-04-2015 10:44:23', author: 'Vasile Bordea'},
        {id: 22, date: '13-04-2015 11:13:22', author: 'Vasile Bordea'},
        {id: 23, date: '13-04-2015 12:30:02', author: 'Diana Diaconu'},
        {id: 24, date: '13-04-2015 13:04:55', author: 'Diana Diaconu'},
        {id: 25, date: '13-04-2015 18:15:15', author: 'Vasile Bordea'},
        {id: 26, date: '14-04-2015 17:00:01', author: 'Ana Popescu'},
        {id: 27, date: '14-04-2015 17:05:59', author: 'Vasile Bordea'},
        {id: 28, date: '14-04-2015 18:44:55', author: 'Ana Popescu'},
        {id: 29, date: '14-04-2015 10:44:23', author: 'Vasile Bordea'},
        {id: 30, date: '15-05-2015 11:44:23', author: 'Ana Popescu'},
        {id: 31, date: '13-06-2015 10:44:23', author: 'Vasile Bordea'},
        {id: 32, date: '13-06-2015 11:13:22', author: 'Vasile Bordea'},
        {id: 33, date: '13-06-2015 12:30:02', author: 'Diana Diaconu'},
        {id: 34, date: '13-06-2015 13:04:55', author: 'Diana Diaconu'},
        {id: 35, date: '13-06-2015 18:15:15', author: 'Vasile Bordea'},
        {id: 36, date: '14-06-2015 17:00:01', author: 'Ana Popescu'},
        {id: 37, date: '14-06-2015 17:05:59', author: 'Vasile Bordea'},
        {id: 38, date: '14-06-2015 18:44:55', author: 'Ana Popescu'},
        {id: 39, date: '14-07-2015 10:44:23', author: 'Vasile Bordea'},
        {id: 40, date: '15-07-2015 11:44:23', author: 'Ana Popescu'}
    ];

/*    $http.get('resources/js/services/app.js').success(function(data){
        $scope.historyVersion = data;
    });
    $http.get('resources/js/services/app.js').success(function(data){
        $scope.currentVersion = data;
    });
*/

});
