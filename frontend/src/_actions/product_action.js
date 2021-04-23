import { GET_CATEGORYS_REQUEST } from "../service/types";
import axios from "axios";

export function getCategoriesAction(dataToSubmit) {
  const request = axios
    .get("/api/products/category", dataToSubmit)
    .then((response) => response.data)
    .catch((error) => {
      console.log("Problem submitting New Post", error);
    });

  return {
    type: GET_CATEGORYS_REQUEST,
    payload: request,
  };
}
