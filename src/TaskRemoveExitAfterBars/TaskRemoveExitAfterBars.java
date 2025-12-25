package com.strategyquant.plugin.Task.impl.RemoveExitAfterBars;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;

import com.strategyquant.lib.L;
import com.strategyquant.pluginlib.annotations.Category;
import com.strategyquant.pluginlib.annotations.License;
import com.strategyquant.pluginlib.annotations.Name;
import com.strategyquant.pluginlib.annotations.ShortDesc;
import com.strategyquant.tradinglib.Databank;
import com.strategyquant.tradinglib.ResultsGroup;
import com.strategyquant.tradinglib.SettingsKeys;
import com.strategyquant.tradinglib.project.ProgressEngine;
import com.strategyquant.tradinglib.project.ProjectEngine;
import com.strategyquant.tradinglib.taskImpl.AbstractTask;
import com.strategyquant.tradinglib.taskImpl.ISQTask;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.meta.Author;

@Author(name = "User")
@Name(name = "Remove Exit After Bars")
@Category(name = "Custom Task")
@License(text = "")
@ShortDesc(text = "")
@PluginImplementation
public class TaskRemoveExitAfterBars extends AbstractTask {

    private Databank databankSource = null;
    private Databank databankTarget = null;

    private int totalResults;
    private int totalCopied = 0;

    // ------------------------------------------------------------------------

    public TaskRemoveExitAfterBars() throws Exception {
        super(null, null);
    }

    public TaskRemoveExitAfterBars(String projectName, ProgressEngine progressEngine) throws Exception {
        super(projectName, progressEngine);
    }

    // ------------------------------------------------------------------------

    @Override
    public String getType() {
        return "RemoveExitAfterBars";
    }

    @Override
    public String getName() {
        return L.tsq("Remove Exit After Bars");
    }

    @Override
    public String getPluginFolderName() {
        return "TaskRemoveExitAfterBars";
    }

    @Override
    public int getPreferredPosition() {
        return 1;
    }

    // ------------------------------------------------------------------------

    @Override
    public ISQTask clone(String projectName, ProgressEngine progressEngine) throws Exception {
        TaskRemoveExitAfterBars task = new TaskRemoveExitAfterBars(projectName, progressEngine);
        task.settingTabPlugins = this.settingTabPlugins;
        return task;
    }

    @Override
    public String[] getSettings() {
        return new String[] {
                "RemoveExitAfterBars"
        };
    }

    @Override
    protected Databank[] getUsedDatabanks() {
        return new Databank[] { databankSource, databankTarget };
    }

    @Override
    protected Databank getOutputDatabank() {
        return databankTarget;
    }

    // ------------------------------------------------------------------------

    private void loadSettings() {
        this.databankSource = (Databank) settingsData.getParams().get(SettingsKeys.DatabankSource);
        this.databankTarget = (Databank) settingsData.getParams().get(SettingsKeys.DatabankTarget);
    }

    @Override
    public void start() throws Exception {
        loadSettings();

        this.project = ProjectEngine.get(projectName);

        if (databankSource == null || databankTarget == null) {
            progressEngine.printToLog(L.t("Error: Source or Target databank not configured."));
            return;
        }

        progressEngine.setLogPrefix(taskLogPrefix);
        progressEngine.printToLog(L.t("Starting removal of Exit After Bars..."));
        progressEngine.start();

        ArrayList<ResultsGroup> results = databankSource.getRecords();
        this.totalCopied = 0;
        this.totalResults = results.size();

        for (int i = 0; i < results.size(); i++) {
            ResultsGroup originalRG = results.get(i);
            String strategyName = originalRG.getName();

            try {
                // 1. Clone the strategy
                ResultsGroup strategyCloned = originalRG.clone();

                // 2. Apply recursive deletion logic to XML
                boolean modified = removeExitAfterBarsRule(strategyCloned);

                // 3. Save to target Databank
                databankTarget.add(strategyCloned, true);

                if (modified) {
                    progressEngine.printToLogDebug(L.t("%s: ExitAfterBars removed and copied.", strategyName));
                } else {
                    progressEngine.printToLogDebug(L.t("%s: No ExitAfterBars found, just copied.", strategyName));
                }

                this.totalCopied++;
                increaseGlobalStats(true);

                progressEngine.checkPaused();
                if (progressEngine.isStopped())
                    break;

            } catch (OutOfMemoryError e) {
                project.onMemoryError(e);
                break;
            } catch (Exception e) {
                progressEngine.printToLog(L.t("Error processing strategy %s: %s", strategyName, e.getMessage()));
            }
        }

        databankTarget.updateBestResults();
        progressEngine.finish();
    }

    // ------------------------------------------------------------------------

    private void increaseGlobalStats(boolean passed) {
        project.increaseGlobalStats(getCustomName().hashCode(), L.t("Input"), 1, "Processed", passed ? 1 : 0);
    }

    @Override
    protected int getRunningStatus() {
        return 0;
    }

    /**
     * Main logic: Gets the XML, cleans it and saves it again.
     */
    private boolean removeExitAfterBarsRule(ResultsGroup rg) throws Exception {
        if (rg == null)
            return false;

        Element strategyEl = rg.getStrategyXml();
        if (strategyEl == null)
            return false;

        boolean modified = false;

        // Start recursive search
        if (removeBlockRecursively(strategyEl)) {
            rg.setStrategyXml(strategyEl);
            modified = true;
        }

        return modified;
    }

    /**
     * Recursively searches for XML elements corresponding to ExitAfterBars and
     * removes them.
     * Now searches for both blocks and Parameters (<Param key="...">).
     */
    private boolean removeBlockRecursively(Element parent) {
        boolean removed = false;
        if (parent == null)
            return false;

        // Copy the list to avoid errors when deleting while iterating
        List<Element> children = new ArrayList<>(parent.getChildren());

        for (Element child : children) {
            String nameAttr = child.getAttributeValue("name");
            String classAttr = child.getAttributeValue("class");
            String keyAttr = child.getAttributeValue("key");

            boolean isTarget = false;

            // 1. Classic Case: Independent Block (Item or Block)
            if (nameAttr != null
                    && (nameAttr.equalsIgnoreCase("ExitAfterBars") || nameAttr.equalsIgnoreCase("ExitAtBar"))) {
                isTarget = true;
            } else if (classAttr != null && (classAttr.contains("ExitAfterBars") || classAttr.contains("ExitAtBar"))) {
                isTarget = true;
            }

            // 2. Modern Case: Parameter inside an order
            // Example: <Param key="#ExitAfterBars.ExitAfterBars#" ...>
            else if (keyAttr != null && keyAttr.contains("ExitAfterBars")) {
                isTarget = true;
            }

            if (isTarget) {
                // Remove the element (whether block or parameter)
                parent.removeContent(child);
                removed = true;
                // Do not continue deeper into this child because it no longer exists
                continue;
            }

            // If we didn't delete it, search inside its children
            if (removeBlockRecursively(child)) {
                removed = true;
            }
        }
        return removed;
    }
}