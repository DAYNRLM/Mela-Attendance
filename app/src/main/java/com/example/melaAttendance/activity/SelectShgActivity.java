package com.example.melaAttendance.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.melaAttendance.R;
import com.example.melaAttendance.adapter.SelectShgAdapter;
import com.example.melaAttendance.database.ShgsDetailsOnLoginData;

import java.util.List;

public class SelectShgActivity extends AppCompatActivity {

    RecyclerView selectShgRecyclerView;
    ProgressDialog progressDialog;
    SelectShgAdapter selectShgAdapter;
    private List<ShgsDetailsOnLoginData> shgsDetailsOnLoginDataList;
    private ImageView backIV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_shg);
        selectShgRecyclerView = (RecyclerView)findViewById(R.id.rvSelectShg);
        backIV=findViewById(R.id.imgBackIV);
        getShgDetailOnLoginData();
        setDataOnAdapter();
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectShgActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
   private void  getShgDetailOnLoginData(){
        shgsDetailsOnLoginDataList=SplashScreen.getInstance()
               .getDaoSession()
               .getShgsDetailsOnLoginDataDao()
               .queryBuilder()
               .build()
               .list();
    }
    private void setDataOnAdapter() {

        selectShgAdapter = new SelectShgAdapter(SelectShgActivity.this,shgsDetailsOnLoginDataList);
        selectShgRecyclerView.setLayoutManager(new LinearLayoutManager(SelectShgActivity.this));
        selectShgRecyclerView.setAdapter(selectShgAdapter);
        selectShgAdapter.notifyDataSetChanged();
    }


   /* private void loadShgDetailFromServer() {
        ProgressDialog progressDialogss = new ProgressDialog(SelectShgActivity.this);
        progressDialogss.setMessage("Loading...");
        if (!NetworkFactory.isInternetOn(SelectShgActivity.this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SelectShgActivity.this);
            builder.setTitle("No Internet!");
            builder.setMessage("Please open your internet connection.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();
                }
            });
            builder.show();
            return;
        } else {
            progressDialogss.show();
        }
        String FINAL_LOGIN_NUMBER_URL = LoginActivity.LOGIN_NUMBER_URL + mobileNumber;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FINAL_LOGIN_NUMBER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialogss.dismiss();

                try {
                    JSONArray mainArr = new JSONArray(s);
                    for (int i = 0; i < mainArr.length(); i++) {
                        JSONObject obj = mainArr.getJSONObject(i);
                        String shgCode = obj.getString("shg_code");
                        int shgRegId = obj.getInt("shg_reg_id");
                        String shgName = obj.getString("shg_name");
                        long loginMobileNumber = obj.getLong("mobile");
                        int melaId = obj.getInt("mela_id");
                        String stallNo = obj.getString("stall_no");
                        ShgsDetailsOnLoginData shgsDetailsOnLoginData = new ShgsDetailsOnLoginData();

                        shgsDetailsOnLoginData.setShgCode(shgCode);
                        shgsDetailsOnLoginData.setShgRegistrationId(shgRegId);
                        shgsDetailsOnLoginData.setLoginMobilenumber(String.valueOf(loginMobileNumber));
                        shgsDetailsOnLoginData.setShgName(shgName);
                        shgsDetailsOnLoginData.setMelaId(melaId);
                        shgsDetailsOnLoginData.setStallNo(stallNo);
                        shgLoginDetailDataList.add(shgsDetailsOnLoginData);
                    }
                    setDataOnAdapter();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // progressDialog.dismiss();
                Toast.makeText(SelectShgActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("TAG", "volleyError.getMessage()" + volleyError.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }*/


}
