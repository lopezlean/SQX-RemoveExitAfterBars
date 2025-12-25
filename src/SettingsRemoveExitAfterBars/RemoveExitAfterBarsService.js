angular.module('app.settings').service('RemoveExitAfterBarsService', function($q, BackendService, AppService, L, DatabankService, SQEvents) {

    function loadDatabanks() {
        DatabankService.loadVisibleDatabanks(instance.config.availableDatabanks);
    }

    this.saveSettings = function() {
        var xmlDoc = AppService.xmlDoc;
        
        //databanks
        var databanksObj = AppService.getCurrentTaskTabSettings("Databanks");
        if(databanksObj) {
            var databankObjs = getChildElements(databanksObj, 'Databank');

            for(var i=0; i<databankObjs.length; i++) {
                var databankObj = databankObjs[i];
                var databankName = getAttrValue(databankObj, "name", "Results");

                if(databankName=="Source") { databankObj.setAttribute("value", instance.config.databankSource); }
                if(databankName=="Target") { databankObj.setAttribute("value", instance.config.databankTarget); }
            }
        }
    }
    
    this.loadSettings = function() {
        //databanks
        var databanksObj = AppService.getCurrentTaskTabSettings("Databanks");
        if(databanksObj) {
            var databankObjs = getChildElements(databanksObj, 'Databank');

            for(var i=0; i<databankObjs.length; i++) {
                var databankObj = databankObjs[i];

                var databankName = getAttrStringValue(databankObj, "name", "NA");
                var databankValue = getAttrStringValue(databankObj, "value", "Results");
                
                if(databankName=="Source") { instance.config.databankSource = databankValue; }
                if(databankName=="Target") { instance.config.databankTarget = databankValue; }
            }
        }
        
        loadDatabanks();
    }
	
    var instance = this;

    this.config = {
        //databanks
        databankSource: "Results",
        databankTarget: "Results",
        availableDatabanks: []
    }
	
    function onEvent(event, data){
        if(event == SQEvents.get("RELOAD_DATABANKS")){
            loadDatabanks();
        }
    }

    SQEvents.addListener("RemoveExitAfterBarsService", [SQEvents.get('RELOAD_DATABANKS')], onEvent);
});