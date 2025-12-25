//product:TASKMANAGER
angular.module('app.settings').config(function(sqPluginProvider) {

    sqPluginProvider.plugin("SettingsTab", 50, {
        title: Ltsq('Remove Exit After Bars'),
        help: Ltsq('Removes the ExitAfterBars rule from strategies.'),
        helpURL: '',
        task: 'RemoveExitAfterBars',
        configElemName: 'RemoveExitAfterBars',
        dataItem: 'tm-remove-exit-after-bars',
        templateUrl: '../../../plugins/SettingsRemoveExitAfterBars/settings.html',
        controller: 'RemoveExitAfterBarsCtrl',
        getSettingsDescription : function(settingsElement, injector){
            return "N/A";
        }
    });
});