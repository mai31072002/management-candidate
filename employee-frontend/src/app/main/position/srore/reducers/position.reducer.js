import * as Action from "../actions";

const initialState = {
    loading: false,
    error: null,
    positionList: {
        data: [],
        status: null,
        message: "",
    },
    createUpdatePosition: null,
}

const positionReducer = (state  = initialState, action) => {
    switch (action.type) {
        case Action.LIST_POSITION_LOADING: 
            return {
                ...state,
                loading: true,
            }
        case Action.LIST_POSITION_FETCHED:
            return {
                ...state,
                loading: false,
                positionList: {
                    data: action.payload.data,
                    status: action.payload.status,
                    message: action.payload.message
                },
            }
        // case Action.LIST_USER_SEARCH_FETCHED:
        //     return {
        //         ...state,
        //         loading: false,
        //         userList: {
        //             data: action.payload.data,
        //             status: action.payload.status,
        //             message: action.payload.message,
        //             page: action.payload.pageableRep.page, 
        //             limit: action.payload.pageableRep.limit,
        //             totalElement: action.payload.pageableRep.totalElements,
        //         },
        //     }
        case Action.LIST_POSITION_ERROR:
            return {
                ...state,
                loading: false,
                error: action.payload,
            }
        case Action.CREATE_POSITION:
            return {
                ...state,
                createUpdatePosition: action.payload,
            }
        case Action.UPDATE_POSITION:
            return {
                ...state,
                createUpdatePosition: action.payload,
            }
        case Action.DELETE_POSITION:
            return {
                ...state,
                createUpdatePosition: action.payload,
            }
        default:
            return state;
    }
}

export default positionReducer;