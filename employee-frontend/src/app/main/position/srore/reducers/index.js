import { combineReducers } from "redux";
import position from "./position.reducer";
import lever from "./lever.reducer";

const reducers = combineReducers({ 
    position,
    lever
});

export default reducers;
