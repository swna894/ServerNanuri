import React from "react";
import { Row, Col } from "antd";
import HitoryHeader from "./HistoryHeader";
import HistoryFooter from "./HistoryFooter";

const styleLeft = { background: "#0092ff", padding: "8px 0", height: "90vh" };
const styleRight = { background: "#1188aa", padding: "8px 0", height: "90vh" };

function LandingPage() {
  return (
    <div>
      <HitoryHeader />
      <div
        style={{
          //height: "100vh",
          padding: "80px 10px 10px 10px",
          backgroundColor: "#f0f0f0",
        }}
      >
        <Row
          justify="center"
          align="top"
          style={{
            padding: "10px 10px 10px 10px",
            backgroundColor: "#f0f0f0",
          }}
        >
          <Col
            xxl={12}
            xl={12}
            lg={12}
            md={24}
            sm={24}
            xs={24}
            style={styleLeft}
          ></Col>
          <Col
            xxl={12}
            xl={12}
            lg={12}
            md={24}
            sm={24}
            xs={24}
            style={styleRight}
          ></Col>
        </Row>
      </div>
      <HistoryFooter />
    </div>
  );
}

export default LandingPage;
