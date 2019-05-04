let timeStamps = ['-2', '-1', '0', '1', '2', '5'];
let curPath = null, curSegments = null, curWeather = null, departTime = null;
let currentTime = null;

$("#slider").on('input', event => {
  if (!departTime) return;

  const $targetSlider = $(event.target);
  const sliderVal = $targetSlider.val();
  const displayTime = departTime.addHours(parseInt(timeStamps[sliderVal]));
  $targetSlider.next().text(getTime(displayTime));
  if (!curWeather) return;
  displayWeather(curWeather[timeStamps[sliderVal]]);
});

Date.prototype.addHours = function (h) {
  let res = new Date();
  res.setTime(this.getTime() + (h * 60 * 60 * 1000));
  return res;
};

function getTime(date) {
  let hr = date.getHours(), mn = date.getMinutes();
  let ampm = "AM";
  if (hr >= 12) {
    ampm = "PM";
    hr = (hr > 12) ? hr - 12 : 12;
  } else {
    hr = (hr > 0) ? hr : 12;
  }
  mn = (mn >= 10) ? mn : ("0" + mn);
  hr = (hr >= 10) ? hr : ("0" + hr);
  return hr + ":" + mn + " " + ampm;
}

function resetWeather(weather) {
  timeStamps = [];
  for (const x of Object.keys(weather)) {
    if (x === 'best') continue;
    timeStamps.push(x);
  }
  timeStamps.sort(function (a, b) {
    return (parseInt(a) > parseInt(b)) ? 1 : ((parseInt(a) === parseInt(b)) ? 0 : -1);
  });
  console.log(timeStamps);
  const $slider = $("#slider");
  const displayTime = departTime.addHours(parseInt(weather.best));
  $slider.prev().text('Best Departure Time: ' + getTime(displayTime));
  $slider.prop({
    min: 0,
    max: timeStamps.length - 1,
    value: timeStamps.indexOf(weather.best)
  });
  $slider.next().text(getTime(displayTime));
}


let weatherPref = {snow: 0, rain: 0, wind: 0};
const $wslides = $(".slider");

$wslides.on('input', event => {
  const $targetSlider = $(event.target);
  const sliderId = $targetSlider.attr('id');
  const sliderVal = $targetSlider.val();
  $targetSlider.next().text(sliderVal + "%");
  weatherPref[sliderId] = sliderVal;
});

