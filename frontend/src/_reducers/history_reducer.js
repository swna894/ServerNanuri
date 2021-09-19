import { GET_ORDER_HISTORY } from "../service/types";

const initialState = {
  history: 0,
};

export default function reducerProduct(state = initialState, action) {
  switch (action.type) {
    case GET_ORDER_HISTORY:
      return { ...state, history: action.payload };

    default:
      return state;
  }
}

