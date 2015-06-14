myModule
    .factory('CompileSvc', function ($http) {
        return {
            compile : function (obj, fn) {
                $http.post('compileFile', obj).success(fn);
            }
        }
    })
;