import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import OrderHeader from "./OrderHeader";
import OrderFooter from "./OrderFooter";
import { Card, Row, Col } from "antd";

import {
  actionGetSuppliers,
  actionGetSupplier,
} from "../../_actions/supplier_action";
import { getProductsInitAction } from "../../_actions/product_action";

function LandingPage() {
  const dispatch = useDispatch();
  const products = useSelector((state) => state.product.products.content);
  useEffect(() => {
    dispatch(actionGetSupplier());
    dispatch(actionGetSuppliers());
    dispatch(getProductsInitAction());
    console.log(products);
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  const orderLists =
    products &&
    products.map((item, index) => (
      <Col apan={6} key={index}>
        <Card
          raised
          style={{ height: "400px", width: "458px" }}
          cover={
            <img
              style={{
                bordered: true,
                height: "auto",
                maxHeight: "250px",
                width: "auto",
                maxWidth: "350px",
                textAlign: "center",
                margin: "10px auto",
              }}
              alt={item.code}
              src={`/images/${item.abbr}/${item.code}.jpg`}
            />
          }
        >
          [ {item.code} ]{""}
          {item.description}
          <p>$ {item.price}</p>
          <p>{item.stock}</p>
          <p style={{ color: "#fff" }}>- </p>
          <p style={{ color: "#fff" }}>- </p>
        </Card>
      </Col>
    ));

  return (
    <div>
      <OrderHeader />
      <div
        className={products}
        style={{
          display: "flex",
          width: "100%",
          //height: "100vh",
          padding: "80px 10px 10px 10px",
        }}
      >
        <Row gutter={[16, 16]}>{orderLists}</Row>
      </div>
      <OrderFooter />
    </div>
  );
}

export default LandingPage;
