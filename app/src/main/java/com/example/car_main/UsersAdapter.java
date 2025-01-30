package com.example.car_main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.BiConsumer;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<User> userList;
    private BiConsumer<Integer, String> onRoleChange;

    public UsersAdapter(List<User> userList, BiConsumer<Integer, String> onRoleChange) {
        this.userList = userList;
        this.onRoleChange = onRoleChange;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvTitle.setText(user.getFirstName() + " " + user.getLastName());
        holder.tvSubtitle.setText(user.getEmail() + " - " + user.getUserType());

        holder.itemView.setOnClickListener(v -> {
            String newRole = user.getUserType().equals("normal") ? "admin" : "normal";
            onRoleChange.accept(user.getId(), newRole);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSubtitle;

        public UserViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(android.R.id.text1);
            tvSubtitle = itemView.findViewById(android.R.id.text2);
        }
    }
}
