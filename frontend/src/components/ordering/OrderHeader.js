import React, { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { Layout, Select, Space } from "antd";
import { actionChangeSupplier } from "../../_actions/supplier_action";

import "./OrderPage.css";

function OrderHeader() {
  const { Header } = Layout;
  const { Option } = Select;
  const suppliers = useSelector((state) => state.supplier.suppliers);
  const headTitle = useSelector((state) => state.supplier.supplier);
  const dispatch = useDispatch();
  const formRef = React.useRef();

  const [title, setTitle] = useState("");
  const [category, setCategory] = useState("Selet category");

  useEffect(() => {
    //console.log("company.company " + JSON.stringify(suppliers));
    // 브라우저 API를 이용하여 문서 타이틀을 업데이트합니다.
    document.title = `David's Na Order System`;
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  function onChangeSuppiler(value) {
    setTitle(value);
    setCategory("Selet category");
    dispatch(actionChangeSupplier(value));
    //console.log(`selected ${value}`);
  }

  function onChangeCategory(value) {
    setCategory(value);
    dispatch(actionChangeSupplier(title + " / " + value));
  }

  const listSelectOptions = suppliers.map((item) => (
    <Option key={item.id} value={item.company}>
      {item.company}
    </Option>
  ));

  return (
    <div>
      <Header
        style={{
          position: "fixed",
          zIndex: 1,
          width: "100%",
        }}
      >
        <Space>
          <h2 style={{ display: "inline", color: "#fff" }}>{headTitle}</h2>

          <Select
            ref={formRef}
            name="supplier"
            showSearch
            onChange={onChangeSuppiler}
            style={{ width: 230 }}
            placeholder="Selet supplier"
            optionFilterProp="children"
          >
            {listSelectOptions}
          </Select>

          <Select
            showSearch
            style={{ width: 200 }}
            value={category}
            onChange={onChangeCategory}
            placeholder="Selet category"
            optionFilterProp="children"
          >
            <Option value="1">Not Identified</Option>
            <Option value="Closed">Closed</Option>
            <Option value="Communicated">Communicated</Option>
            <Option value="Identified">Identified</Option>
            <Option value="Resolved">Resolved</Option>
            <Option value="Cancelled">Cancelled</Option>
          </Select>
        </Space>
      </Header>
    </div>
  );
}

export default OrderHeader;
