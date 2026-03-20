package com.example.roommanage.controller;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommanage.R;
import com.example.roommanage.adapter.RoomAdapter;
import com.example.roommanage.model.Room;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RoomAdapter.OnItemClickListener {

    private List<Room> roomList;
    private List<Room> displayList; // List actually shown in RecyclerView
    private RoomAdapter adapter;
    private RecyclerView rvRooms;
    private FloatingActionButton fabAdd;
    private SearchView searchView;
    private ImageButton btnSort, btnFilter;
    
    // Dashboard views
    private TextView tvTotalRooms, tvRentedRooms, tvAvailableRooms, tvTotalRevenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initData();
        setupRecyclerView();
        setupSearch();
        setupSortAndFilter();
        updateDashboard();

        fabAdd.setOnClickListener(v -> showRoomDialog(null, -1));
    }

    private void initViews() {
        rvRooms = findViewById(R.id.rvRooms);
        fabAdd = findViewById(R.id.fabAdd);
        searchView = findViewById(R.id.searchView);
        btnSort = findViewById(R.id.btnSort);
        btnFilter = findViewById(R.id.btnFilter);
        
        tvTotalRooms = findViewById(R.id.tvTotalRooms);
        tvRentedRooms = findViewById(R.id.tvRentedRooms);
        tvAvailableRooms = findViewById(R.id.tvAvailableRooms);
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
    }

    private void initData() {
        roomList = new ArrayList<>();
        roomList.add(new Room("R01", "Phòng 101", 1500000.0, false, "", ""));
        roomList.add(new Room("R02", "Phòng 102", 2000000.5, true, "Nguyễn Văn A", "0987654321"));
        displayList = new ArrayList<>(roomList);
    }

    private void setupRecyclerView() {
        adapter = new RoomAdapter(displayList, this);
        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        rvRooms.setAdapter(adapter);
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void setupSortAndFilter() {
        btnSort.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenu().add("Giá tăng dần");
            popup.getMenu().add("Giá giảm dần");
            popup.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("Giá tăng dần")) {
                    Collections.sort(displayList, (r1, r2) -> Double.compare(r1.getPrice(), r2.getPrice()));
                } else {
                    Collections.sort(displayList, (r1, r2) -> Double.compare(r2.getPrice(), r1.getPrice()));
                }
                adapter.notifyDataSetChanged();
                return true;
            });
            popup.show();
        });

        btnFilter.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenu().add("Tất cả");
            popup.getMenu().add("Còn trống");
            popup.getMenu().add("Đã thuê");
            popup.setOnMenuItemClickListener(item -> {
                String title = item.getTitle().toString();
                displayList.clear();
                if (title.equals("Còn trống")) {
                    for (Room r : roomList) if (!r.isRented()) displayList.add(r);
                } else if (title.equals("Đã thuê")) {
                    for (Room r : roomList) if (r.isRented()) displayList.add(r);
                } else {
                    displayList.addAll(roomList);
                }
                adapter.updateData(new ArrayList<>(displayList));
                return true;
            });
            popup.show();
        });
    }

    private void updateDashboard() {
        int total = roomList.size();
        int rented = 0;
        double revenue = 0;
        for (Room r : roomList) {
            if (r.isRented()) {
                rented++;
                revenue += r.getPrice();
            }
        }
        tvTotalRooms.setText("Tổng: " + total);
        tvRentedRooms.setText("Đã thuê: " + rented);
        tvAvailableRooms.setText("Trống: " + (total - rented));
        tvTotalRevenue.setText("Doanh thu: " + String.format("%.0f", revenue) + " VNĐ");
    }

    private void showRoomDialog(Room room, int positionInDisplay) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_room, null);
        builder.setView(dialogView);

        EditText etRoomId = dialogView.findViewById(R.id.etRoomId);
        EditText etRoomName = dialogView.findViewById(R.id.etRoomName);
        EditText etRoomPrice = dialogView.findViewById(R.id.etRoomPrice);
        CheckBox cbIsRented = dialogView.findViewById(R.id.cbIsRented);
        EditText etTenantName = dialogView.findViewById(R.id.etTenantName);
        EditText etPhoneNumber = dialogView.findViewById(R.id.etPhoneNumber);

        boolean isEdit = (room != null);
        if (isEdit) {
            builder.setTitle("Sửa thông tin phòng");
            etRoomId.setText(room.getId());
            etRoomId.setEnabled(false);
            etRoomName.setText(room.getName());
            etRoomPrice.setText(String.valueOf(room.getPrice()));
            cbIsRented.setChecked(room.isRented());
            etTenantName.setText(room.getTenantName());
            etPhoneNumber.setText(room.getPhoneNumber());
        } else {
            builder.setTitle("Thêm phòng mới");
        }

        builder.setPositiveButton(isEdit ? "Cập nhật" : "Thêm", null);
        builder.setNegativeButton("Hủy", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String id = etRoomId.getText().toString().trim();
            String name = etRoomName.getText().toString().trim();
            String priceStr = etRoomPrice.getText().toString().trim();
            boolean isRented = cbIsRented.isChecked();
            String tenantName = etTenantName.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();

            if (validateData(id, name, priceStr, isRented, phoneNumber)) {
                double price = Double.parseDouble(priceStr);
                if (isEdit) {
                    room.setName(name);
                    room.setPrice(price);
                    room.setRented(isRented);
                    room.setTenantName(tenantName);
                    room.setPhoneNumber(phoneNumber);
                } else {
                    Room newRoom = new Room(id, name, price, isRented, tenantName, phoneNumber);
                    roomList.add(newRoom);
                }
                refreshList();
                updateDashboard();
                dialog.dismiss();
            }
        });
    }

    private void refreshList() {
        displayList.clear();
        displayList.addAll(roomList);
        adapter.updateData(new ArrayList<>(displayList));
    }

    private boolean validateData(String id, String name, String priceStr, boolean isRented, String phoneNumber) {
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ ID, Tên và Giá", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isRented) {
            if (!phoneNumber.startsWith("0") || phoneNumber.length() != 10) {
                Toast.makeText(this, "SĐT phải bắt đầu bằng 0 và có 10 số", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onItemClick(Room room, int position) {
        showRoomDialog(room, position);
    }

    @Override
    public void onItemLongClick(Room room, int position) {
        Room deletedRoom = displayList.get(position);
        int originalIndex = roomList.indexOf(deletedRoom);

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa " + deletedRoom.getName() + " không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    roomList.remove(deletedRoom);
                    refreshList();
                    updateDashboard();

                    Snackbar.make(rvRooms, "Đã xóa " + deletedRoom.getName(), Snackbar.LENGTH_LONG)
                            .setAction("Hoàn tác", v -> {
                                roomList.add(originalIndex, deletedRoom);
                                refreshList();
                                updateDashboard();
                            }).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
