import {combineReducers} from 'redux';
import userReducer from '../_reducers/user_reducer';
import supplierReducer from "../_reducers/supplier_reducer";
import productReducer from "../_reducers/product_reducer";

const rootReducer = combineReducers({
  user: userReducer,
  supplier: supplierReducer,
  product:productReducer,
});

export default rootReducer;