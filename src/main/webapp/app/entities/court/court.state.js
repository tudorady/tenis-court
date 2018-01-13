(function() {
    'use strict';

    angular
        .module('tenisApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('court', {
            parent: 'entity',
            url: '/court',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Courts'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/court/courts.html',
                    controller: 'CourtController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('court-detail', {
            parent: 'court',
            url: '/court/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Court'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/court/court-detail.html',
                    controller: 'CourtDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Court', function($stateParams, Court) {
                    return Court.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'court',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('court-detail.edit', {
            parent: 'court-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/court/court-dialog.html',
                    controller: 'CourtDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Court', function(Court) {
                            return Court.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('court.new', {
            parent: 'court',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/court/court-dialog.html',
                    controller: 'CourtDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('court', null, { reload: 'court' });
                }, function() {
                    $state.go('court');
                });
            }]
        })
        .state('court.edit', {
            parent: 'court',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/court/court-dialog.html',
                    controller: 'CourtDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Court', function(Court) {
                            return Court.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('court', null, { reload: 'court' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('court.delete', {
            parent: 'court',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/court/court-delete-dialog.html',
                    controller: 'CourtDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Court', function(Court) {
                            return Court.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('court', null, { reload: 'court' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
