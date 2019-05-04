let timeStamp = ['-2', '-1', '0', '1', '2', '5'];
let curPath = null, curSegments = null, curWeather = null, departTime = null;
let currentTime = null;

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
  timeStamp.sort(function(a,b){
    return (parseInt(a) > parseInt(b)) ? 1 : ((parseInt(a) == parseInt(b)) ? 0 : -1);
  });
  console.log(timeStamp);
  const $slider = $("#slider");
  $($slider[0].previousElementSibling).text('Best Departure Time: ' + weather.best + " hr");
  $slider.prop({
  	min: 0,
  	max: timeStamp.length - 1,
  	value: timeStamp.indexOf(weather.best)
  });
  $($slider[0].nextElementSibling).text(weather.best + " hr");
}


let weatherPref = {snow: 0, rain: 0, wind: 0};
const $wslides = $(".slider");
for(let i = 0; i < $wslides.length; i++){
	const cur = $($wslides[i]);
	cur.change(() => {
		const sliderVal = cur.val();
		$(cur[0].nextElementSibling).text(sliderVal + "%");
		weatherPref[cur[0].id] = sliderVal;
	});
}

