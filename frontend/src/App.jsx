import React from "react";
import Header from "./components/Header/Header.jsx";
import Footer from "./components/Footer/Footer.jsx";
import FlightDetails from "./pages/mainPages/FlightDetails.jsx";
import FlightOptions from "./pages/mainPages/FlightOptions.jsx";
import FlightClass from "./pages/mainPages/FlightClass.jsx";
import FlightInsights from "./pages/mainPages/FlightInsights.jsx";
import AboutUs from "./pages/sidePages/AboutUs.jsx";
import OurCalculations from "./pages/sidePages/OurCalculations.jsx";
import ContactUs from "./pages/sidePages/ContactUs.jsx";
import ErrorMsg from "./pages/sidePages/ErrorMsg.jsx";

import { Routes, Route } from "react-router-dom";

function App() {
  return (
    <div>
      <Header />
      <Routes>
        <Route exact path="/" element={<FlightDetails />} />
        <Route path="/options" element={<FlightOptions />} />
        <Route path="/class" element={<FlightClass />} />
        <Route path="/insights" element={<FlightInsights />} />
        <Route path="/aboutUs" element={<AboutUs />} />
        <Route path="/ourCalculations" element={<OurCalculations />} />
        <Route path="/contactUs" element={<ContactUs />} />
        <Route path="/error" element={<ErrorMsg />} />
      </Routes>
      <Footer />
    </div>
  );
}

export default App;
