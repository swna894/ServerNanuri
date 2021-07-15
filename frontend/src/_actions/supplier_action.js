import axios from "axios";
import {
  GET_SUPPLIER_REQUEST,
  // GET_SUPPLIERS_REQUEST,
  GET_SUPPLIERS_SUCCESS,
  GET_SUPPLIERS_FAILUAR,
  CHANGE_TITLE,
  CHANGE_SUPPLIER,
  CHANGE_CATEGORY,
  CHANGE_SEARCH,
  CHANGE_CONDITION,
  CHANGE_ISCART,
  GET_CART_INFORM,
} from "../service/types";

export const actionUpdateSuppliers = (suppliers) => {
  return (dispatch) => {
        dispatch(getSuppliersSucess(suppliers));
     
  };
}; 

export const actionGetSuppliers = (cart = '') => {
  return (dispatch) => {
    //dispatch(getSuppliersRequest());
    let url = cart ? "/api/suppliers/cart" : "/api/suppliers/order";
    axios
      .get(url)
      .then((response) => {
        dispatch(getSuppliersSucess(response.data));
      })
      .catch((error) => {
        dispatch(getSuppliersFailusre(error.message));
      });
  };
}; 

export const actionGetSupplier = () => {
  return (dispatch) => {
    //dispatch(getSuppliersRequest());
    axios
      .get("/api/supplier")
      .then((response) => {
        dispatch(getSupplierRequest(response.data));
      })
      .catch((error) => {
        dispatch(getSuppliersFailusre(error.message));
      });
  };
};

export const actionChangeCategory = (supplier) => {
  return (dispatch) => {
    dispatch(changeCategoryRequest(supplier));
  };
};

export const actionChangeSupplier = (supplier) => {
  return (dispatch) => {
    dispatch(changeSupplierRequest(supplier));
  };
};

export const actionChangeTitle = (title) => {
  return (dispatch) => {
    dispatch(changeTitleRequest(title));
  };
};

export const actionChangeSearch = (search) => {
  return (dispatch) => {
    dispatch(changeSearchRequest(search));
  };
};

// const getSuppliersRequest = () => {
//   return {
//     type: GET_SUPPLIERS_REQUEST,
//   };
// };
export const changeSearchCondition = (condition) => {
  return {
    type: CHANGE_CONDITION,
    payload: condition,
  };
};

const changeSearchRequest = (search) => {
  return {
    type: CHANGE_SEARCH,
    payload: search,
  };
};

export const changeIsCartRequest = (isCart) => {
  return {
    type: CHANGE_ISCART,
    payload: isCart,
  };
};

const changeCategoryRequest = (category) => {
  return {
    type: CHANGE_CATEGORY,
    payload: category,
  };
};

const changeSupplierRequest = (abbr) => {
  const request = axios
    .get(`/api/supplier/${abbr}`)
    .then((response) => response.data)
    .catch((error) => {
      console.log("Problem !!! Get Supplier", error);
      //dispatch(getSuppliersFailusre(error.message));
    });
  return {
    type: CHANGE_SUPPLIER,
    payload: request,
  };
};

const changeTitleRequest = (title) => {
  return {
    type: CHANGE_TITLE,
    payload: title,
  };
};

const getSupplierRequest = (supplier) => {
  return {
    type: GET_SUPPLIER_REQUEST,
    payload: supplier,
  };
};

const getSuppliersSucess = (suppliers) => {
  return {
    type: GET_SUPPLIERS_SUCCESS,
    payload: suppliers,
  };
};

const getSuppliersFailusre = (error) => {
  return {
    type: GET_SUPPLIERS_FAILUAR,
    payload: error,
  };
};

export const getCartInform = (abbr = "") => {
  const request = axios
    .get(`/api/order/cart/inform/${abbr}`)
    .then((response) => response.data)
    .catch((error) => {
      console.log("Problem !!! Get Supplier", error);
      //dispatch(getSuppliersFailusre(error.message));
    });
  return {
    type: GET_CART_INFORM,
    payload: request,
  };
};

