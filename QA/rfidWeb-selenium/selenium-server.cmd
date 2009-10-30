@echo off
taskkill /f /t /im cmd.exe /fi "WINDOWTITLE eq selenium-server"
SET WORKSPACE=C:\workspace\trunk\rfidWeb-selenium
c:
cd %WORKSPACE%\lib
javaw -jar selenium-server.jar -firefoxProfileTemplate %WORKSPACE%\firefox
