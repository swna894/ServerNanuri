import * as config from "../../Config";
import React, { useEffect, useState, useRef } from "react";
import { useSelector, useDispatch } from "react-redux";
import { Button, Layout, Select, Space, Input, Drawer } from "antd";
import { withRouter } from "react-router-dom";
import { useWindowWidthAndHeight } from "../../utils/CustomHooks";
import {
  MenuOutlined,
  ShoppingCartOutlined,
  DeliveredProcedureOutlined,
  LogoutOutlined,
  GiftOutlined,
  CrownOutlined
} from "@ant-design/icons";
import { IoAlarm } from "react-icons/io5";

import {
  actionChangeCategory,
  actionChangeTitle,
  actionChangeSupplier,
  actionChangeSearch,
  changeSearchCondition,
  changeIsCartRequest,
} from "../../_actions/supplier_action";

import {
  getCategoriesAction,
  getProductsAction,
} from "../../_actions/product_action";

import "./OrderPage.css";

// right / left key handler
function useKey(key, cb) {
  const callbackRef = useRef(cb);

  useEffect(() => {
    callbackRef.current = cb;
  });

  useEffect(() => {
    function handle(event) {
      if (event.code === key) {
        callbackRef.current(event);
        event.preventDefault();
      }
    }
    document.addEventListener("keydown", handle);

    return () => document.removeEventListener("keydown", handle);
  }, [key]);
}

// Start pageHeage
function OrderHeader() {
  // 왼쪽 버튼 처리
  function handleEnterRight() {
    pageProducts(
      abbr,
      categoryPrompt === config.SELECT_CATEGORY ? "" : categoryPrompt,
      page === totalPages - 1 ? page : page + 1,
      size
    );
  }

  // 오른쪽 버튼 처리
  function handleEnterLeft() {
    pageProducts(
      abbr,
      categoryPrompt === config.SELECT_CATEGORY ? "" : categoryPrompt,
      page === 0 ? 0 : page - 1,
      size
    );
  }

  useKey("ArrowRight", handleEnterRight);
  useKey("ArrowLeft", handleEnterLeft);

  const { Header } = Layout;
  const { Option } = Select;
  const { Search } = Input;

  const [visible, setVisible] = useState(false);
  const [categoryPrompt, setCategoryPrompt] = useState(config.SELECT_CATEGORY);
  const [width] = useWindowWidthAndHeight();

  const suppliers = useSelector((state) => state.supplier.suppliers);
  const abbr = useSelector((state) => state.supplier.abbr);
  const supplier = useSelector((state) => state.supplier.supplier);
  const headTitle = useSelector((state) => state.supplier.title);
  const condition = useSelector((state) => state.supplier.condition);
  const isCart = useSelector((state) => state.supplier.isCart);
  const isNew = useSelector((state) => state.supplier.isNew);
  const isSpecial = useSelector((state) => state.supplier.isSpecial);
  const categories = useSelector((state) => state.product.categories);
  //const category = useSelector((state) => state.supplier.category);
  const page = useSelector((state) => state.product.products.number);
  const size = useSelector((state) => state.product.products.size);
  const totalPages = useSelector((state) => state.product.products.totalPages);

  const dispatch = useDispatch();
  const formRef = React.useRef();

  useEffect(() => {
    let parm = { params: { abbr: headTitle } };
    dispatch(getCategoriesAction(parm));
    dispatch(changeSearchCondition("Co"));
    dispatch(changeIsCartRequest(false));
    console.log("catgegory = " + categoryPrompt);
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  function onChangeSuppiler(abbr, searchValue) {
    setCategoryPrompt(config.SELECT_CATEGORY);
    //dispatch(actionChangeTitle(searchValue.children));
    dispatch(actionChangeSupplier(abbr));
    dispatch(actionChangeCategory(""));
    let parm = { params: { abbr: abbr } };
    dispatch(getCategoriesAction(parm));
    pageProducts(abbr, "", 0, size);
    onClose();
    //console.log(`selected ${value}`);
  }

  function onChangeCategory(category) {
    setCategoryPrompt(category);
    dispatch(actionChangeCategory(category));
    dispatch(actionChangeTitle(supplier + " / " + category));
    pageProducts(abbr, category, page, size);
    onClose();
  }

  function onChangeButton(category) {
    setCategoryPrompt(category);
    dispatch(actionChangeCategory(category));
    dispatch(actionChangeTitle(supplier + " / " + category));
    pageProducts(abbr, category, 0, size);
    onClose();
  }

  const onSearchInput = (search) => {
    if (search === "") {
      onChangeSuppiler(abbr, headTitle);
    } else {
      setCategoryPrompt("SEARCH");
      dispatch(actionChangeCategory("SEARCH"));
      dispatch(actionChangeTitle(supplier + " / SEARCH"));
      dispatch(actionChangeSearch(search));
      pageProducts(abbr, "SEARCH", 0, size, search, condition);
      onClose();
      document.documentElement.scrollTop = 0;
    }
  };

  const pageProducts = (
    abbr,
    category,
    page = 0,
    size = config.PAGE_SIZE,
    search,
    condition = "Co"
  ) => {
    if (category === "SEARCH") {
      let param = {
        params: {
          page: page,
          size: size,
          sort: "seq",
          search: search,
          condition: condition,
        },
      };
      console.log(param);
      dispatch(getProductsAction(abbr, category.replace("/", "_"), param));
    } else {
      let param = { params: { page: page, size: size, sort: "seq" } };
      dispatch(getProductsAction(abbr, category.replace("/", "_"), param));
    }

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
      : categories.map((item, index) => (
          <Option key={index} value={item}>
            {item}
          </Option>
        ));

  const buttonStyle = { width: "100px" };
  const persentStyle = { width: "100%", marginTop: "5px", marginBottom: "5px" };
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
      placeholder={config.SELECT_CATEGORY}
      optionFilterProp="children"
    >
      {listSelectOptions}
    </Select>
  );

  const listCategorySelect =
    (categories && categories.length > 0) && isCart === false ? (
      <Select
        showSearch
        style={width > 800 ? categoryStyle : persentStyle}
        value={categoryPrompt}
        onChange={onChangeCategory}
        placeholder={config.SELECT_CATEGORY}
        optionFilterProp="children"
      >
        {listCategoryOptions}
      </Select>
    ) : (
      ""
    );

  const onChangeSelect = (value) => {
    dispatch(changeSearchCondition(value));
  };

  const searchInput = (
    <div style={{ width: "103%" }}>
      <Input.Group compact>
      
        <Select defaultValue="Co" onChange={onChangeSelect}>
          <Option value="All">All</Option>
          <Option value="Co">Co.</Option>
        </Select>
        <Search
          placeholder="input search text"
          allowClear
          enterButton
          onSearch={onSearchInput}
          style={{ width: "70%" }}
        />
      </Input.Group>
    </div>
  );

  const showDrawer = () => {
    setVisible(true);
  };
  const onClose = () => {
    setVisible(false);
  };

  const onClickCart = () => {
    onChangeButton("CART");
    dispatch(changeIsCartRequest(true));
    onClose();
    document.documentElement.scrollTop = 0;
  };

  const onClickOrder = () => {
    onChangeSuppiler(abbr, headTitle);
    dispatch(changeIsCartRequest(false));
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
      type="primary"
      style={
        isCart === true
          ? { display: "none" }
          : width > 1400
            ? { display: "inline-block", width: "100px", marginTop: "5px" }
            : { display: "inline-block", width: "100%", marginTop: "5px", marginBottom: "5px" }
      }
      onClick={onClickCart}
    >
      <ShoppingCartOutlined />
      CART
    </Button>
  );

  const ordrerButton = (
    <Button
      type="primary"
      style={
        isCart === true
          ? width > 1400
            ? { display: "inline-block", width: "100px", marginTop: "5px" }
            : {
                display: "inline-block",
                width: "100%",
                marginTop: "5px",
                marginBottom: "5px",
              }
          : { display: "none" }
      }
      onClick={onClickOrder}
    >
      <DeliveredProcedureOutlined />
     ORDER
    </Button>
  );

  const isNewButton = isNew ? (
    <Button
      type="primary"
       style={ isCart === true
          ? { display: "none" }
          : width > 1400
            ? { display: "inline-block", width: "100px" }
            : { display: "inline-block", width: "100%" }}
      onClick={onClickNew}
    >
      <CrownOutlined />
       NEW
    </Button>
  ) : (
    ""
  );
  const isSpecialButton = isSpecial ? (
    <Button
      type="primary"
      style={
        isCart === true
          ? { display: "none" }
          : width > 1400
          ? { display: "inline-block", width: "100px" }
          : { display: "inline-block", width: "100%" }
      }
      onClick={onClickSpecial}
    >
      <GiftOutlined />
      SPECIAL
    </Button>
  ) : (
    ""
  );

  function onClickHead(e) {
    e.preventDefault();
    dispatch(changeIsCartRequest(false));
    onChangeSuppiler(abbr, headTitle);
  }

  const headH2 = (
    <a href="order2david.com" onClick={onClickHead}>
      <h2 style={{ display: "inline-block", color: "#000" }}>{headTitle}</h2>
    </a>
  );

  const signoutButton = (
    <Button type="primary" style={width > 1400 ? buttonStyle : persentStyle}>
      <LogoutOutlined />
      LOGOUT
    </Button>
  );
  return (
    <div>
      <Header
        style={{
          position: "fixed",
          zIndex: 1,
          width: "100%",
          backgroundColor: "#bfbfbf",
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
              {ordrerButton}
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
                {ordrerButton}
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
                {ordrerButton}
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
