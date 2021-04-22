import { SIGNIN_USER, SIGNUP_USER, AUTH_USER } from "../service/types";

export default function reducerUser(state = {}, action) {
  switch (action.type) {
    case SIGNIN_USER:
      return { ...state, loginShop: action.payload };

    case SIGNUP_USER:
      return { ...state, register: action.payload };

    case AUTH_USER:
      return { ...state, userData: action.payload };

    default:
      return state;
  }
}
