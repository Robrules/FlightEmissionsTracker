import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate, useLocation } from "react-router-dom";
import $ from "jquery";
import Swal from "sweetalert2";
import "./shared/MainPage.css";

function FlightDetails() {
  // states and other variables
  const navigate = useNavigate();
  const location = useLocation();
  const [airports, setAirports] = useState(location?.state?.airports || null);
  const [flights, setFlights] = useState(location?.state?.flights || null);
  const isLoaded = airports != null && flights != null;

  // load data when mounted
  useEffect(() => {
    if (!airports) {
      axios
        .get(`${process.env.REACT_APP_BASE_URL}/airport/allAirports`)
        .then((res) => {
          setAirports(res.data);
        })
        .catch(() => {
          navigate("/error");
        });
    }

    if (!flights) {
      axios
      .get(`${process.env.REACT_APP_BASE_URL}/flight/allFlights`)
      .then((res) => {
        setFlights(res.data);
      })
      .catch(() => {
        navigate("/error");
      });
    }
  }, []);

  // validate flight no input
  const checkFlightNoInput = () => {
    let flightNo = $("[id=flight_no_input]").val().trim();

    if (flightNo !== null && flightNo !== "") {
      const matches = flights.filter((flight) => {
        return flight.flightNo === flightNo;
      });
      if (matches.length != 1) {
        Swal.fire({
          title: "Failure",
          text: "You must enter a valid flight number",
          icon: "error",
        });
      } else {
        const from = matches[0].source;
        const to = matches[0].destination;
        navigate("/class", { state: { from, to, flightNo, airports, flights, prev: "/" } });
      }
    } else {
      Swal.fire({
        title: "Failure",
        text: "You must select the source and destination of your flight, or enter a valid flight number",
        icon: "error",
      });
    }
  };

  // validate airports input
  const checkAirportsInput = () => {
    const fromIdx = $("[id=from_input]").val();
    const toIdx = $("[id=to_input]").val();

    const from = fromIdx != null ? airports[parseInt(fromIdx)] : null;
    const to = toIdx != null ? airports[parseInt(toIdx)] : null;

    if (from !== null && to !== null) {
      if (from === to) {
        Swal.fire({
          title: "Failure",
          text: "You cannot select the same source and destination of your flight",
          icon: "error",
        });
      } else {
        axios
          .get(`${process.env.REACT_APP_BASE_URL}/flight/filterFlights?from=${from.code}&to=${to.code}`)
          .then((res) => {
            if (res.data.length === 0) {
              Swal.fire({
                title: "Failure",
                text: "There are no flights between these two locations",
                icon: "error",
              });
            } else {
              navigate("/options", { state: { from, to, flightsWithNo: res.data, fromIdx: parseInt(fromIdx), toIdx: parseInt(toIdx), airports, flights, prev: "/" } });
            }
          })
          .catch(() => {
            navigate("/error");
          });
      }
    } else {
      Swal.fire({
        title: "Failure",
        text: "You must select the source and destination of your flight, or enter a valid flight number",
        icon: "error",
      });
    }
  };

  // loading component
  const loadingComponent = <h2>Loading...</h2>;

  // loaded components
  const generateContents = () => {
    const airportOptions = airports.map((airport, idx) => {
      return (
        <option key={`${airport.name} (${airport.country})`} value={idx}>
          {airport.name} ({airport.country})
        </option>
      );
    });

    return (
      <div>
        <h5>Enter your flight details:</h5>

        <table>
          <tbody>
            <tr>
              <td>From</td>
              <td className="details_td">
                <select id="from_input" name="Category" defaultValue={location.state?.fromIdx || "DEFAULT"}>
                  <option value="DEFAULT" disabled>
                    Select from drop down list
                  </option>
                  {airportOptions}
                </select>
              </td>
            </tr>
            <tr>
              <td>To</td>
              <td className="details_td">
                <select id="to_input" name="Category" defaultValue={location.state?.toIdx || "DEFAULT"}>
                  <option value="DEFAULT" disabled>
                    Select from drop down list
                  </option>
                  {airportOptions}
                </select>
              </td>
            </tr>
          </tbody>
        </table>

        <button
          type="button"
          className="btn btn-primary button_right"
          onClick={checkAirportsInput}
        >
          NEXT
        </button>

        <br />

        <h5>OR</h5>

        <table>
          <tbody>
            <tr>
              <td>Flight No.</td>
              <td className="details_td">
                <input id="flight_no_input" type="text" defaultValue={location.state?.flightNo || undefined}/>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    );
  };

  return (
    <div className="display_box">
      {isLoaded ? generateContents() : loadingComponent}

      <button
        type="button"
        className="btn btn-primary button_right"
        onClick={checkFlightNoInput}
        disabled={isLoaded ? false : true}
      >
        NEXT
      </button>
    </div>
  );
}

export default FlightDetails;
