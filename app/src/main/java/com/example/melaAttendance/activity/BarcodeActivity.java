package com.example.melaAttendance.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.melaAttendance.R;
import com.example.melaAttendance.utils.AnyOrientationCaptureActivity;
import com.example.melaAttendance.utils.PreferenceManager;
import com.example.melaAttendance.utils.ProjectPrefrences;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class BarcodeActivity extends AppCompatActivity {
    IntentIntegrator scanBarCode;
    String k="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan something");
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
                ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getProductIdForBarcode(),k,BarcodeActivity.this);
                finish();
            } else {
                try {
                    Toast.makeText(this, "Result is:-" + result.getContents(), Toast.LENGTH_LONG).show();
                    int count=0;
                    String info=result.getContents();
                  Log.d("TAG","Info of barcode"+info.length());
                    if(info.length()==7) {
                        for (int i = 0; i < info.length(); i++) {
                            if (info.charAt(i) == '-' || count >= 1) {
                                count++;
                                if (count > 1) {
                                    k = k + info.charAt(i);
                                }
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(BarcodeActivity.this,"Wrong Barcode",Toast.LENGTH_LONG);
                    }
                    ProjectPrefrences.getInstance().saveSharedPrefrecesData(PreferenceManager.getProductIdForBarcode(),k,BarcodeActivity.this);
                    finish();
                   /* //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    textViewName.setText(obj.getString("name"));
                    textViewAddress.setText(obj.getString("address"));*/
                } catch (Exception e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(),Toast.LENGTH_SHORT ).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
/*        public void displayFragment(Fragment fr)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace()
        }*/
    }
}
