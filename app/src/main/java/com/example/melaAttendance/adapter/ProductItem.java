package com.example.melaAttendance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.melaAttendance.R;
import com.example.melaAttendance.database.ShgMemberDetailsData;

import java.util.List;

public class ProductItem extends RecyclerView.Adapter<ProductItem.ViewHolder> {

    private List<ShgMemberDetailsData> productPojos;
    private Context context;

    public ProductItem(List<ShgMemberDetailsData> productPojos, Context context) {
        this.productPojos = productPojos;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductItem.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_data, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductItem.ViewHolder holder, int position) {
        ShgMemberDetailsData productPojo = productPojos.get(position);
        holder.membr_name.setText( productPojo.getMember_name() );
        holder.membr_mobile.setText(productPojo.getMobile());
        holder.ass_name.setText(productPojo.getAss_name());
        holder.ass_mobile.setText(productPojo.getAss_mobile());
        holder.textView1.setText(productPojo.getState_name());
        holder.textView2.setText(productPojo.getVillage_name());
        holder.textView3.setText(productPojo.getGrampanchayat_name());
        holder.textView4.setText(productPojo.getProduct_name());
        holder.textView5.setText(productPojo.getCategory_name());
    }

    @Override
    public int getItemCount() {
        return productPojos.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView membr_name;
        public TextView membr_mobile;
        public TextView ass_name;
        public TextView ass_mobile;
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;
        public TextView textView4;
        public TextView textView5;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            membr_name=(TextView) itemView.findViewById(R.id.membr_name);
            membr_mobile=(TextView)itemView.findViewById(R.id.membr_mobile);
            ass_name=(TextView) itemView.findViewById(R.id.ass_name);
            ass_mobile=(TextView)itemView.findViewById(R.id.ass_mobile) ;
            textView1 = (TextView) itemView.findViewById(R.id.textView1);
            textView2 = (TextView) itemView.findViewById(R.id.textView2);
            textView3=(TextView) itemView.findViewById(R.id.textView3);
            textView4=(TextView) itemView.findViewById(R.id.textView4);
            textView5=(TextView) itemView.findViewById(R.id.textView5);
        }
    }
}
