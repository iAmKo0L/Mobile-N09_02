# RoomManage - Ứng dụng Quản lý Nhà trọ

Ứng dụng Android giúp chủ nhà trọ quản lý danh sách phòng trọ, thông tin người thuê và tình trạng phòng theo mô hình MVC.

## 🚀 Tính năng chính
- **Quản lý phòng trọ**: Thêm, sửa, xóa thông tin phòng.
- **Thống kê (Dashboard)**: Xem tổng số phòng, phòng trống, phòng đã thuê và doanh thu.
- **Tìm kiếm & Lọc**: Tìm kiếm theo tên phòng/người thuê, lọc theo trạng thái.
- **Sắp xếp**: Sắp xếp danh sách theo giá thuê tăng/giảm dần.
- **Hoàn tác (Undo)**: Khôi phục lại phòng vừa xóa ngay lập tức.
- **Validate**: Kiểm tra dữ liệu nhập vào.

## 📂 Cấu trúc Project (MVC)
- `model/`: Chứa lớp `Room.java` định nghĩa dữ liệu.
- `controller/`: `MainActivity.java` điều khiển luồng ứng dụng và xử lý logic.
- `adapter/`: `RoomAdapter.java` xử lý hiển thị danh sách lên RecyclerView.
- `res/layout/`: Chứa các file giao diện (XML).

## 👥 Phân công nhiệm vụ

### 1. Trần Quang Huy B22DCCN397: UI & Model Designer
- Thiết kế các Layout: `activity_main.xml`, `item_room.xml`, `dialog_room.xml`.
- Xây dựng lớp `Room.java`.
- Tạo các tài nguyên hình ảnh, icon (Vector Drawables).
- Đảm bảo giao diện đồng nhất và responsive.

### 2. Trần Quang Huy B22DCCN398: List & Logic Manager
- Xây dựng `RoomAdapter.java` để hiển thị danh sách.
- Xử lý tính năng **Tìm kiếm (Search)** và **Sắp xếp (Sort)**.
- Xử lý bộ lọc **Filter** theo trạng thái phòng.
- Quản lý việc cập nhật dữ liệu từ `displayList` lên UI.

### 3. Đỗ Đức Cảnh B22DCCN086: CRUD & Feature Developer
- Xây dựng logic **Thêm/Sửa** phòng thông qua Dialog.
- Xử lý logic **Xóa** và tính năng **Hoàn tác (Undo)** bằng Snackbar.
- Viết các hàm **Validate** dữ liệu (SĐT, giá tiền...).
- Xử lý tính toán cho màn hình **Dashboard** (Doanh thu, số lượng phòng).
