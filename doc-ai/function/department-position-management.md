# Quản lý Phòng ban & Vị trí

## Mô tả nghiệp vụ
Chức năng này giải quyết vấn đề quản lý cơ cấu tổ chức của công ty:
- Quản lý thông tin phòng ban, bộ phận trong tổ chức
- Quản lý các vị trí công việc và chức danh
- Thiết lập cấu trúc tổ chức và mối quan hệ giữa phòng ban - vị trí
- Hỗ trợ phân quyền và quản lý nhân sự theo cơ cấu
- Cung cấp dữ liệu cho quản lý nhân viên và báo cáo

## Luồng dữ liệu (Data Flow)
```
Department Component → Redux Action (department.action.js) → 
Redux Store → Axios Request → Backend Controller (DepartmentController.java) → 
Service Layer (DepartmentService.java) → Repository Layer → Database (MySQL)
```

```
Position Component → Redux Action (position.action.js) → 
Redux Store → Axios Request → Backend Controller (PositionController.java) → 
Service Layer (PositionService.java) → Repository Layer → Database (MySQL)
```

- **Frontend Flow**: Component → Form Validation → Dispatch Action → API Call → Token Refresh (401) → State Update
- **Backend Flow**: Controller → Service → Repository → Database → Response → Frontend State Update
- **Integration Flow**: Department ↔ Position ↔ Employee relationships

## Chi tiết logic & Validation

### Department Validation Rules
| Field | Validation | Mô tả |
|-------|------------|-------|
| Department Name | Required, 2-100 chars | Tên phòng ban bắt buộc, độ dài 2-100 ký tự |
| Description | Optional, max 500 chars | Mô tả chi tiết về chức năng phòng ban |
| Status | Required, 1-2 | 1=Hoạt động, 2=Ngừng hoạt động |
| Parent Department | Optional, valid ID | Phòng ban cha (nếu có cấu trúc phân cấp) |

### Position Validation Rules
| Field | Validation | Mô tả |
|-------|------------|-------|
| Position Name | Required, 2-100 chars | Tên vị trí bắt buộc, độ dài 2-100 ký tự |
| Description | Optional, max 500 chars | Mô tả chi tiết về vai trò, trách nhiệm |
| Department ID | Required, valid UUID | Phòng ban trực thuộc |
| Status | Required, 1-2 | 1=Hoạt động, 2=Ngừng hoạt động |
| Level | Optional, 1-10 | Cấp bậc trong tổ chức |

### Business Logic
- **Department Hierarchy**: Hỗ trợ cấu trúc phòng ban phân cấp (parent-child)
- **Position Assignment**: Vị trí thuộc về phòng ban cụ thể
- **Employee Integration**: Nhân viên được gán vị trí và phòng ban
- **Status Management**: Trạng thái hoạt động/ngừng hoạt động
- **Unique Validation**: Tên phòng ban/vị trí phải unique trong hệ thống

### Organizational Rules
- **Department Structure**: Có thể có phòng ban cha (parent department)
- **Position Levels**: Cấp bậc từ 1-10 (1 cao nhất)
- **Active Status**: Chỉ hiển thị các phòng ban/vị trí đang hoạt động
- **Employee Assignment**: Không thể xóa phòng ban/vị trí đã có nhân viên

## Liên kết file (Traceability)

### Frontend Files
- `src/app/main/department/component/AddEditDepartment.js` - Department form component
- `src/app/main/department/srore/actions/department.action.js` - Department Redux actions
- `src/app/main/department/component/DepartmentTable.js` - Department table component
- `src/app/main/position/component/AddEditPosition.js` - Position form component
- `src/app/main/position/srore/actions/position.action.js` - Position Redux actions
- `src/app/main/position/component/PositionTable.js` - Position table component

### Backend Files
- `src/main/java/com/candidate/candidate_backend/controller/DepartmentController.java` - Department REST endpoints
- `src/main/java/com/candidate/candidate_backend/service/DepartmentService.java` - Department business logic
- `src/main/java/com/candidate/candidate_backend/entity/DtbDepartment.java` - Department entity model
- `src/main/java/com/candidate/candidate_backend/dto/department/DtoDepartmentReq.java` - Department request DTO
- `src/main/java/com/candidate/candidate_backend/repository/DepartmentRepository.java` - Department data access
- `src/main/java/com/candidate/candidate_backend/controller/PositionController.java` - Position REST endpoints
- `src/main/java/com/candidate/candidate_backend/service/PositionService.java` - Position business logic
- `src/main/java/com/candidate/candidate_backend/entity/DtbPosition.java` - Position entity model
- `src/main/java/com/candidate/candidate_backend/dto/position/DtoPositionReq.java` - Position request DTO
- `src/main/java/com/candidate/candidate_backend/repository/PositionRepository.java` - Position data access

### API Endpoints
#### Department APIs
- `GET /api/department` - Lấy danh sách phòng ban
- `POST /api/department` - Tạo phòng ban mới
- `PUT /api/department/{departmentId}` - Cập nhật phòng ban
- `DELETE /api/department/{departmentId}` - Xóa phòng ban

#### Position APIs
- `GET /api/position` - Lấy danh sách vị trí
- `POST /api/position` - Tạo vị trí mới
- `PUT /api/position/{positionId}` - Cập nhật vị trí
- `DELETE /api/position/{positionId}` - Xóa vị trí

### Key Classes & Functions
- **Frontend**: `fetchListDepartment()`, `CreateDepartment()`, `UpdateDepartment()`, `DeleteDepartment()`
- **Frontend**: `fetchListPosition()`, `CreatePosition()`, `UpdatePosition()`, `DeletePosition()`
- **Backend**: `DepartmentService.createDepartment()`, `DepartmentService.updateDepartment()`
- **Backend**: `PositionService.createPosition()`, `PositionService.updatePosition()`
- **Validation**: `validateDepartmentName()`, `validatePositionName()`, `checkEmployeeAssignment()`
