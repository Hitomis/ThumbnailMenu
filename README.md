# ThumbnailMenu

这是一个简单而精致的 Fragment 菜单控件，它可以让你切换 Fragment 的时候不再单调、死板。

# Preview

<img src="preview/men_left.gif"/>
<img src="preview/menu_bottom.gif"/>
<img src="preview/menu_right.gif"/>

# Usage

布局文件中：

    <com.hitomi.tmlibrary.ThumbnailMenu
        android:id="@+id/thumb"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:menu_direction="right">
    
        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@mipmap/profile_background">
    
        </RelativeLayout>
    
    </com.hitomi.tmlibrary.ThumbnailMenu>

RelativeLayout 可以让您编写自己的背景布局

Activity 中：

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

# Attributes

    <declare-styleable name="ThumbnailMenu">
        <attr name="menu_direction" format="enum">
            <enum name="left" value="1000" /> // 缩略图置于屏幕左侧
            <enum name="bottom" value="1001" /> // 缩略图置于屏幕底部
            <enum name="right" value="1002" /> // 缩略图置于屏幕右侧
        </attr>
    </declare-styleable>

#Licence

MIT



