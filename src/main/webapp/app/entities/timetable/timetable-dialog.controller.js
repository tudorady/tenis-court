(function() {
    'use strict';

    angular
        .module('tenisApp')
        .controller('TimetableDialogController', TimetableDialogController);

    TimetableDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Timetable'];

    function TimetableDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Timetable) {
        var vm = this;

        vm.timetable = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.timetable.id !== null) {
                Timetable.update(vm.timetable, onSaveSuccess, onSaveError);
            } else {
                Timetable.save(vm.timetable, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tenisApp:timetableUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
