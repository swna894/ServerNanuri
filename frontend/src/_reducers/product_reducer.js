import {
  GET_CATEGORYS_REQUEST,
  GET_PRODUCTS_REQUEST,
  GET_PRODUCTS_INIT,
  CHANGE_CART,
  GET_CART_INFORM,
  SET_ORDERING_REQUEST,
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

    case CHANGE_CART:
      return { ...state, products: action.payload, cart: action.payload.cart };

    case GET_CART_INFORM:
      return { ...state, cart: action.payload };

    case SET_ORDERING_REQUEST:
      return { ...state, error: action.payload };

    default:
      return state;
  }
}
