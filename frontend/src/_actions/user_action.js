import axios from "axios";
import { LOGIN_USER, SIGNUP_USER, AUTH_USER } from "./types";
export function loginUser(dataToSubmit) {
  const request = axios
    .post("/api/signin", dataToSubmit)
    .then((response) => response.data);

  return {
    type: LOGIN_USER,
    payload: request,
  };
}

export function signupUser(datatosubmit) {
  const request = axios
    .post("/api/signup", datatosubmit)
    .then((response) => response.data);

  return {
    type: SIGNUP_USER,
    payload: request,
  };
}

export function auth() {
  const request = axios
    .get("/api/users/auth")
    .then((response) => response.data);

  return {
    type: AUTH_USER,
    payload: request,
  };
}
