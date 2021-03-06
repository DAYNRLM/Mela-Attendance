package com.example.melaAttendance.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class MyDatePicker extends DialogFragment {

public static TextView selectedDate;
public MyDatePicker(TextView selectedDate)
{
this.selectedDate=selectedDate;
}
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);

    }

    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    Toast.makeText(getActivity(), "selected date is " + view.getYear() +
                            " / " + (view.getMonth()+1) +
                            " / " + view.getDayOfMonth(), Toast.LENGTH_SHORT).show();
                    String date  = ""+ view.getYear() + "-" + (view.getMonth()+1) + "-" + view.getDayOfMonth();
                  //  String date  = ""+ view.getDayOfMonth() + "-" + (view.getMonth()+1) + "-" + view.getYear() ;

                    selectedDate.setText(date);
                }
            };

}
