import React from "react";
import { useSelector, useDispatch } from "react-redux";
import { Layout, Pagination } from "antd";
import { getProductsAction } from "../../_actions/product_action";
import "./OrderPage.css";

function OrderFooter() {
  const supplier = useSelector((state) => state.supplier.supplier);
  const current = useSelector((state) => state.product.products.number);
  const category = useSelector((state) => state.supplier.category);
  //const categories = useSelector((state) => state.product.categories);
  const { Footer } = Layout;
  const dispatch = useDispatch();
  const totalElements = useSelector(
    (state) => state.product.products.totalElements
  );

  function onChange(pageNumber, pageSize) {
    let param = {
      params: { page: pageNumber - 1, size: pageSize, sort: "seq" },
    };
    //console.log("abbr = " + supplier);
    console.log(category);
    category
      ? dispatch(getProductsAction(supplier, category, param))
      : dispatch(getProductsAction(supplier, "", param));
    document.documentElement.scrollTop = 0;
    //console.log("Page: ", pageNumber-1);
    //console.log("pageSize: ", pageSize);
  }

  return (
    <div>
      <Footer
        style={{
          position: "fixed",
          width: "100%",
          bottom: "0",
          paddingTop: 10,
          paddingBottom: 10,
          textAlign: "right",
        }}
      >
        <Pagination
          current={current + 1}
          defaultPageSize={36}
          pageSizeOptions={[16, 24, 36, 60, 100]}
          showSizeChanger
          howSizeChanger={true}
          showQuickJumper
          defaultCurrent={1}
          total={totalElements}
          onChange={onChange}
          showTotal={(total) => `Total ${total} items`}
        />
      </Footer>
    </div>
  );
}

export default OrderFooter;
