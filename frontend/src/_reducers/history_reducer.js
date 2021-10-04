import { GET_ORDER_HISTORY, GET_IS_HISTORY } from "../service/types";

const initialState = {
  orders: [],
};

export default function reducerProduct(state = initialState, action) {
  switch (action.type) {
    case GET_ORDER_HISTORY:
      return { ...state, orders: action.payload };
    case GET_IS_HISTORY:
        return { ...state, history: action.payload };

    default:
      return state;
  }
}

