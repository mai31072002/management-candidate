import { combineReducers } from "redux";
import dashboard from "./dashboard.reducer";
import otDate from "./otDate.reducer";
import rewardPenalty from "./reward_penalty.reducer";

const reducers = combineReducers({ 
    dashboard,
    otDate,
    rewardPenalty,
});

export default reducers;
