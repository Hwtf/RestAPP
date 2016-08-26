package com.example.jdk.restapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.example.jdk.restapp.Activity.ShowWebViewActivity;
import com.example.jdk.restapp.Adapter.RecyclerViewDataBindingAdapter;
import com.example.jdk.restapp.DataBase.MySQLiteWebViewTextBussiness;
import com.example.jdk.restapp.ModelData.Constant;
import com.example.jdk.restapp.ModelData.entity.Base;
import com.example.jdk.restapp.ModelData.entity.URLTableData;
import com.example.jdk.restapp.R;
import com.example.jdk.restapp.Utils.MyDecoration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JDK on 2016/8/13.
 */
public class CollectionFragment extends BaseFragment{
    private Context mContext;
    private List<Base> myList;
    private MySQLiteWebViewTextBussiness mySQLiteWebViewTextBussiness;
    RecyclerViewDataBindingAdapter recyclerViewDataBindingAdapter;

    public CollectionFragment() {
        super(R.layout.fragment_collection);
    }

    public CollectionFragment(Context mContext) {
        super(R.layout.fragment_collection);
        this.mContext=mContext;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySQLiteWebViewTextBussiness=new MySQLiteWebViewTextBussiness(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(1);
    }

    @Override
    public void getData(int page) {
        List<URLTableData> urlTableDataList=mySQLiteWebViewTextBussiness.queryAllFromTable();
        myList=new ArrayList<>();
       for(int i=0;i<urlTableDataList.size();i++){
           myList.add(urlTableDataList.get(i));
       }
        recyclerViewDataBindingAdapter = new RecyclerViewDataBindingAdapter(mContext, myList, Constant.IsCollection);
        getMyRecyclerView().setAdapter(recyclerViewDataBindingAdapter);
        recyclerViewDataBindingAdapter.setRecyclerViewItemOnClickListener(new RecyclerViewDataBindingAdapter.recyclerViewDataBindingItemOnClickListener() {
            @Override
            public void recyclerViewDataBindingItemOnClick(String url, String desc, String who, Date CreateAt,String type,int position) {
                Intent intent = new Intent();
                intent.setClass(mContext, ShowWebViewActivity.class);
                View v=getMyRecyclerView().getChildAt(position);
                TextView textView= (TextView) v.findViewById(R.id.id_tv);
                int _id=Integer.valueOf(textView.getText().toString());
                URLTableData urlTableData = new URLTableData(url, who, desc, CreateAt);
                urlTableData.setType(type);
                urlTableData.set_id(_id);
                urlTableData.setIsCollected(true);
                Bundle bundle = new Bundle();
                bundle.putSerializable("urlTableData", urlTableData);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        recyclerViewDataBindingAdapter.notifyDataSetChanged();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMyRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
        getMyRecyclerView().addItemDecoration(new MyDecoration(getActivity(), MyDecoration.VERTICAL_LIST));
        InitListener();
    }
}
