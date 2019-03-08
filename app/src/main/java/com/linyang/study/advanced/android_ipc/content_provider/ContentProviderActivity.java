package com.linyang.study.advanced.android_ipc.content_provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.view.View;

import com.linyang.study.R;
import com.linyang.study.app.BaseActivity;
import com.linyang.study.app.util.L;

import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述:
 * Created by fzJiang on 2018-11-08
 */
public class ContentProviderActivity extends BaseActivity {

    @BindView(R.id.btn_add)
    AppCompatButton mBtnAdd;
    @BindView(R.id.btn_delete)
    AppCompatButton mBtnDelete;
    @BindView(R.id.btn_modify)
    AppCompatButton mBtnModify;
    @BindView(R.id.btn_query)
    AppCompatButton mBtnQuery;

    private int idValue = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_content_provider_test;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.btn_add, R.id.btn_delete, R.id.btn_modify, R.id.btn_query})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                addBook();
                break;

            case R.id.btn_delete:
                deleteBook();
                break;

            case R.id.btn_modify:
                updateBook();
                break;

            case R.id.btn_query:
                queryBook();
                break;
        }
    }

    private void addBook() {
        ContentValues values = new ContentValues();
        idValue++;
        values.put("_id", idValue);
        values.put("name", "Android开发与艺术" + idValue);
        getContentResolver().insert(IContentProvider.BOOK_CONTENT_URI, values);
    }

    private void deleteBook() {
        int count = getContentResolver().delete(IContentProvider.BOOK_CONTENT_URI, "_id=?", new String[]{"5"});
        L.i("删除成功：" + count + ",总：" + idValue);

        if (count > 0 && count < idValue) {
            idValue -= count;
        }
    }

    private void updateBook() {
        ContentValues values = new ContentValues();
        values.put("_id", idValue);
        values.put("name", "Android开发与艺术2" + idValue);
        getContentResolver().update(IContentProvider.BOOK_CONTENT_URI, values, null, null);
    }

    private void queryBook() {
        Cursor bookCursor = getContentResolver().query(IContentProvider.BOOK_CONTENT_URI, new String[]{"_id", "name"}, null, null, null);
        if (bookCursor != null) {
            idValue = 1;
            while (bookCursor.moveToNext()) {
                idValue++;
                int id = bookCursor.getInt(0);
                String name = bookCursor.getString(1);
                L.i("id:" + id + ",name:" + name);
            }
            bookCursor.close();
        }
    }
}
