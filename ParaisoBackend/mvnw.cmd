@ECHO OFF
REM Apache Maven Wrapper startup (auto-downloads wrapper jar if missing)
SETLOCAL

SET "BASEDIR=%~dp0"
SET "WRAPPER_DIR=%BASEDIR%.mvn\wrapper"
SET "PROPERTIES_FILE=%WRAPPER_DIR%\maven-wrapper.properties"
SET "JAR=%WRAPPER_DIR%\maven-wrapper.jar"

FOR /F "usebackq tokens=1,* delims==" %%A in ("%PROPERTIES_FILE%") DO (
  IF "%%A"=="distributionUrl" SET "DISTRIBUTION_URL=%%B"
  IF "%%A"=="wrapperUrl" SET "WRAPPER_URL=%%B"
)

IF NOT EXIST "%WRAPPER_DIR%" MKDIR "%WRAPPER_DIR%"

IF NOT EXIST "%JAR%" (
  ECHO Downloading Maven Wrapper jar from %WRAPPER_URL%
  powershell -Command "Invoke-WebRequest -UseBasicParsing -Uri '%WRAPPER_URL%' -OutFile '%JAR%'"
  IF ERRORLEVEL 1 (
    ECHO Failed to download Maven Wrapper jar. Ensure PowerShell and internet access are available.
    EXIT /B 1
  )
)

SET "JAVA_EXE=java"
IF NOT "%JAVA_HOME%"=="" SET "JAVA_EXE=%JAVA_HOME%\bin\java.exe"

"%JAVA_EXE%" -Dmaven.multiModuleProjectDirectory="%BASEDIR%" -cp "%JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
