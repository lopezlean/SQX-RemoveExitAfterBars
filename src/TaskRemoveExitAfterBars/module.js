angular.module('app.settings').config(function(sqPluginProvider, SQEventsProvider) {
    
    sqPluginProvider.plugin("SimpleTaskSettings", 1, {
        taskType: 'RemoveExitAfterBars',
        templateUrl: '../../../plugins/TaskRemoveExitAfterBars/simpleSettings/simpleSettings.html',
        controller: 'SimpleRemoveExitAfterBarsCtrl',
        getInfoPanels: function(xmlConfig, injector){
            var L = injector.get("L");
            var settingsPlugins = sqPluginProvider.getPlugins('SettingsTab');

            var groups = [
                {
                    title: L.tsq('Removal Options'),
                    settings: [
                        { name : L.tsq("Status"), value : "Removes ExitAfterBars from strategies" }
                    ]
                }
            ];
            
            return groups;
        }
    });
    
});