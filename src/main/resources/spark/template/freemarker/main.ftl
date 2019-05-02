<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- In real-world webapps, css is usually minified and
         concatenated. Here, separate normalize from our code, and
         avoid minification for clarity. -->
    <link rel="stylesheet" href="/css/normalize.css">
    <link rel="stylesheet" href="/css/html5bp.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css"
          integrity="sha384-50oBUHEmvpQ+1lW4y57PTFmhCaXp0ML5d60M1M7uH2+nqUivzIebhndOJK28anvf"
          crossorigin="anonymous">

    <link rel="stylesheet" href="/css/main.css">
    <link rel="stylesheet" href="/css/demo.css">

    <#-- Mapbox GL JS -->
    <script src="https://api.mapbox.com/mapbox-gl-js/v0.53.0/mapbox-gl.js"></script>
    <link href="https://api.mapbox.com/mapbox-gl-js/v0.53.0/mapbox-gl.css" rel="stylesheet">
    <script src='https://api.mapbox.com/mapbox.js/v3.2.0/mapbox.js'></script>    
    <script src='https://api.mapbox.com/mapbox-gl-js/plugins/mapbox-gl-geocoder/v4.0.0/mapbox-gl-geocoder.min.js'></script>
    <link rel='stylesheet' href='https://api.mapbox.com/mapbox-gl-js/plugins/mapbox-gl-geocoder/v4.0.0/mapbox-gl-geocoder.css' type='text/css' />

    <link rel="stylesheet" href="/css/autocomplete.css">
  </head>
  <body>
     ${content}
     <script src="/js/jquery-3.1.1.js"></script>
     <script src="/js/skycons.js"></script>
     <script src="/js/map.js"></script>
     <script src="/js/collapse.js"></script>
     <script src="/js/waypoint.js"></script>
     <script src="/js/route.js"></script>
     <script src="/js/time_slider.js"></script>
  </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
       dealing with real world issues like old browsers.  -->
</html>
