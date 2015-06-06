myModule.factory('AlertService', ['$http', '$modal', function($http, $modal) {
    return {
        confirm : function (size, messageCode, scope, callback) {
            scope.message = messageCode;
            scope.callback = function(modalInstance) {
                callback();
                scope.closeModal(modalInstance);
            };
            scope.closeModal = function() {
                scope.modalInstance.dismiss();
            }
            scope.modalInstance = $modal.open({
                templateUrl: 'resources/views/alert/confirmAlert.html',
                scope: scope,
                size: size
            });
        },
        showError : function (size, messageCode, scope) {
            scope.message = messageCode;
            scope.closeModal = function() {
                scope.modalInstance.dismiss();
            }
            scope.modalInstance = $modal.open({
                templateUrl: 'resources/views/alert/errorAlert.html',
                scope: scope,
                size: size
            });
        },
        showSuccess : function (size, messageCode, scope, callback) {
            scope.message = messageCode;
            scope.closeModal = function() {
                scope.modalInstance.dismiss();
                if (callback) {
                    callback();
                }
            }
            scope.modalInstance = $modal.open({
                templateUrl: 'resources/views/alert/successAlert.html',
                scope: scope,
                size: size
            });
        }
    }
}])