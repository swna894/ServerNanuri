import * as config from "../../Config";
import React from "react";
import { useSelector, useDispatch } from "react-redux";
import { Layout, Pagination } from "antd";
import { getProductsAction } from "../../_actions/product_action";
import { useWindowWidthAndHeight } from "../../utils/CustomHooks";
import "./OrderPage.css";

function OrderFooter() {
  const { Footer } = Layout;
  const dispatch = useDispatch();

  const abbr = useSelector((state) => state.supplier.abbr);
  const cartInform = useSelector((state) => state.product.cart);
  const search = useSelector((state) => state.supplier.search);
  const condition = useSelector((state) => state.supplier.condition);
  const current = useSelector((state) => state.product.products.number);
  const category = useSelector((state) => state.supplier.category);
  const totalElements = useSelector(
    (state) => state.product.products.totalElements
  );


 // eslint-disable-line react-hooks/exhaustive-deps

  function onChange(pageNumber, pageSize = config.PAGE_SIZE) {
    if (category === "SEARCH") {
      let param = {
        params: {
          page: pageNumber - 1,
          size: pageSize,
          sort: "seq",
          abbr: abbr,
          search: search,
          condition:condition
        },
      };
       dispatch(getProductsAction(abbr, category, param));
    } else {
      let param = {
        params: { page: pageNumber - 1, size: pageSize, sort: "seq" },
      };
      category
        ? dispatch(getProductsAction(abbr, category, param))
        : dispatch(getProductsAction(abbr, "", param));
    }
    //console.log("Page: ", pageNumber-1);
    //console.log("pageSize: ", pageSize);
  }

  const [width] = useWindowWidthAndHeight();

  const footerStyle = {
    position: "fixed",
    width: "100%",
    bottom: "0",
    paddingTop: 10,
    paddingBottom: 10,
    textAlign: "right",
    backgroundColor: "#bfbfbf",
  };

  return (
    <div>
      <Footer style={footerStyle}>
      <div
        style={{
          position: "fixed",
          zIndex: 1,
          height: "8px",
          textAlign: "left",
          fontWeight: "bold",
          fontStyle: "italic",
          fontSize : "18px",
          paddingRight: "52px",
          paddingTop: "3px",
        }}
      >
      {cartInform}
      </div>
        {width > 1000 ? (
          <Pagination
            className={'paginationItemStyle'}
            current={current + 1}
            defaultPageSize={config.PAGE_SIZE}
            pageSizeOptions={[16, 24, 36, 60, 100]}
            showSizeChanger
            howSizeChanger={true}
            showQuickJumper
            defaultCurrent={1}
            total={totalElements}
            showTotal={(total) => `Total ${total} items`}
            onChange={onChange}
          />
        ) : (
          <Pagination
            total={totalElements}
            pageSizeOptions={[16, 24, 36, 60, 100]}
            defaultPageSize={config.PAGE_SIZE}
            onChange={onChange}
            defaultCurrent={1}
            //showTotal={(total) => `Total ${total} items`}
          />
        )}
      </Footer>
    </div>
  );
}

export default OrderFooter;
