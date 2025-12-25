package com.strategyquant.plugin.Settings.impl.RemoveExitAfterBars;

import org.jdom2.Element;
import org.json.JSONArray;
import org.json.JSONObject;

import com.strategyquant.lib.L;
import com.strategyquant.lib.XMLUtil;
import com.strategyquant.lib.constants.SQConst;
import com.strategyquant.pluginlib.annotations.Category;
import com.strategyquant.pluginlib.annotations.License;
import com.strategyquant.pluginlib.annotations.Name;
import com.strategyquant.pluginlib.annotations.ShortDesc;
import com.strategyquant.tradinglib.Databank;
import com.strategyquant.tradinglib.SettingsKeys;
import com.strategyquant.tradinglib.project.ProjectConfigHelper;
import com.strategyquant.tradinglib.task.settings.ISettingTabPlugin;
import com.strategyquant.tradinglib.task.settings.TaskSettingsData;
import com.strategyquant.tradinglib.taskImpl.ISQTask;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.meta.Author;

@Author(name = "User")
@Name(name = "Remove Exit After Bars")
@Category(name = "Settings")
@License(text = "")
@ShortDesc(text = "")
@PluginImplementation
public class SettingsRemoveExitAfterBars implements ISettingTabPlugin {

    @Override
    public String getProduct() {
        return SQConst.CODE_SQ;
    }

    // ------------------------------------------------------------------------

    @Override
    public int getPreferredPosition() {
        return 50;
    }

    // ------------------------------------------------------------------------

    @Override
    public void initPlugin() throws Exception {

    }

    // ------------------------------------------------------------------------

    @Override
    public void readSettings(String projectName, ISQTask task, Element elSettings, TaskSettingsData data) {
        // In this case we don't need a specific child XML element like
        // "FilterByCorrelation"
        // because we only read standard Databanks, but we keep the structure for
        // consistency.

        try {
            Databank dbSource = ProjectConfigHelper.getDatabankByType(projectName, "Source", elSettings);
            Databank dbTarget = ProjectConfigHelper.getDatabankByType(projectName, "Target", elSettings);

            data.addParam(SettingsKeys.DatabankSource, dbSource);
            data.addParam(SettingsKeys.DatabankTarget, dbTarget);

        } catch (Exception e) {
            data.addError(getSettingName(), null, L.t("Cannot load RemoveExitAfterBars settings. ") + e.getMessage());
        }
    }

    // ------------------------------------------------------------------------

    @Override
    public void getStrategyConfigSettings(Element elSettings, JSONArray settingsArray) throws Exception {

    }

    // ------------------------------------------------------------------------

    @Override
    public String getSettingName() {
        return "RemoveExitAfterBars";
    }

    // ------------------------------------------------------------------------

    @Override
    public String getName() {
        return L.tsq("Remove Exit After Bars");
    }

    // ------------------------------------------------------------------------

    @Override
    public JSONObject getInitializationData() throws Exception {
        return null;
    }
}