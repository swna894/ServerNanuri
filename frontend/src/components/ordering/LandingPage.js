import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import OrderHeader from "./OrderHeader";
import OrderFooter from "./OrderFooter";
import { Card, Row, Col, BackTop, Button, Input, Tooltip } from "antd";
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
  npborderRadius: 4,
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
  top: "-42px",
  left: "90px",
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
  const condition = useSelector((state) => state.supplier.condition);
  const category = useSelector((state) => state.supplier.category);
  const comapny = useSelector((state) => state.user.userData);

  useEffect(() => {
    dispatch(actionGetSupplier());
    dispatch(actionGetSuppliers());
    dispatch(getProductsInitAction());
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  const onClickIncrease = (abbr, code, pack, qty) => {
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

  const onClickDecrease = (abbr, code, pack, qty) => {
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
              parseInt(item.qty) - item.pack > 0
                ? parseInt(item.qty) - item.pack
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

  const onChangeInputHandler = (abbr, code, qty) => {
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
    fontSize: "20px",
    marginBottom: "7px",
    marginLeft: "32px",
    fontWeight: "bold",
    fontStyle: "italic",
    whiteSpace: "nowrap",
    textOverflow: "ellipsis",
  };

  const inputQtyStyle = {
    width: "20%",
    fontSize: "14px",
    textAlign: "center",
    fontWeight: "bold",
  };

  const priceStyle = {
    overflow: "hidden",
    fontSize: "22px",
    fontWeight: "bold",
    marginLeft: "32px",
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
  const newStyle = { position: "absolute", top: "200px", left: "25px" };
  const hiddenStyle = { display: "none" };
  const cardNormalStyle = {
    height: "400px",
    // width: "450px",
    borderStyle: "groove",
  }; // 458px
  const cardOrderStyle = {
    height: "400px",
    //width: "450px",
    backgroundColor: "#e6f7ff",
    borderStyle: "groove",
  };

  const buttonStyle = {
    display: "inline",
    position: "absolute",
    top: "350px",
    right: "-117px",
  };

  const specDivStyle = {
    display: "inline-flex",
    float: "right",
    marginRight: "40px",
    paddingTop: "13px",
  };

  const specStyle = {
    fontWeight: "bold",
    fontStyle: "Georgia",
  };

  const labelStyle = {
    fontWeight: "bold",
    color: "#8c8c8c",
    fontSize: "10px",
    fontStyle: "italic",
    paddingTop: "4px",
  };
  // <Col xxl={6} xl={8} lg={12} md={12} sm={24} xs={24} key={index}></Col>
  const orderLists =
    content &&
    content.map((item, index) => (
      <Col xxl={6} xl={8} lg={10} md={12} sm={24} xs={24} key={index}>
        <Card
          bodyStyle={{ padding: "0" }}
          style={item.qty > 0 ? cardOrderStyle : cardNormalStyle}
          cover={
            <Tooltip title={item.description}>
              <img
                style={imageStyel}
                alt={item.code}
                //src={`/images/${item.abbr}/${item.code}.jpg`}
                src={"data:image/jpg;base64," + item.image}
              />
            </Tooltip>
          }
        >
          <div
            style={
              item.price === 0
                ? { width: "1em", display: "none" }
                : { width: "1em", display: "inline" }
            }
          >
            <div style={item.new ? newStyle : hiddenStyle}>
              <img src={newProduct} alt="special"></img>
            </div>
            <div style={item.special ? specialStyle : hiddenStyle}>
              <img src={discount} alt="special"></img>
            </div>
            <p style={descriptionStyle}>{item.description}</p>
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

            <div style={specDivStyle}>
              <span style={labelStyle}>&nbsp;CODE :&nbsp;</span>
              <span style={specStyle}> {item.code}</span> &nbsp;
              <span style={labelStyle}>&nbsp;&nbsp;PACKING : &nbsp;</span>
              <span style={specStyle}>{item.pack}</span>
              <span style={labelStyle}>&nbsp;&nbsp;STOCK : &nbsp;</span>
              <span style={specStyle}>{item.stock}</span>
            </div>
            <p
              style={{
                color: "#f5222d",
                marginLeft: "32px",
                marginBottom: "5px",
                fontWeight: "bold",
              }}
            >
              {item.orderedDate}
            </p>
            {console.log("condtion = " + condition)}
            <p
              style={
                condition === "All" && category === "SEARCH"
                  ? {
                      display: "inline",
                      color: "#40a9ff",
                      marginTop: "-50px",
                      marginLeft: "32px",
                      fontWeight: "bold",
                    }
                  : { display: "none" }
              }
            >
              {item.company}
            </p>
            <p style={{ color: "#fff", margin: "50px" }}>&nbsp; </p>
            <div style={buttonStyle}>
              <Button
                type="primary"
                icon={<MinusOutlined />}
                onClick={() =>
                  onClickDecrease(item.abbr, item.code, item.pack, item.qty)
                }
              ></Button>

              <Input
                style={inputQtyStyle}
                value={item.qty}
                onChange={(e) =>
                  onChangeInputHandler(item.abbr, item.code, e.target.value)
                }
              ></Input>
              <Button
                type="primary"
                icon={<PlusOutlined />}
                onClick={() =>
                  onClickIncrease(item.abbr, item.code, item.pack, item.qty)
                }
              ></Button>
            </div>
          </div>
        </Card>
      </Col>
    ));
  //{content.length === 0 ? <h2>length 0</h2> : orderLists}
  const [width] = useWindowWidthAndHeight();
  return (
    <div>
      <OrderHeader />
      <div
        style={{
          //display: "flex",
          width: "100%",
          //height: "100vh",
          padding: "80px 10px 10px 10px",
          //backgroundColor: "#f0f0f0",
        }}
      >
        <Row
          gutter={[16, 16]}
          //style={{ display: "flex", justifyContent: "center" }}
        >
          {orderLists}
        </Row>
      </div>
      <BackTop>
        <div style={width > 850 ? backTopstyle : backMobileTopstyle}>UP</div>
      </BackTop>
      <OrderFooter />
    </div>
  );
}

export default LandingPage;
