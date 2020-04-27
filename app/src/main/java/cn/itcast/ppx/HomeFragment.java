package cn.itcast.ppx;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    private GridView mGridView;
    private GridView mGridView2;


    private String[] names={"登单于台","酬曹侍御","春风动春心","落叶","春夜喜雨","常恒阁"};

    private String[] titles={"中国小说史略","谁在收藏中国","爆发"};

    private int[] icons={R.drawable.book1,R.drawable.book2,R.drawable.book3,R.drawable.book4,R.drawable.book5,R.drawable.book6};

    public HomeFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_home, container, false);

        mGridView=(GridView)view.findViewById(R.id.gv_list);
        mGridView2=(GridView)view.findViewById(R.id.gv_list2);
        MyBaseAdapter1 mAdapter=new MyBaseAdapter1();
        MyBaseAdapter2 mAdapter2=new MyBaseAdapter2();
        mGridView.setAdapter(mAdapter);
        mGridView2.setAdapter(mAdapter2);
        return view;
    }

    class MyBaseAdapter1 extends BaseAdapter {

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return names[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                convertView=View.inflate(getContext(),R.layout.list_item_photo,null);
                holder=new ViewHolder();
                holder.mTexTView=convertView.findViewById(R.id.tv_title);
                holder.imageView=convertView.findViewById(R.id.iv_pic);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder) convertView.getTag();
            }
            holder.mTexTView.setText(names[position]);
            holder.imageView.setBackgroundResource(icons[position]);
            return convertView;
        }
         class ViewHolder{
            TextView mTexTView;
            ImageView imageView;
        }
    }

    class MyBaseAdapter2 extends BaseAdapter {

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Object getItem(int position) {
            return titles[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder2 holder;

            if (convertView == null) {
                convertView=View.inflate(getContext(),R.layout.list_item_photo,null);
                holder=new ViewHolder2();
                holder.mTexTView=convertView.findViewById(R.id.tv_title);
                holder.mPrice=convertView.findViewById(R.id.tv_time);
                holder.imageView=convertView.findViewById(R.id.iv_pic);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder2) convertView.getTag();
            }
            holder.mTexTView.setText(titles[position]);
            holder.mPrice.setText("4.99贝");
            holder.mPrice.setTextColor(Color.RED);
            holder.imageView.setBackgroundResource(icons[position]);
            return convertView;
        }
        class ViewHolder2{
            TextView mTexTView;
            TextView mPrice;
            ImageView imageView;
        }
    }

}
