import {
  GET_CATEGORYS_REQUEST,
  GET_PRODUCTS_REQUEST,
  GET_PRODUCTS_INIT,
  CHANGE_CART,
  GET_CART_INFORM,
  SET_ORDERING_REQUEST,
  GET_CHECKOUT_REQUEST,
  SET_CART_INIT,
  ACTION_ISPRODUCTS,
} from "../service/types";

//import axios from "axios";
import axiosInstance from "../service/axiosInstance";

export function getCategoriesAction(params) {
  const request =  axiosInstance
    .get("/api/categorys", params)
    .then((response) => {
         return response.data
        }
      )
    .catch((error) => {
      console.log("Problem !!! Get Categoies", error);
    });

  return {
    type: GET_CATEGORYS_REQUEST,
    payload: request,
  };
}

export function getProductsAction(abbr, category, params) {
  const request = axiosInstance
    .get(`/api/products/${abbr}/${category}`, params)
    .then((response) => {
         return response.data
     }
    )
    .catch((error) => {
      console.log("Problem !!! Get Products", error);
    });
  return {
    type: GET_PRODUCTS_REQUEST,
    payload: request,
  };
}

export function getProductsInitAction() {
  //axios.defaults.headers.Authorization = `Bearer ${localStorage.jwtToken}`;
  const request = axiosInstance
    .get(`/api/products/init`)
    .then((response) => {
      return response.data})
    .catch((error) => {
      console.log("Problem !!! Get Products", error);
       // window.location.href = "/";
    });

  return {
    type: GET_PRODUCTS_INIT,
    payload: request,
  };
}

export async function changeCart(products, pageable, param) {
  //authToken(localStorage.jwtToken);
  const request = await axiosInstance
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
  const request = axiosInstance
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

export const setInitCartInform = () => {
  return {
    type: SET_CART_INIT,
    payload: "",
  };
};

export const actionIsProducts = () => {
  return {
    type: ACTION_ISPRODUCTS,
    payload: false,
  };
};

export const setOrderRequest =  async (abbr) => {
  const request =  await axiosInstance
    .put(`/api/order/confirm/${abbr}`)
    .then( (response) => response.data)
    .catch((error) => {
      console.log("Problem !!! Get Supplier", error);
      //dispatch(getSuppliersFailusre(error.message));
    });
  return {
    type: SET_ORDERING_REQUEST,
    payload: request,
  };
};

export  const getCheckout =  async (abbr, size) => {
  const request =  await axiosInstance
    .get(`/api/order/checkout/${abbr}/${size}`)
    .then( (response) => response.data)
    .catch((error) => {
      console.log("Problem !!! Get Supplier", error);
      //dispatch(getSuppliersFailusre(error.message));
    });
  return {
    type: GET_CHECKOUT_REQUEST,
    payload: request,
  };
};
