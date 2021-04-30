import {
  GET_CATEGORYS_REQUEST,
  GET_PRODUCTS_REQUEST,
  GET_PRODUCTS_INIT,
  ORDER_INCREMENT,
  ORDER_DECREMENT,
  ORDER_CHANGE_INPUT,
} from "../service/types";
import axios from "axios";
//import authToken from "../utils/authToken";

export function getCategoriesAction(params) {
  if (localStorage.jwtToken) {
    axios.defaults.headers.common[
      "Authorization"
    ] = `Bearer ${localStorage.jwtToken}`;
  }
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
  if (localStorage.jwtToken) {
    axios.defaults.headers.common[
      "Authorization"
    ] = `Bearer ${localStorage.jwtToken}`;
  }
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
  if (localStorage.jwtToken) {
    axios.defaults.headers.common[
      "Authorization"
    ] = `Bearer ${localStorage.jwtToken}`;
  }
  const request = axios
    .get(`/api/products/init`)
    .then((response) => response.data)
    .catch((error) => {
      console.log("Problem !!! Get Products", error);
    });

  return {
    type: GET_PRODUCTS_INIT,
    payload: request,
  };
}

export function changeIncremant(products, pageable, param) {
  const contenst = {content:products, ...pageable};
  return {
    type: ORDER_INCREMENT,
    payload: contenst,
  };
} 

export function changeInput(products, pageable, param) {
  const contenst = { content: products, ...pageable };
  return {
    type: ORDER_CHANGE_INPUT,
    payload: contenst,
  };
} 

export function changeDecremant(products, pageable, param) {
  // const request = axios
  //   .post("/api/order/decremant", param)
  //   .then((response) => response.data)
  //   .catch((error) => {
  //     console.log("Problem !!! Get Categoies", error);
  //   });
  const contenst = { content: products, ...pageable };
  return {
    type: ORDER_DECREMENT,
    payload: contenst,
  };
}
