import {
  GET_CATEGORYS_REQUEST,
  GET_PRODUCTS_REQUEST,
  GET_PRODUCTS_INIT,
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
