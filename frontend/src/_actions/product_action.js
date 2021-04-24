import { GET_CATEGORYS_REQUEST, GET_PRODUCTS_REQUEST } from "../service/types";
import axios from "axios";

export function getCategoriesAction(params) {
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

export function getProductsAction(params) {
  const request = axios
    .get("/api/products/category", params)
    .then((response) => response.data)
    .catch((error) => {
      console.log("Problem !!! Get Products", error);
    });

  return {
    type: GET_PRODUCTS_REQUEST,
    payload: request,
  };
}