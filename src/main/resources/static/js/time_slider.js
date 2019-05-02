const timeStamp = [-2, -1, 0, 1, 2, 5];
let curPath = null, curSegments = null, curWeather = null;

$("#slider").change(() => {
	const sliderVal = $("#slider").val();
	$($("#slider")[0].nextElementSibling).text(timeStamp[sliderVal] + " hr");
	if(curWeather == null) return ;
	displayWeather(curWeather[timeStamp[sliderVal]]);
});