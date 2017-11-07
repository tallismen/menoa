@echo off

if "%1"=="" goto ERROR
if "%2"=="" goto ERROR
set DOC_DIR=%cd%\docs\build\docs
set MAPPING_DIR=%cd%\app\build\outputs\mapping\release
set START_DIR=%cd%
set DIST_DIR=%cd%\dist
set SOURCE=%cd%\app\build\outputs\apk
set VERSION=%1
set PREFIX=menoa-v%VERSION%
set TARGET_DIR=\\anwb.local\project\WW_Digitaal\WWI Software\Android Apps\menoa\%PREFIX%
set WORKSPACE=%2

rd /s/q "%DIST_DIR%"
if not exist "%DIST_DIR%" mkdir "%DIST_DIR%"

cd %SOURCE%
copy "app-debug.apk" "%DIST_DIR%\%PREFIX%-debug.apk"
copy "app-release.apk" "%DIST_DIR%\%PREFIX%.apk"
cd %DOC_DIR%
copy "%PREFIX%.pdf" "%DIST_DIR%"
cd %MAPPING_DIR%
copy "mapping.txt" "%DIST_DIR%\%PREFIX%-mapping.txt"
cd %2%

rd /s/q "%TARGET_DIR%"
if not exist "%TARGET_DIR%" mkdir "%TARGET_DIR%"
robocopy  %DIST_DIR% "%TARGET_DIR%" %PREFIX%.apk
robocopy  %DIST_DIR% "%TARGET_DIR%" *%VERSION%.pdf
robocopy %DIST_DIR% "%TARGET_DIR%" *-mapping.txt
echo files verplaatst
echo apk files hernoemd
cd %WORKSPACE%
goto END

:ERROR
echo Je moet een versie nummer meegeven en de workspacenaam
:END

