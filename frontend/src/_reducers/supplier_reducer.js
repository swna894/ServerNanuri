import {
  GET_SUPPLIER_REQUEST,
  GET_SUPPLIERS_REQUEST,
  GET_SUPPLIERS_SUCCESS,
  GET_SUPPLIERS_FAILUAR,
  CHANGE_SUPPLIER,
} from "../service/types";

const initialState = {
  suppliers: [],
  supplier: "",
  error: "",
};

const reducer = (state = initialState, action) => {
  switch (action.type) {
    case GET_SUPPLIERS_REQUEST:
      return {
        ...state,
      };

    case GET_SUPPLIER_REQUEST:
      return { ...state, supplier: action.payload };

    case CHANGE_SUPPLIER:
      return { ...state, supplier: action.payload };

    case GET_SUPPLIERS_SUCCESS:
      return {
        ...state,
        suppliers: action.payload,
        error: "",
      };

    case GET_SUPPLIERS_FAILUAR:
      return {
        ...state,
        suppliers: [],
        error: action.payload,
      };
    default:
      return state;
  }
};

export default reducer;

// export default function reducers(state = {}, action) {
//   switch (action.type) {
//     case GET_SUPPLIERS_REQUEST:
//       return { ...state, suppliers: action.payload };

//     default:
//       return state;
//   }
// }
