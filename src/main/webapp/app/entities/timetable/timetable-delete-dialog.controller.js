(function() {
    'use strict';

    angular
        .module('tenisApp')
        .controller('TimetableDeleteController',TimetableDeleteController);

    TimetableDeleteController.$inject = ['$uibModalInstance', 'entity', 'Timetable'];

    function TimetableDeleteController($uibModalInstance, entity, Timetable) {
        var vm = this;

        vm.timetable = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Timetable.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
