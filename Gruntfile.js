var glob = require('glob'),
    util = require('util');

function buildLessFileMap(/* less_base_dir, css_base_dir, less_file_1, ... less_file_n  */) {
    var args = Array.prototype.slice.call(arguments, 0);

    var lessDir = args.shift();
    var cssDir = args.shift();

    var lessFileMap = {};
    args.forEach(function (style) {
        // if target ends with '/' then treat as directory
        if (/\/$/.test(style)) {
            glob.sync(lessDir + style + '**/*.less').forEach(function (lessFile) {
                lessFileMap[lessFile.replace(lessDir, cssDir).replace(/less$/, 'css')] = lessFile;
            });
        } else {
            lessFileMap[cssDir + style + '.css'] = lessDir + style + '.less';
        }
    });
    return lessFileMap;
}
module.exports = function(grunt) {
    var styleBase = "fid-web/src/main/webapp/style/";
    var lessBase = styleBase + 'less/';
    var cssBase = styleBase;

    var lessFileMap = buildLessFileMap(lessBase, cssBase, 'global', 'ie-all', 'ie-lte8', 'pages/', 'plugins/');

    util.print('Found Less root files: \n' + util.inspect(lessFileMap) + '\n\n');

    grunt.initConfig({
        clean: Object.getOwnPropertyNames(lessFileMap),
        less: {
            dev: {
                options: {
                    dumpLineNumbers: true
                },
                files: lessFileMap
            },
            prod: {
                options: {
                    compress: true,
                    yuicompress: true,
                    cleancss: true,
                    optimization: 2
                },
                files: lessFileMap
            }
        },
        watch: {
            styles: {
                files: [lessBase + '**/*.less'],
                tasks: ['less:dev'],
                options: { nospawn: true }
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-clean');

    grunt.registerTask('default', ['clean', 'less:dev', 'watch']);
    grunt.registerTask('package', ['less:prod']);
};
