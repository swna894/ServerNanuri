import { LOGIN_USER, SIGNUP_USER, AUTH_USER } from "../_actions/types";

export default function reducers(state = {}, action) {
  switch (action.type) {
    case LOGIN_USER:
      return { ...state, loginSuccess: action.payload };

    case SIGNUP_USER:
      return { ...state, register: action.payload };

    case AUTH_USER:
      return { ...state, userData: action.payload };

    default:
      return state;
  }
}
