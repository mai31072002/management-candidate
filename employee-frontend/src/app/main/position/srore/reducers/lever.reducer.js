import * as Action from "../actions";

const initialState = {
    loading: false,
    error: null,
    leverList: {
        data: [],
        status: null,
        message: "",
    },
    createUpdateLever: null,
}

const leverReducer = (state  = initialState, action) => {
    switch (action.type) {
        case Action.LIST_LEVER_LOADING: 
            return {
                ...state,
                loading: true,
            }
        case Action.LIST_LEVER_FETCHED:
            return {
                ...state,
                loading: false,
                leverList: {
                    data: action.payload.data,
                    status: action.payload.status,
                    message: action.payload.message
                },
            }
        case Action.LIST_LEVER_ERROR:
            return {
                ...state,
                loading: false,
                error: action.payload,
            }
        case Action.CREATE_LEVER:
            return {
                ...state,
                createUpdateLever: action.payload,
            }
        case Action.UPDATE_LEVER:
            return {
                ...state,
                createUpdateLever: action.payload,
            }
        case Action.DELETE_LEVER:
            return {
                ...state,
                createUpdateLever: action.payload,
            }
        default:
            return state;
    }
}

export default leverReducer;