L.mapbox.accessToken = "pk.eyJ1IjoiamNyb3dsZTUiLCJhIjoiY2p0dmg5YzJoMXg1YTRlbXVsMWo2YWxtaSJ9.5-JA68Dz-1Vdhpt3cF2hJg";
// let MapboxG.eocoder = require('@mapbox/mapbox-gl-geocoder');
//let geocoder = new L.mapbox.geocoder('mapbox.places');
// let geocoder = new MapboxGeocoder({ accessToken: mapboxgl.accessToken });

function autoCompleteLocation(elem, event){
	console.log(event);

	const cur = $(elem);
	const input = cur.val();
	// console.log(input);
	if(input.length < 2)
		return;
	const offset = cur.offset();
	console.log(offset);
	// console.log(cur);
	let par = $(cur[0].parentNode);
	// console.log(par);
	if(par[0].lastChild.tagName != 'UL'){
		par.append("<ul id='autocomplete'></ul>");
	}
	par = $(cur[0].parentNode);
	const suggestList = $(cur[0].parentNode)[0].lastChild;
	$(suggestList).css({top: offset.top + 20, left: offset.left});
	// console.log(suggestList);
	$(suggestList).empty();
	function addSuggestions(err, responseObj){
		console.log(responseObj);
		// const responseObj = JSON.parse(responseJSON);
		const suggestions = responseObj.results.features;
		for(let i = 0; i < suggestions.length; i++){
			$(suggestList).append("<li id='suggest'>" + suggestions[i].place_name + "</li>");
		}
	}
	geocoder.query(input, addSuggestions);
}

function clearSuggests(){
	const autocompleteAll = $("ul#autocomplete");
	for(let i = 0; i < autocompleteAll.length; i++){
		$(autocompleteAll[i]).empty();
	}
}