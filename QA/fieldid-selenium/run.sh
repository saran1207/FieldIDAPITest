set LIBS=lib/log4j-1.2.15.jar;lib/selenium-java-client-driver.jar;/lib/selenium-server.jar
set CLASSPATH=lib/junit-4.5.jar;bin;${LIBS}


echo "Firefox test suite"
java -Dfieldid-browser=*firefox -cp ${CLASSPATH} org.junit.runner.JUnitCore com.n4systems.fieldid.selenium.testcase.LoginTests

