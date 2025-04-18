import React, { useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import $ from "jquery";
import Swal from "sweetalert2";
import "./shared/MainPage.css";

function FlightClass() {
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    if (!location.state?.prev) {
      navigate("/");
    }
  }, []);

  const checkInput = () => {
    const classInput = $("[id=class_input]").val();

    if (classInput !== null) {
      navigate("/insights", {
        state: {
          ...location.state,
          class: classInput,
        },
      });
    } else {
      Swal.fire({
        title: "Failure",
        text: "You must select the flight class you are sitting in",
        icon: "error",
      });
    }
  };

  return (
    <div className="display_box">
      <h5>Enter your flight class:</h5>

      <table>
        <tbody>
          <tr>
            <td>Class</td>
            <td className="details_td">
              <select id="class_input" name="Category" defaultValue={location.state?.class || "DEFAULT"}>
                <option value="DEFAULT" disabled>
                  Select from drop down list
                </option>
                <option value="economy">Economy Class</option>
                <option value="business">Business Class</option>
                <option value="first">First Class</option>
              </select>
            </td>
          </tr>
        </tbody>
      </table>

      <button
        type="button"
        className="btn btn-primary"
        onClick={() => navigate(location.state.prev, {state: {...location.state}})}
      >
        BACK
      </button>

      <button
        type="button"
        className="btn btn-primary button_right"
        onClick={checkInput}
      >
        SUBMIT
      </button>
    </div>
  );
}

export default FlightClass;
