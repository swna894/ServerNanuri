import { GET_CATEGORYS_REQUEST } from "../service/types";

const initialState = {
  categories: [],
};

export default function reducerProduct(state = initialState, action) {
  switch (action.type) {
    case GET_CATEGORYS_REQUEST:
      return { ...state, categories: action.payload };

    default:
      return state;
  }
}
