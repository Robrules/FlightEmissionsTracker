import React from "react";
import "./Header.css";
import { useNavigate } from "react-router-dom";

function Header() {
  const navigate = useNavigate();
  return (
    <div id="header" className="row">
      <div id="website_name_div" className="col-md-6">
        <button onClick={() => navigate("/")}>Flight Emission Tracker</button>
      </div>
      <div id="header_buttons_div" className="col-md-6">
        <button onClick={() => navigate("/ourCalculations")}>
          Our Calculations
        </button>
        <button onClick={() => navigate("/aboutUs")}>About Us</button>
      </div>
    </div>
  );
}

export default Header;
