import {
  GET_SUPPLIER_REQUEST,
  GET_SUPPLIERS_REQUEST,
  GET_SUPPLIERS_SUCCESS,
  GET_SUPPLIERS_FAILUAR,
  CHANGE_TITLE,
  CHANGE_SUPPLIER,
  CHANGE_CATEGORY,
  CHANGE_SEARCH,
} from "../service/types";

const initialState = {
  suppliers: [],
  abbr: "",
  title: "",
  error: "",
};

const reducer = (state = initialState, action) => {
  switch (action.type) {
    case GET_SUPPLIERS_REQUEST:
      return {
        ...state,
      };

    case GET_SUPPLIER_REQUEST:
      return {
        ...state,
        title: action.payload.company,
        abbr: action.payload.abbr,
        supplier: action.payload.company,
        isNew: action.payload.isNew,
        isSpecial: action.payload.isSpecial,
      };

    case CHANGE_TITLE:
      return { ...state, title: action.payload };

    case CHANGE_SUPPLIER:
      return {
        ...state,
        title: action.payload.company,
        abbr: action.payload.abbr,
        supplier: action.payload.company,
        isNew: action.payload.isNew,
        isSpecial: action.payload.isSpecial,
      };

    case CHANGE_CATEGORY:
      return { ...state, category: action.payload };

    case CHANGE_SEARCH:
      return { ...state, search: action.payload };

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

