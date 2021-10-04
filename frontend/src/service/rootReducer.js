import {combineReducers} from 'redux';
import userReducer from '../_reducers/signin_reducer';
import supplierReducer from "../_reducers/supplier_reducer";
import productReducer from "../_reducers/product_reducer";
import historyReducer from "../_reducers/history_reducer";

const rootReducer = combineReducers({
  user: userReducer,
  supplier: supplierReducer,
  product:productReducer,
  history:historyReducer,
});

export default rootReducer;