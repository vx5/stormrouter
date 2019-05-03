let timeStamp = ['-2', '-1', '0', '1', '2', '5'];
let curPath = null, curSegments = null, curWeather = null;

$("#slider").change(() => {
	const sliderVal = $("#slider").val();
	$($("#slider")[0].nextElementSibling).text(timeStamp[sliderVal] + " hr");
	if(curWeather == null) return ;
	displayWeather(curWeather[timeStamp[sliderVal]]);
});


function resetWeather(weather){
  timeStamp = [];
  for(const x of Object.keys(weather)){
  	if(x == 'best') continue;
  	timeStamp.push(x);
  }
  timeStamp.sort();
  const $slider = $("#slider");
  $($slider[0].previousElementSibling).text('Best Departure Time: ' + weather.best + " hr");
  $slider.prop({
  	min: 0,
  	max: timeStamp.length - 1,
  	value: 0
  });
  $slider.val(timeStamp.indexOf(weather.best));
  $($slider[0].nextElementSibling).text(weather.best + " hr");
}