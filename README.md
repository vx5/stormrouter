# StormRouter

## Introduction and description

StormRouter is an application designed to help travelers plan a road trip around inclement weather, to help keep their trip safe and stress free. The app allows users to input the details of a road trip (including the destination and any stops along the way) and receive a customized weather report for conditions along their route. This information is displayed visually on a map to provide an immediate overview of the areas where dangerous road conditions may arise, and the app suggests alternate departure times or routes that may have safer conditions.

![Example image](https://github.com/vx5/stormrouter/blob/master/stormrouter_example_screenshot.jpg?raw=true)

## Key details

#### Collaboration

#### Key challenges

#### How to build and run

#### Acknowledgments

I obtained permission from the course professor to share the project publicly

#### Jake Polatty

*Strengths:*
* Data management (interned with oceanographic consulting company and developed several data upload and transfer portals)
* Mapping interfaces and data visualization (created a global weather and climate data mapping overlay tool in second year of internship)
* API handling (worked with many different weather and map data APIs, also have experience setting up an AWS endpoint to return processed data from a database)

*Weaknesses:*
* GUI Design (less experienced with pure CSS, particularly CSS positioning)

#### Varun Narayan

*Strengths:*   
* Experience with Java (including 1.5 years in high school, using it during a summer research project, and the CS15/16 introductory sequence)
* Certain aspects of user design (experience interning in consulting)
* Certain aspects of statistics (use in independent study, have taken Econometrics I)

*Weaknesses:*
* Experience with APIs
* Experience with the “web” part of web applications (poor understanding of anything web-related)

#### Jason Crowley

*Strengths:*
* Experience with Java from high school
* Some experience with developing web applications

*Weaknesses:*
* Front-end design (all of JavaScript, HTML, CSS for GUIs)
* Understanding how to integrate APIs

#### Champ Chairattana-Apirom
 
*Strengths:*
* Algorithms and back-end programming
* Code optimization

*Weaknesses:*
* GUI designs and front-end coding
* Less experience with Java


## Project Ideas

### Project Idea 1: StormRouter



*Requirements:*

The Problems:
* On road trips, particularly in the winter, it can be difficult to find the optimal time to depart for a trip to avoid possible weather hazards along your route.  This challenge is exacerbated by the fact that a thorough search will require a traveler to keep track of the weather reports at several locations along their route to notice any last-minute changes.
  * This issue is not only a problem of convenience but can also have serious consequences on a traveler’s safety, as the decision to try to drive through conditions such as snow, hail, or torrential rain can endanger a driver’s life.
Additionally, particularly dangerous conditions may lead to road closings or blockages that can delay or disrupt travel plans.

The Critical Features:
* The focus of the app’s interface will be a map that displays the user’s route (similar to Google Maps) and overlays weather information for locations along the path.
* The weather data would be retrieved from a public API and the forecast will be returned for several points along the route at the estimated time of arrival.
  * For example if the estimated travel time between points A and B is one hour, the report could display the 3:00pm forecast for point A and the 4:00pm forecast for point B.
* As an additional feature, the application could accept a range of possible departure dates or times and return the optimal travel schedule for avoiding potential weather hazards.

Most Challenging Aspects of Each Feature:
* The most challenging aspect of the first feature is designing an interface that is able to balance showing the user enough information while also keeping the map display uncluttered so it can be interacted with as expected.  One possible means of implementing this would be to show a single weather icon at each selected waypoint along the route (such as snow, rain, or sunny), and then clicking on this icon or the waypoint could open a dialog box or sidebar that displays more detailed weather information for that location.
* There will be two main difficulties for this section of the functionality: the first being how we choose to select which locations along the route we will retrieve weather reports for, and the second being the management of the ETA times.  Obviously there will not be weather forecast data for the exact ETA at a given location, so we will have to come up with a solution to round to the nearest available time so that we are able to retrieve the most accurate forecast possible.
* The greatest difficulty here will be identifying which time of departure is best out of a range of options, none of which may have perfectly clear weather.  We may need to develop some kind of ranking system that sets different danger levels for certain weather conditions and computes some form of safety index for a given departure time, with the least dangerous time ultimately being suggested.