import React from "react";
import { Layout } from "antd";

export default function OrderHeader() {
  const { Header } = Layout;
  return (
    <div>
      <Header
        style={{
          position: "fixed",
          zIndex: 1,
          width: "100%",
        }}
      >
        <h2 style={{ color: "#fff" }}>Welcome to David Na's Ordering System</h2>
      </Header>
    </div>
  );
}
