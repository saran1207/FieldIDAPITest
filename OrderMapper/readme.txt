Overview:
=========
	The purpose of the helloworld quickstart sample is to prove that the ESB is 
	properly configured and happy.  As well as to demonstrate the needed minimal
	files to make a basic ESB component execute.

Running this quickstart:
========================
	Please refer to 'ant help-quickstarts' for prerequisites about the quickstarts and a 
	more detailed descripton of the different ways to run the quickstarts.

To Run standalone mode:
=======================
    1.  In a command terminal window in the quickstart folder type 'ant deploy-jms-dests'.
    2.  In a command terminal window in this folder ("Window1"), type 'ant run'.
    3.  Open another command terminal window in this folder ("Window2"), type 'ant runtest'.
    4.  Switch back to "Window1" to see the output from the ESB

To Run '.esb' archive mode:
===========================
    1.  In a command terminal window in this folder ("Window1"), type 'ant undeploy-jms-dests'.
    1.  In a command terminal window in this folder ("Window1"), type 'ant deploy'.
    2.  Open another command terminal window in this folder ("Window2"), type 'ant runtest'.
    3.  Switch back to Application Server console to see the output from the ESB

	'run' target description:
	In a command prompt window, start the listeners by simply running "ant" 
	in this directory.  You might review the build.xml file to see how it is setup. 
	More details on the build.xml can be found later in this document.  Shutdown
	the listener by simply using Ctrl-C in that window.

	'runtest' target description:
	In a separate command prompt window, run "ant runtest" to shoot a JMS message
	into the listener which will then invoke the MyJMSListenerAction and display
	it to the console.  You can modify the build.xml to change the phrase 
	"Hello World" to something else and re-run "ant runtest".

Project file descriptions:
==========================

	jbossesb.xml: 			
	The JMS gateway and ESB configuration file. It is listening for JMS ObjectMessages or TextMessages 
	at "queue/quickstart_helloworld_Request".  Messages received at "quickstart_helloworld_Request" are 
	converted bossesb-properties.xml. Also needed by jUDDI and needs to be at the head of the classpath.  
	Both juddi.properties and jbossesb-properties.xml are used when the service first boots up for 
	self-registration based upon the service-category and service-name found in the esb-config.xml file.

	jndi.properties:
	Needed primarily for org.jboss.soa.esb.samples.quickstart.helloworld.test.SendJMSMessage that is fired by ant runtest.

	log4j.xml:
	Needed to configure log4J used by both the quickstart and the ESB itself. A listener needs a place to log.

	src\quickstart\MyJMSListenerAction.java:
	The action class that is identified in the esb-config.xml file and is called whenever a message is received.  

	src\quickstart\helloworld\test\SendJMSMessage.java:
	Shoots in the string passed in via the command line or in this case the arg attribute in the ant runtest task.

	src\quickstart\helloworld\test\SendEsbMessage.java:
	Shoots in the string passed in via the command line or in this case the arg atribute in the ant sendesb task.
	This demonstrates how to build an "ESB aware" client that can invoke an ESB service.

	build.xml:
	Targets and structure description:
	*	the classpath property pulls the jbossesb-properties.xml file and the juddi.properties file to the
		front of the list
	*	the echoCP task is useful for making sure what you think is in your classpath is actually in your classpath
		Usage is: ant echoCP > myclasspath.txt 
		This generates a file called myclasspath.txt which can be reviewed in a text editor
	*	the run task calls the Launcher passing in 3 arguments the most important are the esb-config.xml and 
		esb-config-gateway.xml files
	*	the runtest task calls the org.jboss.soa.esb.samples.quickstart.helloworld.test.SendJMSMessage class and passes in an argument representing
		the string-based message to be pused into the queue the gateway is listening on.  Note: SendJMSMessage 
		contains a hard-coded queue name.


