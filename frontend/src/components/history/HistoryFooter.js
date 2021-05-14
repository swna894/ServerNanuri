import React from "react";
import { withRouter } from "react-router-dom";
import { Layout } from "antd";


function HistoryFooter(props) {
 const footerStyel = {
   position: "fixed",
   width: "100%",
   bottom: "0",
   backgroundColor: "#bfbfbf",
 };
  return (
    <div style={{ backgroundColor: "#f0f0f0" }}>
      <Layout.Header style={footerStyel}>
        <h2>
          Enquiry ? Please Contact : David Na 027-652-1111
        </h2>
      </Layout.Header>
    </div>
  );
}

export default withRouter(HistoryFooter);
