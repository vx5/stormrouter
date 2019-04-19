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
    <link rel="stylesheet" href="/css/main.css"> 

    <#-- Leaflet + OpenStreetMap -->
    <link rel="stylesheet" href="/css/leaflet.css">
    
    <link rel="stylesheet" href="/css/demo.css">
    
    <script src="/js/leaflet.js"></script>

    <#-- Mapbox GL JS -->
    <#--<script src="https://api.mapbox.com/mapbox-gl-js/v0.53.0/mapbox-gl.js"></script>
    <link href="https://api.mapbox.com/mapbox-gl-js/v0.53.0/mapbox-gl.css" rel="stylesheet">-->
  </head>
  <body>
     ${content}
     <!-- Again, we're serving up the unminified source for clarity. -->
     <script src="/js/jquery-3.1.1.js"></script>
     <script src="/js/map.js"></script>
  </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
       dealing with real world issues like old browsers.  -->
</html>
