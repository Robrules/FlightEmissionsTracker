import React from "react";
import "./shared/SidePage.css";

function OurCalculations() {
  return (
    <div className="display_box">
      <h1>Our Calculations</h1>
      <div id="our_calculations_text">
        <p>
          Transparency is very important to us at Emission Solutions. We want
          our users to have information available on how we calculate the
          emissions on our web application.
        </p>
        <p>The calculation requires the following parameters:</p>
        <ol>
          <li>
            Legs - These are the legs of a trip. It can be one or more. Each leg
            requires:
            <ul>
              <li>From - Airport where the flight leg starts.</li>
              <li>To - Airport where the flight leg ends.</li>
              <li>Passengers - Number of passengers on the flight.</li>
              <li>Class - The flight class the user is flying in.</li>
            </ul>
          </li>
          <li>
            Aircraft year of manufacture - Newer aircraft are more efficient
            than old aircraft in general.
          </li>
        </ol>
        <p>
          For calculating emissions of a trip, we use Climatiq&apos;s API. More
          information on how they calculate emissions can be found at their
          official website&nbsp;
          <a href="https://www.climatiq.io/">here</a>. As a general overview,
          they use emission models developed by researchers, and which take the
          Legs parameter (as described above) to estimate the emissions in kgs.
        </p>
        <p>
          According to an&nbsp;
          <a href="https://www.mckinsey.com/industries/aerospace-and-defense/our-insights/future-air-mobility-blog/fuel-efficiency-why-airlines-need-to-switch-to-more-ambitious-measures">
            analysis done by McKinsey & Company
          </a>
          , airline carriers have managed to reduce the fuel consumption per
          passenger-kilometer by ~39% between 2005 and 2019 (before COVID-19).
          However, they also noted that future progress is unlikely to keep up
          the same pace, as the categories that contributed to the past
          performance are steadily approaching their limits. Regardless, as of
          March 1 2022, the date this analysis was published, the average
          year-on-year reduction in fuel consumption per passenger-kilometer is
          3.4%. This is also accounted for in our emission estimations.
        </p>
      </div>
    </div>
  );
}

export default OurCalculations;
