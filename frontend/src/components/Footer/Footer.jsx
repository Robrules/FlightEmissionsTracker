import React from "react";
import { useNavigate } from "react-router-dom";
import "./Footer.css";

function Footer() {
  const navigate = useNavigate();
  return (
    <div id="footer">
      <div>
        <button className="btn btn-link" onClick={() => navigate("/aboutUs")}>
          About Us
        </button>
        <p>|</p>
        <button
          className="btn btn-link"
          onClick={() => navigate("/ourCalculations")}
        >
          Our Calculations
        </button>
        <p>|</p>
        <button className="btn btn-link" onClick={() => navigate("/contactUs")}>
          Contact Us
        </button>
      </div>
      <div>Copyright Emission Solutions 2022</div>
    </div>
  );
}

export default Footer;
