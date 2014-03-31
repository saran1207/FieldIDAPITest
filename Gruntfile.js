var glob = require('glob'),
    util = require('util');

function buildScssFileMap(/* scss_base_dir, css_base_dir, scss_file_1, ... scss_file_n  */) {
    var args = Array.prototype.slice.call(arguments, 0);

    var scssDir = args.shift();
    var cssDir = args.shift();

    var scssFileMap = {};
    args.forEach(function (style) {
        // if target ends with '/' then treat as directory
        if (/\/$/.test(style)) {
            glob.sync(scssDir + style + '**/*.scss').forEach(function (scssFile) {
                scssFileMap[scssFile.replace(scssDir, cssDir).replace(/scss$/, 'css')] = scssFile;
            });
        } else {
            scssFileMap[cssDir + style + '.css'] = scssDir + style + '.scss';
        }
    });
    return scssFileMap;
}
module.exports = function(grunt) {
    var styleBase = "fid-web/src/main/webapp/style/";
    var scssBase = styleBase + 'sass/';
    var cssBase = styleBase;

    var scssFileMap = buildScssFileMap(scssBase, cssBase, 'global', 'ie-all', 'ie-lte8', 'pages/', 'plugins/');

    util.print('Found scss root files: \n' + util.inspect(scssFileMap) + '\n\n');

    grunt.initConfig({
        clean: Object.getOwnPropertyNames(scssFileMap),
        sass: {
            dev: {
                options: {
                    style: 'expanded',
                    sourcemap: true,
                    // lineNumbers: true
                },
                files: scssFileMap
            },
            prod: {
                options: {
                    style: 'compressed' 
                    // compress: true,
                    // yuicompress: true,
                    // cleancss: true,
                    // optimization: 2
                },
                files: scssFileMap
            }
        },
        watch: {
            styles: {
                files: [scssBase + '**/*.scss'],
                tasks: ['sass:dev'],
                options: { nospawn: true }
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-sass');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-clean');

    grunt.registerTask('default', ['clean', 'sass:dev', 'watch']);
    grunt.registerTask('package', ['scss:prod']);
};
