myModule
    .factory('CompileSvc', function ($resource) {
        return $resource('compileFile', {}, {
            compile: { method: 'POST'}
        })
    })
;