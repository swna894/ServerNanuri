import React, { useEffect } from "react";
import { useDispatch } from "react-redux";
import OrderHeader from "./OrderHeader";
import OrderFooter from "./OrderFooter";
import {
  actionGetSuppliers,
  actionGetSupplier,
} from "../../_actions/supplier_action";

function LandingPage() {
  const dispatch = useDispatch();
  useEffect(() => {
    dispatch(actionGetSupplier());
    dispatch(actionGetSuppliers());
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

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
