# Yêu cầu Hệ thống Phần mềm (SRS)

## Tổng quan
Tài liệu này mô tả yêu cầu chi tiết cho hệ thống Quản lý Nhân viên, bao gồm các chức năng quản lý nhân sự, chấm công, tính lương, và báo cáo.

## 1. Mô tả nghiệp vụ tổng quan

### 1.1 Mục tiêu hệ thống
- Quản lý thông tin nhân viên toàn diện
- Tự động hóa quy trình chấm công và tính lương
- Quản lý khen thưởng và kỷ luật
- Cung cấp báo cáo thống kê cho quản lý
- Đảm bảo bảo mật và phân quyền truy cập

### 1.2 Phạm vi hệ thống
- Quản lý thông tin cá nhân nhân viên
- Quản lý cơ cấu tổ chức (phòng ban, vị trí)
- Chấm công và quản lý thời gian làm việc
- Tính lương và quản lý thu nhập
- Quản lý làm thêm giờ
- Khen thưởng và kỷ luật
- Báo cáo và phân tích

## 2. Yêu cầu chức năng

### 2.1 Quản lý Nhân viên

#### 2.1.1 Quản lý thông tin nhân viên
| Chức năng | Mô tả chi tiết |
|----------|----------------|
| Thêm nhân viên | Nhập thông tin cá nhân, liên hệ, công việc |
| Cập nhật thông tin | Sửa đổi thông tin nhân viên hiện tại |
| Xóa nhân viên | Xóa mềm (không hiển thị) nhân viên |
| Tìm kiếm nhân viên | Tìm theo tên, email, CCCD, phòng ban |
| Xem chi tiết | Hiển thị đầy đủ thông tin nhân viên |

#### 2.1.2 Quy tắc validation
| Trường | Quy tắc | Mô tả |
|-------|--------|-------|
| Họ | Required, 2-50 chars | Bắt buộc, độ dài 2-50 ký tự |
| Tên | Required, 2-50 chars | Bắt buộc, độ dài 2-50 ký tự |
| Email | Required, email format | Bắt buộc, định dạng email hợp lệ |
| SĐT | Required, 9-15 digits | Bắt buộc, 9-15 số |
| CCCD | Required, 9-24 digits | Bắt buộc, 9-24 số |
| Ngày sinh | Required, age 15-70 | Bắt buộc, tuổi từ 15-70 |
| Giới tính | Required, 1-2 | Bắt buộc, 1=Nữ, 2=Nam |
| Tỉnh/Thành phố | Required | Bắt buộc |
| Quận/Huyện | Required | Bắt buộc |
| Địa chỉ | Required, max 200 | Bắt buộc, tối đa 200 ký tự |
| Phòng ban | Required | Bắt buộc |
| Vị trí | Required | Bắt buộc |
| Mã nhân viên | Required, unique | Bắt buộc, duy nhất |
| Lương cơ bản | Required, min 1000000 | Bắt buộc, tối thiểu 1,000,000 VNĐ |
| Phụ cấp | Optional, min 0 | Tùy chọn, tối thiểu 0 VNĐ |
| Trực tiếp | Optional, max 100 | Tùy chọn, tối đa 100 ký tự |
| Mô tả | Optional, max 500 | Tùy chọn, tối đa 500 ký tự |
| Ngày bắt đầu | Required | Bắt buộc |
| Ngày kết thúc | Optional, > start date | Tùy chọn, phải sau ngày bắt đầu |
| Trạng thái | Required, 1-2 | Bắt buộc, 1=Hoạt động, 2=Ngừng hoạt động |

### 2.2 Quản lý Phòng ban & Vị trí

#### 2.2.1 Quản lý Phòng ban
| Chức năng | Mô tả chi tiết |
|----------|----------------|
| Thêm phòng ban | Tạo phòng ban mới với tên và mô tả |
| Cập nhật phòng ban | Sửa đổi thông tin phòng ban |
| Xóa phòng ban | Xóa mềm phòng ban (kiểm tra nhân viên) |
| Xem danh sách | Hiển thị tất cả phòng ban đang hoạt động |

#### 2.2.2 Quản lý Vị trí
| Chức năng | Mô tả chi tiết |
|----------|----------------|
| Thêm vị trí | Tạo vị trí mới thuộc phòng ban |
| Cập nhật vị trí | Sửa đổi thông tin vị trí |
| Xóa vị trí | Xóa mềm vị trí (kiểm tra nhân viên) |
| Xem danh sách | Hiển thị tất cả vị trí đang hoạt động |

#### 2.2.3 Quy tắc validation
| Trường | Quy tắc | Mô tả |
|-------|--------|-------|
| Tên phòng ban | Required, 2-100, unique | Bắt buộc, 2-100 ký tự, duy nhất |
| Mô tả phòng ban | Optional, max 500 | Tùy chọn, tối đa 500 ký tự |
| Trạng thái phòng ban | Required, 1-2 | Bắt buộc, 1=Hoạt động, 2=Ngừng hoạt động |
| Tên vị trí | Required, 2-100, unique | Bắt buộc, 2-100 ký tự, duy nhất |
| Mô tả vị trí | Optional, max 500 | Tùy chọn, tối đa 500 ký tự |
| Phòng ban (vị trí) | Required | Bắt buộc |
| Cấp bậc | Optional, 1-10 | Tùy chọn, 1-10 |
| Trạng thái vị trí | Required, 1-2 | Bắt buộc, 1=Hoạt động, 2=Ngừng hoạt động |

### 2.3 Quản lý Làm thêm giờ

#### 2.3.1 Chức năng OT
| Chức năng | Mô tả chi tiết |
|----------|----------------|
| Đăng ký OT | Nhân viên đăng ký làm thêm giờ |
| Phê duyệt OT | Quản lý phê duyệt/từ chối yêu cầu |
| Cập nhật OT | Sửa đổi thông tin đăng ký |
| Xóa OT | Xóa yêu cầu OT |
| Xem lịch sử | Hiển thị lịch sử OT theo tháng |

#### 2.3.2 Quy tắc validation
| Trường | Quy tắc | Mô tả |
|-------|--------|-------|
| Ngày làm việc | Required, not future | Bắt buộc, không phải ngày tương lai |
| Giờ bắt đầu | Required, valid time | Bắt buộc, giờ hợp lệ |
| Giờ kết thúc | Required, > start time | Bắt buộc, sau giờ bắt đầu |
| Loại OT | Required, 1-3 | Bắt buộc, 1=Ngày thường, 2=Cuối tuần, 3=Ngày lễ |
| Trạng thái | Required, 1-3 | Bắt buộc, 1=Chờ duyệt, 2=Đã duyệt, 3=Từ chối |
| Thời lượng | Min 1 hour | Tối thiểu 1 tiếng |

#### 2.3.3 Quy tắc tính toán OT
| Loại OT | Hệ số lương | Mô tả |
|--------|------------|-------|
| Ngày thường | 1.5x | 150% lương giờ |
| Cuối tuần | 2.0x | 200% lương giờ |
| Ngày lễ | 3.0x | 300% lương giờ |

### 2.4 Quản lý Khen thưởng & Kỷ luật

#### 2.4.1 Chức năng Reward/Penalty
| Chức năng | Mô tả chi tiết |
|----------|----------------|
| Thêm khen thưởng | Tạo quyết định khen thưởng nhân viên |
| Thêm kỷ luật | Tạo quyết định kỷ luật nhân viên |
| Cập nhật quyết định | Sửa đổi thông tin quyết định |
| Xóa quyết định | Xóa quyết định (kiểm tra lương) |
| Xem lịch sử | Hiển thị lịch sử theo khoảng thời gian |

#### 2.4.2 Quy tắc validation
| Trường | Quy tắc | Mô tả |
|-------|--------|-------|
| Nhân viên | Required | Bắt buộc |
| Loại | Required, REWARD/PENALTY | Bắt buộc, REWARD hoặc PENALTY |
| Số tiền | Required, min 1000 | Bắt buộc, tối thiểu 1,000 VNĐ |
| Lý do | Required, max 500 | Bắt buộc, tối đa 500 ký tự |
| Ngày | Required, not future | Bắt buộc, không phải ngày tương lai |
| Người duyệt | Required | Bắt buộc |

#### 2.4.3 Quy tắc tính toán
| Loại | Giới hạn | Mô tả |
|------|---------|-------|
| Khen thưởng | Max 50% lương tháng | Tối đa 50% lương tháng |
| Kỷ luật | Max 30% lương tháng | Tối đa 30% lương tháng |
| Số lần/tháng | Max 5 lần | Tối đa 5 quyết định mỗi tháng |

### 2.5 Xác thực & Phân quyền

#### 2.5.1 Xác thực
| Chức năng | Mô tả chi tiết |
|----------|----------------|
| Đăng nhập | Đăng nhập với username/password |
| Đăng xuất | Đăng xuất và xóa token |
| Làm mới token | Tự động làm mới access token |
| Quên mật khẩu | Khôi phục mật khẩu (nếu có) |

#### 2.5.2 Phân quyền
| Quyền | Mô tả | Scope |
|------|-------|-------|
| EMPLOYEE_VIEW | Xem danh sách nhân viên | Read |
| EMPLOYEE_CREATE | Tạo nhân viên mới | Write |
| EMPLOYEE_UPDATE | Cập nhật thông tin | Write |
| EMPLOYEE_DELETE | Xóa nhân viên | Delete |
| OT_VIEW | Xem thông tin OT | Read |
| OT_CREATE | Tạo yêu cầu OT | Write |
| OT_APPROVE | Phê duyệt OT | Manager |
| REWARD_PENALTY_VIEW | Xem khen thưởng/kỷ luật | Read |
| REWARD_PENALTY_CREATE | Tạo quyết định | Write |
| DEPARTMENT_VIEW | Xem phòng ban | Read |
| DEPARTMENT_MANAGE | Quản lý phòng ban | Admin |
| POSITION_VIEW | Xem vị trí | Read |
| POSITION_MANAGE | Quản lý vị trí | Admin |

#### 2.5.3 Quy tắc bảo mật
| Yếu tố | Quy tắc | Mô tả |
|--------|--------|-------|
| Username | 4-128 chars, không "admin" | Độ dài 4-128, không chứa "admin" |
| Password | 6+ chars | Tối thiểu 6 ký tự |
| Access Token | 15 minutes expiry | Hết hạn sau 15 phút |
| Refresh Token | 7 days expiry | Hết hạn sau 7 ngày |
| Session | Stateless | Không lưu trạng thái session |

## 3. Yêu cầu phi chức năng

### 3.1 Yêu cầu hiệu năng
| Yếu tố | Yêu cầu | Mô tả |
|--------|---------|-------|
| Response time | < 2 seconds | Thời gian phản hồi < 2 giây |
| Concurrent users | 100+ | Hỗ trợ 100+ người dùng đồng thởi |
| Database | MySQL 8.0+ | CSDL MySQL phiên bản 8.0 trở lên |
| Memory | 4GB+ | Bộ nhớ tối thiểu 4GB |

### 3.2 Yêu cầu bảo mật
| Yếu tố | Yêu cầu | Mô tả |
|--------|---------|-------|
| Authentication | JWT-based | Xác thực JWT |
| Authorization | Role-based | Phân quyền theo vai trò |
| Data encryption | HTTPS | Mã hóa dữ liệu truyền |
| Password | BCrypt | Mã hóa mật khẩu |

### 3.3 Yêu cầu khả dụng
| Yếu tố | Yêu cầu | Mô tả |
|--------|---------|-------|
| Uptime | 99.5% | Thời gian hoạt động 99.5% |
| Browser support | Chrome, Firefox, Edge | Hỗ trợ các trình duyệt phổ biến |
| Mobile | Responsive | Tương thích thiết bị di động |
| Language | Tiếng Việt | Giao diện tiếng Việt |

### 3.4 Yêu cầu dữ liệu
| Yếu tố | Yêu cầu | Mô tả |
|--------|---------|-------|
| Backup | Daily | Sao lưu hàng ngày |
| Retention | 7 years | Lưu trữ 7 năm |
| Export | CSV/Excel | Xuất báo cáo CSV/Excel |
| Import | CSV | Nhập dữ liệu từ CSV |

## 4. Ràng buộc kỹ thuật

### 4.1 Công nghệ frontend
| Công nghệ | Phiên bản | Mô tả |
|----------|---------|-------|
| React | 18.x | Library frontend |
| Ant Design | 5.x | UI Component Library |
| Redux | 4.x | State Management |
| Axios | 1.x | HTTP Client |
| Day.js | 1.x | Date/Time Library |

### 4.2 Công nghệ backend
| Công nghệ | Phiên bản | Mô tả |
|----------|---------|-------|
| Spring Boot | 3.x | Backend Framework |
| Spring Security | 6.x | Security Framework |
| Spring Data JPA | 3.x | Data Access |
| MySQL | 8.0+ | Database |
| JWT | 0.11.x | Token Authentication |

### 4.3 Ràng buộc triển khai
| Yếu tố | Yêu cầu | Mô tả |
|--------|---------|-------|
| Server | Linux/Windows | Hệ điều hành |
| Java | 17+ | Java Runtime |
| Memory | 8GB+ | Bộ nhớ server |
| Storage | 100GB+ | Ổ lưu trữ |

## 5. Yêu cầu báo cáo

### 5.1 Báo cáo nhân sự
| Báo cáo | Nội dung | Tần suất |
|---------|---------|---------|
| Danh sách nhân viên | Thông tin cơ bản, trạng thái | Theo yêu cầu |
| Biến động nhân sự | Nhân viên mới, nghỉ việc | Hàng tháng |
| Tuổi nhân viên | Phân tích độ tuổi | Hàng quý |
| Lương nhân viên | Phân tích lương | Hàng tháng |

### 5.2 Báo cáo chấm công
| Báo cáo | Nội dung | Tần suất |
|---------|---------|---------|
| Chấm công hàng ngày | Thời gian làm việc | Hàng ngày |
| Chấm công hàng tháng | Tổng hợp tháng | Hàng tháng |
| OT hàng tháng | Thời gian làm thêm | Hàng tháng |
| Vi phạm kỷ luật | Các vi phạm | Hàng tháng |

### 5.3 Báo cáo lương
| Báo cáo | Nội dung | Tần suất |
|---------|---------|---------|
| Bảng lương tháng | Chi tiết lương | Hàng tháng |
| Tổng quỹ lương | Tổng chi lương | Hàng tháng |
| Phân tích lương | So sánh, xu hướng | Hàng quý |

## 6. Yêu cầu tích hợp

### 6.1 Tích hợp hệ thống
| Hệ thống | Loại tích hợp | Mô tả |
|----------|--------------|-------|
| Email | Notification | Gửi thông báo email |
| File Storage | Document | Lưu trữ tài liệu |
| Backup | Data | Sao lưu dữ liệu |
| Logging | Audit | Ghi log hoạt động |

### 6.2 API tích hợp
| API | Mô tả |
|-----|-------|
| REST API | Giao diện RESTful |
| JSON Format | Định dạng JSON |
| Authentication | Bearer Token |
| Error Handling | HTTP Status Codes |
    }
}
```

### 6.2 Error Response Format
```json
{
    "status": 400,
    "message": "Validation failed",
    "errors": [
        {
            "field": "firstName",
            "message": "First name is required"
        }
    ]
}
```

## 7. Pagination and Search

### 7.1 Pagination Parameters
- `page`: Page number (default: 0)
- `size`: Page size (default: 10)
- `keyword`: Search term for text search

### 7.2 Supported Search Operations
- Employee search by name, email, phone
- Department search by name
- Position search by name
- OT date search by date range

## 8. Data Integrity Constraints

### 8.1 Unique Constraints
- Username must be unique
- Email must be unique
- Employee code must be unique

### 8.2 Foreign Key Constraints
- Position must exist when assigned to employee
- Department must exist when assigned to employee
- User must be linked to valid employee

### 8.3 Soft Delete
- All entities use soft delete via `isDeleted` flag
- Audit fields: `createAt`, `updateAt`, `version`

## 9. Internationalization

### 9.1 Supported Languages
- Vietnamese (primary)
- English (secondary)

### 9.2 Translation Keys
All UI text uses translation keys with `useTranslation()` hook:
- `employee.form.firstNameRequired`
- `account.validationEmailRequired`
- `common.save`, `common.cancel`

## 10. Performance Requirements

### 10.1 Response Times
- API responses: < 2 seconds
- Page loads: < 3 seconds
- Search results: < 1 second

### 10.2 Pagination Limits
- Maximum page size: 100
- Default page size: 10
- Search result limit: 1000 records

This SRS document provides the foundation for understanding the current system architecture and data requirements for future development and maintenance.