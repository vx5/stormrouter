let timeStamp = ['-2', '-1', '0', '1', '2', '5'];
let curPath = null, curSegments = null, curWeather = null, departTime = null;
let currentTime = null;

$("#slider").change(() => {
	const sliderVal = $("#slider").val();
	const displayTime = departTime.addHours(parseInt(timeStamp[sliderVal]));
	$($("#slider")[0].nextElementSibling).text(getTime(displayTime));
	if(curWeather == null) return ;
	displayWeather(curWeather[timeStamp[sliderVal]]);
});

Date.prototype.addHours = function(h) {    
  	let res = new Date();
  	res.setTime(this.getTime() + (h*60*60*1000));
	return res; 
}

function getTime(date){
	let hr = date.getHours(), mn = date.getMinutes();
	let ampm = "AM";
	if(hr >= 12) {
		ampm = "PM";
		hr = (hr > 12) ? hr - 12 : 12;
	} else {
		hr = (hr > 0) ? hr : 12;
	}
	mn = (mn >= 10) ? mn : ("0"+mn);
	hr = (hr >= 10) ? hr : ("0"+hr);
	return hr + ":" + mn + ampm; 
}

function resetWeather(weather){
  console.log(departTime);
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
  const displayTime = departTime.addHours(parseInt(weather.best));
  $($slider[0].previousElementSibling).text('Best Departure Time: ' + displayTime);
  $slider.prop({
  	min: 0,
  	max: timeStamp.length - 1,
  	value: timeStamp.indexOf(weather.best)
  });
  $($("#slider")[0].nextElementSibling).text(getTime(displayTime));
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

