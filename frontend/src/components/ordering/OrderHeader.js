import React, { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { Layout, Select, Space } from "antd";
import { actionChangeSupplier } from "../../_actions/supplier_action";
import { getCategoriesAction } from "../../_actions/product_action";
import { withRouter } from "react-router-dom";

import "./OrderPage.css";

function OrderHeader() {
  const { Header } = Layout;
  const { Option } = Select;
  const suppliers = useSelector((state) => state.supplier.suppliers);
  const supplier = useSelector((state) => state.supplier.supplier);
  const headTitle = useSelector((state) => state.supplier.title);
  const categories = useSelector((state) => state.product.categories);

  const dispatch = useDispatch();
  const formRef = React.useRef();

  const [title, setTitle] = useState("");
  const [category, setCategory] = useState("Selet category");

  useEffect(() => {
    let parm = { params: { abbr : headTitle } };
    dispatch(getCategoriesAction(parm));

    //console.log("company.company " + JSON.stringify(suppliers));
    // 브라우저 API를 이용하여 문서 타이틀을 업데이트합니다.
    document.title = `David's Na Order System`;
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  function onChangeSuppiler(value, searchValue) {
    setTitle(value);
    setCategory("Selet category");
    dispatch(actionChangeSupplier(searchValue.children));
    let parm = { params: { abbr : value } };
    dispatch(getCategoriesAction(parm));
    console.log("searchValue = " + JSON.stringify(searchValue.children));
    //console.log("categories = " + JSON.stringify(categories));
    //console.log(`selected ${value}`);
  }

  function onChangeCategory(value) {
    setCategory(value);
    dispatch(actionChangeSupplier(title + " / " + value));
  }

  const listSelectOptions = suppliers.map((item) => (
    <Option key={item.id} value={item.abbr}>
      {item.company}
    </Option>
  ));

  const listCategoryOptions =
    categories === undefined
      ? []
      : categories.map((item) => (
          <Option key={item.id} value={item.category}>
            {item.category}
          </Option>
        ));

  const listCategorySelect =
    categories.length > 0 ? (
      <Select
        showSearch
        style={{ width: 200 }}
        value={category}
        onChange={onChangeCategory}
        placeholder="Selet category"
        optionFilterProp="children"
      >
        {listCategoryOptions}
      </Select>
    ) : (
      ""
    );

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
          {listCategorySelect}
        </Space>
      </Header>
    </div>
  );
}

export default withRouter(OrderHeader);
