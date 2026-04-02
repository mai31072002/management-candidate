# Công nghệ Frontend - Hệ thống Quản lý Nhân viên

## 1. UI Framework & Components

### React Ecosystem
| Thư viện | Vấn đề giải quyết | Mẫu áp dụng |
|----------|----------------|------------|
| React 19.1.1 | Component-based UI development | Functional components với hooks |
| React DOM 19.1.1 | Browser rendering | Virtual DOM reconciliation |
| React Router 5.3.4 | Client-side routing | Route protection với authentication guards |

### UI Component Library
| Thư viện | Vấn đề giải quyết | Mẫu áp dụng |
|----------|----------------|------------|
| Ant Design 6.1.4 | Pre-built UI components | Form, Table, Modal, DatePicker components |
| Perfect Scrollbar 1.5.6 | Custom scrollbar styling | Enhanced scrolling experience |

## 2. State Management

### Redux Pattern
| Thư viện | Vấn đề giải quyết | Mẫu áp dụng |
|----------|----------------|------------|
| Redux 5.0.1 | Global state management | Redux store với action creators |
| Redux Thunk 3.1.0 | Async actions | API calls với token refresh |
| React Redux 9.2.0 | React-Redux binding | useDispatch và useSelector hooks |

### State Structure Pattern
```
// Redux Store Structure
{
    auth: { user, token, loading, error },
    dashboard: { employees, positions, departments },
    employee: { detail, loading, error },
    // ... other modules
}
```

## 3. Data Handling & Validation

### Date & Time Management
| Thư viện | Vấn đề giải quyết | Mẫu áp dụng |
|----------|----------------|------------|
| Day.js 1.11.19 | Date manipulation & timezone handling | Luôn convert sang object dayjs trước khi đưa vào Form |
| - | Date formatting | Pattern: `format("YYYY-MM-DD")` cho API, `format("DD/MM/YYYY")` cho display |
| - | Date validation | Pattern: Age calculation với `dayjs().diff(birthday, "year")` |

### Form Validation Pattern
| Validation Type | Rule | Mô tả |
|-----------------|------|-------|
| Text fields | min/max length, pattern validation | Ví dụ: First name 2-50 chars, letters only |
| Email | RFC 5322 format | Standard email validation |
| Phone | Vietnam format | Regex: `^(0|+84)(\d{8,10})$` |
| Age range | 15-70 years | Business rule validation |
| Date range | Future date prevention | Disabled dates trong DatePicker |

## 4. HTTP Client & API Integration

### Axios Configuration
| Thư viện | Vấn đề giải quyết | Mẫu áp dụng |
|----------|----------------|------------|
| Axios 1.13.2 | HTTP requests với interceptors | Global authorization header setup |
| - | Token refresh mechanism | Automatic retry on 401 errors |
| - | Request/Response interceptors | Centralized error handling |

### Token Refresh Pattern
```
// Fixed Token Refresh Pattern
try {
    const tokenData = await jwtService.signInWithToken();
    if (tokenData?.data) {
        const newToken = tokenData.data.accessToken;
        axios.defaults.headers.common.Authorization = `Bearer ${newToken}`;
        // Retry original request
    }
} catch (refreshError) {
    // Handle refresh failure
}
```

## 5. Internationalization

### Multi-language Support
| Thư viện | Vấn đề giải quyết | Mẫu áp dụng |
|----------|----------------|------------|
| React i18next 16.5.1 | Multi-language UI | Translation keys với useTranslation hook |
| i18next 25.7.4 | Internationalization core | Vietnamese (primary) và English support |

### Translation Pattern
```
// Translation Key Structure
const translationKeys = {
    "employee.form.firstNameRequired": "Tên không được để trống",
    "account.validationEmailInvalid": "Email không hợp lệ",
    "common.save": "Lưu",
    "common.cancel": "Hủy"
};
```

## 6. Utility Libraries

### Helper Functions
| Thư viện | Vấn đề giải quyết | Mẫu áp dụng |
|----------|----------------|------------|
| Lodash 4.17.21 | Data manipulation | Utility functions cho array/object operations |
| JWT Decode 4.0.0 | Token parsing | Extract user information từ JWT tokens |
| React Device Detect 2.2.3 | Device detection | Responsive design adjustments |

## 7. Development & Testing

### Testing Framework
| Thư viện | Vấn đề giải quyết | Mẫu áp dụng |
|----------|----------------|------------|
| React Scripts 5.0.1 | Build tool & development server | Hot module replacement, build optimization |
| Testing Library 16.1.0 | Component testing | User interaction simulation |
| Jest DOM 6.6.3 | DOM testing utilities | DOM assertions và queries |

### Build Configuration
| Tool | Vấn đề giải quyết | Mẫu áp dụng |
|------|----------------|------------|
| Webpack (via React Scripts) | Module bundling | Code splitting và optimization |
| Babel | JavaScript transpilation | ES6+ to ES5 conversion |
| ESLint | Code quality | Linting rules và formatting |

## 8. Performance Optimization

### Component Optimization
| Technique | Vấn đề giải quyết | Mẫu áp dụng |
|-----------|----------------|------------|
| React.memo | Unnecessary re-renders | Memoization cho pure components |
| useMemo | Expensive calculations | Cache computed values |
| useCallback | Function references | Stable function references |
| Lazy Loading | Code splitting | Dynamic imports cho large components |

### State Optimization
| Pattern | Vấn đề giải quyết | Mẫu áp dụng |
|--------|----------------|------------|
| Normalization | Efficient state updates | Store data in normalized format |
| Selector Optimization | Specific state access | useSelector với specific selectors |
| Action Batching | Grouped state updates | Multiple dispatch calls batching |

## 9. Security Best Practices

### Authentication Security
| Pattern | Vấn đề giải quyết | Mẫu áp dụng |
|--------|----------------|------------|
| JWT Storage | Token persistence | LocalStorage với automatic cleanup |
| Token Refresh | Session continuity | Automatic token refresh on expiry |
| Route Protection | Unauthorized access | HOC components với authentication checks |

### Data Validation
| Layer | Vấn đề giải quyết | Mẫu áp dụng |
|-------|----------------|------------|
| Frontend Validation | Immediate feedback | Ant Design Form validation rules |
| Backend Validation | Data integrity | Server-side validation as fallback |
| Input Sanitization | XSS prevention | Proper input encoding và sanitization |

## 10. Common Issues & Solutions

### DatePicker Issues
| Vấn đề | Giải quyết | Pattern |
|--------|-----------|---------|
| Wrong date format | Standardize format | Luôn dùng `format("DD/MM/YYYY")` cho display |
| Validation errors | Proper date objects | Convert sang dayjs object trước khi validate |
| Timezone issues | Consistent timezone | Luôn dùng UTC timezone cho API calls |

### Form Validation Issues
| Vấn đề | Giải quyết | Pattern |
|--------|-----------|---------|
| Async validation | Promise-based validators | Custom validators với Promise.resolve/reject |
| Cross-field validation | Dependencies validation | Form dependencies với `dependencies` prop |
| Dynamic validation | Conditional rules | Dynamic validation rules based on form values |

### API Integration Issues
| Vấn đề | Giải quyết | Pattern |
|--------|-----------|---------|
| 401 errors | Token refresh | Automatic retry mechanism với token refresh |
| Network errors | Error handling | Global error handler với user feedback |
| Loading states | User experience | Loading indicators cho async operations |

---

## 11. Development Workflow

### File Structure Pattern
```
src/app/main/[module]/
├── [module].js              # Main component
├── [module].config.js        # Configuration constants
└── component/
    ├── AddEdit[Module].js   # Form components
    ├── [Module]Table.js     # Table components
    └── [Module]List.js      # List components
```

### Naming Conventions
| Type | Convention | Ví dụ |
|------|------------|-------|
| Components | PascalCase | AddEditEmployee.js |
| Files | kebab-case | employee-management.js |
| Constants | UPPER_SNAKE_CASE | LIST_EMPLOYEE_FETCHED |
| Functions | camelCase | fetchListEmployee |

---

## 12. Integration Patterns

### Component Integration
| Pattern | Mô tả | Ví dụ |
|---------|-------|-------|
| Container-Presenter | Logic separation | Container handles Redux, Presenter handles UI |
| Higher-Order Components | Cross-cutting concerns | Authentication guards |
| Render Props | Component composition | Form với dynamic fields |

### API Integration
| Pattern | Mô tả | Ví dụ |
|---------|-------|-------|
| Action Creators | API call encapsulation | Redux actions với error handling |
| Selectors | State access optimization | Specific data selectors |
| Middleware | Cross-cutting logic | Token refresh middleware |

---

Tài liệu này cung cấp cái nhìn tổng quan về stack công nghệ frontend và các patterns được áp dụng trong hệ thống quản lý nhân viên.
