Changing the CSS structure for integration of new customized front-end framework.  

All global CSS will be in /style/
All plugin related CSS will be in /style/plugins/
All page specific CSS will be in /style/pages/

scss will now be use for creating the CSS and be compiled into the appropriate folders
All shared components will be in /style/sass/components/
All page specific CSS will be in /style/sass/pages/

Note that all the various component styles will be imported into one global file. This will then be compiled into global.css and be found under /style/

