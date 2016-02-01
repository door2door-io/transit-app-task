##Mobile App Technical Assignment


Dependencies
---


I used set of libraries in the app:

- Facebook Stetho - for DB monitoring and view tree analysis
- GooglePlayServices - for the map and location service
- Retrofit2 - as one of the best HTTP client
- Dagger2 - for dependency injection
- JodaTime - for easier DateTime manipulation
- Glide and AndroidSVG for loading of SVG images
- JUnit4/Espresso - for testing
- Hamcrest - for easier and readabler tests
- Robolectric - for faster Unit testing
- MockServer - For mocking server requests
- Mockito - For mocking
- For online JSON mocking, I used http://www.mocky.io/


---


Packages
---
- **com.eutechpro.allytransitapp.data** - contains all classes responsibile for fetching data from the web and accesing them.

- **com.eutechpro.allytransitapp.data.rest.retrofit** - contains Retrofit2 implementation and supporting classs

- **com.eutechpro.allytransitapp.data.model** - contains data models classes. The main model class is Route, and all other model classes are supporting classes. 

- **com.eutechpro.allytransitapp.di** - contains Dagger2 oriented classes for dependency injection

- **com.eutechpro.allytransitapp.map** - containes MapActivity and supporting classes such as _RouteDrawingI_ and it's implementation.

- **com.eutechpro.allytransitapp.search** - containser SearchRouteActivity and supporting classes

- **com.eutechpro.allytransitapp.svg** - containser classes needed for helping Glide loading SVG resources.