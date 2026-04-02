# Quản lý Nhân viên

## Mô tả nghiệp vụ
Chức năng này giải quyết vấn đề quản lý thông tin nhân viên trong tổ chức, bao gồm:
- Quản lý hồ sơ nhân viên với thông tin cá nhân và thông tin công việc
- Theo dõi thông tin làm việc, lương và vị trí chức vụ
- Tích hợp với hệ thống phân quyền và quản lý tổ chức
- Hỗ trợ tìm kiếm và lọc dữ liệu nhân viên theo nhiều tiêu chí

## Luồng dữ liệu (Data Flow)
```
AddEditEmployee Component → Redux Action (dashboard.action.js) → 
Redux Store → Axios Request → Backend Controller (EmployeeController.java) → 
Service Layer (EmployeeService.java) → Repository Layer → Database (MySQL)
```

- **Frontend Flow**: Component → Form Validation → Dispatch Action → API Call → Token Refresh (401) → State Update
- **Backend Flow**: Controller → Service → Repository → Database → Response → Frontend State Update

## Chi tiết logic & Validation

### Validation Rules
| Field | Validation | Mô tả |
|-------|------------|-------|
| First Name | 2-50 chars, letters only | Tên phải là chữ cái, không có số/ký tự đặc biệt |
| Last Name | 1-50 chars, letters only | Họ phải là chữ cái, không có số/ký tự đặc biệt |
| Username | 4-128 chars, không chứa "admin" | Tên đăng nhập không được có từ "admin" |
| Email | RFC 5322 format | Định dạng email chuẩn |
| Phone | ^(0|+84)(\d{8,10})$ | Định dạng số điện thoại Việt Nam |
| CCCD | 9-15 digits | Số căn cước công dân |
| Birthday | Age 15-70 years | Tuổi phải trong khoảng 15-70 |
| Start Date | Required, valid date | Ngày bắt đầu làm việc |
| End Date | After start date | Ngày kết thúc phải sau ngày bắt đầu |
| Base Salary | ≥ 0 | Lương cơ bản không âm |
| Province | Required, Vietnam province | Tỉnh/thành phố Việt Nam |
| District | Required, district in province | Quận/huyện thuộc tỉnh đã chọn |

### Business Logic
- **Age Calculation**: Tự động tính tuổi từ birthday
- **District Loading**: Load quận/huyện dựa trên tỉnh đã chọn
- **Employee Code**: Mã nhân viên phải unique
- **Status Management**: 1=Đang làm việc, 2=Đã nghỉ việc, 3=Thử việc
- **Position/Department**: Phải chọn từ danh sách có sẵn

## Liên kết file (Traceability)

### Frontend Files
- `src/app/main/home/component/AddEditEmployee.js` - Form component chính
- `src/app/main/home/store/actions/dashboard.action.js` - Redux actions
- `src/app/main/home/component/EmployeeTable.js` - Table component
- `src/app/main/home/component/SearchEmployee.js` - Search component

### Backend Files
- `src/main/java/com/candidate/candidate_backend/controller/EmployeeController.java` - REST endpoints
- `src/main/java/com/candidate/candidate_backend/service/EmployeeService.java` - Business logic
- `src/main/java/com/candidate/candidate_backend/entity/DtbEmployees.java` - Entity model
- `src/main/java/com/candidate/candidate_backend/dto/employee/DtoEmployeeReq.java` - Request DTO
- `src/main/java/com/candidate/candidate_backend/repository/EmployeeRepository.java` - Data access

### API Endpoints
- `GET /api/users?page={page}&size={size}` - Lấy danh sách nhân viên
- `POST /api/employee` - Tạo nhân viên mới
- `PUT /api/employee/{id}` - Cập nhật nhân viên
- `DELETE /api/employee/{id}` - Xóa nhân viên
- `GET /api/users/search?keyword={keyword}&page={page}&size={size}` - Tìm kiếm nhân viên

### Key Classes & Functions
- **Frontend**: `fetchListEmployee()`, `CreateEmployee()`, `UpdateEmployee()`, `DeleteEmployee()`
- **Backend**: `EmployeeService.createEmployee()`, `EmployeeService.updateEmployee()`, `EmployeeService.deleteEmployee()`
- **Validation**: `validateEmployeeData()`, `validateAgeRange()`, `validatePhoneFormat()`
