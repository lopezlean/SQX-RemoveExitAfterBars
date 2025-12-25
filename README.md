# Remove Exit After Bars - StrategyQuant Plugin

This is a custom plugin for **StrategyQuant X** that allows you to automatically remove "Exit After Bars" and "Exit At Bar" conditions from your trading strategies.

It works as a **Custom Task** in StrategyQuant Custom Project workflows.

## Features

-   **Automated Removal**: Recursively searches through the strategy XML structure.
-   **Deep Cleaning**: Removes both legacy blocks (`ExitAfterBars`, `ExitAtBar`) and modern parameter-based rules (`#ExitAfterBars#`).
-   **Databank Processing**: Reads strategies from a **Source Databank**, cleans them, and saves the new version to a **Target Databank**.
-   **Non-Destructive**: The original strategy is preserved; a clone is modified and saved.

## Installation (Source Code)

Since this is a Code Editor plugin, you need to copy the source files into your StrategyQuant user folder and compile them manually.

1.  Copy the `src` contents to your StrategyQuant installation.
    -   Copy `src/SettingsRemoveExitAfterBars` to `{SQ_INSTALL_DIR}/user/extend/Plugins/`
    -   Copy `src/TaskRemoveExitAfterBars` to `{SQ_INSTALL_DIR}/user/extend/Plugins/`

    *Alternatively, you can use the provided `install.sh` (or `install.bat` on Windows) script to do this automatically.*

2.  Restart StrategyQuant.
3.  Open **Code Editor** in StrategyQuant.
4.  Navigate to the `Plugins` folder in the tree view.
5.  Right-click on the `SettingsRemoveExitAfterBars` and `TaskRemoveExitAfterBars` folders (or the parent folder) and select **Compile Plugin**.
6.  Check the "Log" tab to ensure there are no errors.

## Usage

1.  Open StrategyQuant.
2.  Go to **Custom Tasks** (or wherever you configure workflow tasks).
3.  Add the **Remove Exit After Bars** task to your workflow.
4.  Configure the settings:
    -   **Source Databank**: Choose the databank containing the strategies you want to process.
    -   **Target Databank**: Choose the databank where you want the cleaned strategies to be saved.
5.  Run the task.
