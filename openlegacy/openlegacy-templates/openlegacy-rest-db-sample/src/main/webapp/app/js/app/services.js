( function() {

	'use strict';

	/* Services */
	angular.module( 'services', [] )

	/**
	 * Simulate database.
	 */

	.factory( 'contacts', function( $http ) {

		// Return a promise

		return $http.get( 'js/contacts.json' );
	} )

	.factory( '$olHttp', function( $http ) {
		
		return{
			get:function(url,callback){
				$http(
						{
							method : 'GET',
							data : '',
							url : olConfig.baseUrl + url,
							headers : {
								'Content-Type' : 'application/json',
								'Accept' : 'application/json'
							}
						})
				.success(function(data, status, headers, config) {
					callback(data);
				}).error(function(data, status, headers, config) {					
					alert(data.error);
				});
				
			},

			post:function(url,model,callback){				
				$http(
						{
							method : 'POST',
							data : angular.toJson(model),
							url : olConfig.baseUrl + url,
							headers : {
								'Content-Type' : 'application/json',
								'Accept' : 'application/json'
							}
						})
				.success(function(data, status, headers, config) {
					callback(data);
				}).error(function(data, status, headers, config) {
					alert(data);
				});
			}
		
		
		};
	} )
	.factory('$themeService',['$cookies', '$rootScope', function($cookies, $rootScope) {
		return {
			'changeTheme': function() {
				var themes = this.getThemeList();
				if ($.cookie('ol_theme') == undefined) {
					$.cookie('ol_theme', themes[0]);
				}
				var index = themes.indexOf($.cookie('ol_theme'));
				if (themes.length == index + 1 ) {
					$.cookie('ol_theme', themes[0]);				  
				} else {
					$.cookie('ol_theme', themes[index + 1]);			
				} 
				
				$rootScope.theme = $.cookie('ol_theme');
			},
			
			'getCurrentTheme': function() {				
				if ($.cookie('ol_theme') == undefined) {
					$.cookie('ol_theme', this.getThemeList()[0]);					
					return this.getThemeList()[0];
				} else {					
					return $.cookie('ol_theme');
				}
			},
			
			'getThemeList': function() {
				return ['light', 'emily', 'dynamics'];
			}
			
		};
	}]);
	
} )();