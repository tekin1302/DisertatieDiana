'use strict';

var proxySnippet = require('grunt-connect-proxy/lib/utils').proxyRequest;

module.exports = function (grunt) {
    require('load-grunt-tasks')(grunt);
    require('time-grunt')(grunt);

    grunt.initConfig({
        watch: {
            styles: {
                files: ['src/main/webapp/**/*.css'
                ]
            },
            livereload: {
                options: {
                    livereload: 35729
                },
                files: [
                    'src/main/webapp/**/*.html',
                    'src/main/webapp/**/*.css',
                    'src/main/webapp/**/*.js'
                ]
            }
        },
        connect: {
            proxies: [
                {
                    context: '/rest',
                    host: 'localhost',
                    port: 8080,
                    https: false,
                    changeOrigin: false
                }
            ],
            options: {
                port: 9001,
                hostname: 'localhost',
                livereload: 35729
            },
            livereload: {
                options: {
                    open: true,
                    base: [
//                        '.tmp',
//                        'src/main/webapp'
                    ],
                    middleware: function (connect) {
                        return [
                            proxySnippet,
                            connect.static('.tmp'),
                            connect.static('src/main/webapp')
                        ];
                    }
                }
            }
        },
        clean: {
            dist: {
                files: [{
                    dot: true,
                    src: [
                        '.tmp',
                        '<%= yeoman.dist %>/*',
                        '!<%= yeoman.dist %>/.git*'
                    ]
                }]
            },
            server: '.tmp'
        },
        concurrent: {
            server: [
            ],
            test: [
            ],
            dist: [
                'imagemin',
                'svgmin'
            ]
        }

    });

    grunt.registerTask('serve', function (target) {
        if (target === 'dist') {
            return grunt.task.run(['build', 'connect:dist:keepalive']);
        }

        grunt.task.run([
            'clean:server',
            'concurrent:server',
            'configureProxies',
            'connect:livereload',
            'watch'
        ]);
    });

    grunt.registerTask('server', function (target) {
        grunt.log.warn('The `server` task has been deprecated. Use `grunt serve` to start a server.');
        grunt.task.run([target ? ('serve:' + target) : 'serve']);
    });

};