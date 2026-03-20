package com.example.roommanage.adapter;



import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.roommanage.R;
import com.example.roommanage.model.Room;
import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> implements Filterable {

    private List<Room> roomList;
    private List<Room> roomListFull; // For filtering
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Room room, int position);
        void onItemLongClick(Room room, int position);
    }

    public RoomAdapter(List<Room> roomList, OnItemClickListener listener) {
        this.roomList = roomList;
        this.roomListFull = new ArrayList<>(roomList);
        this.listener = listener;
    }

    public void updateData(List<Room> newList) {
        this.roomList = newList;
        this.roomListFull = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.tvRoomName.setText(room.getName());
        holder.tvRoomPrice.setText(String.format("Giá: %s VNĐ", room.getPrice()));

        if (room.isRented()) {
            holder.tvRoomStatus.setText("Trạng thái: Đã thuê (" + room.getTenantName() + ")");
            holder.tvRoomStatus.setTextColor(Color.RED);
        } else {
            holder.tvRoomStatus.setText("Trạng thái: Còn trống");
            holder.tvRoomStatus.setTextColor(Color.GREEN);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(room, position));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClick(room, position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    @Override
    public Filter getFilter() {
        return roomFilter;
    }

    private Filter roomFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Room> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(roomListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Room item : roomListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern) ||
                            item.getTenantName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            roomList.clear();
            roomList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvRoomPrice, tvRoomStatus;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvRoomPrice = itemView.findViewById(R.id.tvRoomPrice);
            tvRoomStatus = itemView.findViewById(R.id.tvRoomStatus);
        }
    }
}
