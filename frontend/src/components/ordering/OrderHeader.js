import * as config from "../../Config";
import React, { useEffect, useState, useRef } from "react";
import { useSelector, useDispatch } from "react-redux";
import { Button, Layout, Select, Space, Input, Drawer, Modal, Tooltip, } from "antd";
import { withRouter, Link } from "react-router-dom";
import { useWindowWidthAndHeight } from "../../utils/CustomHooks";
import { MenuOutlined } from "@ant-design/icons";
import newProduct from "../../images/new1_24.ico";
import sales from "../../images/sale_24.ico";
import history from "../../images/history2_16.ico";
import cart from "../../images/cart1_16.ico";
import report from "../../images/report_16.ico";
import logout from "../../images/exit_16.ico";
import checkout from "../../images/checkout_24.ico";

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
  getCheckout,
  setInitCartInform,
} from "../../_actions/product_action";

import { signoutUser } from "../../_actions/signin_action";
import { getIsHistory} from "../../_actions/history_action";

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
      size,
      search,
      condition
    );
  }
 // pageProducts(abbr, "SEARCH", 0, size, search, condition);
  // 오른쪽 버튼 처리
  function handleEnterLeft() {
    pageProducts(
      abbr,
      categoryPrompt === config.SELECT_CATEGORY ? "" : categoryPrompt,
      page === 0 ? 0 : page - 1,
      size,
      search,
      condition
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
  const search = useSelector((state) => state.supplier.search);
  //const category = useSelector((state) => state.supplier.category);
  const page = useSelector((state) => state.product.products.number);
  const size = useSelector((state) => state.product.products.size);
  const totalPages = useSelector((state) => state.product.products.totalPages);
  const cartInform = useSelector((state) => state.product.cart);
  const error = useSelector((state) => state.product.error);
  const userData = useSelector((state) => state.user.userData);
  const isHitory = useSelector((state) => state.history.history);

  const dispatch = useDispatch();
  const formRef = React.useRef();

  useEffect(() => {
    return  window.scrollTo(0, 0);
  }, [headTitle, condition, page, size, isCart, isNew, isSpecial, categories]); 


  useEffect(() => {
    document.body.style.background = "#f0f0f0";
    let parm = { params: { abbr: headTitle } };
    dispatch(getCategoriesAction(parm));
    dispatch(changeSearchCondition("Co"));
    dispatch(changeIsCartRequest(false));
    dispatch(getIsHistory(abbr));
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
    dispatch(getIsHistory(abbr));
    pageProducts(abbr, "", 0, size);
    dispatch(getInitCartInform(abbr));
    setInputValue("");
    onClose();
    //console.log(`selected ${value}`);
  }

  function onCheckout(abbr) {
    dispatch(actionChangeSupplier(abbr));
    setCategoryPrompt(config.SELECT_CATEGORY);
      //dispatch(actionChangeTitle(searchValue.children)); 
    dispatch(actionChangeCategory(""));
    dispatch(changeIsCartRequest(false));
    let parm = { params: { abbr: abbr } };
    dispatch(getCategoriesAction(parm));
    dispatch(getCheckout(abbr, size)); 
    dispatch(setInitCartInform());
    dispatch(getIsHistory(abbr));
    onClose();
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
    
    setCategoryPrompt(category === "ORDERED" ? 'HISTORY' : category);
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

  const styleHeader = {
    position: "fixed",
    zIndex: 1,
    width: "100%",
    backgroundColor: "#bfbfbf",
    height: "70px",
    paddingTop: "10px",

  };

  const styleSupplier = { width: "100%", fontSize:"24px", fontWeight: "bold" };
  const styleCategory = { width: "100%", fontSize:"20px", fontWeight: "bold"};

  const styleKeySupplier = 
  {  position: "fixed",
      zIndex: 1,
      width:"30%",
    //        backgroundColor: "#bfbfbf",
      color:'#3455eb',
      textAlign: "left",
      fontWeight: "bold",
      fontStyle: "italic",
      padding: "5px 0px 0px 100px",
      marginBottom: "-10px" 
  };

  const styleKeyOrder = 
  {  position: "fixed",
      zIndex: 1,
    //  width:"30%",
    //        backgroundColor: "#bfbfbf",
      color:'#3455eb',
      textAlign: "left",
      fontWeight: "bold",
      fontStyle: "italic",
      padding: "5px 0px 0px 40px",
      marginBottom: "-10px" 
  };

  const onClickCart = () => {
    onChangeButton(config.CART);
    dispatch(actionGetSuppliers("cart"));
    dispatch(changeIsCartRequest(true));
    onClose();

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

  const onInputChange = (e) => {
    setInputValue(e.target.value)
  }
  const [inputValue, setInputValue] = useState("");
  const searchInput = (
    <div style={{ width: "103%" }}>
      <Input.Group compact>
        <Select
          defaultValue="Co"
          onChange={onChangeSelect}
         // style={{ borderStyle: "ridge" }}
        >
          <Option value="All">All</Option>
          <Option value="Co">one</Option>
        </Select>
        <Search
          value = {inputValue}
          placeholder="input search text"
          allowClear
          enterButton
          onSearch={onSearchInput}
          onChange={onInputChange}
          style={{ width: "68%" }}
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
    //onChangeSuppiler(abbr);
    onCheckout(abbr);
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

  };

  const onClickNew = () => {
    onChangeButton("NEW");
    onClose();

  };

  const onClickSpecial = () => {
    onChangeButton("SPECIAL");
    onClose();

  };

  const onClickHistory = () => {
    onChangeButton("ORDERED");
    onClose();
    dispatch(getInitCartInform());

  };

  const stylesButton = image => ( {
    backgroundImage:`url(${image})`,
    backgroundRepeat  : 'no-repeat',
    backgroundPosition: 'center',
    borderRadius: '4px',
    border: '1px solid black',
    display: "inline-block",
    width: "50px",
  } );

  const stylesHanbger = {
    display: "inline-block",
    width: "100%",
    marginTop: "10px",
    marginBottom: "5px",
  } 

  const buttonCart = (
    width > config.WIDTH_BIG ?
      <Tooltip placement="top" title= 'Cart'>
        <Button
          type="primary"
          style={ isCart || !cartInform? { display: "none" } : stylesButton(cart) }
          onClick={onClickCart}
        > &nbsp; </Button>
      </Tooltip>
    :
      <Button
        type="primary"
        style={ isCart || !cartInform ? { display: "none" } : stylesHanbger }
        onClick={onClickCart}
      >
        <FaShoppingCart size={16} style={{ marginBottom: "-4px" }} />
        &nbsp; CART
      </Button>
  );

  const buttonCheckout = (
    width > config.WIDTH_BIG ?
      <Tooltip placement="top" title= 'Checkout'>
        <Button
          type="primary"
          style={ isCart ? stylesButton(checkout)  : { display: "none" } }
          onClick={onClickCheckout}
        > &nbsp; </Button>
      </Tooltip>
    :
      <Button
        type="primary"
        style={ isCart ? stylesHanbger  : { display: "none" } }
        onClick={onClickCheckout}
      >
        <FaCalendarCheck size={16} style={{ marginBottom: "-4px" }} />
        &nbsp; CHECKOUT
      </Button>
  );


  const buttonHistory = (
    width > config.WIDTH_BIG ?
      <Tooltip placement="top" title= 'History'>
        <Button
          type="primary"
          style={ isHitory ? stylesButton(history)  : { display: "none" } }
          onClick={onClickHistory}
        > &nbsp; </Button>
      </Tooltip>
    : 
      <Button
        type="primary"
        style={  isHitory ? stylesHanbger : { display: "none" } }
        onClick={onClickHistory}
      >
        <FaTruck size={16} style={{ marginBottom: "-4px" }} />
        &nbsp; HISTORY
      </Button>

  );

  const buttonReport = (
    width > config.WIDTH_BIG ?
      <Link to="/history">
        <Tooltip placement="top" title= 'Rreport'>
          <Button
            type="primary"
            style={ stylesButton(report) }
          > &nbsp; </Button>
        </Tooltip>
      </Link>
    : 
      <Link to="/history">
        <Button
            type="primary"
            style={ stylesHanbger }
            onClick={onClickHistory}
          >
            <FaChartLine size={16} style={{ marginBottom: "-4px" }} />
            &nbsp; REPORT
        </Button>
      </Link>
      
  );

  const buttonIsNew = 
    isNew ? (
        width > config.WIDTH_BIG ?
        <Tooltip placement="top" title= 'New'>
          <Button
            type="primary"
            style={ isCart ? { display: "none" } : stylesButton(newProduct)  }
            onClick={onClickNew}
          > &nbsp; </Button>
        </Tooltip>
      : 
        <Button
        type="primary"
        style={ stylesHanbger }
        onClick={onClickNew}
        >
          <FaTags size={16} style={{ marginBottom: "-4px" }} />
            &nbsp; NEW
        </Button>
    ) : (
      ""
    );

  const buttonIsSpecial = 
    isSpecial ? (
      width > config.WIDTH_BIG ?
      <Tooltip placement="top" title= 'Sales'>
        <Button
          type="primary"
          style={ isCart ? { display: "none" } :  stylesButton(sales) }
          onClick={onClickSpecial}
        > &nbsp; </Button>
       </Tooltip> 
      :
        <Button
          type="primary"
          style={ stylesHanbger }
          onClick={onClickNew}
        >
          <FaGifts size={16} style={{ marginBottom: "-4px" }} />
           &nbsp; SPECIAL
        </Button>
    ) : (
      ""
    );

  const buttonSignout = (
    width > config.WIDTH_BIG  ?
      <Tooltip placement="top" title= 'Sign out'>
        <Button
          type="primary"
          style={ stylesButton(logout)}
          onClick={onClickSignout}
        > &nbsp; </Button>
      </Tooltip>
    :
      <Button
        type="primary"
        style={ stylesHanbger }
        onClick={onClickSignout}
      >
        <FaSignOutAlt size={16} style={{ marginBottom: "-4px" }} />
        &nbsp; SIGNOUT
      </Button>

  );


  function onClickHead(e) {
    e.preventDefault();
    setCategoryPrompt(config.SELECT_CATEGORY);
    dispatch(actionGetSuppliers());
    onChangeSuppiler(abbr, headTitle);
   
  }

  const headH2 = (
    <a href="#" onClick={onClickHead}>
      <Space>
        <FaHome size={40} style={{ color: "#000", paddingTop:'10px' }} />
        {/* <h2 style={{ display: "inline-block", color: "#000" }}>{headTitle}</h2> */}
      </Space>
    </a>
  );

 

  // function success() {
  //   Modal.success({
  //     title: "Thanks for your order ...",
  //   });
  // }

  return (
    <div>
      <Header
        style={styleHeader}
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
                {buttonHistory}
                {buttonCheckout}
                {buttonCart}
                {buttonReport}
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
              style={{ float: "right", margin: "0px 0" }}
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
                {buttonHistory}
                {buttonCheckout}
                {buttonCart}
                {buttonReport}
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
              style={{ float: "right", marginTop: "10px" }}
            >
              <MenuOutlined />
            </Button>
            <Drawer
              placement="right"
              closable={true}
              onClose={onClose}
              visible={visible}
            >
              <div style={{ display: "block", color: "#fff", marginTop:"20px"}}>
                {listSupplierSelect}
                {listCategorySelect}
                {searchInput}
                {buttonIsNew}
                {buttonIsSpecial}
                {buttonHistory}
                {buttonCheckout}
                {buttonCart}
                {buttonReport}
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
        Welcome  {userData ? userData.company : ""}
      </div>
      <div xxl={6} xl={8} lg={10} md={12} sm={24} xs={24}
        style={width > config.WIDTH_SMALL ? styleKeySupplier : { display: "none" }}
      >
        Key Suppliers
      </div>
      <div style={width > 1000 ? { display: "none" } : styleKeyOrder } >
          {cartInform}
      </div>

      <Modal
          visible={visibleCart}
          onOk={gotoOrder}
          onCancel={handleCancel}
          footer={ suppliers.length > 1 ? [
            <Button key="back" onClick={handleCancel}>
              Cancel
            </Button>,
            <Button type="primary"  onClick={gotoCart}>
              Checkout, Another Cart
            </Button>,     
            <Button type="primary" onClick={gotoOrder}>
             Checkout, GoTo Order
            </Button>,
          ] : [
            <Button key="back" onClick={handleCancel}>
              Cancel
            </Button>,   
            <Button type="primary" onClick={gotoOrder}>
              Checkout, GoTo Order
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
