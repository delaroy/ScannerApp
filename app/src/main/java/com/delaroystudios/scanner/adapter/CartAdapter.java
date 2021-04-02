package com.delaroystudios.scanner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delaroystudios.scanner.R;
import com.delaroystudios.scanner.database.ScannerEntity;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ScannerViewHolder> {

    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;
    private List<ScannerEntity> scannerEntities;
    private Context mContext;

    public CartAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    @NonNull
    @Override
    public ScannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.cart_item, parent, false);

        return new ScannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScannerViewHolder holder, int position) {
        // Determine the values of the wanted data
        ScannerEntity scannerEntity = scannerEntities.get(position);
        String name = scannerEntity.getTitle();
        int qty = scannerEntity.getQuantity();
        int price = scannerEntity.getPrice();

        //Set values
        holder.m_title.setText(name);
        holder.qty.setText(String.valueOf(qty));
        holder.m_price.setText(String.valueOf(price));
    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (scannerEntities == null) {
            return 0;
        }
        return scannerEntities.size();
    }

    public List<ScannerEntity> getScannerEntities() {
        return scannerEntities;
    }

    public void setScannerEntities(List<ScannerEntity> mscannerEntities) {
        scannerEntities = mscannerEntities;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        scannerEntities.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(ScannerEntity item, int position) {
        scannerEntities.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public interface ItemClickListener {
        void onItemClickListener(String sku);
    }

    // Inner class for creating ViewHolders
    public class ScannerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView m_title;
        TextView qty;
        TextView m_price;

        ScannerViewHolder(View itemView) {
            super(itemView);

            m_title = itemView.findViewById(R.id.m_title);
            qty = itemView.findViewById(R.id.qty);
            m_price = itemView.findViewById(R.id.m_price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String sku = scannerEntities.get(getAdapterPosition()).getSku();
            mItemClickListener.onItemClickListener(sku);
        }
    }
}
