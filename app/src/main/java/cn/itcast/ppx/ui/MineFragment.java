package cn.itcast.ppx.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import cn.itcast.ppx.R;

public class MineFragment extends Fragment {


    private ListView mListView;

    private Button mExit;

    public MineFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_mine, container, false);

        mListView=(ListView) view.findViewById(R.id.lv_list);
        mExit=(Button)view.findViewById(R.id.btn_logout);
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(getContext(), UserLogin.class));
                    }

                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
        MyAdapter myAdapter=new MyAdapter();

        mListView.setAdapter(myAdapter);

        setListViewHeightBasedOnChildren(mListView);


        return view;
    }




    public void logout(boolean unbindDeviceToken, final EMCallBack callback){
        EMClient.getInstance().logout(unbindDeviceToken, new EMCallBack(){
            @Override
            public void onSuccess() {
                Log.d("TAG", "logout: onSuccess");
                if (callback != null) {
                    callback.onSuccess();
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.d("TAG", "logout: onSuccess");
                if (callback != null) {
                    callback.onError(i, s);
                }
            }

            @Override
            public void onProgress(int i, String s) {
                if (callback != null) {
                    callback.onProgress(i, s);
                }
            }
        });
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView=View.inflate(getContext(),R.layout.dongtai_list_item,null);
                holder=new ViewHolder();
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder) convertView.getTag();
            }
            return convertView;
        }
    }

    class ViewHolder{
        TextView mTitle;
        TextView mContent;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() *(listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

}
