package com.example.melaAttendance.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.melaAttendance.R;
import com.example.melaAttendance.database.SoldProductDetailsData;

import java.util.List;

public class HistoryDialogAdapter extends RecyclerView.Adapter<HistoryDialogAdapter.MyViewHolder> {
    Context context;
    List<SoldProductDetailsData> soldProductDetailsDataList;
    final  String TAG = "tag";

    public HistoryDialogAdapter(Context context, List<SoldProductDetailsData> soldProductDetailsDataList) {
        this.context = context;
        this.soldProductDetailsDataList = soldProductDetailsDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.price_list_layout, parent, false);
        return new HistoryDialogAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: "+soldProductDetailsDataList.get(position).getBill_invoice());
        SoldProductDetailsData soldProductDetailsData=(SoldProductDetailsData) soldProductDetailsDataList.get(position);
        for (int i=0; i<soldProductDetailsDataList.size();i++)
        {
            holder.pName.setText(soldProductDetailsData.getProduct_name());
            holder.pQuantity.setText(soldProductDetailsData.getGetProduct_quantity_sold());
            holder.pPrice.setText(String.valueOf(soldProductDetailsData.getUnit_price()));
            holder.total_price.setText(String.valueOf(soldProductDetailsData.getTotal_amount()));
        }
    }

    @Override
    public int getItemCount() {
        return soldProductDetailsDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView pName,pQuantity,pPrice,total_price;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pName = (TextView)itemView.findViewById(R.id.pName);
            pQuantity = (TextView)itemView.findViewById(R.id.pQuantity);
            pPrice = (TextView)itemView.findViewById(R.id.pPrice);
            total_price = (TextView)itemView.findViewById(R.id.tPrice);
        }
    }
}
