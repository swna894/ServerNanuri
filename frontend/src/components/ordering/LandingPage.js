import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import OrderHeader from "./OrderHeader";
import OrderFooter from "./OrderFooter";
import { Card, Row, Col, BackTop } from "antd";

import {
  actionGetSuppliers,
  actionGetSupplier,
} from "../../_actions/supplier_action";
import { getProductsInitAction } from "../../_actions/product_action";

const style = {
  height: 40,
  width: 40,
  lineHeight: "40px",
  borderRadius: 4,
  backgroundColor: "#1088e9",
  color: "#fff",
  textAlign: "center",
  fontSize: 14,
  position: "absolute",
  top: "-20px", left: "70px",
};

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
          style={{ height: "400px", width: "458px" }}
          cover={
            <img
              style={{
                bordered: true,
                //height: "auto",
                minHeight: "250px",
                maxHeight: "250px",
                width: "auto",
                maxWidth: "300px",
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
      <BackTop>
        <div style={style}>UP</div>
      </BackTop>
      <OrderFooter />
    </div>
  );
}

export default LandingPage;
