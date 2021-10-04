import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {  Table, Row, Col } from "antd";
import moment from "moment";
import HitoryHeader from "./HistoryHeader";
import HistoryFooter from "./HistoryFooter";
import "antd/dist/antd.css";
import { getHistoryOrder } from "../../_actions/history_action";


function currencyFormat(num) {
  return "$" + num.toFixed(2).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
}

function HistoryPage() {
  const orders = useSelector((state) => state.history.orders);
  const dispatch = useDispatch();
  
  useEffect(() => {
    document.body.style.background = "#f0f0f0";
    dispatch(getHistoryOrder());
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  orders.map((item) => {
    let date = moment(new Date(item.orderDate));
    return item.orderDate = date.format("YYYY-MM-DD");
  });

  const data = [];
  for (let i = 0; i < orders.length; ++i) {
    data.push({
      key: i + 1,
      id: orders[i].id,
      invoice: orders[i].invoice,
      shop: orders[i].company,
      amount: currencyFormat(orders[i].amount),
      date: orders[i].orderDate,
      comment: orders[i].comment,
    });
  }

  var orderItems = [];
  for (let j = 0; j < orders.length; ++j) {
    let items = orders[j].orderItems;
    const item = [];
    for (let i = 0; i < items.length; ++i) {
      item.push({
        key: i + 1,
        code: items[i].code,
        description: items[i].description,
        price: currencyFormat(items[i].price),
        qty: items[i].qty,
        amount: currencyFormat(items[i].amount),
        comment: items[i].comment,
      });
    }
    orderItems.push({
      key: j + 1,
      item,
    });
  }
  /*   console.log(JSON.stringify(orderItems));
  console.log("======================="); */

  const expandedRowRender = (row) => {
    const columns = [
      { title: "NO", dataIndex: "key", key: "key" },
      { title: "CODE", dataIndex: "code", key: "code" },
      { title: "DESCRITPION", dataIndex: "description", key: "description" },
      { title: "PRICE", dataIndex: "price", key: "price", align: "right" },
      { title: "QTY", dataIndex: "qty", key: "qty", align: "right" },
      { title: "AMOUNT", dataIndex: "amount", key: "amount", align: "right" },
      { title: "COMMENT", dataIndex: "comment", key: "commnet" },
    ];
    let inTable = orderItems.filter((item) => item.key === row.key);
    /*     console.log("row = " + JSON.stringify(inTable));
    console.log("inTable = " + JSON.stringify(inTable[0].item));
    console.log("======================="); */
    return (
      <Table
        columns={columns}
        dataSource={inTable[0].item}
        pagination={false}
      />
    );
  };

  const columns = [
    { title: "NO", dataIndex: "key", key: "key" },
    { title: "INVOICE", dataIndex: "invoice", key: "invoice" },
    { title: "SUPPLIER", dataIndex: "shop", key: "shop" },
    { title: "AMOUNT", dataIndex: "amount", key: "amount", align: "right" },
    { title: "DATE", dataIndex: "date", key: "date", align: "center" },
    { title: "COMMENT", dataIndex: "comment", key: "commnet" },
  ];

  // rowSelection object indicates the need for row selection
  const rowSelection = {
    onChange: (selectedRowKeys, selectedRows) => {
      console.log(
        `selectedRowKeys: ${selectedRowKeys}`,
        "selectedRows: ",
        selectedRows
      );
    },
    getCheckboxProps: (record) => ({
      disabled: record.name === "Disabled User",
      // Column configuration not to be checked
      name: record.name,
    }),
  };

  const [selectionType, setSelectionType] = useState("checkbox");
  
  return (
    <div
      style={{
        height: "100vh",
        backgroundColor: "#f0f0f0",
      }}
    >
      <HitoryHeader />
      <Row
        justify="center"
        align="top"
        style={{
          padding: "85px 10px 10px 10px",
          backgroundColor: "#f0f0f0",
        }}
      >
        <Col xxl={20} xl={20} lg={22} md={24} sm={24} xs={24}>
          <Table
            rowSelection={{
              type: selectionType,
              ...rowSelection,
            }}
            className="order-table"
            columns={columns}
            expandable={{ expandedRowRender }}
            dataSource={data}
          />
        </Col>
      </Row>
      <p style={{ color: "#fff", margin: "40px" }}>&nbsp; </p>
      <HistoryFooter />
    </div>
  );
}

export default HistoryPage;
