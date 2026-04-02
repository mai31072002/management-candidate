import * as Action from "../actions";

const initialState = {
    loading: false,
    error: null,
    departmentList: {
        data: [],
        status: null,
        message: "",
    },
    createUpdateDepartment: null,
}

const departmentReducer = (state  = initialState, action) => {
    switch (action.type) {
        case Action.LIST_DEPARTMENT_LOADING: 
            return {
                ...state,
                loading: true,
            }
        case Action.LIST_DEPARTMENT_FETCHED:
            return {
                ...state,
                loading: false,
                departmentList: {
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
        case Action.LIST_DEPARTMENT_ERROR:
            return {
                ...state,
                loading: false,
                error: action.payload,
            }
        case Action.CREATE_DEPARTMENT:
            return {
                ...state,
                createUpdateDepartment: action.payload,
            }
        case Action.UPDATE_DEPARTMENT:
            return {
                ...state,
                createUpdateDepartment: action.payload,
            }
        case Action.DELETE_DEPARTMENT:
            return {
                ...state,
                createUpdateDepartment: action.payload,
            }
        default:
            return state;
    }
}

export default departmentReducer;