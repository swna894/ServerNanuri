import React from "react";
import { Layout } from "antd";

function footer() {
  const { Header } = Layout;
  return (
    <div>
      <Header
        style={{
          position: "absolute",
          zIndex: 1,
          width: "100%",
          bottom: "0",
        }}
      >
        <h2 style={{ color: "#fff" }}>
          Enquiry ? Please Contact : David Na 027-652-1111
        </h2>
      </Header>
    </div>
  );
}

export default footer;
