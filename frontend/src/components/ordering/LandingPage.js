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
import {
  getProductsInitAction,
  changeIncremant,
  changeDecremant,
} from "../../_actions/product_action";
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

const onChangeQtyHandler = (code, pack) => {
  console.log("input code = " + code + " pack = " + pack);
};

function LandingPage() {
  const dispatch = useDispatch();
  const products = useSelector((state) =>
    state.product.products.content ? state.product.products.content : []
  );
  const abbr = useSelector((state) => state.supplier.supplier);
  const comapny = useSelector((state) => state.user.userData);

  useEffect(() => {
    dispatch(actionGetSupplier());
    dispatch(actionGetSuppliers());
    dispatch(getProductsInitAction());
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  const onClickIncrease = (code) => {
    const param = { abbr: abbr, code: code, id: comapny.id };
    const goods = products.map((item) =>
      item.code === code ? { ...item, qty: item.qty + item.pack } : item
    );
    dispatch(changeIncremant(goods, param));
  };

  const onClickDecrease = (code) => {
    const param = { abbr: abbr, code: code, id: comapny.id };
    const goods = products.map((item) =>
      item.code === code
        ? { ...item, qty: item.qty - item.pack > 0 ? item.qty - item.pack : 0 }
        : item
    );
    dispatch(changeDecremant(goods, param));
  };

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
  const specialPriceStyle = {
    textDecoration: "line-through",
    overflow: "hidden",
    fontSize: "16px",
    marginBottom: "7px",
    fontWeight: "bold",
    display: "inline",
    color: "red",
  };

  const imageStyel = {
    bordered: true,
    //height: "auto",
    minHeight: "250px",
    maxHeight: "250px",
    width: "auto",
    maxWidth: "300px",
    textAlign: "center",
    margin: "10px auto",
  };
  const specialStyle = { position: "absolute", top: "20px", left: "25px" };
  const newStyle = { position: "absolute", top: "220px", left: "25px" };
  const hiddenStyle = { display: "none" };

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
              style={imageStyel}
              alt={item.code}
              // src={`/images/${item.abbr}/${item.code}.jpg`}
              src={"data:image/jpg;base64," + item.image}
            />
          }
        >
          <div style={item.new ? newStyle : hiddenStyle}>
            <img src={newProduct} alt="special"></img>
          </div>
          <div style={item.special ? specialStyle : hiddenStyle}>
            <img src={discount} alt="special"></img>
          </div>
          <p style={descriptionStyle}>
            [ {item.code} ] &nbsp;
            {item.description}
          </p>
          <span style={item.special ? priceStyle : { display: "none" }}>
            ${item.specialPrice}
          </span>
          <span
            style={item.special ? { display: "inline" } : { display: "none" }}
          >
            &nbsp;&nbsp;
          </span>
          <span style={item.special ? specialPriceStyle : priceStyle}>
            ${item.price}
          </span>
          <span>&nbsp;&nbsp;&nbsp;Packing : {item.pack}</span>
          <p style={{ color: "#1835D0" }}>2001-02-02</p>
          <p style={{ color: "#fff" }}>&nbsp; </p>
          <p style={{ color: "#fff" }}>&nbsp; </p>
          <div style={buttonStyle}>
            <Button
              type="primary"
              icon={<MinusOutlined />}
              onClick={() => onClickDecrease(item.code, item.pack)}
            ></Button>

            <Input
              style={inputQtyStyle}
              value={item.qty}
              onChange={() => onChangeQtyHandler(item.code)}
            ></Input>
            <Button
              type="primary"
              icon={<PlusOutlined />}
              onClick={() => onClickIncrease(item.code)}
            ></Button>
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
