(function() {
    'use strict';

    angular
        .module('tenisApp')
        .controller('ReservationController', ReservationController);

    ReservationController.$inject = ['Reservation'];

    function ReservationController(Reservation) {

        var vm = this;

        vm.reservations = [];

        loadAll();

        function loadAll() {
            Reservation.query(function(result) {
                vm.reservations = result;
                vm.searchQuery = null;
            });
        }
    }
})();
