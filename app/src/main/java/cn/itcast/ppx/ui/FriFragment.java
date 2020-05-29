package cn.itcast.ppx.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.itcast.ppx.R;

public class FriFragment extends Fragment {

    private TextView mTime;

    private ListView mListView;

    public FriFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fri,container,false);

        mTime=view.findViewById(R.id.tv_time);
        new TimeThread().start();

        mListView=(ListView) view.findViewById(R.id.lv_fri);

        MyAdapter myAdapter=new MyAdapter();

        mListView.setAdapter(myAdapter);

        return view;
    }

    public class TimeThread extends Thread{
        @Override
        public void run() {
            super.run();
            do{
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);
        }
    }
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    mTime.setText(new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis())));
                    break;
            }
            return false;
        }
    });


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 7;
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
                convertView=View.inflate(getContext(),R.layout.fri_list_item,null);
                holder=new ViewHolder();
                holder.mTitle=convertView.findViewById(R.id.tv_title);
                holder.mImageView=convertView.findViewById(R.id.iv_icon);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder) convertView.getTag();
            }
            return convertView;
        }
    }

    class ViewHolder{
        TextView mTitle;
        ImageView mImageView;
    }

}
