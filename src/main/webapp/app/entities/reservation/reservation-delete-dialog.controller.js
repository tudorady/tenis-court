(function() {
    'use strict';

    angular
        .module('tenisApp')
        .controller('ReservationDeleteController',ReservationDeleteController);

    ReservationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Reservation'];

    function ReservationDeleteController($uibModalInstance, entity, Reservation) {
        var vm = this;

        vm.reservation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Reservation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
