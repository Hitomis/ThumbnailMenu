package com.hitomi.thumbnailmenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.hitomi.tmlibrary.ThumbnailMenu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Fragment> fragmentList = new ArrayList<>();

    private ThumbnailMenu thumMenu;

    private ImageView ivMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById();

        initFragment();

        setViewListener();
    }

    private void findViewById() {
        thumMenu = (ThumbnailMenu) findViewById(R.id.thumb);
        ivMenu = (ImageView) findViewById(R.id.iv_menu);
    }

    private void initFragment() {
        Fragment1 fragment1 = new Fragment1();
        Fragment2 fragment2 = new Fragment2();
        Fragment3 fragment3 = new Fragment3();
        Fragment4 fragment4 = new Fragment4();
        Fragment5 fragment5 = new Fragment5();

        fragmentList.add(fragment5);
        fragmentList.add(fragment4);
        fragmentList.add(fragment3);
        fragmentList.add(fragment2);
        fragmentList.add(fragment1);

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

        };
        thumMenu.setAdapter(adapter);
    }

    private void setViewListener() {
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivMenu.setVisibility(View.GONE);
                thumMenu.openMenu();
            }
        });

        thumMenu.setOnMenuCloseListener(new ThumbnailMenu.OnThumbnailMenuCloseListener() {
            @Override
            public void onMenuCloseListener() {
                ivMenu.setVisibility(View.VISIBLE);
            }
        });
    }

}
