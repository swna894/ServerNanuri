import {
  GET_ORDER_HISTORY,
} from "../service/types";

import axiosInstance from "../service/axiosInstance";

export const getHistoryOrder = (abbr = '') => {
  const request = axiosInstance
    .get(`/api/orders/history/${abbr}`)
    .then((response) => response.data)
    .catch((error) => {
      console.log("Problem !!! Get History", error);
    });

  return {
    type: GET_ORDER_HISTORY,
    payload: request,
  };
}

