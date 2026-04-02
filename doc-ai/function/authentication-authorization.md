# Xác thực & Phân quyền

## Mô tả nghiệp vụ
Chức năng này giải quyết vấn đề bảo mật và kiểm soát truy cập trong hệ thống:
- Quản lý đăng nhập, đăng xuất người dùng
- Kiểm soát truy cập dựa trên vai trò và quyền hạn
- Quản lý token JWT (access token và refresh token)
- Bảo vệ các endpoint API khỏi truy cập trái phép
- Cung cấp cơ chế phân quyền linh hoạt cho các chức năng khác

## Luồng dữ liệu (Data Flow)
```
Login Component → Redux Action (auth.action.js) → 
JWT Service → Backend Auth Controller → Authentication Provider → 
Token Generation → LocalStorage → Redux State Update → Route Redirect
```

- **Authentication Flow**: Login Form → Credential Validation → Token Generation → Storage → State Update
- **Authorization Flow**: Route Access Check → Permission Validation → Component Render/Redirect
- **Token Refresh Flow**: 401 Error Detection → Token Refresh Request → New Token Storage → Request Retry

## Chi tiết logic & Validation

### Authentication Rules
| Field | Validation | Mô tả |
|-------|------------|-------|
| Username | 4-128 chars, required | Tên đăng nhập bắt buộc, độ dài tối thiểu 4 |
| Password | 6+ chars, required | Mật khẩu bắt buộc, độ dài tối thiểu 6 |
| Access Token | 15 minutes expiry | Token truy cập hết hạn sau 15 phút |
| Refresh Token | 7 days expiry | Token làm mới hết hạn sau 7 ngày |

### Authorization Rules
| Permission | Description | Scope |
|------------|-------------|-------|
| EMPLOYEE_VIEW | Xem danh sách nhân viên | Read access |
| EMPLOYEE_CREATE | Tạo nhân viên mới | Write access |
| EMPLOYEE_UPDATE | Cập nhật thông tin nhân viên | Write access |
| EMPLOYEE_DELETE | Xóa nhân viên | Delete access |
| OT_APPROVE | Phê duyệt overtime | Manager access |

### Security Patterns
- **JWT Storage**: Lưu token trong localStorage với automatic cleanup
- **Route Protection**: HOC components kiểm tra authentication trước khi render
- **Method Security**: @PreAuthorize annotations trên backend endpoints
- **Token Refresh**: Automatic retry mechanism khi token hết hạn

## Liên kết file (Traceability)

### Frontend Files
- `src/app/main/auth/login.js` - Login form component
- `src/app/main/auth/store/actions/auth.action.js` - Authentication Redux actions
- `src/app/service/jwt.js` - JWT token management service
- `src/app/auth/authorization.js` - Authorization HOC components
- `src/app/auth/auth.js` - Authentication utilities

### Backend Files
- `src/main/java/com/candidate/candidate_backend/controller/LoginController.java` - Auth endpoints
- `src/main/java/com/candidate/candidate_backend/config/JwtTokenProvider.java` - JWT token provider
- `src/main/java/com/candidate/candidate_backend/config/SecurityConfig.java` - Security configuration
- `src/main/java/com/candidate/candidate_backend/service/PermissionService.java` - Permission checking
- `src/main/java/com/candidate/candidate_backend/entity/DtbUser.java` - User entity

### API Endpoints
- `POST /api/auth/login` - Đăng nhập người dùng
- `POST /api/auth/refresh-token` - Làm mới access token
- `POST /api/auth/logout` - Đăng xuất
- `GET /api/users/me` - Lấy thông tin user hiện tại

### Key Classes & Functions
- **Frontend**: `login()`, `logout()`, `signInWithToken()`, `hasPermission()`, `withAuthorization()`
- **Backend**: `JwtTokenProvider.generateAccessToken()`, `PermissionService.hasPermission()`, `AuthenticationManager.authenticate()`
- **Security**: `SecurityFilterChain`, `@PreAuthorize`, `BCryptPasswordEncoder`
