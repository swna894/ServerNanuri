import axios from "axios";
import {
  GET_SUPPLIERS_REQUEST,
  GET_SUPPLIERS_SUCCESS,
  GET_SUPPLIERS_FAILUAR,
} from "../service/types";

export const getSuppliers = () => {
  return (dispatch) => {
    dispatch(getSupplierRequest());
    axios
      .get("/api/suppliers")
      .then((response) => {
        dispatch(getSupplierSucess(response.data));
      })
      .catch((error) => {
        dispatch(getSupplierFailusre(error.message));
      });
  };
};

const getSupplierRequest = () => {
  return {
    type: GET_SUPPLIERS_REQUEST,
  };
};

const getSupplierSucess = (suppliers) => {
  return {
    type: GET_SUPPLIERS_SUCCESS,
    payload: suppliers,
  };
};

const getSupplierFailusre = (error) => {
  return {
    type: GET_SUPPLIERS_FAILUAR,
    payload: error,
  };
};

// export function getSuppliers() {
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
