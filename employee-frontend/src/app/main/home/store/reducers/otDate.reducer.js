import * as Action from "../actions";

const initialState = {
    otDateEmployeeId: {
        otDateEmployeeId: [],
        status: null,
        message: "",
    },
    otDate: {
        loading: false,
        page: 1, 
        limit: 10,
        totalElement: 0,
        status: null,
        otDate: [],
        status: null,
        message: "",
    },
    createOrUpdateOt: null,
}

const otDateReducer = (state  = initialState, action) => {
    switch (action.type) {
        case Action.OT_DATE_EMPLOYEE_ID:
            return {
                ...state,
                otDateEmployeeId: {
                    otDateEmployeeId: action.payload.data,
                    status: action.payload.status,
                    message: action.payload.message,
                },
                createOrUpdateOt: null,
            }
        case Action.OT_DATE_EMPLOYEE:
            return {
                ...state,
                otDate: {
                    otDate: action.payload.data.data,
                    page: action.payload.data.pageableRep.page,
                    limit: action.payload.data.pageableRep.limit,
                    totalElement: action.payload.data.pageableRep.totalElements,
                    error: null,
                    status: action.payload.data.status,
                    message: action.payload.data.message,
                },
                createOrUpdateOt: null,
            }
        case Action.CREATE_OT_DATE: 
            return {
                ...state,
                createOrUpdateOt: action.payload,
            }
        case Action.UPDATE_OT_DATE: 
            return {
                ...state,
                createOrUpdateOt: action.payload,
            }
        default:
            return state;
    }
}

export default otDateReducer;