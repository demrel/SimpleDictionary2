package com.example.bv.simpledictionary;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager=(ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new CategoryFragmentPageAdapter(this,getSupportFragmentManager()));
        TabLayout tab=(TabLayout) findViewById(R.id.tab);
        tab.setupWithViewPager(viewPager);
    }
}
