**TÀI LIỆU ĐẶC TẢ YÊU CẦU PHẦN MỀM (SRS)**  
**HỆ THỐNG QUẢN LÝ NHÂN VIÊN**  
**Phiên bản: 1.0 (Trình duyệt Web)**  
**Ngày: 12/03/2026**

## 1. MỤC TIÊU CỦA HỆ THỐNG

Hệ thống được xây dựng nhằm:

- Quản lý tập trung thông tin nhân viên, phân quyền và quyền hạn.
- Tối ưu hóa quy trình chấm công, OT, nghỉ phép, lương thưởng/phạt.
- Hỗ trợ báo cáo nhanh chóng, chính xác cho quản lý cấp cao.
- Đảm bảo bảo mật cao với JWT và phân quyền chi tiết.
- Hoạt động ổn định với nhiều người dùng đồng thời (quản lý, HR, nhân viên).

## 2. PHẠM VI HỆ THỐNG

Hệ thống là website nội bộ (có thể mở rộng truy cập mobile), cho phép:

- HR/Quản lý: Thêm/sửa/xóa nhân viên, phê duyệt yêu cầu, báo cáo.
- Quản lý bộ phận: Xem dữ liệu nhân viên dưới quyền, phê duyệt nghỉ phép/OT.
- Nhân viên: Xem thông tin cá nhân, chấm công, yêu cầu nghỉ phép/OT, lịch sử lương.

## 3. CÁC CHỨC NĂNG CHÍNH CỦA HỆ THỐNG

## 3.1 Quản lý phân quyền (Security + JWT)

Hệ thống hỗ trợ:

- Cấu trúc: user → user-role → role → role-permission → permission.
- Đăng nhập JWT token (refresh token tự động).
- Phân quyền chi tiết: xem/thêm/sửa/xóa theo module (nhân viên, chấm công, lương...).

**📌 Gợi ý:** 

- Role mặc định: Admin (toàn quyền), HR Manager, Department Manager, Employee.
- Audit log: Ghi lại mọi thay đổi quyền hạn.

## 3.2 Quản lý nhân viên

Hệ thống cho phép:
    
- Thêm/sửa/xóa nhân viên (liên kết user → employee).
- Thông tin chi tiết: position → level, department, manager.
- Mỗi nhân viên tự động tạo tài khoản user với quyền mặc định.

**Thông tin bắt buộc khi tạo nhân viên:**

- Họ tên, email, số điện thoại, địa chỉ, cccd, giới tính, ngày sinh, Lương cơ bản, Lương Thưởng 
- Bộ phận (department), vị trí (position), cấp bậc (level).
- Manager trực tiếp.
- Ngày vào làm, lương cơ bản.

**📌 Gợi ý:**

- Import nhân viên từ Excel hàng loạt.
- Lịch sử thay đổi vị trí/công tác.

## 3.3 Chấm công và OT

Dựa trên bảng timeKeeping, ot-day:

- Chấm công tự động: đi sớm/về muộn (check-in/out qua web/mobile/inport từ mẫu excel mặc định).
- Tính OT: giờ OT ngày thường, cuối tuần, lễ Tết.
- Báo cáo chấm công theo tuần/tháng.

**📌 Gợi ý:**

- Tích hợp GPS hoặc QR code cho chấm công tại chỗ.
- Cảnh báo vi phạm (đi muộn >3 lần/tháng).

## 3.4 Quản lý nghỉ phép (Workflow phê duyệt)

Quy trình: Nhân viên request → Manager duyệt cấp 1 → HR duyệt cấp 2.

- Loại phép: phép năm, phép ốm, phép cá nhân.
- Số ngày phép còn lại tự động tính (dựa ngày vào làm).
- Trạng thái: Draft → Submitted → Under Review → Approved/Rejected.

**📌 Gợi ý:**

- Thông báo email/SMS khi được duyệt/từ chối.
- Lịch nghỉ phép hiển thị calendar chung bộ phận.

## 3.5 Quản lý lương thưởng/phạt

Dựa trên salaries, DtbRewardPenalty:

- Tính lương: lương cơ bản + OT + thưởng - phạt.
- Phiếu thưởng/phạt: mô tả lý do, số tiền.
- Báo cáo lương tháng/quý (export Excel/PDF).

**📌 Gợi ý:**

- Tích hợp bảo hiểm (BHXH, BHYT).
- Lương thử việc tự động tính 85% lương chính thức.

## 3.6 Tuyển dụng và thôi việc (Mở rộng thực tế)

- Đăng tin tuyển dụng, quản lý CV ứng viên.
- Quy trình onboard mới: tạo user, phân quyền.
- Xử lý thôi việc: khóa tài khoản, thanh toán lương cuối.

## 3.7 Đào tạo và đánh giá (Mở rộng thực tế)

- Lập kế hoạch đào tạo, theo dõi tham gia.
- Đánh giá hiệu suất hàng quý (KPI), liên kết thưởng/phạt.

## 3.8 Báo cáo và thống kê

- Số lượng nhân viên theo bộ phận/cấp bậc.
- Tỷ lệ nghỉ phép, OT trung bình.
- Doanh thu/lương theo tháng (nếu tích hợp).
- Dashboard realtime với biểu đồ.

**📌 Gợi ý:**

- Export báo cáo tùy chỉnh (PDF/Excel).
- Dự báo nghỉ việc dựa trên dữ liệu chấm công.

## 3.9 Hệ thống ổn định và mở rộng

- EntityBase: createAt, updateAt, version, isDelete (soft delete).
- Upload file lớn (CV, hợp đồng) không gián đoạn.
- Backup dữ liệu định kỳ, hỗ trợ >1 năm dữ liệu.

**📌 Gợi ý:**

- Lưu trữ cloud (AWS S3) cho file đính kèm.
- Dung lượng mở rộng tự động.

## 4. YÊU CẦU BẢO MẬT

- Đăng nhập JWT, 2FA (email/SMS).
- Phân quyền granular theo role-permission.
- Log lịch sử truy cập/thay đổi dữ liệu.
- Mã hóa dữ liệu nhạy cảm (lương, CCCD).

**📌 Gợi ý:**

- Tích hợp LDAP/AD nếu công ty có hệ thống tài khoản hiện tại.
- Watermark trên báo cáo lương khi export.

## 5. NHỮNG VẤN ĐỀ KHÁC

- **Tích hợp:** Có cần kết nối hệ thống kế toán/chấm công máy?
- **Chống sao chép:** Giới hạn export dữ liệu nhạy cảm.
- **Đa ngôn ngữ:** Hỗ trợ tiếng Việt + Anh.
- **Mobile:** App PWA cho chấm công/OT.
- **File mẫu:** Cung cấp template Excel import nhân viên.

## 6. ĐỀ XUẤT CHIẾN LƯỢC TRIỂN KHAI

| Sprint   | Chức năng chính                       | Thời gian   | Ưu tiên         |
| -------- | ------------------------------------- | ----------- | -------         |
| Sprint 1 | Đăng nhập JWT + Phân quyền            | 27/03-09/04 | ⭐⭐⭐⭐⭐    |
| Sprint 2 | Quản lý nhân viên CRUD + Import Excel | 10/04-23/04 | ⭐⭐⭐⭐⭐    |
| Sprint 3 | Chấm công + OT (check-in/out)         | 24/04-07/05 | ⭐⭐⭐⭐       |
| Sprint 4 | Nghỉ phép workflow (2 cấp duyệt)      | 08/05-21/05 | ⭐⭐⭐⭐       |
| Sprint 5 | Lương thưởng/phạt + Báo cáo cơ bản    | 22/05-04/06 | ⭐⭐⭐          |
| Sprint 6 | Dashboard + Export Excel/PDF          | 05/06-18/06 | ⭐⭐⭐          |

--> Tiếp theo đọc /doc-ai/backend và /doc-ai/fronend