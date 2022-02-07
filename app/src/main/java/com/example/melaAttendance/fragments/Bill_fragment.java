package com.example.melaAttendance.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.melaAttendance.R;
import com.example.melaAttendance.activity.HomeActivity;
import com.example.melaAttendance.activity.SplashScreen;
import com.example.melaAttendance.adapter.BillProductDeletAdapter;
import com.example.melaAttendance.database.BillDetailsData;
import com.example.melaAttendance.database.BillDetailsDataDao;
import com.example.melaAttendance.database.SelectedProductDetails;
import com.example.melaAttendance.database.SoldProductDetailsData;
import com.example.melaAttendance.database.SoldProductDetailsDataDao;
import com.example.melaAttendance.utils.AppUtility;
import com.example.melaAttendance.utils.DateFactory;
import com.example.melaAttendance.utils.DialogFactory;
import com.example.melaAttendance.utils.NetworkFactory;
import com.example.melaAttendance.utils.PreferenceManager;
import com.example.melaAttendance.utils.ProjectPrefrences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

public class Bill_fragment extends Fragment implements HomeActivity.OnBackPressedListener {

    String shg_code;
    TextView totalPrice,stall_nosTV,shg_codesTV,shgNameTV,genratebillDate,createbillDate;
    TextView billno;
    RecyclerView bill_recyclerview;
    ProgressDialog progressDialog;
    int count = 0;
    BillProductDeletAdapter billProductDeletAdapter;
    final String TAG = "tag";
    String final_bill_no;
    String totalamount,stallNO,msg;
    BillDetailsDataDao billDetailsDataDao;
    SoldProductDetailsDataDao soldProductDetailsDataDao;
    List<SelectedProductDetails> selectedItemList;
    Bundle bundle;
    SimpleDateFormat sdf;
int shgRegistrationIdForBill;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((HomeActivity)getActivity()).setOnBackPressedListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill_fragment, null);
        bundle= getArguments();
        totalamount = bundle.getString("total");
        shg_code = ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefShgRegistrationCodeForProfile(),getContext());
        shgRegistrationIdForBill=  ProjectPrefrences.getInstance().getSharedPrefrencesIntegerData(PreferenceManager.getPrefKeyShgRegId(),getContext());
        AppUtility.getInstance().showLog("vvvvvvvvvvvvv"+shgRegistrationIdForBill,Bill_fragment.class);

        String url = bundle.getString("url");
        String genratedate=bundle.getString("genrdate");
        String createdate=bundle.getString("creatdate");
        Log.d(TAG, "URL===:" + url);
        billno = (TextView) view.findViewById(R.id.bill_nosTV);
        bill_recyclerview = (RecyclerView) view.findViewById(R.id.bill_recyclerview);
        totalPrice = (TextView) view.findViewById(R.id.total_amountTV);
        stall_nosTV=(TextView)view.findViewById(R.id.stall_nosTV);
        shg_codesTV = (TextView)view.findViewById(R.id.shg_codesTV);
        shgNameTV=(TextView)view.findViewById(R.id.shg_nameTVBill);
        genratebillDate=(TextView)view.findViewById(R.id.genrate_datebill);
        createbillDate=(TextView) view.findViewById(R.id.create_datebill);

        //getStallNoFromLocalDB();
        stall_nosTV.setText( ProjectPrefrences.getInstance().getSharedPrefrencesData("stall",getContext()));

        totalPrice.setText(totalamount);
        ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getPrefKeyShgTotalAmount(),totalamount,getContext());
        shg_codesTV.setText(shg_code);
        String shgNameData=ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefShgNameForProfile(), getContext());
        AppUtility.getInstance().showLog("shgname"+shgNameData,Bill_fragment.class);


        shgNameTV.setText(shgNameData);
        genratebillDate.setText(genratedate);
        createbillDate.setText(createdate);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        if(!NetworkFactory.isInternetOn(getContext()))
            DialogFactory.getInstance().showNoInternetDialog(getContext());
        else  progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                count++;
                Log.d("TAG", "Response : " + s);
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        final_bill_no = jsonObject.getString("invoice_no");
                        msg=jsonObject.getString("messsage");
                        if(msg.equalsIgnoreCase("Future date is selected"))
                        {
                            Toast.makeText(getContext(),"Something went wrong plese check your phone date",Toast.LENGTH_LONG).show();
                            getFragmentManager().beginTransaction().remove(Bill_fragment.this).commitAllowingStateLoss();
                        }
                        else {
                            billno.setText(final_bill_no);
                            getSelectedItemFromLocalDB();
                            setDataOnAdapter();
                            saveInDataBase();
                            SplashScreen.getInstance().getDaoSession().getSelectedProductDetailsDao().deleteAll();
                        }
//                        Log.d("TAG","billno----------"+final_bill_no);
//                        billno.setText(final_bill_no);
//                        getSelectedItemFromLocalDB();
//                        setDataOnAdapter();
//                        saveInDataBase();
//                        SplashScreen.getInstance().getDaoSession().getSelectedProductDetailsDao().deleteAll();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        try {
            stringRequest.setShouldCache(false);
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


        return view;
    }

    private void setDataOnAdapter() {
        billProductDeletAdapter = new BillProductDeletAdapter(selectedItemList,getContext(),final_bill_no);
        bill_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        bill_recyclerview.setAdapter(billProductDeletAdapter);
        billProductDeletAdapter.notifyDataSetChanged();
    }

    private void saveInDataBase() {
        saveProductDetail();
        BillDetailsData billDetailsData = new BillDetailsData();
        billDetailsData.setBillId((long) 1);
        billDetailsData.setBillNo(final_bill_no);
        billDetailsData.setDate(DateFactory.getInstance().getTodayDate());
        billDetailsData.setTime(DateFactory.getInstance().getCurrentTime("HH:mm:ss"));
        billDetailsData.setTotal_amount(Integer.parseInt(totalamount));
        billDetailsData.setShg_code("101");
        billDetailsDataDao = SplashScreen.getInstance().getDaoSession().getBillDetailsDataDao();
        billDetailsDataDao.insert(billDetailsData);
    }

    private void saveProductDetail() {
        for(SelectedProductDetails p :selectedItemList){
            SoldProductDetailsData soldProductDetailsData = new SoldProductDetailsData();
            soldProductDetailsData.setBill_invoice(final_bill_no);
            soldProductDetailsData.setGetProduct_quantity_sold(p.getProductQuantity());
            soldProductDetailsData.setSold_product_id(p.getProductId());
            soldProductDetailsData.setProduct_name(p.getProductName());
            soldProductDetailsData.setTotal_amount(Integer.parseInt(p.getProductTotalPrice()));
            soldProductDetailsData.setUnit_price(Integer.parseInt(p.getProductUnitPrice()));
            soldProductDetailsDataDao = SplashScreen.getInstance().getDaoSession().getSoldProductDetailsDataDao();
            soldProductDetailsDataDao.insert(soldProductDetailsData);

        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void doBack() {

        Intent theIntent = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
        theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        theIntent.putExtras(bundle);
        startActivity(theIntent);
        getActivity().finish();
    }

    public String getRandomOtp() {
        Random random = new Random();
        int otp = 11 + random.nextInt(20);
       // Toast.makeText(getActivity(), "OTP is: " + otp + "", Toast.LENGTH_LONG).show();
        return "" + otp;
    }
    public void getSelectedItemFromLocalDB() {

        selectedItemList = SplashScreen.getInstance().getDaoSession().getSelectedProductDetailsDao().queryBuilder().list();
        AppUtility.getInstance().showLog("PRoductList"+selectedItemList,Bill_fragment.class);

    }



    //   private void getStallNoFromLocalDB(){
//
//        List<ShgDetailsData> shgDetailsDataList=SplashScreen.getInstance().getDaoSession().getShgDetailsDataDao().queryBuilder()
//                .where(ShgDetailsDataDao.Properties.Shg_reg_id.eq(shgRegistrationIdForBill))
//                .build()
//                .list();
//        for(ShgDetailsData stallNo :shgDetailsDataList){
//            stallNO=stallNo.getStallNo();
//        }
//
//    }
}
