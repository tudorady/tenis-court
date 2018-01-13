(function() {
    'use strict';

    angular
        .module('tenisApp')
        .controller('TimetableDetailController', TimetableDetailController);

    TimetableDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Timetable'];

    function TimetableDetailController($scope, $rootScope, $stateParams, previousState, entity, Timetable) {
        var vm = this;

        vm.timetable = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tenisApp:timetableUpdate', function(event, result) {
            vm.timetable = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
