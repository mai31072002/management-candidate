# Hệ thống Quản lý Nhân viên - Tài liệu Kỹ thuật

## Tổng quan

Hệ thống quản lý nhân viên được xây dựng trên kiến trúc microservices với frontend React và backend Spring Boot, sử dụng JWT cho xác thực và Redux cho quản lý state.

---

## 1. Cấu trúc hệ thống

### Frontend Architecture
- **React 19.1.1** - Component-based UI framework
- **Redux 5.0.1** - State management với Redux Thunk
- **Ant Design 6.1.4** - UI component library
- **Axios 1.13.2** - HTTP client với token refresh
- **Day.js 1.11.19** - Date manipulation và formatting
- **React Router 5.3.4** - Client-side routing
- **React i18next 16.5.1** - Internationalization (Việt/Anh)

### Backend Architecture
- **Spring Boot 3.x** - REST API framework
- **Spring Security 6.x** - Authentication & Authorization
- **Spring Data JPA 3.x** - Database access layer
- **MySQL/MariaDB** - Primary database
- **JWT** - Token-based authentication
- **Lombok 1.18.x** - Code generation

---

## 2. Mô hình dữ liệu cốt lõi

### Entity Relationships
```
DtbUsers (1) <-> (1) DtbEmployees
DtbEmployees (*) -> (1) DtbPosition
DtbEmployees (*) -> (1) DtbDepartment
DtbEmployees (*) -> (*) DtbOtDate
DtbEmployees (*) -> (*) DtbRewardPenalty
DtbUsers (*) -> (*) DtbRole -> (*) DtbPermission
```

### Validation Rules Chính
| Field | Validation | Mô tả |
|-------|------------|-------|
| Tuổi | 15-70 | Phạm vi tuổi hợp lệ cho nhân viên |
| CCCD | 9-15 số | Định dạng căn cước công dân |
| Phone | ^(0|+84)(\d{8,10})$ | Định dạng số điện thoại Việt Nam |
| Email | RFC 5322 | Định dạng email chuẩn |
| Lương cơ bản | ≥ 0 | Giá trị không âm |
| Ngày làm việc | Không tương lai | Không chọn ngày trong tương lai |

---

## 3. Luồng xử lý chung

### Authentication Flow
```
Login Component → Redux Action → JWT Service → Backend Auth Controller → 
Token Generation → LocalStorage Storage → Redux State Update → Route Redirect
```

### Data Fetching Pattern
```
Component → useDispatch → Redux Action → Axios Request → 
Token Refresh (401 handling) → Backend API → Redux Store Update → Component Re-render
```

### Form Submission Flow
```
Form Component → Validation Rules → Redux Action → API Call → 
Backend Validation → Database Update → Success/Error Response → UI Update
```

---

## 4. Security Architecture

### Token Management
- **Access Token**: 15 phút expiry
- **Refresh Token**: 7 ngày expiry
- **Storage**: LocalStorage với axios defaults
- **Refresh Pattern**: Automatic retry on 401 errors

### Permission System
- **Role-based Access Control (RBAC)**
- **Method-level Security**: @PreAuthorize annotations
- **Permission Constants**: EMPLOYEE_VIEW, EMPLOYEE_CREATE, etc.
- **Frontend Guards**: HOC components cho route protection

---

## 5. API Architecture

### Base Configuration
- **Base URL**: `/api`
- **Authentication**: Bearer Token
- **Content-Type**: application/json
- **Error Handling**: Centralized error responses

### Response Structure
```json
{
    "status": 200,
    "message": "Success",
    "data": {...},
    "pagination": {
        "page": 0,
        "size": 10,
        "totalElements": 100
    }
}
```

---

## 6. Performance & Optimization

### Frontend Optimizations
- **React.memo** cho component re-render prevention
- **useMemo/useCallback** cho expensive calculations
- **Lazy Loading** cho code splitting
- **Debouncing** cho search inputs

### Backend Optimizations
- **Lazy Loading** cho JPA relationships
- **Connection Pooling** với HikariCP
- **Caching** với Spring Cache
- **Batch Operations** cho bulk updates

---

## 7. Error Handling Strategy

### Frontend Error Handling
- **Form Validation**: Real-time validation với Ant Design
- **API Errors**: Token refresh + retry mechanism
- **Global Error Handler**: Centralized error display
- **User Feedback**: Toast notifications với react-i18next

### Backend Error Handling
- **Global Exception Handler**: @RestControllerAdvice
- **Validation Errors**: Bean Validation với Jakarta Validation
- **Custom Exceptions**: Business-specific exceptions
- **Error Response**: Standardized error format

---

## 8. Testing Strategy

### Frontend Testing
- **Unit Tests**: Jest + React Testing Library
- **Component Tests**: Component rendering và interactions
- **Integration Tests**: Redux action testing
- **E2E Tests**: User flow testing (planned)

### Backend Testing
- **Unit Tests**: JUnit 5 + Mockito
- **Integration Tests**: @SpringBootTest
- **API Tests**: MockMvc for endpoint testing
- **Repository Tests**: Database layer testing

---

## 9. Deployment & DevOps

### Environment Configuration
- **Development**: H2 in-memory database
- **Staging**: MySQL with sample data
- **Production**: MySQL with connection pooling
- **Configuration**: Environment variables với .env files

### Build & Deployment
- **Frontend**: React Scripts build optimization
- **Backend**: Maven build với Spring Boot plugin
- **Containerization**: Docker support (planned)
- **CI/CD**: GitHub Actions workflow (planned)

---

## 10. Future Enhancements

### Planned Features
- **Real-time Notifications**: WebSocket integration
- **File Upload**: Document management system
- **Advanced Reporting**: Chart.js integration
- **Mobile App**: React Native development

### Technical Improvements
- **Microservices Migration**: Service decomposition
- **Event Sourcing**: Audit trail implementation
- **Advanced Caching**: Redis integration
- **API Gateway**: Centralized API management

---

## 11. Development Guidelines

### Code Standards
- **ESLint + Prettier**: Frontend code formatting
- **Checkstyle**: Backend code standards
- **Git Flow**: Feature branch workflow
- **Code Reviews**: Pull request process

### Documentation Standards
- **API Documentation**: OpenAPI 3.0 specification
- **Component Documentation**: JSDoc comments
- **Database Documentation**: Entity relationship diagrams
- **Deployment Documentation**: Environment setup guides

---

Hệ thống tài liệu này cung cấp cái nhìn tổng quan về kiến trúc hệ thống quản lý nhân viên, phục vụ cho việc phát triển, bảo trì và mở rộng trong tương lai.
