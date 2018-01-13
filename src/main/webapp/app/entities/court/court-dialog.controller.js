(function() {
    'use strict';

    angular
        .module('tenisApp')
        .controller('CourtDialogController', CourtDialogController);

    CourtDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Court', 'Reservation'];

    function CourtDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Court, Reservation) {
        var vm = this;

        vm.court = entity;
        vm.clear = clear;
        vm.save = save;
        vm.reservations = Reservation.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.court.id !== null) {
                Court.update(vm.court, onSaveSuccess, onSaveError);
            } else {
                Court.save(vm.court, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tenisApp:courtUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
