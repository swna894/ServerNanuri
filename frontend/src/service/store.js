import { applyMiddleware, createStore } from 'redux';
import thunk from 'react-thunk';
import rootReducer from './rootReducer';

const store = createStore(rootReducer, applyMiddleware(thunk));

export default store;