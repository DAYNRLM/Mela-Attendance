package com.example.melaAttendance.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.melaAttendance.R;
import com.example.melaAttendance.activity.HomeActivity;
import com.example.melaAttendance.activity.SplashScreen;
import com.example.melaAttendance.adapter.ProductItem;
import com.example.melaAttendance.database.ShgDetailsData;
import com.example.melaAttendance.database.ShgMemberDetailsData;
import com.example.melaAttendance.model.ProductPojo;
import com.example.melaAttendance.utils.PreferenceManager;
import com.example.melaAttendance.utils.ProjectPrefrences;

import java.util.ArrayList;
import java.util.List;


public class Profile extends Fragment implements HomeActivity.OnBackPressedListener {

    private TextView name;
    private String shgname;
    private TextView shg_code;
    String regcode;
    private RecyclerView recyclerView;
    private List<ProductPojo> producttDataItemList;


    private List<ShgMemberDetailsData> shgMemberDetailsData;
    private List<ShgDetailsData> shgDetailsData;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((HomeActivity)getActivity()).setOnBackPressedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        shg_code=view.findViewById(R.id.shgcode_pro);
        name=view.findViewById(R.id.name);

        shgname = ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefShgNameForProfile(), getContext());
        regcode=ProjectPrefrences.getInstance().getSharedPrefrencesData(PreferenceManager.getPrefShgRegistrationCodeForProfile(),getContext());
        name.setText(shgname);
        shg_code.setText(regcode);
        recyclerView= view.findViewById(R.id.profile_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        producttDataItemList=new ArrayList<>();
        getProfileDB();
        ProductItem adapter=new ProductItem(shgMemberDetailsData,getContext());

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;
    }
    void getProfileDB() {
        shgMemberDetailsData = new ArrayList<>();
        shgMemberDetailsData = SplashScreen.getInstance().getDaoSession().getShgMemberDetailsDataDao()
                .queryBuilder().list();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void doBack() {
        Intent theIntent = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
        theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(theIntent);
        getActivity().finish();
    }
}
