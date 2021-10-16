import {
  GET_CATEGORYS_REQUEST,
  GET_PRODUCTS_REQUEST,
  GET_PRODUCTS_INIT,
  CHANGE_CART,
  GET_CART_INFORM,
  SET_ORDERING_REQUEST,
  GET_CHECKOUT_REQUEST,
  SET_CART_INIT,
  ACTION_ISPRODUCTS
} from "../service/types";

const initialState = {
  categories: [],
  products: [],
  isProducts : false,
};

export default function reducerProduct(state = initialState, action) {
  switch (action.type) {
    case GET_CATEGORYS_REQUEST:
      return { ...state, categories: action.payload };

    case GET_PRODUCTS_REQUEST:
      const product = JSON.stringify(action.payload.content);
      if(product.length === 2) {
        return {...state, isProducts : true };
      } else {
        return { ...state, products: action.payload };
      }

    case ACTION_ISPRODUCTS:
        return { ...state, isProducts : false };

    case GET_PRODUCTS_INIT:
      return { ...state, products: action.payload };

    case CHANGE_CART:
      return { ...state, products: action.payload, cart: action.payload.cart };

    case GET_CART_INFORM:
      return { ...state, cart: action.payload };

    case SET_ORDERING_REQUEST:
      return { ...state, error: action.payload };

    case GET_CHECKOUT_REQUEST:
      return { ...state, products: action.payload };
    case SET_CART_INIT:
        return { ...state, cart: "" };
    default:
      return state;
  }
}
