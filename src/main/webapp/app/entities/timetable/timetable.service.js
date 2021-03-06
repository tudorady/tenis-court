(function() {
    'use strict';
    angular
        .module('tenisApp')
        .factory('Timetable', Timetable);

    Timetable.$inject = ['$resource', 'DateUtils'];

    function Timetable ($resource, DateUtils) {
        var resourceUrl =  'api/timetables/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startDate = DateUtils.convertDateTimeFromServer(data.startDate);
                        data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
