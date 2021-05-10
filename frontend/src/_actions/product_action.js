import {
  GET_CATEGORYS_REQUEST,
  GET_PRODUCTS_REQUEST,
  GET_PRODUCTS_INIT,
  CHANGE_CART,
  GET_CART_INFORM,
  SET_ORDERING_REQUEST,
} from "../service/types";

import axios from "axios";
//import authToken from "../utils/authToken";
function authToken(token) {
  if (token) {
    axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
  } else {
    delete axios.defaults.headers.common["Authorization"];
  }
}

export function getCategoriesAction(params) {
  authToken(localStorage.jwtToken);
  const request = axios
    .get("/api/products/category", params)
    .then((response) => response.data)
    .catch((error) => {
      console.log("Problem !!! Get Categoies", error);
    });

  return {
    type: GET_CATEGORYS_REQUEST,
    payload: request,
  };
}

export function getProductsAction(abbr, category, params) {
  authToken(localStorage.jwtToken);
  const request = axios
    .get(`/api/products/${abbr}/${category}`, params)
    .then((response) => response.data)
    .catch((error) => {
      console.log("Problem !!! Get Products", error);
    });
  return {
    type: GET_PRODUCTS_REQUEST,
    payload: request,
  };
}

export function getProductsInitAction() {
  authToken(localStorage.jwtToken);
  const request = axios
    .get(`/api/products/init`)
    .then((response) => response.data)
    .catch((error) => {
      console.log("Problem !!! Get Products", error);
       window.location.href = "/";
    });

  return {
    type: GET_PRODUCTS_INIT,
    payload: request,
  };
}

export async function changeCart(products, pageable, param) {
  authToken(localStorage.jwtToken);
  const request = await axios
    .post(`/api/order/cart`, param)
    .then((response) => response.data)
    .catch((error) => {
      console.log("Problem !!! Get Products", error);
    });
  const contenst = { content: products, ...pageable, cart: request };
  return {
    type: CHANGE_CART,
    payload: contenst,
  };
}

export const getInitCartInform = (abbr = "") => {
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

export const setOrderRequest = (abbr) => {
  const request = axios
    .put(`/api/order/confirm/${abbr}`)
    .then((response) => response.data)
    .catch((error) => {
      console.log("Problem !!! Get Supplier", error);
      //dispatch(getSuppliersFailusre(error.message));
    });
  return {
    type: SET_ORDERING_REQUEST,
    payload: request,
  };
};
