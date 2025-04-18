import React from "react";
import "./shared/SidePage.css";

function ContactUs() {
  return (
    <div className="display_box">
      <h1>Contact Us</h1>
      <div className="people row">
        <div className="col">
          <h3>Adrian Currington</h3>
          <img src="icon.png" alt="Adrian Currington" />
          <h6>acur4177@uni.sydney.edu.au</h6>
          <h6>0400 000 000</h6>
        </div>
        <div className="col">
          <h3>Biagio Di Mento</h3>
          <img src="icon.png" alt="Biagio Di Mento" />
          <h6>bidi7355@uni.sydney.edu.au</h6>
          <h6>0412 345 62</h6>
        </div>
        <div className="col">
          <h3>Brenda Keo</h3>
          <img src="icon.png" alt="Brenda Keo" />
          <h6>bkeo0834@uni.sydney.edu.au</h6>
          <h6>0400 000 000</h6>
        </div>
        <div className="col">
          <h3>Harshit Pathak</h3>
          <img src="icon.png" alt="Harshit Pathak" />
          <h6>hpat6151@uni.sydney.edu.au</h6>
          <h6>0400 000 000</h6>
        </div>
        <div className="col">
          <h3>Robert Bannayan</h3>
          <img src="icon.png" alt="Robert Bannayan" />
          <h6>rban8770@uni.sydney.edu.au</h6>
          <h6>0400 000 000</h6>
        </div>
      </div>
    </div>
  );
}

export default ContactUs;
