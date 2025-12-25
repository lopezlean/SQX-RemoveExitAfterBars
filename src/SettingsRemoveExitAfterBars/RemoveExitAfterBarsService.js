angular.module('app.settings').service('RemoveExitAfterBarsService', function ($q, BackendService, AppService, L, DatabankService, SQEvents) {

    function loadDatabanks() {
        DatabankService.loadVisibleDatabanks(instance.config.availableDatabanks);
    }

    this.saveSettings = function () {
        var xmlDoc = AppService.xmlDoc;
        var settingsObj = AppService.getCurrentTaskTabSettings("RemoveExitAfterBars");
        if (!settingsObj) return;

        setDatabankValue(settingsObj, "Source", instance.config.databankSource, xmlDoc);
        setDatabankValue(settingsObj, "Target", instance.config.databankTarget, xmlDoc);
    }

    this.loadSettings = function () {
        var settingsObj = AppService.getCurrentTaskTabSettings("RemoveExitAfterBars");
        if (!settingsObj) return;

        var databankObjs = getChildElements(settingsObj, 'Databank');

        for (var i = 0; i < databankObjs.length; i++) {
            var databankObj = databankObjs[i];

            var databankName = getAttrStringValue(databankObj, "name", "NA");
            var databankValue = getAttrStringValue(databankObj, "value", "Results");

            if (databankName == "Source") { instance.config.databankSource = databankValue; }
            if (databankName == "Target") { instance.config.databankTarget = databankValue; }
        }

        loadDatabanks();
    }

    function setDatabankValue(parentObj, name, value, xmlDoc) {
        var databankObjs = getChildElements(parentObj, 'Databank');
        var found = false;
        for (var i = 0; i < databankObjs.length; i++) {
            var db = databankObjs[i];
            if (getAttrStringValue(db, "name") == name) {
                db.setAttribute("value", value);
                found = true;
                break;
            }
        }
        if (!found) {
            var newDb = xmlDoc.createElement("Databank");
            newDb.setAttribute("name", name);
            newDb.setAttribute("value", value);
            parentObj.appendChild(newDb);
        }
    }

    var instance = this;

    this.config = {
        //databanks
        databankSource: "Results",
        databankTarget: "Results",
        availableDatabanks: []
    }

    function onEvent(event, data) {
        if (event == SQEvents.get("RELOAD_DATABANKS")) {
            loadDatabanks();
        }
    }

    SQEvents.addListener("RemoveExitAfterBarsService", [SQEvents.get('RELOAD_DATABANKS')], onEvent);
});