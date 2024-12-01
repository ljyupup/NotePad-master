package com.example.android.notepad;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class NoteColor extends Activity {

    private Cursor mCursor;       // 用于存储数据库查询结果的 Cursor
    private Uri mUri;             // 用于存储当前笔记的 URI
    private int color;            // 当前选中的背景色
    private static final int COLUMN_INDEX_TITLE = 1;  // 笔记标题列的索引
    private static final String[] PROJECTION = new String[] {
            NotePad.Notes._ID,                 // 笔记 ID 列
            NotePad.Notes.COLUMN_NAME_BACK_COLOR,  // 背景色列
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_color);

        // 从 Intent 获取传入的 URI（当前笔记的 URI）
        mUri = getIntent().getData();

        // 查询笔记的背景色列
        mCursor = managedQuery(
                mUri,        // 查询的 URI
                PROJECTION,  // 查询的列
                null,        // 不使用查询条件
                null,        // 不需要查询参数
                null         // 不需要排序
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 当 Activity 恢复时，读取当前笔记的背景色
        if (mCursor != null) {
            mCursor.moveToFirst();  // 移动到第一条记录
            color = mCursor.getInt(COLUMN_INDEX_TITLE);  // 获取当前背景色
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在 Activity 暂停时，将选中的背景色保存到数据库
        ContentValues values = new ContentValues();
        values.put(NotePad.Notes.COLUMN_NAME_BACK_COLOR, color);
        getContentResolver().update(mUri, values, null, null);  // 更新数据库中的背景色
    }

    // 设置背景色为白色，并结束当前 Activity
    public void white(View view) {
        color = NotePad.Notes.DEFAULT_COLOR;
        finish();
    }

    // 设置背景色为黄色，并结束当前 Activity
    public void yellow(View view) {
        color = NotePad.Notes.YELLOW_COLOR;
        finish();
    }

    // 设置背景色为蓝色，并结束当前 Activity
    public void blue(View view) {
        color = NotePad.Notes.BLUE_COLOR;
        finish();
    }

    // 设置背景色为绿色，并结束当前 Activity
    public void green(View view) {
        color = NotePad.Notes.GREEN_COLOR;
        finish();
    }

    // 设置背景色为红色，并结束当前 Activity
    public void red(View view) {
        color = NotePad.Notes.RED_COLOR;
        finish();
    }
}
