import { combineReducers } from "redux";
import userRole from "../reducers/user_role.reducer";

const reducers = combineReducers({ 
    userRole,
});

export default reducers;
