import * as config from "../../Config";
import React, { useEffect, useState, useRef } from "react";
import { useSelector, useDispatch } from "react-redux";
import { Button, Layout, Select, Space, Input, Drawer, Modal } from "antd";
import { withRouter, Link } from "react-router-dom";
import { useWindowWidthAndHeight } from "../../utils/CustomHooks";
import { MenuOutlined } from "@ant-design/icons";

import {
  FaHome,
  FaTruck,
  FaShoppingCart,
  FaSignOutAlt,
  FaTags,
  FaGifts,
  FaCalendarCheck,
  FaChartLine,
} from "react-icons/fa";

import {
  actionChangeCategory,
  actionChangeTitle,
  actionChangeSupplier,
  actionChangeSearch,
  changeSearchCondition,
  changeIsCartRequest,
  actionGetSuppliers,
  actionUpdateSuppliers,
} from "../../_actions/supplier_action";

import {
  getCategoriesAction,
  getProductsAction,
  getInitCartInform,
  setOrderRequest,
} from "../../_actions/product_action";

import { signoutUser } from "../../_actions/user_action";

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
function OrderHeader(props) {
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
  const [visibleCart, setVisibleCart] = useState(false);
  const [categoryPrompt, setCategoryPrompt] = useState(config.SELECT_CATEGORY);
  const [width] = useWindowWidthAndHeight();

  const supplier = useSelector((state) => state.supplier.supplier);
  const suppliers = useSelector((state) => state.supplier.suppliers);
  const abbr = useSelector((state) => state.supplier.abbr);

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
  const cartInform = useSelector((state) => state.product.cart);
  const error = useSelector((state) => state.product.error);
  const userData = useSelector((state) => state.user.userData);

  const dispatch = useDispatch();
  const formRef = React.useRef();

  useEffect(() => {
    document.body.style.background = "#f0f0f0";
    let parm = { params: { abbr: headTitle } };
    dispatch(getCategoriesAction(parm));
    dispatch(changeSearchCondition("Co"));
    dispatch(changeIsCartRequest(false));
    dispatch(getInitCartInform());
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  function onChangeSuppiler(abbr) {
    dispatch(actionChangeSupplier(abbr));
    setCategoryPrompt(config.SELECT_CATEGORY);
      //dispatch(actionChangeTitle(searchValue.children)); 
    dispatch(actionChangeCategory(""));
    dispatch(changeIsCartRequest(false));
    let parm = { params: { abbr: abbr } };
    dispatch(getCategoriesAction(parm));
    pageProducts(abbr, "", 0, size);
    dispatch(getInitCartInform(abbr));
    onClose();
    // }

    //console.log(`selected ${value}`);
  }

  function onChangeCart(abbr) {
    dispatch(actionChangeSupplier(abbr));
    //onChangeButton(config.CART);
    pageProducts(abbr, "CART", 0, size);
    dispatch(getInitCartInform(abbr));
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

  const onClickSignout = () => {
    localStorage.setItem("jwtToken", "");
    dispatch(signoutUser());
    window.location.href = "/";
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

  const stylePersent = {
    width: "100%",
    marginTop: "5px",
    marginBottom: "5px",
    borderStyle: "ridge",
  };
  const styleSupplier = { width: "100%", fontSize:"24px", fontWeight: "bold" };
  const styleCategory = { width: "100%", fontSize:"20px", fontWeight: "bold"};
  const styleButton = {
    display: "inline-block",
    width: "110px",
  };

  const onClickCart = () => {
    onChangeButton(config.CART);
    dispatch(actionGetSuppliers("cart"));
    dispatch(changeIsCartRequest(true));
    onClose();
    document.documentElement.scrollTop = 0;
  };

  const listSupplierSelect = (
    <Select
      bordered={false}
      ref={formRef}
      name="supplier"
      showSearch
      value={supplier}
      onChange={isCart ? onChangeCart : onChangeSuppiler}
      style={width > config.WIDTH_SMALL ? styleSupplier : stylePersent}
      placeholder={config.SELECT_CATEGORY}
      optionFilterProp="children"
    >
      {listSelectOptions}
    </Select>
  );

  const listCategorySelect =
    categories && categories.length > 0 && isCart === false ? (
      <Select
        bordered={false}
        showSearch
        style={width > config.WIDTH_SMALL ? styleCategory : stylePersent}
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
        <Select
          defaultValue="Co"
          onChange={onChangeSelect}
         // style={{ borderStyle: "ridge" }}
        >
          <Option value="All">All</Option>
          <Option value="Co">Co</Option>
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

  
  // const showCartModal = () => {
  //   setVisibleCart(true);
  // };

  const gotoOrder = () => {
    setVisibleCart(false);
    dispatch(actionGetSuppliers());
    dispatch(changeIsCartRequest(false));
    dispatch(setOrderRequest(abbr));
    onClose();
    onChangeSuppiler(abbr, headTitle);
  };

  const gotoCart = () => {
    setVisibleCart(false);
      const newList = suppliers.filter((item) => item.abbr !== abbr);
      dispatch(setOrderRequest(abbr));
      dispatch(actionUpdateSuppliers(newList));
      dispatch(actionChangeSupplier(newList[0].abbr));
      onChangeCart(newList[0].abbr)
  
  };

  const handleCancel = () => {
    setVisibleCart(false);
  };

  const onClickCheckout = () => {
    if (!error) {
      setVisibleCart(true);
    }
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

  const onClickOrdered = () => {
    onChangeButton("ORDERED");
    onClose();
    dispatch(getInitCartInform());
    document.documentElement.scrollTop = 0;
  };

  const buttonCart = (
    <Button
      type="primary"
      style={
        isCart === true || cartInform === undefined || cartInform === ""
          ? { display: "none" }
          : width > config.WIDTH_BIG
          ? styleButton
          : {
              display: "inline-block",
              width: "100%",
              marginTop: "10px",
              marginBottom: "5px",
            }
      }
      onClick={onClickCart}
    >
      <FaShoppingCart size={16} style={{ marginBottom: "-4px" }} />
      &nbsp; CART
    </Button>
  );

  const buttonCheckout = (
    <Button
      type="primary"
      style={
        isCart === true
          ? width > config.WIDTH_BIG
            ? styleButton
            : {
                display: "inline-block",
                width: "100%",
                marginTop: "10px",
                marginBottom: "5px",
              }
          : { display: "none" }
      }
      onClick={onClickCheckout}
    >
      <FaCalendarCheck size={16} style={{ marginBottom: "-4px" }} />
      &nbsp; CHECKOUT
    </Button>
  );

  const buttonOrdered = (
    <Button
      type="primary"
      style={
        //isCart === true
        width > config.WIDTH_BIG
          ? styleButton
          : {
              display: "inline-block",
              width: "100%",
              marginTop: "10px",
              marginBottom: "5px",
            }
        // : { display: "none" }
      }
      onClick={onClickOrdered}
    >
      <FaTruck size={16} style={{ marginBottom: "-4px" }} />
      &nbsp; HISTORY
    </Button>
  );

  const buttonHistory = (
    <Link to="/history">
      <Button
        type="primary"
        style={
          //isCart === true
          width > config.WIDTH_BIG
            ? styleButton
            : {
                display: "inline-block",
                width: "100%",
                marginTop: "10px",
                marginBottom: "5px",
              }
          // : { display: "none" }
        }
        //onClick={onClickHistory}
      >
        <FaChartLine size={16} style={{ marginBottom: "-4px" }} />
        &nbsp; REPORT
      </Button>
    </Link>
  );

  const buttonIsNew = isNew ? (
    <Button
      type="primary"
      style={
        isCart === true
          ? { display: "none" }
          : width > config.WIDTH_BIG
          ? styleButton
          : {
              display: "inline-block",
              width: "100%",
              marginBottom: "5px",
              marginTop: "5px",
            }
      }
      onClick={onClickNew}
    >
      <FaTags size={16} style={{ marginBottom: "-4px" }} />
      &nbsp; NEW
    </Button>
  ) : (
    ""
  );

  const buttonIsSpecial = isSpecial ? (
    <Button
      type="primary"
      style={
        isCart === true
          ? { display: "none" }
          : width > config.WIDTH_BIG
          ? styleButton
          : {
              display: "inline-block",
              width: "100%",
              marginBottom: "5px",
              marginTop: "5px",
            }
      }
      onClick={onClickSpecial}
    >
      <FaGifts size={16} style={{ marginBottom: "-4px" }} />
      &nbsp; SPECIAL
    </Button>
  ) : (
    ""
  );

  function onClickHead(e) {
    e.preventDefault();
    setCategoryPrompt(config.SELECT_CATEGORY);
    dispatch(actionGetSuppliers());
    onChangeSuppiler(abbr, headTitle);
   
  }

  const headH2 = (
    <a href="order2david.com" onClick={onClickHead}>
      <Space>
        <FaHome size={28} style={{ color: "#000" }} />
        {/* <h2 style={{ display: "inline-block", color: "#000" }}>{headTitle}</h2> */}
      </Space>
    </a>
  );

  const buttonSignout = (
    <Button
      onClick={onClickSignout}
      type="primary"
      style={width > config.WIDTH_BIG ? styleButton : stylePersent}
    >
      <FaSignOutAlt size={16} style={{ marginBottom: "-4px" }} />
      &nbsp; LOGOUT
    </Button>
  );

  // function success() {
  //   Modal.success({
  //     title: "Thanks for your order ...",
  //   });
  // }

  return (
    <div>
      <Header
        style={{
          position: "fixed",
          zIndex: 1,
          width: "100%",
          backgroundColor: "#bfbfbf",
          height: "70px",
          paddingTop: "6px",
        }}
      >
        {width > config.WIDTH_BIG ? (
          <div>
            <Space style={{ marginTop: "10px" }}>
              {headH2}
              {listSupplierSelect}
              {listCategorySelect}
            </Space>
            <Space style={{ float: "right", color: "#fff", marginTop: "3px" }}>
              {searchInput}
              <div>
                {buttonIsNew}
                {buttonIsSpecial}
                {buttonOrdered}
                {buttonCheckout}
                {buttonCart}
                {buttonHistory}
                {buttonSignout}
              </div>
            </Space>
          </div>
        ) : width > config.WIDTH_SMALL ? (
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
              placement="right"
              closable={true}
              onClose={onClose}
              visible={visible}
            >
              <div style={{ display: "block", color: "#fff" }}>
                {searchInput}
                {buttonIsNew}
                {buttonIsSpecial}
                {buttonOrdered}
                {buttonCheckout}
                {buttonCart}
                {buttonHistory}
                {buttonSignout}
              </div>
            </Drawer>
          </div>
        ) : (
          <div style={{ marginTop: "10px" }} >
            {headH2}
            <Button
              type="primary"
              onClick={showDrawer}
              style={{ float: "right", margin: "18px 0" }}
            >
              <MenuOutlined />
            </Button>
            <Drawer
              placement="right"
              closable={true}
              onClose={onClose}
              visible={visible}
            >
              <div style={{ display: "block", color: "#fff"}}>
                {listSupplierSelect}
                {listCategorySelect}
                {searchInput}
                {buttonIsNew}
                {buttonIsSpecial}
                {buttonOrdered}
                {buttonCheckout}
                {buttonCart}
                {buttonHistory}
                {buttonSignout}
              </div>
            </Drawer>
          </div>
        )}
      </Header>

      <div
        style={{
          position: "fixed",
          zIndex: 1,
          width: "100%",
          backgroundColor: "#bfbfbf",
          height: "10px",
          textAlign: "right",
          fontWeight: "bold",
          fontStyle: "italic",
          paddingRight: "52px",
          paddingTop: "3px",
        }}
      >
        Welcome to {userData ? userData.company : ""}
      </div>
      <div
        xxl={6}
        xl={8}
        lg={10}
        md={12}
        sm={24}
        xs={24}
        style={{
          position: "fixed",
          zIndex: 1,
          width:"30%",
          backgroundColor: "#bfbfbf",
          color:'#3455eb',
          textAlign: "left",
          fontWeight: "bold",
          fontStyle: "italic",
          padding: "5px 0px 0px 100px",
          marginBottom: "-10px"
        }}
      >
        Key Suppliers
      </div>
      <Modal
          visible={visibleCart}
          onOk={gotoOrder}
          onCancel={handleCancel}
          footer={ suppliers.length > 1 ? [
            <Button key="back" onClick={handleCancel}>
              Return
            </Button>,
            <Button type="primary"  onClick={gotoCart}>
              Continue Cart
            </Button>,     
            <Button type="primary" onClick={gotoOrder}>
              Go to ordering
            </Button>,
          ] : [
            <Button key="back" onClick={handleCancel}>
              Return
            </Button>,   
            <Button type="primary" onClick={gotoOrder}>
              Go to ordering
            </Button>,
          ]}
        >
          <p  style={{
          fontWeight: "bold",
          fontStyle: "italic",
          fontSize: "24px",
        }}>Thanks for your order ...</p>
        </Modal>
    </div>
  );
}

export default withRouter(OrderHeader);
