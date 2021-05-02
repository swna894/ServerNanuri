import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import OrderHeader from "./OrderHeader";
import OrderFooter from "./OrderFooter";
import { Card, Row, Col, BackTop, Button, Input } from "antd";
import { MinusOutlined, PlusOutlined } from "@ant-design/icons";
import { useWindowWidthAndHeight } from "../../utils/CustomHooks";

import {
  actionGetSuppliers,
  actionGetSupplier,
} from "../../_actions/supplier_action";
import {
  getProductsInitAction,
  changeCart,
} from "../../_actions/product_action";
import newProduct from "../../images/new.png";
import discount from "../../images/discount.png";

const backMobileTopstyle = {
  height: 40,
  width: 40,
  lineHeight: "40px",
  borderRadius: 4,
  backgroundColor: "#1088e9",
  color: "#fff",
  textAlign: "center",
  fontSize: 14,
  position: "absolute",
  top: "-54px",
  left: "45px",
};

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
  top: "-55px",
  left: "70px",
};

function LandingPage() {
  const dispatch = useDispatch();
  const content = useSelector((state) =>
    state.product.products.content ? state.product.products.content : []
  );
  const products = useSelector((state) => state.product.products);
  const totalElements = useSelector(
    (state) => state.product.products.totalElements
  );
  const size = useSelector((state) => state.product.products.size);
  const number = useSelector((state) => state.product.products.number);

  const abbr = useSelector((state) => state.supplier.abbr);
  const comapny = useSelector((state) => state.user.userData);

  useEffect(() => {
    dispatch(actionGetSupplier());
    dispatch(actionGetSuppliers());
    dispatch(getProductsInitAction());
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  const onClickIncrease = (code, pack, qty) => {
    const param = {
      abbr: abbr,
      code: code,
      id: comapny.id,
      qty: parseInt(pack) + parseInt(qty),
    };
    const goods = products.content.map((item) =>
      item.code === code
        ? { ...item, qty: parseInt(item.qty) + item.pack }
        : item
    );
    const pageable = {
      totalElements: totalElements,
      size: size,
      number: number,
    };
    dispatch(changeCart(goods, pageable, param));
  };

  const onClickDecrease = (code, pack, qty) => {
    const count = parseInt(qty) - parseInt(pack);
    const param = {
      abbr: abbr,
      code: code,
      id: comapny.id,
      qty: count > 0 ? count : 0,
    };
    const goods = content.map((item) =>
      item.code === code
        ? {
            ...item,
            qty:
              (parseInt(item.qty) - item.pack) > 0
                ? (parseInt(item.qty) - item.pack)
                : 0,
          }
        : item
    );
    const pageable = {
      totalElements: totalElements,
      size: size,
      number: number,
    };

    dispatch(changeCart(goods, pageable, param));
  };

  const onChangeInputHandler = (code, qty) => {
    const param = { abbr: abbr, code: code, id: comapny.id, qty: qty };
    const goods = content.map((item) =>
      item.code === code ? { ...item, qty: qty } : item
    );
    const pageable = {
      totalElements: totalElements,
      size: size,
      number: number,
    };
    dispatch(changeCart(goods, pageable, param));
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
    //bordered: true,
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
  const cardNormalStyle = {
    height: "400px",
    width: "450px",
    borderStyle: "groove",
  }; // 458px
  const cardOrderStyle = {
    height: "400px",
    width: "450px",
    backgroundColor: "#e6f7ff",
    borderStyle: "groove",
  };

  const buttonStyle = {
    display: "inline",
    position: "absolute",
    top: "340px",
    right: "-98px",
  };

  // <Col xxl={6} xl={8} lg={12} md={12} sm={24} xs={24} key={index}></Col>
  const orderLists =
    content &&
    content.map((item, index) => (
      <Col  key={index}>
        <Card
          style={item.qty > 0 ? cardOrderStyle : cardNormalStyle}
          cover={
            <img
              style={imageStyel}
              alt={item.code}
              //src={`/images/${item.abbr}/${item.code}.jpg`}
              src={"data:image/jpg;base64," + item.image}
            />
          }
        >
          <div style={{ width: "100%" }}>
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
            <span>&nbsp;&nbsp;&nbsp;PACKING : {item.pack}</span>
            <span>&nbsp;&nbsp;&nbsp;STOCK : {item.stock}</span>
            <p style={{ color: "#1835D0" }}>2001-02-02</p>
            <p style={{ color: "#fff" }}>&nbsp; </p>
            <p style={{ color: "#fff" }}>&nbsp; </p>
            <div style={buttonStyle}>
              <Button
                type="primary"
                icon={<MinusOutlined />}
                onClick={() => onClickDecrease(item.code, item.pack, item.qty)}
              ></Button>

              <Input
                style={inputQtyStyle}
                value={item.qty}
                onChange={(e) =>
                  onChangeInputHandler(item.code, e.target.value)
                }
              ></Input>
              <Button
                type="primary"
                icon={<PlusOutlined />}
                onClick={() => onClickIncrease(item.code, item.pack, item.qty)}
              ></Button>
            </div>
          </div>
        </Card>
      </Col>
    ));

  const [width] = useWindowWidthAndHeight();
  return (
    <div>
      <OrderHeader />
      <div
        style={{
          display: "flex",
          width: "100%",
          //height: "100vh",
          padding: "80px 10px 10px 10px",
          backgroundColor: "#f0f0f0",
          paddingLeft : "28px"
        }}
      >
        <Row gutter={[16, 16]}>{orderLists}</Row>
      </div>
      <BackTop>
        <div style={width > 850 ? backTopstyle : backMobileTopstyle}>UP</div>
      </BackTop>
      <OrderFooter />
    </div>
  );
}

export default LandingPage;
