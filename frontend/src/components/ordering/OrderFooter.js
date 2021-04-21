import React from "react";
import { Layout, Pagination } from "antd";
import "./OrderPage.css";

 function OrderFooter() {
  const { Footer } = Layout;

  function onChange(pageNumber, pageSize) {
    console.log("Page: ", pageNumber);
    console.log("pageSize: ", pageSize);
  }

  return (
    <div>
      <Footer
        className="styles.custom"
        style={{
          position: "absolute",
          zIndex: 1,
          width: "100%",
          bottom: "0",
        }}
      >
        <Pagination
          style={{ float: "right" }}
          defaultPageSize={16}
          pageSizeOptions={[16, 24, 36, 60]}
          showSizeChanger
          howSizeChanger={true}
          showQuickJumper
          defaultCurrent={1}
          total={500}
          onChange={onChange}
          showTotal={(total) => `Total ${total} items`}
        />
      </Footer>
    </div>
  );
}

export default OrderFooter;
