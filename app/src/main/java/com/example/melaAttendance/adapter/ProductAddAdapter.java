package com.example.melaAttendance.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.melaAttendance.R;
import com.example.melaAttendance.activity.SplashScreen;
import com.example.melaAttendance.database.ProductAvailableQuantityData;
import com.example.melaAttendance.database.ProductAvailableQuantityDataDao;
import com.example.melaAttendance.database.SelectedProductDetails;
import com.example.melaAttendance.database.SelectedProductDetailsDao;
import com.example.melaAttendance.utils.AppUtility;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.greenrobot.greendao.query.DeleteQuery;

import java.util.List;

public class ProductAddAdapter extends RecyclerView.Adapter<ProductAddAdapter.MyViewHolderolder> {
    private List<SelectedProductDetails> productPriceLists;
    TextView avlblQuantity,priceET,quantEditText;
    MaterialBetterSpinner productNameList;

    public ProductAddAdapter(List<SelectedProductDetails> productPriceLists, TextView textView, MaterialBetterSpinner productNameList,TextView priceET,TextView quantEditText) {
        this.productPriceLists = productPriceLists;
        this.avlblQuantity = textView;
        this.productNameList = productNameList;
        this.priceET = priceET;
        this.quantEditText = quantEditText;
    }

    @NonNull
    @Override
    public MyViewHolderolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.price_list_layout, parent, false);
        return new ProductAddAdapter.MyViewHolderolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderolder holder, int position) {
        SelectedProductDetails selectedProductDetailsList = (SelectedProductDetails) productPriceLists.get(position);
        for (int i = 0; i < productPriceLists.size(); i++) {

            holder.pName.setText(selectedProductDetailsList.getProductName());
            holder.pQuantity.setText(selectedProductDetailsList.getProductQuantity());
            holder.pPrice.setText(selectedProductDetailsList.getProductUnitPrice());
            holder.total_price.setText("" + selectedProductDetailsList.getProductTotalPrice());
        }
        holder.imageDeletItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int j = 0; j < productPriceLists.size(); j++) {

                    if (j == position) {
                        Long autoIncrementedIdFromSelectedProductDetails = selectedProductDetailsList.getAutoGeneratedProductId();
                        AppUtility.getInstance().showLog("autogeneratedid" + autoIncrementedIdFromSelectedProductDetails, ProductAddAdapter.class);

                        String productIdOfDeletedItem = productPriceLists.get(position).getProductId();
                        String updateQtyOnDelete = String.valueOf(getAvialableQtyFromLocalDB(productIdOfDeletedItem) + Integer.parseInt(productPriceLists.get(position).getProductQuantity()));
                        updateAvailaibleQtyOnDeleteItem(productIdOfDeletedItem, updateQtyOnDelete);
                        productNameList.setText("");

                        deleteItemFromList(position);
                        deleteItemFromLocalDB(autoIncrementedIdFromSelectedProductDetails);

                        avlblQuantity.setText(updateQtyOnDelete);
                        quantEditText.setText("");
                        quantEditText.clearFocus();
                        priceET.setText("");
                        priceET.clearFocus();
                    }
                }
            }
        });
    }

    private String getTotalPrice(int quantity, int unitPrice) {
        long result = quantity * unitPrice;
        return "" + result;
    }

    private void deleteItemFromList(int position) {
        productPriceLists.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productPriceLists.size());
        AppUtility.getInstance().showLog("list size after remove the data " + productPriceLists.size(), BillProductDeletAdapter.class);
    }


    private int getAvialableQtyFromLocalDB(String deletedProductId) {
        String avlQtyOfDeletedItem = "";
        List<ProductAvailableQuantityData> productAvailableQuantityDataList = SplashScreen.getInstance()
                .getDaoSession()
                .getProductAvailableQuantityDataDao()
                .queryBuilder()
                .where(ProductAvailableQuantityDataDao.Properties.Product_id.eq(deletedProductId))
                .build()
                .list();
        for (ProductAvailableQuantityData productAvailableQuantityData : productAvailableQuantityDataList) {
            avlQtyOfDeletedItem = productAvailableQuantityData.getAvailable_quantity();
            return Integer.parseInt(avlQtyOfDeletedItem);
        }
        return Integer.parseInt(avlQtyOfDeletedItem);
    }


    private void updateAvailaibleQtyOnDeleteItem(String productId, String updateQty) {

        List<ProductAvailableQuantityData> productAvailableQuantityDataList = SplashScreen.getInstance()
                .getDaoSession().getProductAvailableQuantityDataDao()
                .queryBuilder()
                .where(ProductAvailableQuantityDataDao.Properties.Product_id.eq(productId))
                .build()
                .list();
        for (ProductAvailableQuantityData productAvailableQuantityData : productAvailableQuantityDataList) {
            productAvailableQuantityData.setAvailable_quantity(updateQty);
            SplashScreen.getInstance().getDaoSession().getProductAvailableQuantityDataDao().update(productAvailableQuantityData);
        }

    }

    private void deleteItemFromLocalDB(Long deleteItemId) {

        DeleteQuery<SelectedProductDetails> selectedProductDetailsDeleteQuery = SplashScreen.getInstance().getDaoSession().getSelectedProductDetailsDao()
                .queryBuilder().where(SelectedProductDetailsDao.Properties.AutoGeneratedProductId.eq(deleteItemId))
                .buildDelete();
        selectedProductDetailsDeleteQuery.executeDeleteWithoutDetachingEntities();
        SplashScreen.getInstance().getDaoSession().clear();

        List<SelectedProductDetails> selectedProductDetails = SplashScreen.getInstance().getDaoSession().getSelectedProductDetailsDao().queryBuilder().list();
        AppUtility.getInstance().showLog("" + selectedProductDetails, ProductAddAdapter.class);
    }

    @Override
    public int getItemCount() {
        return productPriceLists.size();
    }

    public class MyViewHolderolder extends RecyclerView.ViewHolder {
        TextView pName, pQuantity, pPrice, total_price;
        ImageView imageDeletItem;

        public MyViewHolderolder(@NonNull View itemView) {
            super(itemView);
            pName = (TextView) itemView.findViewById(R.id.pName);
            pQuantity = (TextView) itemView.findViewById(R.id.pQuantity);
            pPrice = (TextView) itemView.findViewById(R.id.pPrice);
            total_price = (TextView) itemView.findViewById(R.id.tPrice);
            imageDeletItem = (ImageView) itemView.findViewById(R.id.imageDeletItem);
        }
    }
}
