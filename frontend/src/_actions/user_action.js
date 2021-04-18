import axios from "axios";
import { LOGIN_USER, SIGNUP_USER, AUTH_USER } from "./types";

export function signinUser(dataToSubmit) {
  const request = axios
    .post("/api/signin", dataToSubmit)
    .then((response) => response.data)
    .catch((error) => {
      console.log("Problem submitting New Post", error);
    });

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
  const request = axios.get("/api/auth").then((response) => response.data);

  return {
    type: AUTH_USER,
    payload: request,
  };
}
