@REM Maven Wrapper for Windows
@echo off
SET JAVA_HOME=C:\Program Files\Java\jdk-17
SET PATH=%JAVA_HOME%\bin;%PATH%

SET MAVEN_HOME=%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6
IF NOT EXIST "%MAVEN_HOME%\bin\mvn.cmd" (
    echo Downloading Maven 3.9.6...
    powershell -Command "Invoke-WebRequest -Uri https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip -OutFile '%TEMP%\maven.zip'"
    powershell -Command "Expand-Archive -Path '%TEMP%\maven.zip' -DestinationPath '%USERPROFILE%\.m2\wrapper\dists' -Force"
    SET MAVEN_HOME=%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6
)
"%MAVEN_HOME%\bin\mvn.cmd" %*
