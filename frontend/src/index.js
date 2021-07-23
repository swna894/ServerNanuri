import React from "react";
import ReactDOM from "react-dom";
import reportWebVitals from "./reportWebVitals";
import { Provider } from "react-redux";
import { applyMiddleware, createStore } from "redux";
import promiseMiddleware from "redux-promise";
import Reduxthunk from "redux-thunk";
import Reducer from "./service/rootReducer";
import App from "./App";
import "./index.css";
import "antd/dist/antd.css";
//import axios from "axios";

const createStoreWithMiddleware = applyMiddleware(
  promiseMiddleware,
  Reduxthunk
)(createStore);

//console.log("localStorage.jwtToken" + localStorage.jwtToken);


ReactDOM.render(
  <Provider
    store={createStoreWithMiddleware(
      Reducer,
      window.__REDUX_DEVTOOLS_EXTENSION__ &&
        window.__REDUX_DEVTOOLS_EXTENSION__()
    )}
  >
    <App />
  </Provider>,
  document.getElementById("root")
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
