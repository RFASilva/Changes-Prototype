# Changes-Prototype
The prototype allows to explore spatio-temporal events at different levels of detail.
A screenshot of the prototype is given:

![Alt text](https://github.com/RFASilva/Changes-Prototype/blob/master/screenshot.png "Screenshot Changes Prototype")

This prototype allows to explore spatio-temporal events at different spatial or temporal levels of detail.
It contains two main perspectives which are synchronized: (i) spatial perspective; (ii) temporal perspective. 
In both perspectives the user may interact with data, and any interaction in one perspective affects the other one as explained below.

In the spatial perspective, the user can defined a particular area. When this happens, the information in the temporal perspective 
regards only the events on that area.

In the temporal perspective, the user may select a time-point or an interval of time. When that happens, the map only show the events occurs for the period chosen.

The server was implemented in Java (using the REST API RESTeasy JAX-RS) and it is responsible for listening to client requests, processing them and 
retrieving the appropriate results. The browser-based client handles user interaction and data presentation and 
is written in Javascript, HTML5, WebGL, and it uses Leaflet to support the visualization of data on a map.

It handles with spatio-temporal events. By spatio-temporal events, we mean data with the following structure: (S, T, A1, …, AN) 
where S describes the location of the event (in two-dimensional space), T specifies the time moment, and  A1, …, AN are attributes detailing what has happened. 
The prototype was tested with data about accidents in USA and forest fires in Portugal
