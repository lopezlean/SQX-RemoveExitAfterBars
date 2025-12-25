angular.module('app.settings').controller('SimpleRemoveExitAfterBarsCtrl', function($scope, RemoveExitAfterBarsService) {
    // Reutilizamos el servicio del plugin de Settings para mostrar los datos
    $scope.config = RemoveExitAfterBarsService.config;
    
    // Aseguramos que se carguen los settings
    RemoveExitAfterBarsService.loadSettings();
});