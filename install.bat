@echo off
REM This script copies the source code to the StrategyQuant user folder on Windows.
REM Adjust the SQ_USER_DIR variable if your installation is different.

REM Default typical path on Windows (C:\StrategyQuantX)
SET "SQ_USER_DIR=C:\StrategyQuantX\user\extend\Plugins"

IF NOT EXIST "%SQ_USER_DIR%" (
  echo Error: Destination directory not found: %SQ_USER_DIR%
  echo Please edit this script (Right click -> Edit) and adjust the SQ_USER_DIR variable.
  pause
  exit /b 1
)

echo Copying sources to %SQ_USER_DIR%...

xcopy "src\SettingsRemoveExitAfterBars" "%SQ_USER_DIR%\SettingsRemoveExitAfterBars" /E /I /Y
xcopy "src\TaskRemoveExitAfterBars" "%SQ_USER_DIR%\TaskRemoveExitAfterBars" /E /I /Y

echo Installation completed. Restart StrategyQuant.
pause
