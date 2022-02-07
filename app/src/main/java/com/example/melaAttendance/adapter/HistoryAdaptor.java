package com.example.melaAttendance.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.melaAttendance.R;
import com.example.melaAttendance.activity.SplashScreen;
import com.example.melaAttendance.database.BillDetailsData;
import com.example.melaAttendance.database.BillDetailsDataDao;
import com.example.melaAttendance.database.SoldProductDetailsData;
import com.example.melaAttendance.database.SoldProductDetailsDataDao;
import com.example.melaAttendance.fragments.HistoryFragment;
import com.example.melaAttendance.utils.AppUtility;

import org.greenrobot.greendao.query.DeleteQuery;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdaptor extends RecyclerView.Adapter<HistoryAdaptor.BillDetailsView> {
    private LayoutInflater layoutInflater;
    private Context context;
    private List joes;
    final String TAG = "tag";
   private String bill_no;
    private List<String> bill_list;
    private String billNo;
    private List productListofHistory;
    private RecyclerView history_rv;
   private Button cancel;
   private HistoryDialogAdapter historyDialogAdapter;
   private int onClickPosition;
   Fragment fragment;
   private String billNoWithClickPosition;
    public HistoryAdaptor(Context context, List joes) {
        this.context = context;
        this.joes = joes;
        this.bill_list = new ArrayList();
    }

    @NonNull
    @Override
    public BillDetailsView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(context);
        View billdetailsView = layoutInflater.inflate(R.layout.bill_history_item, null);
        return new BillDetailsView(billdetailsView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BillDetailsView holder, final int position) {
         //  this.onClickPosition=position;
        BillDetailsData billDetailsListItems = (BillDetailsData) joes.get(position);
        bill_no = billDetailsListItems.getBillNo();
        for (int i = 0; i < joes.size(); i++) {

            holder.billNoTV.setText(bill_no);
            holder.billDateTV.setText(billDetailsListItems.getDate());
            holder.billTimeTV.setText(billDetailsListItems.getTime());
            holder.totalBillTV.setText(String.valueOf(billDetailsListItems.getTotal_amount()));
            holder.shgIdTV.setText(billDetailsListItems.getShg_code());
        }
        bill_list.add(bill_no);
        Log.d(TAG, "Adpter list size........." + bill_list.toString());
        holder.productDetailsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // billNo=getBillNoOnClick(bill_list);
                for (int i = 0; i < bill_list.size(); i++) {
                    if (i == position) {
                        billNo = bill_list.get(position);
                    }
                }
                Log.d(TAG, "billno" + billNo+"billposision on click"+position);
                AppUtility.getInstance().showLog(billNo+"position"+position,HistoryAdaptor.class);
                getProductListOfHistory(billNo);

            }
        });

        holder.deleteBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                for (int i = 0; i < bill_list.size(); i++) {
                    if (i == position) {
                       billNoWithClickPosition  = bill_list.get(position);
                    }
                }
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delet this bill invoice");
                builder.setMessage("You want to Delete this bill invoice..");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    DeleteQuery<SoldProductDetailsData> dltSoldProductdetailsData=SplashScreen.getInstance()
                            .getDaoSession()
                            .getSoldProductDetailsDataDao()
                            .queryBuilder()
                            .where(SoldProductDetailsDataDao.Properties.Bill_invoice.eq(billNoWithClickPosition))
                            .buildDelete();
                    dltSoldProductdetailsData.executeDeleteWithoutDetachingEntities();
                    SplashScreen.getInstance().getDaoSession().clear();

                    DeleteQuery<BillDetailsData> dltBillDetailsData=SplashScreen.getInstance()
                            .getDaoSession()
                            .getBillDetailsDataDao()
                            .queryBuilder()
                            .where(BillDetailsDataDao.Properties.BillNo.eq(billNoWithClickPosition))
                            .buildDelete();
                    dltBillDetailsData.executeDeleteWithoutDetachingEntities();
                    SplashScreen.getInstance().getDaoSession().clear();
                    fragment=new HistoryFragment();
                    displayFragment(view,fragment);
                }
            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
                builder.show();
        }
        });
    }

    public void displayFragment(View view,Fragment fragment)
    {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame_layout, fragment).addToBackStack(null);
        ft.commit();
    }

    @Override
    public int getItemCount() {
        return joes.size();
    }

    public void getProductListOfHistory(String bill_no) {
        productListofHistory = SplashScreen.getInstance().getDaoSession().getSoldProductDetailsDataDao().queryBuilder()
                .where(SoldProductDetailsDataDao.Properties.Bill_invoice.eq(bill_no))
                .list();
        for (int i = 0; i < productListofHistory.size(); i++) {
            SoldProductDetailsData soldProductDetailsItem = (SoldProductDetailsData) productListofHistory.get(i);
            String productid = String.valueOf(soldProductDetailsItem.getProduct_id());
            String productname = soldProductDetailsItem.getProduct_name();
            String productQty = soldProductDetailsItem.getGetProduct_quantity_sold();
            String unitprice = String.valueOf(soldProductDetailsItem.getUnit_price());
            String total = String.valueOf(soldProductDetailsItem.getTotal_amount());
            String billinvoice = soldProductDetailsItem.getBill_invoice();
            String finalStr = "" + productid + "--" + productname + "--" + productQty + "--" + unitprice + "--" + total + "--" + billinvoice + "";
            AppUtility.getInstance().showLog(finalStr, HistoryAdaptor.class);
        }
        showView();
    }

    private void showView() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.history_product_dilog);
        history_rv  = (RecyclerView)dialog.findViewById(R.id.history_dialog_rv);
        cancel = (Button)dialog.findViewById(R.id.cancel_button);
        historyDialogAdapter = new HistoryDialogAdapter(context,productListofHistory);
        history_rv.setLayoutManager(new LinearLayoutManager(context));
        history_rv.setAdapter(historyDialogAdapter);
        historyDialogAdapter.notifyDataSetChanged();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    public String getBillNoOnClick(List<String> billList){
        String str="";
        for (int i = 0; i < billList.size(); i++) {
            if (i == onClickPosition) {
                str = bill_list.get(onClickPosition);
            }
        }
        return str;
    }
    public class BillDetailsView extends RecyclerView.ViewHolder {
        public TextView billNoTV, shgIdTV, billDateTV, billTimeTV, totalBillTV,deleteBill;
        public Button productDetailsBTN;
        FrameLayout frameLayout;
        public BillDetailsView(@NonNull View itemView) {
            super(itemView);
            billNoTV = (TextView) itemView.findViewById(R.id.bill_noTV);
            shgIdTV = (TextView) itemView.findViewById(R.id.shg_idTV);
            billDateTV = (TextView) itemView.findViewById(R.id.bill_dateTV);
            billTimeTV = (TextView) itemView.findViewById(R.id.bill_timeTV);
            totalBillTV = (TextView) itemView.findViewById(R.id.total_billTV);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.history_fragment_framlayout);
            productDetailsBTN = (Button) itemView.findViewById(R.id.product_detailsBTN);
            deleteBill=(TextView)itemView.findViewById(R.id.delete_bill);
        }
    }
}
