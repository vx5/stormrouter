# CS 0320 Term Project

## Team Members

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

StormRouter is an application designed to help travelers plan a road trip around inclement weather to help keep their trip safe and stress free.  The app will allow a user to input the details of a road trip (including the destination and any stops along the way) and receive a customized weather report for conditions along their route.  This information would be displayed visually on a map to provide an immediate overview of the areas where dangerous road conditions may arise, and the app could also suggest alternate departure times or routes that could give safer conditions.

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


### Project Idea 2: TripWallet

TripWallet is a road trip planner that is not designed to integrate a Maps-like GUI. It primarily lets users manage roadtrip budgets — to input expenses, and receive a) recommendations on how to manage spending and b) at the trip’s end, a clear and navigable idea of who owes whom how much money.

*Requirements:*

The Problems
* On long trips, keeping track of how to resolve payments can be very difficult. Travelers must either perform error-prone calculations at every meal table, errors that add up over the course of a trip, or must calculate their way through grueling stacks of receipts. Some transactions are particularly complex — multiple front the cash for an expense, but not in equal proportion, and the people who did not front cash do not owe equal amounts.
* If there is a large number of individual payments, travelers often venmo or zelle each other many times over — on the net, only one, smaller transaction would have been necessary to satisfy the imbalance. 
* Travelers often rely on intuition to understand if some people are fronting more expenses than others, or if they are on track to blow past spending quotas. They lack an intelligent way to forecast likely spending totals, and how to manage their spending now to ensure a) equitable (or desired) breakdown of expense fronting and b) total expenses do not exceed targets.

The Critical Features
* A GUI that allows users to identify which trip they are on, easily and quickly add new expenses
* A storage system that tracks all expense entries for a trip, and keeps track of who owes whom how much
* An algorithm that makes spending-related forecasts based on existing expenses

Most Challenging Aspects of Each Feature
* The entry system will need a) an easy way to verify that someone should have access to a given “trip” (whether by password or confirmation code) and b) a way to store trip information after users close and leave the site
* The storage system might need to handle many different trips and, at that, many entries per trip. A clean and flexible backend is imperative to making sure that information retrieval is fast and reliable
* The algorithm that makes spending forecast will need to process a complex input — the many expense entries, and their timing — and quickly produce a forecast of likely future expense characteristics.


### Project Idea 3: Road Trip Planner

Road trip planner is an application for planning a road trip by considering about the weather conditions and the interest of the users about some locations/theme. Basically, the app will contain the functions from both StormRouter and TripWallet. In addition to that, the app will receive the user’s input about their interest in attractions or locations (ex. restaurants, important places, gas stations, etc.). The app will plan the road trip with the account for the weather and the inputs. It will also suggest some locations based on the user’s interest and let the user decide whether or not they will include that to the trip. 

Requirements:

The Problems:
* When planning on a road trip, besides from the problem of the StormRouter and TripWallet, the most trivial problem that comes up is which places should be the stops of the trip. Also, along the trip, we need to decide where to make a detour for food, or visiting some places which takes time. So, if we could plan all of the detours and figure out the stops beforehand along with the weather, the trip will be a lot easier.


The Critical Features:
* A map interface that allows users browse different possible destinations and see previously visited locations
* A GUI that will allow users to easily input a desired start and end location and time and a payment scheme
* A way to display the trip that was planned by the app
* A way to make mutually agreed upon modifications to settings during the trip
* A way to easily confirm then redistribute money at the end of the trip

Most Challenging Aspects of Each Feature:
* Designing an algorithm to take all of the users past trips or points of interest and giving one or many recommendations for possible paths for a trip. The user might also want to specify the maximum time allotted for the trip, which must be taken into consideration
* The same challenges from our StormRouter app will be relevant for much of this project. For example, retrieving weather information and working with the appropriate APIs to get the job done is critical for the success of our app.
* Our TripWallet idea will introduce similar challenges for this project. Being able to quickly input transactions and let the app do the heavy lifting of determining who owes whom is a must. It will be a challenge to account for all of the information that is put into the app and have the app predict likely future patterns of spending.
