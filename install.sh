#!/bin/bash

# This script copies the source code to the StrategyQuant user folder.
# Adjust the SQ_USER_DIR variable if your installation is different.

# Default path (Mac based)
SQ_USER_DIR="/Applications/StrategyQuantXB143.app/Contents/Resources/user/extend/Plugins"

if [ ! -d "$SQ_USER_DIR" ]; then
  echo "Error: Destination directory not found: $SQ_USER_DIR"
  echo "Please edit this script and adjust the SQ_USER_DIR variable."
  exit 1
fi

echo "Copying sources to $SQ_USER_DIR..."

cp -R src/SettingsRemoveExitAfterBars "$SQ_USER_DIR/"
cp -R src/TaskRemoveExitAfterBars "$SQ_USER_DIR/"

echo "Installation completed. Restart StrategyQuant to detect changes."
