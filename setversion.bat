@ECHO OFF

if "%1"=="" goto ERROR
if "%2"=="" goto ERROR
if "%3"=="" goto ERROR

set VERSION=%1
set CURRENT_VERSION=%2
set BUILD_NUMBER=%3
set GRADLE_FILE=gradle.properties
set SONAR_FILE=sonar-project.properties

echo Versie gewijzigd naar: %VERSION%
type %GRADLE_FILE% | replace.bat "(app.version=%CURRENT_VERSION%)" "app.version=%VERSION%"  > tmp_gradle.txt
copy tmp_gradle.txt %GRADLE_FILE%
type %GRADLE_FILE% | replace.bat "(app.build=)(\d*\d*\d*\d*\d*\d*\d*\d*)" "app.build=%BUILD_NUMBER%"  > tmp_gradle.txt
copy tmp_gradle.txt %GRADLE_FILE%

type %SONAR_FILE% | replace.bat "(sonar.projectVersion=%CURRENT_VERSION%)" "sonar.projectVersion=%VERSION%" > tmp_snr.txt
copy tmp_snr.txt %SONAR_FILE%
goto END

:ERROR
echo Je moet een geldig versie nummer meegeven (b.v. 8.2.0.3) en het huidige versienummer

:END

del tmp_*
