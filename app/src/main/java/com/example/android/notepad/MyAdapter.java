package com.example.android.notepad;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.widget.SimpleCursorAdapter;

public class MyAdapter extends SimpleCursorAdapter {

    // 构造函数：初始化适配器，绑定布局和数据源
    public MyAdapter(Context context, int layout, Cursor c,
                     String[] from, int[] to) {
        super(context, layout, c, from, to);  // 调用父类构造函数
    }

    // 绑定数据到视图（每一项列表数据展示时调用）
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);  // 调用父类的 bindView 方法

        // 获取背景色列的数据
        int backgroundColorCode = cursor.getInt(cursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_BACK_COLOR));

        // 根据背景色代码设置视图的背景色
        switch (backgroundColorCode) {
            case NotePad.Notes.DEFAULT_COLOR:
                // 默认背景色：白色
                view.setBackgroundColor(Color.rgb(255, 255, 255));
                break;
            case NotePad.Notes.YELLOW_COLOR:
                // 黄色背景色
                view.setBackgroundColor(Color.rgb(247, 216, 133));
                break;
            case NotePad.Notes.BLUE_COLOR:
                // 蓝色背景色
                view.setBackgroundColor(Color.rgb(165, 202, 237));
                break;
            case NotePad.Notes.GREEN_COLOR:
                // 绿色背景色
                view.setBackgroundColor(Color.rgb(161, 214, 174));
                break;
            case NotePad.Notes.RED_COLOR:
                // 红色背景色
                view.setBackgroundColor(Color.rgb(244, 149, 133));
                break;
            default:
                // 如果没有匹配的颜色，使用默认的白色
                view.setBackgroundColor(Color.rgb(255, 255, 255));
                break;
        }
    }
}
