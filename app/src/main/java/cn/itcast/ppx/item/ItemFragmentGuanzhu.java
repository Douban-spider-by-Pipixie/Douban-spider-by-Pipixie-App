package cn.itcast.ppx.item;

import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import cn.itcast.ppx.R;

public class ItemFragmentGuanzhu extends Fragment {

    private ListView mListView;

    private String[] titles={"上海","北京","产品经理","产品经理","产品经理"};

    private String[] contents={"一个月了还没找到合适的工作","今年的北京怎么了，从来没有这么难过，职位少，面试更少，石沉大海，你们呢？","在拉勾做产品时说明样子的体验？","热议|你在街道说明需求时瞬间想打人？","热议|你在街道说明需求时瞬间想打人？"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item2, container, false);

        mListView=(ListView) view.findViewById(R.id.lv_list);

        MyAdapter myAdapter=new MyAdapter();

        mListView.setAdapter(myAdapter);

        setListViewHeightBasedOnChildren(mListView);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private class MyAdapter extends BaseAdapter {

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
            ViewHolder holder;
            if (convertView == null) {
                convertView=View.inflate(getContext(),R.layout.guanzhu_list_item,null);
                holder=new ViewHolder();
                holder.mTitle=convertView.findViewById(R.id.tv_title);
                holder.mContent=convertView.findViewById(R.id.tv_content);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder) convertView.getTag();
            }
            holder.mTitle.setText(titles[position]);
            holder.mContent.setText(contents[position]);
            holder.mTitle.setTextColor(Color.BLACK);
            holder.mContent.setTextColor(Color.BLACK);
            holder.mContent.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
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
