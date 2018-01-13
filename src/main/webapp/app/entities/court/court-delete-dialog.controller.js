(function() {
    'use strict';

    angular
        .module('tenisApp')
        .controller('CourtDeleteController',CourtDeleteController);

    CourtDeleteController.$inject = ['$uibModalInstance', 'entity', 'Court'];

    function CourtDeleteController($uibModalInstance, entity, Court) {
        var vm = this;

        vm.court = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Court.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
