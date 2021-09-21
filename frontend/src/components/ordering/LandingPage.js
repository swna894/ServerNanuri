import * as config from "../../Config";
import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Card, Row, Col, BackTop, Image } from "antd";
import { useWindowWidthAndHeight } from "../../utils/CustomHooks";
import { trackPromise } from 'react-promise-tracker';
import { Spinner } from './../../common/spinner';

import OrderHeader from "./OrderHeader";
import OrderFooter from "./OrderFooter";
import { OrderWindow } from "./OrderWindow";
import { OrderWindowMin } from "./OrderWindowMin";


import {
  actionGetSuppliers,
  actionGetSupplier,
} from "../../_actions/supplier_action";

import {
  getProductsInitAction,
} from "../../_actions/product_action";

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
  top: "-10px",
  left: "55px",
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
  top: "-10px",
  left: "90px",
};

function LandingPage() {
  const dispatch = useDispatch()
  const content = useSelector((state) =>
          state.product.products.content ? state.product.products.content : []);
  const [width] = useWindowWidthAndHeight();

  useEffect(() => {
    dispatch(actionGetSupplier());
    dispatch(actionGetSuppliers());
    trackPromise(dispatch(getProductsInitAction()));
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  const imageStyel = {
    //bordered: true,
    //height: "auto",
    minHeight: "250px",
    maxHeight: "250px",
    width: "auto",
    maxWidth: "300px",   // 300
    textAlign: "center",
    margin: "2px auto",
  };
 
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

  // <Col xxl={6} xl={8} lg={12} md={12} sm={24} xs={24} key={index}></Col>
  const orderLists =
    content &&
    content.map((item, index) => (
      <Col xxl={6} xl={8} lg={10} md={12} sm={24} xs={24} key={index}>
        <Card
          bodyStyle={{ padding: "0" }}
          style={item.qty > 0 ? cardOrderStyle : cardNormalStyle}
          cover={
            // <Tooltip title={item.description}>  </Tooltip>
              <Image
                style={imageStyel}
                alt={item.code}
                //src={`/images/${item.abbr}/${item.code}.jpg`}
                src={"data:image/jpg;base64," + item.image}
              />
          
          }
        > 
          {
            width > config.WIDTH_SMALL ?
            <OrderWindow item={item} />
            :
            <OrderWindowMin item={item} />
          }
        </Card>
      </Col>
    ));
  //{content.length === 0 ? <h2>length 0</h2> : orderLists}
 
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
          gutter={[16, 16]}   //style={{ display: "flex", justifyContent: "center" }}
        >
          {orderLists}
          
        </Row>
      </div>
      <Spinner/>
      <BackTop>
        <div style={width > 850 ? backTopstyle : backMobileTopstyle}>TOP</div>
      </BackTop>
      <OrderFooter />
    </div>
  );
}

export default LandingPage;
