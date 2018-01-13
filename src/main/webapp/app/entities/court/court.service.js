(function() {
    'use strict';
    angular
        .module('tenisApp')
        .factory('Court', Court);

    Court.$inject = ['$resource'];

    function Court ($resource) {
        var resourceUrl =  'api/courts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
