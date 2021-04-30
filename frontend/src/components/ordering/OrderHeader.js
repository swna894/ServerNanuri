import React, { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import {
  Button,
  Layout,
  Select,
  Space,
  Input,
  Drawer,
} from "antd";
import { withRouter } from "react-router-dom";
import { useWindowWidthAndHeight } from "../../utils/CustomHooks";
import { MenuOutlined } from "@ant-design/icons";

import {
  actionChangeCategory,
  actionChangeTitle,
  actionChangeSupplier,
} from "../../_actions/supplier_action";
import {
  getCategoriesAction,
  getProductsAction,
} from "../../_actions/product_action";

import "./OrderPage.css";

function OrderHeader() {
  const { Header } = Layout;
  const { Option } = Select;
  const { Search } = Input;

  const [visible, setVisible] = useState(false);
  const [title, setTitle] = useState("");
  const [abbr, setAbbr] = useState("");
  const [category, setCategory] = useState("Selet category");
  const [width] = useWindowWidthAndHeight();

  const suppliers = useSelector((state) => state.supplier.suppliers);
  //const supplier = useSelector((state) => state.supplier.supplier);
  const headTitle = useSelector((state) => state.supplier.title);
  const categories = useSelector((state) => state.product.categories);
  const page = useSelector((state) => state.product.products.number);
  const size = useSelector((state) => state.product.products.size);

  const dispatch = useDispatch();
  const formRef = React.useRef();

  useEffect(() => {
    // 브라우저 API를 이용하여 문서 타이틀을 업데이트합니다.
    document.title = `David's Na Order System`;
    window.addEventListener("keydown", (event) => {
      console.log("keydown = " + event.key);
    });
    let parm = { params: { abbr: headTitle } };
    dispatch(getCategoriesAction(parm));
    //console.log("company.company " + JSON.stringify(suppliers));
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  const buttonStyle = {
    width: "100px",
  };

  function onChangeSuppiler(supplier, searchValue) {
    setTitle(searchValue.children);
    setAbbr(supplier);
    setCategory("Selet category");

    dispatch(actionChangeTitle(searchValue.children));
    dispatch(actionChangeSupplier(supplier));
    dispatch(actionChangeCategory(""));
    let parm = { params: { abbr: supplier } };
    dispatch(getCategoriesAction(parm));
    pageProducts(supplier, "", 0, size);

    //console.log("searchValue = " + JSON.stringify(searchValue.children));
    //console.log(`selected ${value}`);
  }

  function onChangeCategory(category) {
    setCategory(category);
    dispatch(actionChangeCategory(category));
    dispatch(actionChangeTitle(title + " / " + category));
    pageProducts(abbr, category, page, size);
  }

  const pageProducts = (abbr, category, page = 0, size = 36) => {
    let param = { params: { page: page, size: size, sort: "seq" } };
    //console.log("abbr = " + abbr );
    //console.log(param);
    dispatch(getProductsAction(abbr, category, param));
    document.documentElement.scrollTop = 0;
  };

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
    categories && categories.length > 0 ? (
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

    
  const showDrawer = () => {
    setVisible(true);
  };
  const onClose = () => {
    setVisible(false);
  };

  const onSearch = () => {};
  return (
    <div>
      <Header
        style={{
          position: "fixed",
          zIndex: 1,
          width: "100%",
        }}
      >
        {width > 1400 ? (
          <div>
            <Space>
              <h2 style={{ display: "inline-block", color: "#fff" }}>
                {headTitle}
              </h2>
              <Select
                ref={formRef}
                name="supplier"
                showSearch
                value={headTitle}
                onChange={onChangeSuppiler}
                style={{ width: 230 }}
                placeholder="Selet supplier"
                optionFilterProp="children"
              >
                {listSelectOptions}
              </Select>
              {listCategorySelect}
            </Space>
            <Space style={{ float: "right", color: "#fff" }}>
              <Search
                placeholder="input search text"
                allowClear
                onSearch={onSearch}
                style={{ width: 300, margin: "16px  0" }}
              />
              <Button style={buttonStyle}>NEW</Button>
              <Button style={buttonStyle}>SPECIAL</Button>
              <Button style={buttonStyle}>CART</Button>
              <Button style={buttonStyle}>SIGNOUT</Button>
            </Space>
          </div>
        ) : width > 800 ? (
          <div>
            <Space>
              <h2 style={{ display: "inline-block", color: "#fff" }}>
                {headTitle}
              </h2>
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
            <Button
              type="primary"
              onClick={showDrawer}
              style={{ float: "right", margin: "18px 0" }}
            >
              <MenuOutlined />
            </Button>
            <Drawer
              placement="left"
              closable={false}
              onClose={onClose}
              visible={visible}
            >
              <div style={{ display: "block", color: "#fff" }}>
                <Search
                  placeholder="input search text"
                  allowClear
                  onSearch={onSearch}
                  style={{ margin: "16px  0", width: "100%" }}
                />
                <Button style={{ width: "100%" }}>NEW</Button>
                <Button style={{ width: "100%" }}>SPECIAL</Button>
                <Button style={{ width: "100%" }}>CART</Button>
                <Button style={{ width: "100%" }}>LOGOUT</Button>
              </div>
            </Drawer>
          </div>
        ) : (
          <div>
            <h2 style={{ display: "inline-block", color: "#fff" }}>
              {headTitle}
            </h2>
            <Button
              type="primary"
              onClick={showDrawer}
              style={{ float: "right", margin: "18px 0" }}
            >
              <MenuOutlined />
            </Button>
            <Drawer
              placement="left"
              closable={false}
              onClose={onClose}
              visible={visible}
            >
              <div style={{ display: "block", color: "#fff" }}>
                <Select
                  ref={formRef}
                  name="supplier"
                  showSearch
                  onChange={onChangeSuppiler}
                  style={{ width: "100%" }}
                  placeholder="Selet supplier"
                  optionFilterProp="children"
                >
                  {listSelectOptions}
                </Select>
                {categories && categories.length > 0 ? (
                  <Select
                    showSearch
                    style={{ width: "100%" }}
                    value={category}
                    onChange={onChangeCategory}
                    placeholder="Selet category"
                    optionFilterProp="children"
                  >
                    {listCategoryOptions}
                  </Select>
                ) : (
                  ""
                )}
                <Search
                  placeholder="input search text"
                  allowClear
                  onSearch={onSearch}
                  style={{ margin: "16px  0", width: "100%" }}
                />
                <Button style={{ width: "100%" }}>NEW</Button>
                <Button style={{ width: "100%" }}>SPECIAL</Button>
                <Button style={{ width: "100%" }}>CART</Button>
                <Button style={{ width: "100%" }}>LOGOUT</Button>
              </div>
            </Drawer>
          </div>
        )}
      </Header>
    </div>
  );
}

export default withRouter(OrderHeader);
