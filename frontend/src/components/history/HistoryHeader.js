import React from "react";
import { withRouter, Link } from "react-router-dom";
import { Layout,  Space } from "antd";
import { FaHome } from "react-icons/fa";


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
//  const supplier = useSelector((state) => state.supplier.supplier);
//  const suppliers = useSelector((state) => state.supplier.suppliers);

  // const listSupplierSelect = (
  //   <Select
  //     name="supplier"
  //     showSearch
  //     value={supplier}
  //     //onChange={onChangeSuppiler}
  //     style={width > 800 ? styleSupplier : stylePersent}
  //     placeholder={config.SELECT_CATEGORY}
  //     optionFilterProp="children"
  //   >
  //     {suppliers.map((item) => (
  //       <Select.Option key={item.id} value={item.abbr}>
  //         {item.company}
  //       </Select.Option>
  //     ))}
  //   </Select>
  // );

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
{/*          {listSupplierSelect} */}
        </Space>
      </Layout.Header>
    </div>
  );
}

export default withRouter(HistoryHeader);
