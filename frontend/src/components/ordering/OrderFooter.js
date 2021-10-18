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
  function onShowSizeChange(pageSize) {
    // let param = {
    //   params: { page: 0, size: pageSize, sort: "seq" },
    // };
    // category
    //   ? dispatch(getProductsAction(abbr, category, param))
    //   : dispatch(getProductsAction(abbr, "", param));
  }

  function onChange(pageNumber, pageSize) {
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

  const cartInformStyle = {
    position: "fixed",
    zIndex: 1,
    height: "8px",
    textAlign: "left",
    fontWeight: "bold",
    fontStyle: "italic",
    fontSize : "18px",
    paddingRight: "52px",
    paddingTop: "3px",
  };

  return (
    <div>
      <Footer style={footerStyle}>
        <div style={width > 600 ? cartInformStyle : { display: "none" }} >
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
            onShowSizeChange={onShowSizeChange}
            onChange={onChange}
          />
        ) : (
          <Pagination simple 
            current={current + 1}
            defaultCurrent={1} 
            // defaultCurrent={config.PAGE_SIZE} 
            total={totalElements}     
            onChange={onChange}
          />
  
        )}
      </Footer>
    </div>
  );
}

export default OrderFooter;
