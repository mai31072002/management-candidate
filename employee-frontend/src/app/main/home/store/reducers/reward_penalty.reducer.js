import * as Action from "../actions";

const initialState = {
    rewardPenaltyEmployeeId: {
        data: [],
        status: null,
        message: "",
    },
    createUpdateDeleteRewardPenalty: {
        status: null,
        message: "",
    }

}

const RewardPenaltyReducer = (state  = initialState, action) => {
    switch (action.type) {
        case Action.LIST_REWARD_PENALTY_BY_EMPLOYEE_FETCHED:
            return {
                ...state,
                rewardPenaltyEmployeeId: {
                    data: action.payload.data,
                    status: action.payload.status,
                    message: action.payload.message,
                },
            }
        case Action.LIST_REWARD_PENALTY_BY_EMPLOYEE_ERROR:
            return {
                ...state,
                rewardPenaltyEmployeeId: {
                    data: [],
                    status: action.payload.status,
                    message: action.payload.message,
                }
            }
        case Action.CREATE_REWARD_PENALTY:
            return {
                ...state,
                createUpdateDeleteRewardPenalty: {
                    status: action.payload.status,
                    message: action.payload.message,
                }
            }
        case Action.UPDATE_REWARD_PENALTY:
            return {
                ...state,
                createUpdateDeleteRewardPenalty: {
                    status: action.payload.status,
                    message: action.payload.message,
                }
            }
        case Action.DELETE_REWARD_PENALTY:
            return {
                ...state,
                createUpdateDeleteRewardPenalty: {
                    status: action.payload.status,
                    message: action.payload.message,
                }
            }
        default:
            return state;
    }
}

export default RewardPenaltyReducer;