import React from "react";
import OrderHeader from "./OrderHeader";
import OrderFooter from "./OrderFooter";
//import axios from 'axios';

function LandingPage() {
  //  useEffect(() => {
  //    axios.get("/api/suppliers").then((response) => {
  //      console.log(response.data);
  //    });
  //  }, []);

  return (
    <div>
      <OrderHeader />
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          width: "100%",
          height: "100vh",
        }}
      >
        <h2>시작 페이지</h2>
      </div>
      <OrderFooter />
    </div>
  );
}

export default LandingPage;
