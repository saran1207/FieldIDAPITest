@echo off
set ROOT=C:\workspace\2010.1\Scratch
set LIBS=%ROOT%\lib\log4j-1.2.15.jar;%ROOT%\lib\selenium-java-client-driver.jar;%ROOT%\lib\selenium-server.jar
set CLASSPATH=%ROOT%\junit-4.5.jar;%ROOT%\bin;%LIBS%

echo.
echo "Firefox test suite"
java -Dfieldid-browser=*firefox -cp %CLASSPATH% org.junit.runner.JUnitCore com.n4systems.fieldid.selenium.testcase.LoginTests

echo "Internet Explorer test suite"
java -Dfieldid-browser=*iehta -cp %CLASSPATH% org.junit.runner.JUnitCore com.n4systems.fieldid.selenium.testcase.LoginTests
