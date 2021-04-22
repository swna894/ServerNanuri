import {
  GET_SUPPLIERS_REQUEST,
  GET_SUPPLIERS_SUCCESS,
  GET_SUPPLIERS_FAILUAR,
}  from "../service/types";

const initialState = {
  suppliers : [],
  error : ''

}


const reducer = (state = initialState, action) => {
  switch (action.type) {
    case GET_SUPPLIERS_REQUEST:
      return {
        ...state,
      };

    case GET_SUPPLIERS_SUCCESS:
      return {
        suppliers: action.payload,
        error: "",
      };

    case GET_SUPPLIERS_FAILUAR:
      return {
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
