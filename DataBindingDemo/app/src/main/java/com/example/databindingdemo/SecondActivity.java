package com.example.databindingdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sjk on 17-6-12.
 */

public class SecondActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<User>> {

    private ListView listView;
    private UniversalAdapter<User> adapter;
    private static final int LOADER_ID = 29;
    private List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        listView = (ListView) findViewById(R.id.listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = userList.get(position).getUsername();

                Toast.makeText(SecondActivity.this, name, Toast.LENGTH_SHORT).show();
            }
        });

        // BR.user中的BR是binding生成的R类，用来记录要绑定的资源（variable）的id
        adapter = new UniversalAdapter<>(this, R.layout.activity_second_list_item, userList, BR.user);
        listView.setAdapter(adapter);

        getSupportLoaderManager()
                .initLoader(LOADER_ID, savedInstanceState, this);
    }

    private void setItems(List<User> newUsers) {
        userList.clear();
        userList.addAll(newUsers);
        adapter.notifyDataSetChanged();
    }

    private void addItems(List<User> newUsers) {
        userList.addAll(newUsers);
        adapter.notifyDataSetChanged();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle bundle) {
        LogUtil.log("onCreateLoader: " + Thread.currentThread().getId());

        return new MyLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<User>> loader, List<User> data) {
        LogUtil.log("onLoadFinished: " + Thread.currentThread().getId());

        // 更新数据到view
        setItems(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        LogUtil.log("onLoaderReset: " + Thread.currentThread().getId());
    }


    /**
     * 类的继承关系为：
     *  Loader-->AsyncTaskLoader-->CursorLoader
     *
     *  一般的网络异步请求，使用AsyncTaskLoader即可
     *  CursorLoader涉及到例如ContentProvider中的Cursor类
     *  */
    private static class MyLoader extends AsyncTaskLoader<List<User>> {

        public MyLoader(Context context) {
            super(context);
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }

        @Override
        public List<User> loadInBackground() {
            LogUtil.log("loadInBackground: " + Thread.currentThread().getId());

            //  模拟耗时操作
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            List<User> ret = new ArrayList<>();

            ret.add(new User("Astro", "android", "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2434560235,935681266&fm=58"));
            ret.add(new User("Bender", "android", "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=2218701630,3728047195&fm=58"));
            ret.add(new User("Cupcake", "android 1.5", "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2434560235,935681266&fm=58"));
            ret.add(new User("Donut", "android 1.6", "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=2218701630,3728047195&fm=58"));
            ret.add(new User("Eclair", "android 2.0/2.1", "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=2218701630,3728047195&fm=58"));
            ret.add(new User("Froyo", "android 2.2", "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2434560235,935681266&fm=58"));
            ret.add(new User("Gingerbread", "android 2.3", "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2434560235,935681266&fm=58"));
            ret.add(new User("Honeycomb", "android 3.0", "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2434560235,935681266&fm=58"));
            ret.add(new User("Ice Cream Sandwich", "android 4.0", "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=2218701630,3728047195&fm=58"));
            ret.add(new User("Jelly Bean", "android 4.1/4.2/4.3", "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2434560235,935681266&fm=58"));

            return ret;
        }
    }
}
