olApp.directive('editor', function(){
	return{
		restrict:'C',
		link: function(scope, element, attrs){
			//$(element).wysihtml5();
			
			$(element).wysihtml5({
			    "font-styles": true, //Font styling, e.g. h1, h2, etc. Default true
			    "emphasis": true, //Italics, bold, etc. Default true
			    "lists": true, //(Un)ordered lists, e.g. Bullets, Numbers. Default true
			    "html": false, //Button which allows you to edit the generated HTML. Default false
			    "link": true, //Button to insert a link. Default true
			    "image": true, //Button to insert an image. Default true,
			    "color": false, //Button to change color of font
			    "size": 'sm' //Button size like sm, xs etc.
			});
		}
	}
})

olApp.directive('touchspin', function(){
	return{
		restrict:'C',
		link: function(scope, element, attrs){			
			$(element).TouchSpin({
				verticalbuttons: true,
				min: 0,
				max: 1000000000
			});
		}
	}
})


olApp.directive('highcharts', function(){
	return{
		restrict:'C',
		link: function(scope, element, attrs){			
			$(element).highcharts(scope.chartOptions[attrs.highchartsOptions]);
		}
	}
})