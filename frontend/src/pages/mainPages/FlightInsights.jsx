import React, { useRef, useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import mapboxgl from "mapbox-gl";
import "@mapbox/mapbox-gl-geocoder/dist/mapbox-gl-geocoder.css";
import "./shared/MainPage.css";
import along from "@turf/along";
import { point } from "@turf/helpers";
import length from "@turf/length";
import bearing from "@turf/bearing";
import { CSVLink } from "react-csv";
import axios from "axios";
import Swal from "sweetalert2";

mapboxgl.accessToken = process.env.REACT_APP_MAPBOX_ACCESS_TOKEN;

function FlightClass() {
  const navigate = useNavigate();
  const location = useLocation();

  const recLegs = {
    "from": location.state?.from.code,
    "to": location.state?.to.code
  };

  const tripLegs = {
    "from": location.state?.from.code,
    "to": location.state?.to.code,
    "class": location.state?.class,
    "flightNo": location.state?.flightNo,
    };

  const origin = [location.state?.from.coordLong, location.state?.from.coordLat];

  const destination = [location.state?.to.coordLong, location.state?.to.coordLat];

  //roughly where the view of the map should be based on origin and destination flights
  const lng_val = (origin[0] + destination[0]) / 2;
  const lat_val = (origin[1] + destination[1]) / 2;

  const mapContainer = useRef(null);
  const map = useRef(null);
  const [lng] = useState(lng_val);
  const [lat] = useState(lat_val);
  const [zoom] = useState(2);
  const [projection] = useState("globe");

  const [emissions, setEmissions] = useState();
  const [recomms, setRecomms] = useState();
  const [eff, setEff] = useState();
  const isLoaded = emissions != null && recomms != null && eff != null
  const [starEfficiency, setStarEfficiency] = useState();
  const [csvRecomms, setCsvRecomms] = useState();

  // loading component
  const loadingComponent = <h2>Loading...</h2>;

  useEffect(() => {
    if (!location.state?.prev) {
      navigate("/");
    } else {
      axios.post(`${process.env.REACT_APP_BASE_URL}/trip/carbonEmissions`, JSON.stringify([tripLegs]), {
        headers: {
          "Content-Type": "application/json"
        }
      })
          .then(result => {
            const co2e = result.data["legs"][0].co2e;
            const emissionsEstimate = parseFloat(co2e).toFixed(2);
  
            let fullStar = '&starf;';
            let emptyStar = '&star;';
            let rating;
            /*Melbourne - 1:35 -> 40,000.00
              Perth - 5:05 -> 180,000.00
              Jakarta - 7:35 -> 250,000.00
              Tokyo - 9:50 -> 360,000.00
              Dubai- 14:25 -> 420,000.00
            * */
            if (emissionsEstimate > 420000.0) {
              rating = fullStar.repeat(1) + emptyStar.repeat(4);
              setEff(rating);
              setStarEfficiency(1);
            } else if (emissionsEstimate <= 360000.0 && emissionsEstimate > 250000.0) {
              rating = fullStar.repeat(2) + emptyStar.repeat(3);
              setEff(rating);
              setStarEfficiency(2);
            } else if (emissionsEstimate <= 250000.0 && emissionsEstimate > 180000.0) {
              rating = fullStar.repeat(3) + emptyStar.repeat(2);
              setEff(rating);
              setStarEfficiency(3);
            } else if (emissionsEstimate <= 180000.0 && emissionsEstimate > 40000.0) {
              rating = fullStar.repeat(4) + emptyStar.repeat(1);
              setEff(rating);
              setStarEfficiency(4);
            } else {
              rating = fullStar.repeat(5);
              setEff(rating);
              setStarEfficiency(5);
            }
  
            setEmissions(emissionsEstimate);
          })
          .catch(reason => {
            Swal.fire(
                {
                  title: "Error",
                  text: reason
                }
            )
            navigate("/error");
          })
  
      axios.post(`${process.env.REACT_APP_BASE_URL}/trip/recommendations`, JSON.stringify([recLegs]), {
        headers: {
          "Content-Type": "application/json"
        }
      })
          .then(result => {
            const recommendations = result.data["legs"][0].recommendations;
            let recommendedFlights = "";
            let temp = "";
            for (let i = 0; i <recommendations.length; i++) {
              recommendedFlights += "<tr>" + (i + 1).toString() + ". " + recommendations[i] + " </tr><br />";
              temp += (i + 1).toString() + ". " + recommendations[i] + ", ";
            }
            setCsvRecomms(temp);
            setRecomms(recommendedFlights);
          })
          .catch(reason => {
            Swal.fire(
                {
                  title: "Error",
                  text: reason
                }
            )
            navigate("/error");
          })
  
      if (map.current) return; // initialize map only once
      map.current = new mapboxgl.Map({
        container: mapContainer.current,
        style: "mapbox://styles/mapbox/streets-v11",
        center: [lng, lat],
        zoom: zoom,
        projection: projection,
      });
  
      map.current.on("style.load", () => {
        map.current.setFog({ "horizon-blend": 0.05 }); // Enable stars with reduced atmosphere
      });
  
      const route = {
        type: "FeatureCollection",
        features: [
          {
            type: "Feature",
            geometry: {
              type: "LineString",
              coordinates: [origin, destination],
            },
          },
        ],
      };
  
      const aPoint = {
        type: "FeatureCollection",
        features: [
          {
            type: "Feature",
            properties: {},
            geometry: {
              type: "Point",
              coordinates: origin,
            },
          },
        ],
      };
  
      const lineDistance = length(route.features[0]);
  
      const arc = [];
  
      // Number of steps to use in the arc and animation, more steps means
      // a smoother arc and animation, but too many steps will result in a
      // low frame rate
      const steps = 500;
  
      // Draw an arc between the `origin` & `destination` of the two points
      for (let i = 0; i < lineDistance; i += lineDistance / steps) {
        const segment = along(route.features[0], i);
        arc.push(segment.geometry.coordinates);
      }
  
      route.features[0].geometry.coordinates = arc;
  
      let counter = 0;
  
      map.current.on("load", () => {
        // Add a source and layer displaying a point which will be animated in a circle.
        map.current.addSource("route", {
          type: "geojson",
          data: route,
        });
  
        map.current.addSource("point", {
          type: "geojson",
          data: point,
        });
  
        map.current.addLayer({
          id: "route",
          source: "route",
          type: "line",
          paint: {
            "line-width": 2,
            "line-color": "#007cbf",
          },
        });
  
        map.current.addLayer({
          id: "point",
          source: "point",
          type: "symbol",
          layout: {
            // This icon is a part of the Mapbox Streets style.
            // To view all images available in a Mapbox style, open
            // the style in Mapbox Studio and click the "Images" tab.
            // To add a new image to the style at runtime see
            // https://docs.mapbox.com/mapbox-gl-js/example/add-image/
            "icon-image": "airport-15",
            "icon-size": 2,
            "icon-rotate": ["get", "bearing"],
            "icon-rotation-alignment": "map",
            "icon-allow-overlap": true,
            "icon-ignore-placement": true,
          },
        });
  
        function animate() {
          const start =
            route.features[0].geometry.coordinates[
              counter >= steps ? counter - 1 : counter
            ];
          const end =
            route.features[0].geometry.coordinates[
              counter >= steps ? counter : counter + 1
            ];
          if (!start || !end) return;
  
          // Update point geometry to a new position based on counter denoting
          // the index to access the arc
          aPoint.features[0].geometry.coordinates =
            route.features[0].geometry.coordinates[counter];
  
          // Calculate the bearing to ensure the icon is rotated to match the route arc
          // The bearing is calculated between the current point and the next point, except
          // at the end of the arc, which uses the previous point and the current point
          aPoint.features[0].properties.bearing = bearing(
            point(start),
            point(end)
          );
  
          // Update the source with this new data
          map.current.getSource("point").setData(aPoint);
  
          // Request the next frame of animation as long as the end has not been reached
          if (counter < steps) {
            requestAnimationFrame(animate);
          }
  
          counter = counter + 1;
        }
  
        // Start the animation
        animate(counter);
      });
    }
  }, []);

  const csvData = [
    {
      "Flight Number": location.state?.flightNo,
      "Source": location.state?.from.name,
      "Destination": location.state?.to.name,
      "Class": location.state?.class,
      "Flight Emissions": emissions,
      "Flight Efficiency": starEfficiency,
      "Recommendations": csvRecomms
    },
  ];

  const generateInsights = () => {
    return (
      <div>
        <h5>Insights for your flight from {location.state?.from.name} to {location.state?.to.name} on flight {location.state?.flightNo}, flying {location.state?.class}</h5>

        <table>
          <tbody>
          <tr>
            <td>Flight Emissions</td>
            <td id={"flight-emissions"} className="details_td">{emissions} kg</td>
          </tr>
          <tr>
            <td>Flight Efficiency</td>
            <td className="details_td" dangerouslySetInnerHTML={{__html: eff}}></td>
          </tr>
          <tr>
            <td>
              Recommendations
              <br />
              (Lowest to highest emissions)
            </td>
            <td id="flight-recommendations" className="details_td" dangerouslySetInnerHTML={{__html: recomms}}></td>
          </tr>
          </tbody>
        </table>

      <button
        type="button"
        className="btn btn-primary"
        onClick={() => navigate('/class', {state: {...location.state}})}
      >
        BACK
      </button>

        <div className="float-end">
          <CSVLink data={csvData} filename="flight_results">
            <button type="button" className="btn btn-secondary mx-2">
              DOWNLOAD
            </button>
          </CSVLink>

          <button
              type="button"
              className="btn btn-primary"
              onClick={() => navigate("/")}
          >
            START OVER
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="display_box">
      <div ref={mapContainer} className="map-container"/>
      {isLoaded ? generateInsights() : loadingComponent}

    </div>
  )
}

export default FlightClass;
