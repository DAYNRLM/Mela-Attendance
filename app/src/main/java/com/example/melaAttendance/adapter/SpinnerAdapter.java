package com.example.melaAttendance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.melaAttendance.R;
import com.example.melaAttendance.model.ProductPojo;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<ProductPojo> {
    ArrayList<ProductPojo> productPojoArrayList = new ArrayList<>();


    public SpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, ArrayList<ProductPojo> productPojoArrayList) {
        super(context, resource, textViewResourceId);
        this.productPojoArrayList = productPojoArrayList;
    }

    @Override
    public int getCount() {
        return productPojoArrayList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.spinner_custom_layout, null);
        TextView textView = (TextView) v.findViewById(R.id.tvCustomSpinner);
        textView.setText(productPojoArrayList.get(position).getProduct_name());
        return v;
    }
}
