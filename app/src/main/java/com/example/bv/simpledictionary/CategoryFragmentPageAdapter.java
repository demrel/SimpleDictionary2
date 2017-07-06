package com.example.bv.simpledictionary;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.bv.simpledictionary.Fragments.FolderFragment;
import com.example.bv.simpledictionary.Fragments.SearchFragment;



class CategoryFragmentPageAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public CategoryFragmentPageAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.mContext=context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0) return  new FolderFragment();
        else  return new SearchFragment();

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0) return mContext.getString(R.string.folder);
            else return mContext.getString(R.string.search);



    }
}
