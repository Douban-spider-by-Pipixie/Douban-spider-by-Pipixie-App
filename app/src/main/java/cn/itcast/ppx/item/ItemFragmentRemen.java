package cn.itcast.ppx.item;

import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import cn.itcast.ppx.R;

public class ItemFragmentRemen extends Fragment {

    private GridView mGridView;

    private ListView mListView;

    private String[] names={"#小仲马#","#假日书单#","#读后感#"};

    private String[] titles={"1984讲的是什么故事？","《切尔诺贝利》描述的是真的吗？","《三体》什么时候发布"};

    private String[] sum={"995参与","166万参与","1.4万参与"};

    private int[] icons={R.drawable.book7,R.drawable.book8,R.drawable.book9};

    private int[] icons2={R.drawable.list1,R.drawable.list2,R.drawable.list3};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item, container, false);
        mGridView=(GridView)view.findViewById(R.id.gv_list);
        mListView=(ListView) view.findViewById(R.id.lv_list);

        MyBaseAdapter mAdapter=new MyBaseAdapter();
        MyAdapter myAdapter2=new MyAdapter();

        mGridView.setAdapter(mAdapter);
        mListView.setAdapter(myAdapter2);
        setListViewHeightBasedOnChildren(mListView);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    class MyBaseAdapter extends BaseAdapter {

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
                holder.mPrice=convertView.findViewById(R.id.tv_time);
                holder.imageView=convertView.findViewById(R.id.iv_pic);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder) convertView.getTag();
            }
            holder.mTexTView.setText(names[position]);
            holder.mPrice.setText(sum[position]);
            holder.mPrice.setTextColor(Color.GRAY);
            holder.imageView.setBackgroundResource(icons[position]);
            return convertView;
        }
        class ViewHolder{
            TextView mTexTView;
            TextView mPrice;
            ImageView imageView;
        }
    }

    private class MyAdapter extends BaseAdapter{

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
                convertView=View.inflate(getContext(),R.layout.remen_list_item,null);
                holder=new ViewHolder2();
                holder.mTitle=convertView.findViewById(R.id.tv_title);
                holder.mContent=convertView.findViewById(R.id.tv_content);
                holder.mComment=convertView.findViewById(R.id.tv_comment);
                holder.mReadCount=convertView.findViewById(R.id.tv_read);
                holder.imageView=convertView.findViewById(R.id.iv_icon);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder2) convertView.getTag();
            }
            holder.mTitle.setText(titles[position]);
            holder.mContent.setText("现在在朋友圈公布恋情已经非常普遍，但是有人");
            holder.mComment.setText("6970讨论");
            holder.mReadCount.setText("820.2万阅读");
            holder.mTitle.setTextColor(Color.BLACK);
            holder.mComment.setTextColor(Color.GRAY);
            holder.mReadCount.setTextColor(Color.GRAY);
            holder.imageView.setBackgroundResource(icons2[position]);
            return convertView;
        }
    }

    class ViewHolder2{
        TextView mTitle;
        TextView mComment;
        TextView mContent;
        TextView mReadCount;
        ImageView imageView;
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
