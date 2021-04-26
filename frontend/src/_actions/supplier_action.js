import axios from "axios";
import {
  GET_SUPPLIER_REQUEST,
  // GET_SUPPLIERS_REQUEST,
  GET_SUPPLIERS_SUCCESS,
  GET_SUPPLIERS_FAILUAR,
  CHANGE_TITLE,
  CHANGE_SUPPLIER,
} from "../service/types";

export const actionGetSuppliers = () => {
  return (dispatch) => {
    //dispatch(getSuppliersRequest());
    axios
      .get("/api/suppliers")
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

// const getSuppliersRequest = () => {
//   return {
//     type: GET_SUPPLIERS_REQUEST,
//   };
// };

const changeSupplierRequest = (supplier) => {
  return {
    type: CHANGE_SUPPLIER,
    payload: supplier,
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

// export function getSupplier() {
//   const request = axios
//                     .get("/api/suppliers")
//                     .then((response) => response.data)
//                     .catch((error) => {
//                       console.log("Problem submitting Supplier", error);
//                     });

//   return {
//     type: GET_SUPPLIERS_REQUEST,
//     payload: request,
//   };
//}
