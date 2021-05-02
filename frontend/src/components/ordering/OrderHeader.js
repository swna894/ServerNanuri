import React, { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { Button, Layout, Select, Space, Input, Drawer } from "antd";
import { withRouter } from "react-router-dom";
import { useWindowWidthAndHeight } from "../../utils/CustomHooks";
import { MenuOutlined, ShoppingCartOutlined } from "@ant-design/icons";

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
  const [category, setCategory] = useState("Selet category");
  const [width] = useWindowWidthAndHeight();

  const suppliers = useSelector((state) => state.supplier.suppliers);
  const abbr = useSelector((state) => state.supplier.abbr);
  const supplier = useSelector((state) => state.supplier.supplier);
  const headTitle = useSelector((state) => state.supplier.title);
  const isNew = useSelector((state) => state.supplier.isNew);
  const isSpecial = useSelector((state) => state.supplier.isSpecial);
  const categories = useSelector((state) => state.product.categories);
  const page = useSelector((state) => state.product.products.number);
  const size = useSelector((state) => state.product.products.size);

  const dispatch = useDispatch();
  const formRef = React.useRef();

  useEffect(() => {
    // 브라우저 API를 이용하여 문서 타이틀을 업데이트합니다.
    document.title = `David's Na Order System`;
    // window.addEventListener("keydown", (event) => {
    //   console.log("keydown = " + event.key);
    // });
    let parm = { params: { abbr: headTitle } };

    dispatch(getCategoriesAction(parm));
    //console.log("company.company " + JSON.stringify(suppliers));
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  function onChangeSuppiler(abbr, searchValue) {
    setCategory("Selet category");
    dispatch(actionChangeTitle(searchValue.children));
    dispatch(actionChangeSupplier(abbr));
    dispatch(actionChangeCategory(""));
    let parm = { params: { abbr: abbr } };
    dispatch(getCategoriesAction(parm));
    pageProducts(abbr, "", 0, size);
    onClose();
    //console.log("searchValue = " + JSON.stringify(searchValue.children));
    //console.log(`selected ${value}`);
  }

  function onChangeCategory(category) {
    setCategory(category);
    dispatch(actionChangeCategory(category));
    dispatch(actionChangeTitle(supplier + " / " + category));
    pageProducts(abbr, category, page, size);
    onClose();
  }

  function onChangeButton(category) {
    setCategory(category);
    dispatch(actionChangeCategory(category));
    dispatch(actionChangeTitle(supplier + " / " + category));
    pageProducts(abbr, category, 0, size);
    onClose();
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

  const buttonStyle = { width: "100px" };
  const persentStyle = { width: "100%" };
  const supplierStyle = { width: "230px" };
  const categoryStyle = { width: "200px" };

  const listSupplierSelect = (
    <Select
      ref={formRef}
      name="supplier"
      showSearch
      value={supplier}
      onChange={onChangeSuppiler}
      style={width > 800 ? supplierStyle : persentStyle}
      placeholder="Selet supplier"
      optionFilterProp="children"
    >
      {listSelectOptions}
    </Select>
  );

  const listCategorySelect =
    categories && categories.length > 0 ? (
      <Select
        showSearch
        style={width > 800 ? categoryStyle : persentStyle}
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

  const signoutButton = (
    <Button style={width > 1400 ? buttonStyle : persentStyle}>SIGNOUT</Button>
  );

  const searchInput = (
    <Search
      placeholder="input search text"
      allowClear
      onSearch={(value) => onSearch(value)}
      style={{ margin: "16px  0", width: "100%" }}
    />
  );

  const headH2 = (
    <h2 style={{ display: "inline-block", color: "#fff" }}>{headTitle}</h2>
  );
  const showDrawer = () => {
    setVisible(true);
  };
  const onClose = () => {
    setVisible(false);
  };

  const onSearch = (value) => {
    console.log("value = " + value);
  };

  const onClickCart = () => {
    onChangeButton("CART");
    onClose();
    document.documentElement.scrollTop = 0;
  };

  const onClickNew = () => {
    onChangeButton("NEW");
    onClose();
    document.documentElement.scrollTop = 0;
  };

  const onClickSpecial = () => {
    onChangeButton("SPECIAL");
    onClose();
    document.documentElement.scrollTop = 0;
  };

  const cartButton = (
    <Button
      style={width > 1400 ? buttonStyle : persentStyle}
      onClick={onClickCart}
    >
      <ShoppingCartOutlined />
      CART
    </Button>
  );

  const isNewButton = isNew ? (
    <Button
      style={width > 1400 ? buttonStyle : persentStyle}
      onClick={onClickNew}
    >
      NEW
    </Button>
  ) : (
    ""
  );
  const isSpecialButton = isSpecial ? (
    <Button
      style={width > 1400 ? buttonStyle : persentStyle}
      onClick={onClickSpecial}
    >
      SPECIAL
    </Button>
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
        {width > 1400 ? (
          <div>
            <Space>
              {headH2}
              {listSupplierSelect}
              {listCategorySelect}
            </Space>
            <Space style={{ float: "right", color: "#fff", marginTop: "3px" }}>
              {searchInput}
              {isNewButton}
              {isSpecialButton}
              {cartButton}
              {signoutButton}
            </Space>
          </div>
        ) : width > 800 ? (
          <div>
            <Space>
              {headH2}
              {listSupplierSelect}
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
                {searchInput}
                {isNewButton}
                {isSpecialButton}
                {cartButton}
                {signoutButton}
              </div>
            </Drawer>
          </div>
        ) : (
          <div>
            {headH2}
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
                {listSupplierSelect}
                {listCategorySelect}
                {searchInput}
                {isNewButton}
                {isSpecialButton}
                {cartButton}
                {signoutButton}
              </div>
            </Drawer>
          </div>
        )}
      </Header>
    </div>
  );
}

export default withRouter(OrderHeader);
