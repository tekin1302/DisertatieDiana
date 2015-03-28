myModule
        .constant('USER_ROLES', {
            all: '*',
            admin: 'admin',
            dev: 'dev',
            ROLE_USER: 'ROLE_USER'
        })
        .constant('AUTH_EVENTS', {
            loginSuccess: 'auth-login-success',
            loginFailed: 'auth-login-failed',
            logoutSuccess: 'auth-logout-success',
            sessionTimeout: 'auth-session-timeout',
            notAuthenticated: 'auth-not-authenticated',
            notAuthorized: 'auth-not-authorized'
        })