import {
  GET_CATEGORYS_REQUEST,
  GET_PRODUCTS_REQUEST,
  GET_PRODUCTS_INIT,
} from "../service/types";

const initialState = {
  categories: [],
  products: [],
};

export default function reducerProduct(state = initialState, action) {
  switch (action.type) {
    case GET_CATEGORYS_REQUEST:
      return { ...state, categories: action.payload };

    case GET_PRODUCTS_REQUEST:
      return { ...state, products: action.payload };

    case GET_PRODUCTS_INIT:
      return { ...state, products: action.payload };

    default:
      return state;
  }
}
