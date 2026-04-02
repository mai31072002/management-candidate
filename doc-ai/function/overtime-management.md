# Quản lý Làm thêm giờ

## Mô tả nghiệp vụ
Chức năng này giải quyết vấn đề quản lý và phê duyệt làm thêm giờ trong tổ chức:
- Ghi nhận yêu cầu làm thêm giờ của nhân viên
- Phê duyệt/ từ chối yêu cầu OT từ quản lý
- Theo dõi lịch sử làm thêm giờ theo tháng/năm
- Tính toán và quản lý thời gian làm thêm giờ
- Tích hợp với hệ thống tính lương và chấm công

## Luồng dữ liệu (Data Flow)
```
AddEditOT Component → Redux Action (otDate.action.js) → 
Redux Store → Axios Request → Backend Controller (OtDateController.java) → 
Service Layer (OtDateService.java) → Repository Layer → Database (MySQL)
```

- **Frontend Flow**: Component → Form Validation → Dispatch Action → API Call → Token Refresh (401) → State Update
- **Backend Flow**: Controller → Service → Repository → Database → Response → Frontend State Update
- **Approval Flow**: Manager Review → Status Update → Notification → Salary Integration

## Chi tiết logic & Validation

### Validation Rules
| Field | Validation | Mô tả |
|-------|------------|-------|
| Work Date | Required, not future | Ngày làm việc phải trong quá khứ |
| Start Time | Required, valid time | Giờ bắt đầu làm thêm giờ |
| End Time | After start time | Giờ kết thúc phải sau giờ bắt đầu |
| OT Type | Required, 1-3 | 1=Ngày thường, 2=Cuối tuần, 3=Ngày lễ |
| Status | Required, 1-3 | 1=Chờ duyệt, 2=Đã duyệt, 3=Từ chối |
| Duration | Min 1 hour | Thời gian làm thêm tối thiểu 1 tiếng |

### Business Logic
- **OT Type Calculation**: Tự động tính hệ số lương theo loại OT
- **Time Validation**: Kiểm tra giờ làm thêm không trùng lịch chấm công
- **Approval Workflow**: Yêu cầu phê duyệt từ quản lý trực tiếp
- **Monthly Limits**: Giới hạn số giờ làm thêm tối đa mỗi tháng
- **Status Management**: Chờ duyệt → Đã duyệt → Tích hợp lương

### OT Type Rules
- **Type 1 (Ngày thường)**: Hệ số 1.5x
- **Type 2 (Cuối tuần)**: Hệ số 2.0x  
- **Type 3 (Ngày lễ)**: Hệ số 3.0x

## Liên kết file (Traceability)

### Frontend Files
- `src/app/main/home/component/AddEditOT.js` - OT form component
- `src/app/main/home/store/actions/otDate.action.js` - OT Redux actions
- `src/app/main/home/component/OTTable.js` - OT table component
- `src/app/main/home/component/OTCalendar.js` - OT calendar component

### Backend Files
- `src/main/java/com/candidate/candidate_backend/controller/OtDateController.java` - OT REST endpoints
- `src/main/java/com/candidate/candidate_backend/service/OtDateService.java` - OT business logic
- `src/main/java/com/candidate/candidate_backend/entity/DtbOtDate.java` - OT entity model
- `src/main/java/com/candidate/candidate_backend/dto/otdate/DtoOtDateReq.java` - OT request DTO
- `src/main/java/com/candidate/candidate_backend/repository/OtDateRepository.java` - OT data access

### API Endpoints
- `GET /api/ot-date/{employeeId}?month={month}` - Lấy danh sách OT theo nhân viên và tháng
- `POST /api/ot-date` - Tạo yêu cầu OT mới
- `PUT /api/ot-date/{otDateId}` - Cập nhật yêu cầu OT
- `DELETE /api/ot-date/{otDateId}` - Xóa yêu cầu OT
- `PUT /api/ot-date/{otDateId}/approve` - Phê duyệt yêu cầu OT

### Key Classes & Functions
- **Frontend**: `fetchListOtDate()`, `CreateOtDay()`, `UpdateOtDate()`, `DeleteOtDate()`
- **Backend**: `OtDateService.createOtDate()`, `OtDateService.approveOtDate()`, `OtDateService.calculateOtPay()`
- **Validation**: `validateOtTimeRange()`, `validateOtDuration()`, `checkMonthlyOtLimit()`
