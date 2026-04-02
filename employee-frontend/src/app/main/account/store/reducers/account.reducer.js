import * as Action from "../actions";

const initialState = {
    updateAccount: {},
}

const accountReducer = (state  = initialState, action) => {
    switch (action.type) {
        case Action.UPDATE_ACCOUNT: {
            return {
                ...state,
                updateAccount:action.payload,
            }
        }
        default:
            return state;
    }
}

export default accountReducer;