import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import OrderHeader from "./OrderHeader";
import OrderFooter from "./OrderFooter";
import { Card, Row, Col, BackTop, Button, Input } from "antd";
import { MinusOutlined, PlusOutlined } from "@ant-design/icons";
import {
  actionGetSuppliers,
  actionGetSupplier,
} from "../../_actions/supplier_action";
import { getProductsInitAction } from "../../_actions/product_action";
import newProduct from "../../images/new.png";
import discount from "../../images/discount.png";

const backTopstyle = {
  height: 40,
  width: 40,
  lineHeight: "40px",
  borderRadius: 4,
  backgroundColor: "#1088e9",
  color: "#fff",
  textAlign: "center",
  fontSize: 14,
  position: "absolute",
  top: "-70px",
  left: "70px",
};

const onChangeQtyHandler = () => {};

function LandingPage() {
  const dispatch = useDispatch();
  const products = useSelector((state) => state.product.products.content);

  useEffect(() => {
    dispatch(actionGetSupplier());
    dispatch(actionGetSuppliers());
    dispatch(getProductsInitAction());
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  const isSecial = true;
  const isNew = true;

  const descriptionStyle = {
    overflow: "hidden",
    fontSize: "16px",
    marginBottom: "7px",
  };

  const inputQtyStyle = {
    width: "20%",
    fontSize: "14px",
    textAlign: "center",
    fontWeight: "bold",
  };

  const priceStyle = {
    overflow: "hidden",
    fontSize: "18px",
    marginBottom: "7px",
    fontWeight: "bold",
  };
  const specialPriceStyle = isSecial
    ? {
        textDecoration: "line-through",
        overflow: "hidden",
        fontSize: "16px",
        marginBottom: "7px",
        fontWeight: "bold",
        display: "inline",
        color: "red",
      }
    : { display: "none" };

  const specialSpaceStyle = isSecial
    ? { display: "inline" }
    : { display: "none" };
  const specialStyle = isSecial
    ? { position: "absolute", top: "220px", left: "25px" }
    : { display: "none" };
  const newStyle = isNew
    ? { position: "absolute", top: "220px", right: "25px" }
    : { display: "none" };

  const buttonStyle = {
    display: "inline",
    position: "absolute",
    top: "340px",
    right: "-90px",
  };
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
          <div style={newStyle}>
            <img src={newProduct} alt="special"></img>
          </div>
          <div style={specialStyle}>
            <img src={discount} alt="special"></img>
          </div>

          <p style={descriptionStyle}>
            [ {item.code} ] &nbsp;
            {item.description}
          </p>
          <span style={specialPriceStyle}>${item.price}</span>
          <span style={specialSpaceStyle}>&nbsp;&nbsp;</span>
          <span style={priceStyle}>${item.price} </span>
          <span>&nbsp;&nbsp;&nbsp;Packing : {item.pack}</span>
          <p style={{ color: "#1835D0" }}>2001-02-02</p>
          <p style={{ color: "#fff" }}>- </p>
          <p style={{ color: "#fff" }}>- </p>

          <div style={buttonStyle}>
            <Button type="primary" icon={<MinusOutlined />}></Button>
            <Input
              style={inputQtyStyle}
              value={item.qty}
              onChange={onChangeQtyHandler}
            ></Input>
            <Button type="primary" icon={<PlusOutlined />}></Button>
          </div>
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
          backgroundColor: "#f0f0f0",
        }}
      >
        <Row gutter={[16, 16]}>{orderLists}</Row>
      </div>
      <BackTop>
        <div style={backTopstyle}>UP</div>
      </BackTop>
      <OrderFooter />
    </div>
  );
}

export default LandingPage;
