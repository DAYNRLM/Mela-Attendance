package com.example.melaAttendance.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.melaAttendance.R;
import com.example.melaAttendance.activity.HomeActivity;
import com.example.melaAttendance.activity.SplashScreen;

import com.example.melaAttendance.adapter.SelectShgAdapter;
import com.example.melaAttendance.database.ShgsDetailsOnLoginData;
import com.example.melaAttendance.utils.DialogFactory;
import com.example.melaAttendance.utils.PreferenceManager;
import com.example.melaAttendance.utils.ProjectPrefrences;

import java.util.List;

import static com.example.melaAttendance.R.*;


public class MainFragment extends Fragment implements HomeActivity.OnBackPressedListener  {
    String shgRegId;
    RecyclerView selectShgRecyclerView;
    SelectShgAdapter selectShgAdapter;
    private List<ShgsDetailsOnLoginData> shgsDetailsOnLoginDataList;

    @Nullable
    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((HomeActivity) getActivity()).setOnBackPressedListener(this);
        getActivity();
        shgRegId = "" + ProjectPrefrences.getInstance().getSharedPrefrencesIntegerData(PreferenceManager.getPrefKeyShgRegId(), getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layout.fragment_main, null);
        selectShgRecyclerView = (RecyclerView)view.findViewById(R.id.rvSelectShg);
        setHasOptionsMenu(true);
        getShgDetailOnLoginData();
        setDataOnAdapter();
        return view;
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
          if (shgsDetailsOnLoginDataList.size()==0){
               DialogFactory.getInstance().showServerErrorDialog(getContext(),"No data Found!!!","OK");
          }else {
              selectShgAdapter = new SelectShgAdapter(getContext(),shgsDetailsOnLoginDataList);
              selectShgRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
              selectShgRecyclerView.setAdapter(selectShgAdapter);
              selectShgAdapter.notifyDataSetChanged();
          }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.search_menu);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint(getString(string.type_shg_name));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Here is where we are going to implement the filter logic
                selectShgAdapter.getFilter().filter(newText);
                return true;
            }

        });
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void doBack() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Do you want to exit?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SplashScreen.getInstance().getDaoSession().getSelectedProductDetailsDao().deleteAll();
                getActivity().finish();
                //System.exit(0);
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
