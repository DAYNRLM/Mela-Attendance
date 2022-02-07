package com.example.melaAttendance.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.melaAttendance.R;
import com.example.melaAttendance.activity.SplashScreen;
import com.example.melaAttendance.database.SelectedProductDetails;
import com.example.melaAttendance.database.SoldProductDetailsData;
import com.example.melaAttendance.database.SoldProductDetailsDataDao;
import com.example.melaAttendance.utils.AppUtility;


import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class BillProductDeletAdapter extends RecyclerView.Adapter<BillProductDeletAdapter.MyViewHolder> {
    Context context;
   private List<SelectedProductDetails> productPriceLists;
    String billno,id ;
    private int clickPosition;
    public BillProductDeletAdapter(List<SelectedProductDetails> productPriceLists,Context context,String billno) {
        this.context = context;
        this.productPriceLists = productPriceLists;
        this.billno= billno;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_product_with_delet_layout, parent, false);
        return new BillProductDeletAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.pName.setText(productPriceLists.get(position).getProductName());
        holder.pQuantity.setText(productPriceLists.get(position).getProductQuantity());
        holder.pPrice.setText(productPriceLists.get(position).getProductUnitPrice());
        holder.total_price.setText(""+productPriceLists.get(position).getProductTotalPrice());

        holder.bDeletImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (productPriceLists.size() == 1) {
                    Toast.makeText(context, "You can't delete this product.If you want to delete, go through the history ", Toast.LENGTH_LONG).show();
                } else {
                    for (int i = 0; i < productPriceLists.size(); i++) {
                        if (i == position) {
                            clickPosition = position;
                            id = productPriceLists.get(position).getProductId();
                        }
                    }


                    Toast.makeText(context, "product id=" + id + billno, Toast.LENGTH_LONG).show();

                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete Product");
                    builder.setMessage("You want to Delete this Product..");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteItem(clickPosition);
                            deletFromDataBase();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
            }
        });

    }
    private void deleteItem(int position) {
        productPriceLists.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productPriceLists.size());
        AppUtility.getInstance().showLog("list size after remove the data "+productPriceLists.size(),BillProductDeletAdapter.class);
    }

    private void deletFromDataBase() {

        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;

        QueryBuilder<SoldProductDetailsData> queryBuilder=SplashScreen.getInstance().getDaoSession()
                .getSoldProductDetailsDataDao().queryBuilder();
        queryBuilder.where(SoldProductDetailsDataDao.Properties.Bill_invoice.eq(billno),
                queryBuilder.and(SoldProductDetailsDataDao.Properties.Bill_invoice.eq(billno),
                        SoldProductDetailsDataDao.Properties.Sold_product_id.eq(id)));

        DeleteQuery<SoldProductDetailsData>dltSoldProductdetailsData =queryBuilder.buildDelete();
        dltSoldProductdetailsData.executeDeleteWithoutDetachingEntities();
        SplashScreen.getInstance().getDaoSession().clear();
    }

    @Override
    public int getItemCount() {
        return productPriceLists.size();
    }
    
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView pName,pQuantity,pPrice,total_price;
        ImageView bDeletImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pName = (TextView)itemView.findViewById(R.id.bpName);
            pQuantity = (TextView)itemView.findViewById(R.id.bpQuantity);
            pPrice = (TextView)itemView.findViewById(R.id.bpPrice);
            total_price = (TextView)itemView.findViewById(R.id.btPrice);
            bDeletImage = (ImageView) itemView.findViewById(R.id.bimageDelet);
        }
    }
}
