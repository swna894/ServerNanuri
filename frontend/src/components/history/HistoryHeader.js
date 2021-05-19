import React from "react";
import { withRouter, Link } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { Button, Layout, Space, Select } from "antd";
import { FaHome } from "react-icons/fa";
import { signoutUser } from "../../_actions/user_action";
import { FaSignOutAlt, FaGifts, FaRedoAlt } from "react-icons/fa";
import { getHistoryOrder } from "../../_actions/history_action";
import { actionChangeSupplier, } from "../../_actions/supplier_action";

const headH2 = (
  <Link to="/order">
    <Space>
      <FaHome size={28} style={{ color: "#000" }} />
      <h2 style={{ display: "inline-block", color: "#000" }}>ORDER HISTORY</h2>
    </Space>
  </Link>
);

// Start pageHeage
function HistoryHeader() {
  const dispatch = useDispatch();
  const supplier = useSelector((state) => state.supplier.supplier);
  const suppliers = useSelector((state) => state.supplier.suppliers);

  const styleSupplier = { width: "230px" };

  function onChangeSuppiler(value) {
    // setCategoryPrompt(config.SELECT_CATEGORY);
    // dispatch(actionChangeSupplier(abbr));
    // dispatch(actionChangeCategory(""));
    // dispatch(changeIsCartRequest(false));
    // let parm = { params: { abbr: abbr } };
    // dispatch(getCategoriesAction(parm));
    // pageProducts(abbr, "", 0, size);
    // dispatch(getInitCartInform(abbr));
    // onClose();
    dispatch(actionChangeSupplier(value));
    dispatch(getHistoryOrder(value));
    console.log(`selected ${value}`);
  }


    function onClickReload(value) {
      dispatch(actionChangeSupplier());
      dispatch(getHistoryOrder());
    }

  const listSupplierSelect = (
    <Select
      name="supplier"
      showSearch
      value={supplier}
      onChange={onChangeSuppiler}
      style={styleSupplier}
      optionFilterProp="children"
    >
      {suppliers.map((item) => (
        <Select.Option key={item.id} value={item.abbr}>
          {item.company}
        </Select.Option>
      ))}
    </Select>
  );
  const styleButton = {
    display: "inline-block",
    width: "110px",
    borderStyle: "ridge",
  };

  const onClickSignout = () => {
    localStorage.setItem("jwtToken", "");
    dispatch(signoutUser());
    window.location.href = "/";
  };

  const buttonSignout = (
    <Button onClick={onClickSignout} type="primary" style={styleButton}>
      <FaSignOutAlt size={16} style={{ marginBottom: "-4px" }} />
      &nbsp; LOGOUT
    </Button>
  );

  const buttonOrder = (
    <Button type="primary" style={styleButton}>
      <Link to="/order">
        <FaGifts size={16} style={{ marginBottom: "-4px" }} />
        &nbsp; To ORDER
      </Link>
    </Button>
  );

    const buttonReload = (
      <Button type="primary" style={styleButton} onClick={onClickReload}>
        <FaRedoAlt size={16} style={{ marginBottom: "-4px" }} />
        &nbsp; RELOAD
      </Button>
    );

  return (
    <div>
      <Layout.Header
        style={{
          position: "fixed",
          zIndex: 1,
          width: "100%",
          backgroundColor: "#bfbfbf",
          height: "70px",
          paddingTop: "6px",
        }}
      >
        <Space>
          {headH2}
          {listSupplierSelect}
        </Space>
        <Space style={{ float: "right" }}>
          {buttonOrder}
          {buttonReload}
          {buttonSignout}
        </Space>
      </Layout.Header>
    </div>
  );
}

export default withRouter(HistoryHeader);
