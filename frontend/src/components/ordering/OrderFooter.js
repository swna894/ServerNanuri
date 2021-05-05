import * as config from "../../Config";
import React, { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { Layout, Pagination } from "antd";
import { getProductsAction } from "../../_actions/product_action";
import { useWindowWidthAndHeight } from "../../utils/CustomHooks";
import "./OrderPage.css";

function OrderFooter() {
    const { Footer } = Layout;
    const dispatch = useDispatch();

  const abbr = useSelector((state) => state.supplier.abbr);
  const current = useSelector((state) => state.product.products.number);
  const category = useSelector((state) => state.supplier.category);
  const totalElements = useSelector(
    (state) => state.product.products.totalElements
  );

  useEffect(() => {
    // window.addEventListener("keydown", (event) => {
    //   console.log("abbr = " + abbr);
    //    console.log("category = " + category);
    //   console.log("keydown = " + event.key);
    // });

    //console.log("company.company " + JSON.stringify(suppliers));
  }, []); // eslint-disable-line react-hooks/exhaustive-deps


  function onChange(pageNumber, pageSize = config.PAGE_SIZE) {
    let param = {
      params: { page: pageNumber - 1, size: pageSize, sort: "seq" },
    };
    category
      ? dispatch(getProductsAction(abbr, category, param))
      : dispatch(getProductsAction(abbr, "", param));
    document.documentElement.scrollTop = 0;
    //console.log("Page: ", pageNumber-1);
    //console.log("pageSize: ", pageSize);
  }

  const [width] = useWindowWidthAndHeight();

  const footerStyel = {
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
      <Footer style={footerStyel}>
        {width > 1000 ? (
          <Pagination
            current={current + 1}
            defaultPageSize={config.PAGE_SIZE}
            pageSizeOptions={[16, 24, 36, 60, 100]}
            showSizeChanger
            howSizeChanger={true}
            showQuickJumper
            defaultCurrent={1}
            total={totalElements}
            onChange={onChange}
          />
        ) : (
          <Pagination
            total={totalElements}
            pageSizeOptions={[16, 24, 36, 60, 100]}
            defaultPageSize={config.PAGE_SIZE}
            onChange={onChange}
            defaultCurrent={1}
            showTotal={(total) => `Total ${total} items`}
          />
        )}
      </Footer>
    </div>
  );
}

export default OrderFooter;
