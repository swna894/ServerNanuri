import axios from "axios";

function authToken(token) {
  if (token) {
    axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;;
  } else {
    delete axios.defaults.headers.common["Authorization"];
  }
}

export default authToken;
