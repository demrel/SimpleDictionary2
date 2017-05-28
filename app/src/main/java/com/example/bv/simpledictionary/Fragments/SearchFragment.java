package com.example.bv.simpledictionary.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bv.simpledictionary.R;

/**
 * Created by bv on 3/21/2017.
 */

public class SearchFragment extends Fragment {
    public SearchFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.searchfragment,container,false);
    }
}
