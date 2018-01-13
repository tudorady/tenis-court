(function() {
    'use strict';

    angular
        .module('tenisApp')
        .controller('CourtController', CourtController);

    CourtController.$inject = ['Court'];

    function CourtController(Court) {

        var vm = this;

        vm.courts = [];

        loadAll();

        function loadAll() {
            Court.query(function(result) {
                vm.courts = result;
                vm.searchQuery = null;
            });
        }
    }
})();
