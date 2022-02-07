package com.example.melaAttendance.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.example.melaAttendance.adapter.HistoryJsonAdpt;
import com.example.melaAttendance.model.HistoryJson;
import com.example.melaAttendance.utils.DialogFactory;
import com.example.melaAttendance.utils.NetworkFactory;
import com.example.melaAttendance.utils.PreferenceManager;
import com.example.melaAttendance.utils.ProjectPrefrences;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HistoryFragment extends Fragment implements HomeActivity.OnBackPressedListener {

    private RecyclerView shgHistoryRV;
    private LinearLayout noHistoryLL;
    private Context context;
    private HashMap<String, String> hashMap = new HashMap<String, String>();
    List joes;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    //---------manish--------------
    private String mParam1;
    private String mParam2,payment_ModeR,invoice_No,bill_No,regid,grantAmt,created_date;
    private int shg_reg_Id,mela_Id;
    Bundle bundle;
    private Button api_today,api_begining;
    String URL,urlTodayHistory;
    List<HistoryJson> historyJsonsitem;
    HistoryJsonAdpt historyJsonAdpt;
    private ProgressDialog progressDialog;
    private TextView showMsgTV;
    private AlertDialog alertDialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//    Bundle bundle;
//    private Button today,frombegning,api_today,api_begining;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle=getArguments();
        ((HomeActivity)getActivity()).setOnBackPressedListener(this);
        context=getContext();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        regid= ""+ ProjectPrefrences.getInstance().getSharedPrefrencesIntegerData(PreferenceManager.getPrefKeyShgRegId(),getContext());
        //loadTodayHistory();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_history, container, false);
        screenView(view);
        return view;
    }

    private void  screenView(View view){
        api_today=(Button) view.findViewById(R.id.history_today);
        api_begining=(Button) view.findViewById(R.id.from_beginning);
        showMsgTV=view.findViewById(R.id.show_MsgTV);
        noHistoryLL=(LinearLayout)view.findViewById(R.id.no_history_layout);
        shgHistoryRV=(RecyclerView)view.findViewById(R.id.shg_history_recycler);
        showMsgTV.setText("To see history, click on Today or From Beginning Button.");
        showMsgTV.setVisibility(View.VISIBLE);
        refreshHistoryData();
    }

    private void refreshHistoryData() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        api_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                historyJsonsitem=new ArrayList<>();
                android.text.format.DateFormat df = new android.text.format.DateFormat();
                String s= (String) df.format("dd-MM-yyyy", new Date());
                urlTodayHistory="https://nrlm.gov.in/nrlmwebservice/services/melaSales/history?shgRegId="+regid+"&date="+s;
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Loading...");

                if (!NetworkFactory.isInternetOn(getContext())) {
                    DialogFactory.getInstance().showNoInternetDialog(getContext());
                    return;
                } else {

                    progressDialog.show();
                }
                StringRequest stringRequest=new StringRequest(Request.Method.GET,urlTodayHistory , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("TAG","api today"+s);
                        showMsgTV.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        if(s.equals("[]"))
                            noHistoryLL.setVisibility(View.VISIBLE);


                        try {
                            JSONArray mainArr=new JSONArray(s);

                            for(int i=0;i<mainArr.length();i++)
                            {
                                JSONObject obj=mainArr.getJSONObject(i);
                                HistoryJson items=new HistoryJson();
                                payment_ModeR=obj.getString("payment_mode");
                                items.setPayment_ModeR(payment_ModeR);
                                invoice_No=obj.getString("invoice_no");
                                items.setInvoice_No(invoice_No);
                                grantAmt=obj.getString("grantAmt");
                                items.setTotalAmount(grantAmt);
                                shg_reg_Id=obj.getInt("shg_reg_id");
                                items.setShg_reg_Id(shg_reg_Id);
                                bill_No=obj.getString("bill_no");
                                items.setBill_No(bill_No);
                                mela_Id=obj.getInt("mela_id");
                                items.setMela_Id(mela_Id);
                                created_date=obj.getString("created_date");
                                items.setCreatedDate(created_date);
                                historyJsonsitem.add(items);
                            }
                            historyJsonAdpt=new HistoryJsonAdpt(historyJsonsitem,getContext());
                            shgHistoryRV.setHasFixedSize(true);
                            shgHistoryRV.setLayoutManager(new LinearLayoutManager(getContext()));
                            shgHistoryRV.setAdapter(historyJsonAdpt);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(),""+error,Toast.LENGTH_LONG).show();
                        Log.d("TAG","error"+error);

                    }
                });

                RequestQueue requestQueue= Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);
            }
        });




        api_begining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                historyJsonsitem=new ArrayList<>();
                URL="https://nrlm.gov.in/nrlmwebservice/services/melaSales/history?shgRegId="+regid;
                Log.d("TAG","URL"+URL);
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Loading...");
                if (!NetworkFactory.isInternetOn(getContext())) {
                    DialogFactory.getInstance().showNoInternetDialog(getContext());
                    return;
                } else {
                    progressDialog.show();
                }
                StringRequest stringRequest=new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        Log.d("TAG","api beigning"+s);
                        showMsgTV.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        if(s.equals("[]"))
                            noHistoryLL.setVisibility(View.VISIBLE);
                        else noHistoryLL.setVisibility(View.GONE);


                        try {
                            JSONArray mainArr=new JSONArray(s);

                            for(int i=0;i<mainArr.length();i++)
                            {
                                JSONObject obj=mainArr.getJSONObject(i);
                                HistoryJson items=new HistoryJson();
                                payment_ModeR=obj.getString("payment_mode");
                                items.setPayment_ModeR(payment_ModeR);
                                invoice_No=obj.getString("invoice_no");
                                items.setInvoice_No(invoice_No);
                                grantAmt=obj.getString("grantAmt");
                                items.setTotalAmount(grantAmt);
                                shg_reg_Id=obj.getInt("shg_reg_id");
                                items.setShg_reg_Id(shg_reg_Id);
                                bill_No=obj.getString("bill_no");
                                items.setBill_No(bill_No);
                                mela_Id=obj.getInt("mela_id");
                                items.setMela_Id(mela_Id);
                                created_date=obj.getString("created_date");
                                items.setCreatedDate(created_date);
                                historyJsonsitem.add(items);
                            }
                            historyJsonAdpt=new HistoryJsonAdpt(historyJsonsitem,getContext());
                            shgHistoryRV.setHasFixedSize(true);
                            shgHistoryRV.setLayoutManager(new LinearLayoutManager(getContext()));
                            shgHistoryRV.setAdapter(historyJsonAdpt);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(),""+error,Toast.LENGTH_LONG).show();
                        Log.d("TAG","error"+error);

                    }
                });

                RequestQueue requestQueue= Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);
            }
        });
    }

  /*  private void loadTodayHistory() {

        historyJsonsitem=new ArrayList<>();
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String s= (String) df.format("dd-MM-yyyy", new Date());
        urlTodayHistory="https://nrlm.gov.in/nrlmwebservice/services/melaSales/history?shgRegId="+regid+"&date="+s;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.GET,urlTodayHistory , new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();

                try {
                    JSONArray mainArr=new JSONArray(s);

                    for(int i=0;i<mainArr.length();i++)
                    {
                        JSONObject obj=mainArr.getJSONObject(i);
                        HistoryJson items=new HistoryJson();
                        payment_ModeR=obj.getString("payment_mode");
                        items.setPayment_ModeR(payment_ModeR);
                        invoice_No=obj.getString("invoice_no");
                        items.setInvoice_No(invoice_No);
                        grantAmt=obj.getString("grantAmt");
                        items.setTotalAmount(grantAmt);
                        shg_reg_Id=obj.getInt("shg_reg_id");
                        items.setShg_reg_Id(shg_reg_Id);
                        bill_No=obj.getString("bill_no");
                        items.setBill_No(bill_No);
                        mela_Id=obj.getInt("mela_id");
                        items.setMela_Id(mela_Id);
                        historyJsonsitem.add(items);
                    }
                    historyJsonAdpt=new HistoryJsonAdpt(historyJsonsitem,getContext());
                    shgHistoryRV.setHasFixedSize(true);
                    shgHistoryRV.setLayoutManager(new LinearLayoutManager(getContext()));
                    shgHistoryRV.setAdapter(historyJsonAdpt);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(),""+error,Toast.LENGTH_LONG).show();
                Log.d("TAG","error"+error);
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }*/

    @Override
    public void doBack() {
        Intent theIntent = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
       // theIntent.putExtras(bundle);
        theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(theIntent);
        getActivity().finish();
    }
}
