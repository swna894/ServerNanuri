import { GET_ORDER_HISTORY } from "../service/types";

const initialState = {
  orders: [],
};

export default function reducerProduct(state = initialState, action) {
  switch (action.type) {
    case GET_ORDER_HISTORY:
      return { ...state, orders: action.payload };

    default:
      return state;
  }
}

