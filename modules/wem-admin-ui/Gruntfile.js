module.exports = function (grunt) {

    // Add grunt plugins
    grunt.loadNpmTasks('grunt-ts');
    grunt.loadNpmTasks('grunt-directives');
    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-clean');

    // Load grunt task configuration from grunt/*
    require('load-grunt-config')(grunt);

    // Register aliases
    grunt.registerTask('default', 'all');

    grunt.registerTask('all', ['less', 'ts', 'directives']);
    grunt.registerTask('all_no_ts', ['less', 'directives']);

    grunt.registerTask('common', ['all_no_ts', 'ts:common']);
    grunt.registerTask('cm', ['all_no_ts', 'ts:content_manager']);
    grunt.registerTask('sc', ['all_no_ts', 'ts:schema_manager']);
    grunt.registerTask('md', ['all_no_ts', 'ts:module_manager']);
    grunt.registerTask('tm', ['all_no_ts', 'ts:template_manager']);
    grunt.registerTask('al', ['all_no_ts', 'ts:app_launcher']);
    grunt.registerTask('le', ['all_no_ts', 'ts:live_edit']);

};