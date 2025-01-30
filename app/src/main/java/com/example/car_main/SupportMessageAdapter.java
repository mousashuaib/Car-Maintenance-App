package com.example.car_main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SupportMessageAdapter extends ArrayAdapter<SupportMessage> {
    public SupportMessageAdapter(@NonNull Context context, ArrayList<SupportMessage> messages) {
        super(context, 0, messages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.support_message_item, parent, false);
        }

        SupportMessage message = getItem(position);

        TextView name = convertView.findViewById(R.id.name);
        TextView email = convertView.findViewById(R.id.email);
        TextView phone = convertView.findViewById(R.id.phone);
        TextView subject=convertView.findViewById(R.id.subject);
        TextView msg = convertView.findViewById(R.id.message);
        TextView createdAt = convertView.findViewById(R.id.created_at);

        name.setText(message.getName());
        email.setText(message.getEmail());
        phone.setText(message.getPhone());
        msg.setText(message.getMessage());
        createdAt.setText(message.getCreatedAt());

        return convertView;
    }
}
