# ThumbnailMenu

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ThumbnailMenu-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/4297)

这是一个简单而精致的 Fragment 菜单控件，它可以让你切换 Fragment 的时候不再单调、死板。

# Preview

<img src="preview/menu_left.gif"/>
<br/>
<img src="preview/menu_bottom.gif"/>
<br/>
<img src="preview/menu_right.gif"/>

# Usage

导入 tmlibrary module, 或者直接拷贝 com.hitomi.tmlibrary 包下所有 java 文件到您的项目中

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

    <attr name="menu_direction" format="enum">
        <enum name="left" value="1000" />
        <enum name="bottom" value="1001" />
        <enum name="right" value="1002" />
    </attr>
    支持三种方向:
        left :缩略图置于屏幕左侧
        bottom : 缩略图置于屏幕底部
        right : 缩略图置于屏幕右侧

    <attr name="scale_ratio" format="float" />
    支持缩略图大小自定义
        scale_ratio ：取值为 0.1f 到 1.0f 之间。


#Licence

MIT 
 


