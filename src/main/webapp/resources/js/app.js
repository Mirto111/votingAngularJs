function HomeController($scope, $window, authService) {

    $scope.isLoggedIn = false;
    $scope.isAdmin = false;


    authService.getUserInfo(function (userInfo) {
        $scope.isLoggedIn = userInfo ? true : false;
        $scope.isAdmin = userInfo.user.roles.includes("ROLE_ADMIN") ? true : false;


    });

    $scope.logout = function () {
        authService.logout().then(function () {
            $window.location.reload();
        });
    };
}

function ModelVotingController($scope, $http, $location, authService) {

    $http.get(getContextPath() + "/rest/dishes").then(function (response) {
        $scope.dishes = response.data;
    });
    $http.get(getContextPath() + "/rest/restaurants/").then(function (response) {
        $scope.restaurants = response.data;
    });

    $scope.doVote = function (restaurant) {
        $http.post(getContextPath() + "/rest/restaurants/vote", restaurant).then(
            function (response) {
                $location.path("/results");
            }
        );
    }
}

function ResultVotingController($scope, $http, authService) {

    $http.get(getContextPath() + "/rest/restaurants/results").then(function (response) {
        $scope.results = response.data;
    });

}

function SaveResultController($scope, $http, $location) {
    $http.get(getContextPath() + "/rest/restaurants/saveResults").then(function (response) {
        $location.path("/home");
    });
}


function RestaurantController($scope, $http, $location, authService, $uibModal) {

    $scope.isRestSelected = false;
    var selectedRest = {};

    $http.get(getContextPath() + "/rest/restaurants/").then(function (response) {
        $scope.restaurants = response.data;
    });

    $scope.updateRest = function (restaurant) {
        authService.setData(restaurant);
        $location.path("/editRest");

    };

    $scope.deleteRest = function (restaurant) {
        $http.delete(getContextPath() + "/rest/restaurants/" + restaurant.id).then(
            function (response) {
                var index = $scope.restaurants.indexOf(restaurant);
                $scope.restaurants.splice(index, 1);
                $scope.dishes.splice(0, $scope.dishes.length);
                $scope.isRestSelected = false;
            }
        );
    };

    $scope.getDishes = function (restaurant) {
        $http.get(getContextPath() + "/rest/dishes/restaurant/" + restaurant.id).then(function (response) {
            $scope.dishes = response.data;
            selectedRest = restaurant;
            $scope.restaurantName = restaurant.name + ":  ";
            $scope.isRestSelected = true;

        });

        $scope.updateDish = function (dish) {
            authService.setData(dish);
            var modalInstance = $uibModal.open({
                templateUrl: 'editDish.htm',
                controller: EditDishController

            }).result.catch(function (resp) {
                if (['cancel', 'backdrop click', 'escape key press'].indexOf(resp) === -1) throw resp;
            });


        };

        $scope.addDish = function () {

            var modalInstance = $uibModal.open({
                templateUrl: 'addDish.htm',
                controller: AddDishController,
                resolve: {
                    items: function () {
                        return selectedRest;
                    }
                }

            }).result.then(function (response) {

                response.restaurant = selectedRest;
                $scope.dishes.push(response);

            });


        };

        $scope.deleteDish = function (dish) {

            $http.delete(getContextPath() + "/rest/dishes/" + dish.id, {params: {restId: dish.restaurant.id}}).then(
                function (response) {
                    var index = $scope.dishes.indexOf(dish);
                    $scope.dishes.splice(index, 1);
                }
            );
        };
    }

}

function AddController($scope, $http, $location) {


    $scope.createRest = function () {
        $http.post(getContextPath() + "/rest/restaurants/", $scope.restaurant).then(
            function (response) {
                $location.path("/restaurants");
            }
        );
    }
}

function EditController($scope, $http, $location, authService) {
    $scope.restaurant = authService.getData();
    $scope.saveRest = function (id, restaurant) {
        $http.put(getContextPath() + "/rest/restaurants/" + id, restaurant).then(
            function (response) {
                $location.path("/restaurants");
            }
        );
    }
}


function AddDishController($scope, $http, $location, $uibModalInstance, items) {


    $scope.dish = {currentDate: new Date()};
    $scope.restaurantName = items.name;
    $scope.createDish = function (dish) {
        dish.currentDate = new Date(dish.currentDate.getFullYear(), dish.currentDate.getMonth(), dish.currentDate.getDate() + 1);

        $http.post(getContextPath() + "/rest/dishes/", dish, {params: {restId: items.id}}).then(
            function (response) {

                $uibModalInstance.close(response.data);
            }, function (error) {
                console.log(error);
            }
        );
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}

function EditDishController($scope, $http, $location, authService, $uibModalInstance) {
    $scope.dish = authService.getData();
    $scope.dish.currentDate = new Date($scope.dish.currentDate);
    $scope.saveDish = function (dish) {

        $http.put(getContextPath() + "/rest/dishes/", dish, {params: {restId: dish.restaurant.id}}).then(
            function (response) {

                $uibModalInstance.close();
            }
        );
    };
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };

}


function UserController($scope, $http, $location, authService) {


    $http.get(getContextPath() + "/rest/users/").then(function (response) {
        $scope.users = response.data;
    });

    $scope.updateUser = function (user) {
        authService.setData(user);
        $location.path("/editUser");
    };

    $scope.deleteUser = function (user) {
        $http.delete(getContextPath() + "/rest/users/" + user.id).then(
            function (response) {
                var index = $scope.users.indexOf(user);
                $scope.users.splice(index, 1);
            }
        );
    };

}

function AddUserController($scope, $http, $location) {
    $scope.roles = ["ROLE_USER", "ROLE_ADMIN"];
    $scope.selectedList = [];


    $scope.toggleCheck = function (role) {
        if ($scope.selectedList.indexOf(role) === -1) {
            $scope.selectedList.push(role);
        } else {
            $scope.selectedList.splice($scope.selectedList.indexOf(role), 1);
        }
    };

    $scope.createUser = function (user) {
        user.roles = $scope.selectedList;


        $http.post(getContextPath() + "/rest/users/", $scope.user
        ).then(
            function (response) {
                $location.path("/users");
            }
        );
    }
}

function EditUserController($scope, $http, $location, authService) {
    $scope.user = authService.getData();
    $scope.roles = ["ROLE_USER", "ROLE_ADMIN"];
    $scope.selectedList = $scope.user.roles;


    $scope.toggleCheck = function (role) {
        if ($scope.selectedList.indexOf(role) === -1) {
            $scope.selectedList.push(role);
        } else {
            $scope.selectedList.splice($scope.selectedList.indexOf(role), 1);
        }
    };
    $scope.saveUser = function (id, user) {


        user.roles = $scope.selectedList;

        $http.put(getContextPath() + "/rest/users/" + id, user).then(
            function (response) {
                $location.path("/users");
            }
        );
    }
}

function LoginController($rootScope, $window, $scope, authService, $location) {

    $scope.username = '';
    $scope.password = '';
    $scope.loginError = false;

    $scope.login = function () {
        authService
            .authenticate(
                $scope.username,
                $scope.password,
                function (success) {
                    if (success) {

                        $window.location = "#!home";
                    } else
                        $scope.loginError = true;
                });
    };

    $scope.resetStatus = function () {
        $scope.loginError = false;
    };

    $scope.joinAsAdmin = function () {
        $scope.username = "admin@gmail.com";
        $scope.password = "admin";

    };
    $scope.joinAsUser = function () {
        $scope.username = "user@yandex.ru";
        $scope.password = "user";
    };

}


var mainModule = angular
    .module('mainModule', ['ngRoute', 'ngCookies', 'ui.bootstrap'])
    .service(
        'authService',
        [
            '$http',
            '$cookies',
            function ($http, $cookies) {

                /**
                 * @desc Аутентифицировать пользователя. Если
                 *       пользователь вошел в систему - будет
                 *       вызвана функция callback с аргументом true,
                 *       иначе будет вызвана функция callback с
                 *       аргументом false.
                 *
                 * @param name
                 *            Логин
                 * @param password
                 *            Пароль

                 * @param callback
                 *            Функция
                 *
                 */
                this.authenticate = function (name, password,
                                              callback) {

                    if (!name || !password) {
                        callback(false);
                        return;
                    }

                    var headers = {
                        authorization: "Basic "
                        + btoa(name + ":" + password)
                    };

                    $http.get(getContextPath() + '/auth/user', {
                        'headers': headers
                    }).then(function (data) {

                        if (data) {
                            callback(true);
                        } else {
                            callback(false);
                        }

                    }).catch(function () {
                        callback(false);
                    });
                };

                /**
                 * @desc Получить информацию о текущем пользователе.
                 *       Если пользователь вошел в систему - будет
                 *       вызвана функция callback с данными
                 *       пользователя, иначе будет функция callback
                 *       будет вызывана с аргументом null.
                 *
                 * @param callback
                 *            Функция
                 *
                 */
                this.getUserInfo = function (callback) {

                    $http.get(getContextPath() + '/auth/user', {})
                        .then(function (response) {

                            if (response.data.username) {
                                callback(response.data);
                            } else {
                                callback(null);
                            }

                        }).catch(function () {
                        callback(null);
                    });
                };

                /**
                 * @desc Выйти из системы
                 *
                 */
                this.logout = function () {

                    return $http.post(getContextPath()
                        + '/auth/logout', {});
                };


                var savedData = {};

                this.setData = function (data) {
                    savedData = data;
                };

                this.getData = function () {
                    return savedData;
                };


            }])

    .controller('HomeController',
        ['$scope', '$window', 'authService', HomeController])
    .controller(
        'LoginController',
        ['$rootScope', '$window', '$scope', 'authService',
            LoginController])
    .controller('ModelVotingController', ['$scope', '$http', '$location', 'authService', ModelVotingController])

    .controller('ResultVotingController', ['$scope', '$http', 'authService', ResultVotingController])
    .controller('AddController', ['$scope', '$http', '$location', AddController])
    .controller('EditController', ['$scope', '$http', '$location', 'authService', EditController])
    .controller('AddDishController', ['$scope', '$http', '$location', 'items', AddDishController])
    .controller('EditDishController', ['$scope', '$http', '$location', 'authService', '$uibModalInstance', EditDishController])
    .controller('RestaurantController', ['$scope', '$http', '$location', 'authService', '$uibModal', RestaurantController])
    .controller('AddUserController', ['$scope', '$http', '$location', AddUserController])
    .controller('EditUserController', ['$scope', '$http', '$location', 'authService', EditUserController])
    .controller('UserController', ['$scope', '$http', '$location', 'authService', UserController])
    .controller('SaveResultController', ['$scope', '$http', SaveResultController])

    .config(
        [
            '$routeProvider',
            '$httpProvider',
            function ($routeProvider, $httpProvider) {

                $httpProvider.interceptors.push('responseObserver');

                $routeProvider
                    .when('/home', {

                        templateUrl: 'part/home.html',
                        controller: 'HomeController'

                    })
                    .when('/login', {

                        templateUrl: 'login.html',
                        controller: 'LoginController'

                    })
                    .when('/dishes',
                        {

                            templateUrl: 'part/dishes.html',
                            controller: 'ModelVotingController'

                        })
                    .when('/results',
                        {

                            templateUrl: 'part/results.html',
                            controller: 'ResultVotingController'

                        })
                    .when('/saveResults',
                        {

                            templateUrl: 'part/home.html',
                            controller: 'SaveResultController'

                        })
                    .when('/addRestaurant', {
                        templateUrl: 'addRestaurant.htm',
                        controller: 'AddController'
                    })
                    .when('/editRest', {
                        templateUrl: 'editRest.htm',
                        controller: 'EditController'
                    })
                    .when('/restaurants', {
                        templateUrl: 'part/admin/restaurants.html',
                        controller: 'RestaurantController'

                    })
                    .when('/addDish', {
                        templateUrl: 'addDish.htm',
                        controller: 'AddDishController'
                    })
                    .when('/editDish', {
                        templateUrl: 'editDish.htm',
                        controller: 'EditDishController'
                    })
                    .when('/addUser', {
                        templateUrl: 'addUser.htm',
                        controller: 'AddUserController'
                    })
                    .when('/editUser', {
                        templateUrl: 'editUser.htm',
                        controller: 'EditUserController'
                    })
                    .when('/users', {
                        templateUrl: 'part/admin/users.html',
                        controller: 'UserController'

                    })

                    .otherwise({
                        redirectTo: '/home'
                    });

            }])

    .factory(
        'responseObserver',
        [
            '$rootScope',
            '$q',
            '$window',
            function responseObserver($rootScope, $q, $window) {

                return {
                    'responseError': function (errorResponse) {
                        switch (errorResponse.status) {
                            case 403:

                                if ($window.location.hash
                                    .indexOf("login") == -1)
                                    $rootScope.targetUrl = $window.location.hash;

                                $window.location = "#!login";
                                break;
                        }

                        return $q.reject(errorResponse);
                    }
                };
            }]);
