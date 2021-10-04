# StormRouter

## Introduction and description

StormRouter is an application designed to help travelers plan a road trip around inclement weather, to help keep their trip safe and stress free. The app allows users to input the details of a road trip (including the destination and any stops along the way) and receive a customized weather report for conditions along their route. This information is displayed visually on a map to provide an immediate overview of the areas where dangerous road conditions may arise, and the app suggests alternate departure times or routes that may have safer conditions.

![Example screenshot](https://github.com/vx5/stormrouter/blob/master/images/stormrouter_example_screenshot.jpg?raw=true)

**Note:** While you can build and run the application (see below), it cannot return directions, due to external API dependencies which are no longer supported in the form needed. To renew and relaunch the product, code changes would be needed so that the app could leverage different APIs (e.g., National Weather Service API instead of Dark Sky API).

## Key details

#### Collaboration

I (vx5) worked on this project in collaboration with Jacob Polatty, Jason Crowley, and Champ Chairattana-Apirom. The code I worked on primarily can be found in the "conversion" and "route" packages (as well as "WSet.java" in the "weather" package), with classes I worked on primarily indicating "vx5" as the author. This code helps evaluate different departure times to determine which will have the best weather for driving, as well has handling key timezone and unit conversions, critical to returning results.

#### Key challenges

For the functionality that I (vx5) was responsible for, mentioned above, the greatest challenges were:
* **Determining the best departure time without using too many weather API calls:** To solve this, I implemented a caching system where geographic "tiles" could store weather observed within a certain area and within a certain time window for reuse. I also implemented an algorithm that distributes waypoints where weather is measured throughout the route. This helps minimize the number of API calls used (which was a critical factor during development, to allow for regular testing), but also limited how detailed the evaluation of different departure times is.
* **Handling time zone conversions (for weather and for showing when waypoints are reached in local time):** To solve this, I implemented a dedicated class using the util.TimeZone class to identify differences in time zones, and account for daylight savings time, where appropriate.

#### How to build and run

From the project's root directory, enter the following terminal commands:

```
mvn package
./run --gui
```

Then, in a browser window, access the address "0.0.0.0:4567/stormrouter", at which users can interact with the GUI.

#### Acknowledgments

As mentioned above, I worked on this project in collaboration with team members, who contributed to all other functionality. 

The app relies on external APIs, including the Openrouteservice and Dark Sky weather APIs -- both are essential to its functionality.

In addition to reviewing documentation online, we sometimes worked with others on campus to gain input and guidance on strategic choices throughout the development cycle.

This project was submitted as the term project for a course at Brown University, where we were invited to design an implement an application that excited us. As such, some of the skeleton content used (e.g., run file, pom.xml, parts of directory structure) were adapted from content provided by the course in earlier projects. I modified some of these files after the fact, so that the app would stand distinct from the course.

I obtained written permission from the course instructor to share the project publicly. Please let me know if there are any issues or questions!