(function () {
    angular
        .module('market', ['ngRoute', 'ngStorage'])
        .config(config)
        .run(run);

    function config($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'welcome/welcome.html',
                controller: 'welcomeController'
            })
            .when('/registration', {
                templateUrl: 'registration/registration.html',
                controller: 'registerController'
            })
            .when('/store', {
                templateUrl: 'store/store.html',
                controller: 'storeController'
            })
            .when('/cart', {
                templateUrl: 'cart/cart.html',
                controller: 'cartController'
            })
            .when('/orders', {
                templateUrl: 'orders/orders.html',
                controller: 'ordersController'
            })
            .when('/order_pay/:orderId', {
                templateUrl: 'order_pay/order_pay.html',
                controller: 'orderPayController'
            })
            .when('/confirm_email/:username/:email', {
                templateUrl: 'confirm_email/confirm_email.html',
                controller: 'confirmEmailController'
            })
            .when('/account', {
                templateUrl: 'account/account.html',
                controller: 'accountController'
            })
            .otherwise({
                redirectTo: '/'
            });
    }

    // function run($rootScope, $http, $localStorage) {
    //     if ($localStorage.marketUser) {
    //         try {
    //             let jwt = $localStorage.marketUser.accessToken;
    //             let payload = JSON.parse(atob(jwt.split('.')[1]));
    //             let currentTime = parseInt(new Date().getTime() / 1000);
    //             if (currentTime > payload.exp) {
    //                 console.log("DASDSADSADSADSADSAD")
    //                 $http.get('http://localhost:5555/auth/api/v1/authenticate/refresh_tokens')
    //                     .then(function successCallback(response) {
    //                         $http.defaults.headers.common.Authorization = '';
    //                         $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.accessToken;
    //                         $localStorage.marketUser.accessToken = response.data.accessToken;
    //                         console.log("dfdfdfd");
    //                     }, function errorCallback(response) {
    //                         console.log("Token is expired!!!");
    //                         delete $localStorage.marketUser;
    //                         $http.defaults.headers.common.Authorization = '';
    //                         alert(response.data.message);
    //                     });
    //             }
    //         } catch (e) {
    //         }
    //
    //         if ($localStorage.marketUser) {
    //             $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.marketUser.accessToken;
    //         }
    //     }
    //     if (!$localStorage.marchMarketGuestCartId) {
    //         $http.get('http://localhost:5555/cart/api/v1/cart/generate_id')
    //             .then(function (response) {
    //                 $localStorage.marchMarketGuestCartId = response.data.value;
    //             });
    //     }
    // }

// })();
//
// angular.module('market').controller('indexController', function ($rootScope, $scope, $http, $location, $localStorage) {
//     $scope.tryToAuth = function () {
//         $http.post('http://localhost:5555/auth/api/v1/authenticate', $scope.user)
//             .then(function successCallback(response) {
//                 if (response.data.accessToken) {
//                     console.log(response.data.accessToken)
//                     $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.accessToken;
//                     // $http.defaults.headers.common.RefreshAuthorization = 'Bearer ' + $localStorage.marketUser.refreshToken;
//                     $localStorage.marketUser = {
//                         username: $scope.user.username,
//                         accessToken: response.data.accessToken,
//                     };
//
//                     $scope.user.username = null;
//                     $scope.user.password = null;
//
//                     $http.get('http://localhost:5555/cart/api/v1/cart/' + $localStorage.marchMarketGuestCartId + '/merge')
//
//                     $location.path('/');
//                 }
//             }, function errorCallback(response) {
//                 alert(response.data.message);
//             });
//     };
    function run($rootScope, $http, $localStorage) {
        if ($localStorage.marketUser) {
                // try {
                //     let jwt = $localStorage.marketUser.accessToken;
                //     let payload = JSON.parse(atob(jwt.split('.')[1]));
                //     let currentTime = parseInt(new Date().getTime() / 1000);
                //     if (currentTime > payload.exp - 10) {
                //         console.log("index.js")
                //         if ($localStorage.marketUser.refreshToken) {
                //             $http.defaults.headers.common.Authorization = '';
                //             console.log("refresh")
                //             console.log($localStorage.marketUser.refreshToken)
                //             $http({
                //                 url: 'http://localhost:5555/auth/api/v1/authenticate/refresh_tokens',
                //                 method: 'POST',
                //                 data: {
                //                     refreshToken: $localStorage.marketUser.refreshToken,
                //                 }
                //             }).then(function successCallback(response) {
                //                 $http.defaults.headers.common.Authorization = '';
                //                 $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.accessToken;
                //                 console.log(response.data.refreshToken);
                //                 $localStorage.marketUser.accessToken = response.data.accessToken;
                //                 $localStorage.marketUser.refreshToken = response.data.refreshToken;
                //             }, function errorCallback(response) {
                //                 console.log("Token is expired!!!");
                //                 delete $localStorage.marketUser;
                //                 $http.defaults.headers.common.Authorization = '';
                //                 alert(response.data.message);
                //             });
                //         } else {
                //             console.log("Token is expired!!!");
                //             delete $localStorage.marketUser;
                //             $http.defaults.headers.common.Authorization = '';
                //         }
                //     }
                // } catch (e) {
                // }

            if ($localStorage.marketUser) {
                $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.marketUser.accessToken;
            }
        }
        if (!$localStorage.marchMarketGuestCartId) {
            $http.get('http://localhost:5555/cart/api/v1/cart/generate_id')
                .then(function (response) {
                    $localStorage.marchMarketGuestCartId = response.data.value;
                });
        }
    }
})();

angular.module('market').controller('indexController', function ($rootScope, $scope, $http, $location, $localStorage) {
    $scope.tryToAuth = function () {
        $http.post('http://localhost:5555/auth/api/v1/authenticate', $scope.user)
            .then(function successCallback(response) {
                if (response.data.accessToken) {
                    console.log(response.data.accessToken)
                    console.log(response.data.refreshToken)
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.accessToken;
                    $localStorage.marketUser = {
                        username: $scope.user.username,
                        accessToken: response.data.accessToken,
                        refreshToken: response.data.refreshToken
                    };

                    $scope.user.username = null;
                    $scope.user.password = null;

                    $http.get('http://localhost:5555/cart/api/v1/cart/' + $localStorage.marchMarketGuestCartId + '/merge')

                    $location.path('/');
                }
            }, function errorCallback(response) {
                alert(response.data.message);
            });
    };

    $scope.tryToLogout = function () {
        $scope.clearUser();
        $location.path('/');
    };

    $scope.clearUser = function () {
        delete $localStorage.marketUser;
        $http.defaults.headers.common.Authorization = '';
    };

    $rootScope.isUserLoggedIn = function () {
        if ($localStorage.marketUser) {
            return true;
        } else {
            return false;
        }
    };
});

