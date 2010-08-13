	/** 
	 * A test case will require the following:
	 * 
	 * variable				default						define				comment
	 * --------				-------						------				-------
	 * host					localhost					selenium-server		the computer running the selenium remote control (RC) server
	 * port					4444						selenium-port		the port the selenium remote control server is listening on
	 * snapshots			C:\\selenium-snapshots\\	selenium-snapshots	the root location for screen captures to be stored. 
	 * browser				*firefox					fieldid-browser		the selenium string for which browser to run the test case with
	 * protocol				http						fieldid-protocol	either 'http' or 'https' for which ever protocol to run fieldid with
	 * initCompany			fieldid						fieldid-companyid	the tenant to start fieldid with
	 * domain				team.n4systems.net			fieldid-domain		the domain, i.e. the computer running fieldid on
	 * contextRoot			/fieldid/					fieldid-context		the context root for the fieldid application
	 * actionDelay			null						fieldid-delay		the number of milliseconds to delay between selenium actions
	 * log4jConfig			log4j.xml					fieldid-log4j		the log4j configuration file to be used
	 * 
	 * The host and port are pretty straight forward. If I run Selenium RC on localhost
	 * listening on port 4444 then host = "localhost" and port = 4444. You can override
	 * the default values using command line -D options. For example, if the selenium
	 * server is running on port 5656 I can use the following on the command line to
	 * override the default:
	 * 
	 *  		-Dselenium-port=5656
	 *  
	 * or to change the default browser:
	 *  
	 *  		-Dfieldid-browser=*iehta
	 *  
	 * Remember to use quotes or escape things like * when using the command line.
	 * 
	 * The snapshots is the location for where to store screen captures. This is
	 * location will have the current timestamp appended to it. This way if you
	 * have multiple runs, the snapshots will not get overwritten. For MSDOS file
	 * paths, don't forget to escape the slash character in the path. There will
	 * be a different timestamp for each testcase.
	 * 
	 * The browser is defined by Selenium. At the time of creating this framework
	 * Selenium has the following:
	 * 
	 * 		*firefox		http
	 * 		*iexplore		http
	 * 		*chrome			https
	 * 		*iehta			https
	 * 		*pifirefox	
	 * 		*piiexplore
	 * 		*custom c:\\program files\\internet explorer\\iexplore.exe
	 * 
	 * Which you use depends on how the Selenium RC server is running. If
	 * it is running with https and non-proxy injection mode then *chrome
	 * or *iehta should be used. If you are using proxy injection mode then
	 * *piiexplore or *pifirefox. If you are using http then use *firefox
	 * and *iexplore. If you want to run a browser that is not supported,
	 * use the *custom option. See the Selenium documentation for more about
	 * the browser strings.
	 * 
	 * The rest the of variables are for building the URL for opening the
	 * browser. The default will be:
	 * 
	 * 		http://n4.team.n4systems.net/fieldid/
	 * 
	 * the general format is:
	 * 
	 * 		protocol + "://" + initCompany + "." + domain + contextRoot;
	 * 
	 * The fieldid-delay property should normally not be used. If you set
	 * this to a numeric value, it will cause selenium to delay, by the
	 * equivalent number of milliseconds, between each action. This property
	 * was designed to slow down the play back, in case you wanted to watch
	 * what was happening as it happened. Can be used for GUI testing. You
	 * set this to 2000, run the test suite and watch the browser for any GUI
	 * issues. You can set it to a higher amount but I have found 2 seconds
	 * between selenium actions is usually fast enough that you won't get
	 * bored but slow enough you can see what it is doing. Remember that some
	 * pages will have 3 or more selenium calls, so a 2 second delay might
	 * mean you are sitting on one page for 6 to 10 seconds. Obviously the
	 * more there is on the page for the code to 'view' and validate, the
	 * slow things will be BUT it probably means there is more on the page
	 * for you to check visually as well.
	 * 
	 * Additional things to know about FieldIDTestCase:
	 * 
	 * If the class extending FieldIDTestCase is called Foo then this
	 * code will attempt to load the property file Foo.properties. Then
	 * getStringProperty, getIntegerProperty, etc. will use the properties
	 * loaded from that file. If a property is not found it will default
	 * to the value of badProperty, currently set to "INVALID".
	 * 
	 * If a property exists as a System property, it will override the
	 * loaded properties. For example, if I have a property called 'userid'
	 * in the file Foo.properties but I also have:
	 * 
	 * 		-Duserid=n4systems
	 * 
	 * then userid will equal n4systems. Note: if multiple test cases have
	 * the same property, you can override all of them by setting the
	 * System property. This can be helpful or this can be a problem. Be
	 * careful.
	 * 
	 * The class variable misc can be used to log information. We are
	 * using log4j. The Misc class wraps calls to info(), debug(), etc.
	 * So test cases can output log messages using:
	 * 
	 * 		misc.debug("some debug message");
	 * 		misc.info("some message");
	 * 
	 * The log4j.xml file determines which level of logging to output.
	 * Set the priority value to which ever level suits you. Typically,
	 * I have been using debug for debugging a test case and info for
	 * printing the 'testcase' out.
	 * 
	 * The verify*() methods will not stop the execution of a test case.
	 * It will set a flag so that during tearDown for a test case it will
	 * send a failure to jUnit.
	 * 
	 */