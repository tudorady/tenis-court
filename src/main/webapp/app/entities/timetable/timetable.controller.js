(function() {
    'use strict';

    angular
        .module('tenisApp')
        .controller('TimetableController', TimetableController);

    TimetableController.$inject = ['Timetable'];

    function TimetableController(Timetable) {

        var vm = this;

        vm.timetables = [];

        loadAll();

        function loadAll() {
            Timetable.query(function(result) {
                vm.timetables = result;
                vm.searchQuery = null;
            });
        }
    }
})();
