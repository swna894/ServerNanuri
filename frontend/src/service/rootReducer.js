import {combineReducers} from 'redux';
import userReducer from '../_reducers/user_reducer';
import supplierReducer from "../_reducers/supplier_reducer";

const rootReducer = combineReducers({
  user: userReducer,
  supplier: supplierReducer,
});

export default rootReducer;