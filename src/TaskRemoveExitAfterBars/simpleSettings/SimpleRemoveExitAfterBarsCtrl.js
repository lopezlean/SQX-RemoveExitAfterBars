angular.module('app.settings').controller('SimpleRemoveExitAfterBarsCtrl', function ($scope, RemoveExitAfterBarsService) {
    $scope.config = RemoveExitAfterBarsService.config;
    RemoveExitAfterBarsService.loadSettings();
});