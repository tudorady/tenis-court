(function() {
    'use strict';

    angular
        .module('tenisApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('timetable', {
            parent: 'entity',
            url: '/timetable',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Timetables'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/timetable/timetables.html',
                    controller: 'TimetableController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('timetable-detail', {
            parent: 'timetable',
            url: '/timetable/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Timetable'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/timetable/timetable-detail.html',
                    controller: 'TimetableDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Timetable', function($stateParams, Timetable) {
                    return Timetable.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'timetable',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('timetable-detail.edit', {
            parent: 'timetable-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/timetable/timetable-dialog.html',
                    controller: 'TimetableDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Timetable', function(Timetable) {
                            return Timetable.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('timetable.new', {
            parent: 'timetable',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/timetable/timetable-dialog.html',
                    controller: 'TimetableDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                startDate: null,
                                endDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('timetable', null, { reload: 'timetable' });
                }, function() {
                    $state.go('timetable');
                });
            }]
        })
        .state('timetable.edit', {
            parent: 'timetable',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/timetable/timetable-dialog.html',
                    controller: 'TimetableDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Timetable', function(Timetable) {
                            return Timetable.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('timetable', null, { reload: 'timetable' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('timetable.delete', {
            parent: 'timetable',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/timetable/timetable-delete-dialog.html',
                    controller: 'TimetableDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Timetable', function(Timetable) {
                            return Timetable.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('timetable', null, { reload: 'timetable' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
