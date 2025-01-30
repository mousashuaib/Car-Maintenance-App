package com.example.car_main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {
    private Context context; // Context of the application
    private List<Offer> offers; // List of Offer items

    public OfferAdapter(Context context, List<Offer> offers) {
        this.context = context;
        this.offers = offers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offer, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Offer offer = offers.get(position); // Get the offer at the current position
        TextView title = holder.cardView.findViewById(R.id.offer_title); // Get the TextView for the title
        TextView description = holder.cardView.findViewById(R.id.offer_description); // Get the TextView for description
        TextView expiryDate = holder.cardView.findViewById(R.id.offer_expiry); // Get the TextView for expiry date

        title.setText(offer.getTitle());
        description.setText(offer.getDescription());
        expiryDate.setText("Expires on: " + offer.getExpiryDate());
    }

    @Override
    public int getItemCount() {return offers.size();}

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView; // Initialize cardView
        }
    }
}