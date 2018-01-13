(function() {
    'use strict';

    angular
        .module('tenisApp')
        .controller('CourtDetailController', CourtDetailController);

    CourtDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Court', 'Reservation'];

    function CourtDetailController($scope, $rootScope, $stateParams, previousState, entity, Court, Reservation) {
        var vm = this;

        vm.court = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tenisApp:courtUpdate', function(event, result) {
            vm.court = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
