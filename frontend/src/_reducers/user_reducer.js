import {
  SIGNIN_USER,
  SIGNUP_USER,
  AUTH_USER,
  SIGNOUT_USER,
} from "../service/types";

export default function reducerUser(state = {}, action) {
  switch (action.type) {
    case SIGNIN_USER:
      return { ...state, loginShop: action.payload };

    case SIGNUP_USER:
      return { ...state, register: action.payload };

    case AUTH_USER:
      return { ...state, userData: action.payload };

    case SIGNOUT_USER:
      return { ...state, loginShop: action.payload };
      
    default:
      return state;
  }
}
