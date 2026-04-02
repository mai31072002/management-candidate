import authRoles from "app/auth/auth_roles";
import * as Actions from "../actions";

const initialState = {
    role: authRoles.onlyGuest,
    data: null,
    userDetail: {},
    employeeDetail: {},
};

// lưu token và thông tin user vào redux
const user = (state = initialState, action) => {
    switch (action.type) {
        case Actions.SET_USER_DATA: {
            return {
                ...initialState, 
                ...action.payload,
            };
        }
        case Actions.SET_USER_DETAIL: {
            return {
                ...state,
                userDetail: action.payload,
            };
        }

        case Actions.SET_EMPLOYEE_DETAIL: {
            return {
                ...state,
                employeeDetail: action.payload,
            }
        }

        case Actions.REMOVE_USER_DATA: {
            return {
                ...initialState,
            };
        }
        case Actions.USER_LOGGED_OUT: {
            return initialState;
        }
        default: {
            return state;
        }
    }
    };

export default user;
