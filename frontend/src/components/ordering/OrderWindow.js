import React from 'react'
import { Button, Input, Tooltip } from "antd";
import { useDispatch, useSelector } from "react-redux";
import { MinusOutlined, PlusOutlined } from "@ant-design/icons";

import newProduct from "../../images/new.png";
import discount from "../../images/discount.png";

import { changeCart, } from "../../_actions/product_action";


const specialStyle = { position: "absolute", top: "20px", left: "25px" };
const newStyle = { position: "absolute", top: "200px", left: "25px" };
const hiddenStyle = { display: "none" };

const descriptionStyle = {
    overflow: "hidden",
    fontSize: "20px",
    marginBottom: "7px",
    marginLeft: " 20px",
    marginRight: " 20px",
    fontWeight: "bold",
    fontStyle: "italic",
    whiteSpace: "nowrap",
    textOverflow: "ellipsis",
    textAlign: "center",
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

  const companyStyle =  {
    display: "inline",
    color: "#40a9ff",
    marginTop: "-50px",
    marginLeft: "32px",
    fontWeight: "bold",
  };

  const buttonGroupStyle = {
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

export function OrderWindow({item}) {
    const dispatch = useDispatch()
    const content = useSelector((state) =>
            state.product.products.content ? state.product.products.content : []);
    const products = useSelector((state) => state.product.products);
    const totalElements = useSelector((state) => state.product.products.totalElements);
    const size = useSelector((state) => state.product.products.size);
    const number = useSelector((state) => state.product.products.number);
    const condition = useSelector((state) => state.supplier.condition);
    const category = useSelector((state) => state.supplier.category);
    const comapny = useSelector((state) => state.user.userData);

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
    return (
        <div
          style={
            item.price === 0
              ? { width: "1em", display: "none" }
              : { width: "1em", display: "inline" }
          }
        >
        <div style={item.new ? newStyle : hiddenStyle}>
          <img src={newProduct} alt="new"></img>
        </div>
        <div style={item.special ? specialStyle : hiddenStyle}>
          <img src={discount} alt="special"></img>
        </div>
        <Tooltip placement="top" title={item.description}>
          <div style={descriptionStyle}>{item.description}</div>
        </Tooltip>
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
        <div>
          <Tooltip placement="right" title="last ordered date">
            <p
              style={{
                color: "#f5222d",
                marginLeft: "32px",
                marginBottom: "5px",
                fontWeight: "bold",
                width: "100%",
              }}
            >
              {item.orderedDate}            
            </p>
          </Tooltip>
          <p
            style={
              condition === "All" && category === "SEARCH"
                ? companyStyle
                : { display: "none" }
            }
          >
            {item.company} &nbsp;&nbsp;&nbsp;
          </p>
          <p style={item.myStock === 0 ? {display: "none"} : companyStyle}>
                 My Stork : {item.myStock}
          </p>
 
        </div>
        <p style={{ color: "#fff", margin: "50px" }}>&nbsp; </p>
        <div style={buttonGroupStyle}>
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
    )
}


