import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getSuppliers } from "../../_actions/supplier_action";

import { Layout, Select, Space } from "antd";

import "./OrderPage.css";

function OrderHeader() {
  const suppliers = useSelector((state) => state.supplier.suppliers);
  const dispatch = useDispatch();
  const formRef = React.useRef();

  const { Header } = Layout;
  const { Option } = Select;

  const [title, setTitle] = useState("David");
  const [supplier, setSupplier] = useState();

  useEffect((props) => {
    dispatch(getSuppliers());

    console.log("suppliers " + suppliers);
    // 브라우저 API를 이용하여 문서 타이틀을 업데이트합니다.
    //document.title = `Order2david`;
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  function onChangeSuppiler(value) {
    setSupplier(value);
    setTitle(value);
    console.log(`selected ${value}`);
  }

  function onChangeCategory(value) {
    setTitle(supplier + " / " + value);

    console.log(`selected ${value}`);
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
        {" "}
        <Space>
          <h2 style={{ display: "inline", color: "#fff" }}>{title}</h2>

          <Select
            ref={formRef}
            name="supplier"
            showSearch
            onChange={onChangeSuppiler}
            style={{ width: 230 }}
            placeholder="Selet supplier"
            optionFilterProp="children"
            filterOption={(input, option) =>
              option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
            }
            filterSort={(optionA, optionB) =>
              optionA.children
                .toLowerCase()
                .localeCompare(optionB.children.toLowerCase())
            }
          >
            {listSelectOptions}
          </Select>

          <Select
            showSearch
            style={{ width: 200 }}
            onChange={onChangeCategory}
            placeholder="Selet supplier"
            optionFilterProp="children"
            filterOption={(input, option) =>
              option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
            }
            filterSort={(optionA, optionB) =>
              optionA.children
                .toLowerCase()
                .localeCompare(optionB.children.toLowerCase())
            }
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
