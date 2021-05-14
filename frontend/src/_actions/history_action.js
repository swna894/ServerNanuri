import {
  GET_ORDER_HISTORY,
} from "../service/types";

import axios from "axios";

function authToken(token) {
  if (token) {
    axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
  } else {
    delete axios.defaults.headers.common["Authorization"];
  }
}

export function getHistoryOrder() {
  authToken(localStorage.jwtToken);
  const request = axios
    .get("/api/orders/history")
    .then((response) => response.data)
    .catch((error) => {
      console.log("Problem !!! Get History", error);
    });

  return {
    type: GET_ORDER_HISTORY,
    payload: request,
  };
}

