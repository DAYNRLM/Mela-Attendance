package com.example.melaAttendance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.melaAttendance.R;
import com.example.melaAttendance.model.HistoryJson;
import com.example.melaAttendance.utils.ProjectPrefrences;

import java.util.List;

public class HistoryJsonAdpt extends RecyclerView.Adapter<HistoryJsonAdpt.ViewHolder> {
    private List<HistoryJson> historyJsons;
    private Context context;

    public HistoryJsonAdpt(List<HistoryJson>historyJsons, Context context)
    {
        this.historyJsons=historyJsons;
        this.context=context;
    }

    @NonNull
    @Override
    public HistoryJsonAdpt.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.historyjsonview,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryJsonAdpt.ViewHolder holder, int position) {
        HistoryJson historyJson=historyJsons.get(position);
        holder.paymentMode.setText("Cash/Card/POS");
        holder.invoiceNo.setText(historyJson.getInvoice_No());
        holder.shgregId.setText(""+historyJson.getShg_reg_Id());
        holder.bill_No.setText(historyJson.getBill_No());
        holder.stallNo.setText(ProjectPrefrences.getInstance().getSharedPrefrencesData("stall",context));
        holder.created_date.setText(historyJson.getCreatedDate());
        holder.totalAmountTVH.setText(historyJson.getTotalAmount());
    }

    @Override
    public int getItemCount() {
        return historyJsons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView paymentMode,invoiceNo,shgregId,bill_No,totalAmountTVH,stallNo,created_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            paymentMode=(TextView) itemView.findViewById(R.id.payment_mode);
            invoiceNo=(TextView) itemView.findViewById(R.id.invoice_no);
            shgregId=(TextView) itemView.findViewById(R.id.shg_reg_id);
            bill_No=(TextView) itemView.findViewById(R.id.bill_no);
            totalAmountTVH=(TextView)itemView.findViewById(R.id.total_amountTVH);
            stallNo=(TextView)itemView.findViewById(R.id.historyStallNo);
            created_date = (TextView)itemView.findViewById(R.id.created_date);

        }
    }

}
