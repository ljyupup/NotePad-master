package com.example.android.notepad;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

public class NoteSearch extends Activity implements SearchView.OnQueryTextListener {

    private ListView listView;
    private SQLiteDatabase sqLiteDatabase;

    /**
     * 查询时需要的列（字段）
     */
    private static final String[] PROJECTION = new String[]{
            NotePad.Notes._ID,                  // ID
            NotePad.Notes.COLUMN_NAME_TITLE,    // 标题
            NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE, // 修改日期
            NotePad.Notes.COLUMN_NAME_NOTE,     // 笔记内容
            NotePad.Notes.COLUMN_NAME_BACK_COLOR // 背景色
    };

    /**
     * SearchView 文本提交时的回调，通常用于触发搜索操作
     * @param query 用户输入的查询文本
     * @return false 表示继续执行其他操作（如果有）
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        // 当提交查询时显示用户输入的内容
        Toast.makeText(this, "您搜索的是：" + query, Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * onCreate 方法用于初始化 Activity
     * 1. 设置视图
     * 2. 初始化数据库
     * 3. 设置 SearchView 的监听器
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_search);

        // 获取布局中的 SearchView 和 ListView 控件
        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        listView = (ListView) findViewById(R.id.list_view);

        // 确保 ListView 已正确初始化
        if (listView == null) {
            Log.e("NoteSearch", "ListView is null in onCreate!");
        } else {
            Log.d("NoteSearch", "ListView initialized successfully");
        }

        // 获取 Intent 数据，如果没有，则使用默认 URI
        Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(NotePad.Notes.CONTENT_URI); // 默认使用笔记内容 URI
        }

        // 获取数据库的可读实例
        sqLiteDatabase = new NotePadProvider.DatabaseHelper(this).getReadableDatabase();

        // 设置 SearchView 显示提交按钮，并设置提示文本
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("查找");
        searchView.setOnQueryTextListener(this); // 设置查询文本监听器
    }

    /**
     * 查询文本变化时的回调方法
     * @param newText 用户输入的文本
     * @return true 表示已处理文本变化，false 表示继续处理
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        // 构建 SQL 查询条件，模糊匹配标题或笔记内容
        String selection = NotePad.Notes.COLUMN_NAME_TITLE + " LIKE ? OR " + NotePad.Notes.COLUMN_NAME_NOTE + " LIKE ?";
        String[] selectionArgs = {"%" + newText + "%", "%" + newText + "%"};

        // 执行查询，返回符合条件的记录
        Cursor cursor = sqLiteDatabase.query(
                NotePad.Notes.TABLE_NAME,   // 查询的表名
                PROJECTION,                 // 查询的列
                selection,                  // 查询条件
                selectionArgs,             // 查询条件的参数
                null,                       // 不需要分组
                null,                       // 不需要行分组
                NotePad.Notes.DEFAULT_SORT_ORDER // 排序规则
        );

        // 输出查询结果数量，用于调试
        Log.d("NoteSearch", "Query returned " + cursor.getCount() + " rows.");

        // 如果没有匹配的记录，输出调试信息
        if (cursor.getCount() == 0) {
            Log.d("NoteSearch", "No matching results found.");
        }

        // 配置 ListView 的适配器，展示查询结果
        String[] dataColumns = {
                NotePad.Notes.COLUMN_NAME_TITLE,         // 显示标题
                NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE, // 显示修改日期
                NotePad.Notes.COLUMN_NAME_NOTE          // 显示笔记内容
        };

        int[] viewIDs = {
                android.R.id.text1,  // 显示标题
                android.R.id.text2,  // 显示修改日期
                R.id.text3           // 显示笔记内容
        };

        // 创建自定义的适配器，并设置给 ListView
        MyAdapter adapter = new MyAdapter(
                this,                     // 上下文
                R.layout.noteslist_item,  // 列表项布局
                cursor,                   // 查询结果的 Cursor
                dataColumns,              // 数据列
                viewIDs                   // 显示控件的 ID
        );

        listView.setAdapter(adapter); // 设置 ListView 的适配器

        // 设置 ListView 的点击事件，选择一项后打开编辑界面或返回数据
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 根据记录的 ID 创建对应的 URI
                Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);

                // 获取传入 Intent 的 action 类型
                String action = getIntent().getAction();

                // 根据不同的 action 处理不同的操作
                if (Intent.ACTION_PICK.equals(action) || Intent.ACTION_GET_CONTENT.equals(action)) {
                    // 如果是 PICK 或 GET_CONTENT 操作，返回选中的 URI
                    setResult(RESULT_OK, new Intent().setData(uri));
                } else {
                    // 否则启动编辑 Activity
                    startActivity(new Intent(Intent.ACTION_EDIT, uri));
                }
            }
        });

        // 设置返回按钮的点击事件，返回到上一个页面
        Button backButton = (Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 结束当前 Activity
            }
        });

        return true;
    }
}
