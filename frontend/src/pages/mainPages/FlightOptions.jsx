import React, { useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import $ from "jquery";
import Swal from "sweetalert2";
import "./shared/MainPage.css";

function FlightOptions() {
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    if (!location.state?.prev) {
      navigate("/");
    }
  }, []);

  const flightOptions = location.state?.flightsWithNo.map((flight) => {
    return (
      <option key={`${flight.flightNo}`} value={flight.flightNo}>
        {flight.flightNo}
      </option>
    );
  });

  const checkInput = () => {
    const flightInput = $("[id=flight_input]").val();

    if (flightInput !== null) {
      navigate("/class", {
        state: {
          ...location.state,
          prev: "/options",
          flightNo: flightInput,
        },
      });
    } else {
      Swal.fire({
        title: "Failure",
        text: "You must select the flight number of your flight",
        icon: "error",
      });
    }
  };

  return (
    <div className="display_box">

      <h5>Choose your flight number:</h5>

      <table>
        <tbody>
          <tr>
            <td>Flight No.</td>
            <td className="details_td">
              <select id="flight_input" name="Category" defaultValue={location.state?.flightNo || "DEFAULT"}>
                <option value="DEFAULT" disabled>
                  Select from drop down list
                </option>
                {flightOptions}
              </select>
            </td>
          </tr>
        </tbody>
      </table>

      <button
        type="button"
        className="btn btn-primary"
        onClick={() => navigate("/", {state: {...location.state}})}
      >
        BACK
      </button>

      <button
        type="button"
        className="btn btn-primary button_right"
        onClick={checkInput}
      >
        NEXT
      </button>
    </div>
  );
}

export default FlightOptions;
