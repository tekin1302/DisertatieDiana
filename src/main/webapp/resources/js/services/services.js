myModule
    .factory('CompanySvc',function ($resource) {
        return $resource(ctx + '/company', {}, {
            update: { method: 'PUT' },
            get: { method: 'GET'}
        });
    }).factory('RegisterSvc',function ($resource) {
        return $resource(ctx + '/company/register', {}, {
            create: { method: 'POST' }
        });
    })
    .factory('ExceptionsSvc',function ($resource) {
        return $resource(ctx + '/exception/', {}, {
            list: { method: 'GET' },
            create: {method: 'POST'}
        });
    }).factory('ExceptionsImgSvc',function ($resource) {
        return $resource(ctx + '/exception/img?id=:id', {}, {
            show: { method: 'GET' }
        });
    })
    .factory('UserEmailSvc',function ($resource) {
        return $resource(ctx + '/user/check-email?email=:emailP', {}, {
            check: { method: 'GET' }
        });
    })
    .factory('EmployeeListSvc',function ($resource) {
        return $resource(ctx + '/employee', {}, {
            list: { method: 'GET', isArray: true }
        });
    })
    .factory('EmployeeSvc',function ($resource) {
        return $resource(ctx + '/employee/:id', {}, {
            create: { method: 'POST' },
            update: { method: 'PUT' },
            show: { method: 'GET'},
            delete: { method: 'DELETE' }
        });
    }).
    factory('ActivitySvc',function ($resource) {
        return $resource(ctx + '/activity/:id', {}, {
            create: { method: 'POST' },
            update: { method: 'PUT' },
            show: { method: 'GET'},
            delete: { method: 'DELETE' }
        });
    })
    .factory('PositionListSvc',function ($resource) {
        return $resource(ctx + '/position/list', {}, {
            list: { method: 'GET', isArray: true }
        });
    })
    .factory('PositionSvc',function ($resource) {
        return $resource(ctx + '/position/:id', {}, {
            create: { method: 'POST' },
            update: { method: 'PUT' },
            delete: { method: 'DELETE' },
            show: { method: 'GET'}
        });
    })
    .factory('OrganigramElemsSvc',function ($resource) {
        return $resource(ctx + '/organigram', {}, {
            create: { method: 'POST' }
        });
    })
    .factory('OrganigramDataSvc',function ($resource) {
        return $resource(ctx + '/organigram/data', {}, {
            create: { method: 'POST' },
            get: { method: 'GET'}
        });
    })
    .factory('CreateDefaultChatRoom',function ($resource) {
        return $resource(ctx + '/chat/default', {}, {
            create: { method: 'POST' }
        });
    })
    .factory('ChatListSvc',function ($resource) {
        return $resource(ctx + '/chat', {}, {
            list: { method: 'GET', isArray: true }
        });
    })
    .factory('CurrentUserSvc',function ($resource) {
        return $resource(ctx + '/employee/getCurrent', {}, {
            get: { method: 'GET'}
        });
    })
    .factory('MessagesSvc',function ($resource) {
        return $resource(ctx + '/mess?id=:id&last=:last', {}, {
            get: { method: 'GET', isArray: true}
        });
    })
    .factory('EmployeeActivityListSvc',function ($resource) {
        return $resource(ctx + '/employee-activity/:byWhat/:id', {}, {
            get: { method: 'GET', isArray: true},
            delete: { method: 'DELETE' }
        });
    })
    .factory('EmployeeActivitySvc',function ($resource) {
        return $resource(ctx + '/employee-activity/:id', {}, {
            delete: { method: 'DELETE' },
            save: { method: 'POST' }
        });
    })
    .factory('TimesheetSvc',function ($resource) {
        return $resource(ctx + '/timesheet/:id', {}, {
            delete: { method: 'DELETE' },
            save: { method: 'POST' },
            get: {method: 'GET'}
        });
    })
    .factory('TSStatsSvc',function ($resource) {
        return $resource(ctx + '/timesheet/statistics/:id', {}, {
            get: {method: 'GET', isArray: true}
        });
    })
    ;