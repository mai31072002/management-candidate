import { combineReducers } from "redux";
import department from "../reducers/department.reducer";

const reducers = combineReducers({ 
    department,
});

export default reducers;
