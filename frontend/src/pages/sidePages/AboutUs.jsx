import React from "react";
import "./shared/SidePage.css";

function AboutUs() {
  return (
    <div className="display_box">
      <h1>About Us</h1>
      <div id="about_us_text">
        <p>
          Here at Emission Solutions, we are passionate about the environment
          and recognise that Climate Change is becoming an everpresent issue. We
          are also aware of an increase in greenhouse gases due to human
          influence. Particularly, air travel greatly contributes to these
          emissions.
        </p>
        <p>
          Through our web application, Flight Emission Tracker, we aim to help
          people track and reduce their air travel footprint by providing an
          analysis of carbon emissions from their flights. We hope that this
          will help the general public make more sustainable choices and lead to
          a healthier world for us all!
        </p>
      </div>
      <div className="people row">
        <div className="col">
          <h3>Adrian Currington</h3>
          <img src="icon.png" alt="Adrian Currington" />
          <h5>Software Engineer</h5>
        </div>
        <div className="col">
          <h3>Biagio Di Mento</h3>
          <img src="icon.png" alt="Biagio Di Mento" />
          <h5>Software Engineer</h5>
        </div>
        <div className="col">
          <h3>Brenda Keo</h3>
          <img src="icon.png" alt="Brenda Keo" />
          <h5>Software Engineer</h5>
        </div>
        <div className="col">
          <h3>Harshit Pathak</h3>
          <img src="icon.png" alt="Harshit Pathak" />
          <h5>Software Engineer</h5>
        </div>
        <div className="col">
          <h3>Robert Bannayan</h3>
          <img src="icon.png" alt="Robert Bannayan" />
          <h5>Software Engineer</h5>
        </div>
      </div>
    </div>
  );
}

export default AboutUs;
