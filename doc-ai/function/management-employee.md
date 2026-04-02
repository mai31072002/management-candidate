TÀI LIỆU THIẾT KẾ CHI TIẾT: MODULE DOC-AI  
Mục tiêu: Quản lý nhân viên

1. Luồng sử lý:
  1. Thêm nhân viên mới nhập dữ liệu qua from hoặc gửi file excel với các trường của nhân viên
  2. **Upload:** Frontend gửi file (PDF/Image) lên Backend.
  3. **Storage:** Backend lưu tạm vào bộ nhớ hoặc AWS S3.
  4. **Extraction:** Backend gọi một dịch vụ AI (Google Document AI, OpenAI Vision, hoặc Azure Form Recognizer) để trích xuất dữ liệu thô.
  5. **Mapping:** Hệ thống khớp dữ liệu trích xuất vào các trường của `EmployeeEntity`.
  6. **Review:** Trả kết quả về Frontend để HR kiểm tra lại trước khi lưu chính thức.
2. Backend Design:
  1. Đã có API:
    1. Tạo nhân viên: /api/users
    2. Lấy danh sách nhân viên: /api/users?page=0&size=10
    3. Tìm kiếm nhân viên theo searchUsername 
    4. Cập nhật nhân viên chưa hoàn thành vì mới chỉ có cập nhật trong mỗi bảng user các bảng liên quan chưa cập nhật
  2. Yêu cầu: 
    1. Viết API xóa nhân viên xóa mềm chuyển isDeleted = true mà không xóa luôn khỏi DB
    2. Viết lại logic cập nhật nhân viên Khi cập nhật các bảng liên quan như DtbEmployees, DtbPosition, DtbDepartment
    3. Viết hàm sử lý khi frontend gửi file excel lên với các trường trong user, employee, position, department để thêm nhân viên mới có thể upload được nhiều file Lưu ý là phải check trùng lặp nếu DB đã có username, cccd, email, mã nhân viên rồi thì không thêm nữa báo lỗi
3. Frontend Design:
  1. Đã có mà quản lý nhân viên: nằm trong folder: /employee-frontend/src/app/main/home
  2. Yêu cầu: check lại logic xem có bị bug chỗ nào không và dựa vào code backend mà chỉnh lại frontend
4. Các vấn đề logic cần AI quét:  


