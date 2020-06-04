package cn.itcast.ppx.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.itcast.ppx.R;
import cn.itcast.ppx.domain.FriListBean;

public class FrilistAdapter extends BaseAdapter {
    private List<FriListBean> friBeanList; //聊天数据
    private LayoutInflater layoutInflater;
    public FrilistAdapter(List<FriListBean> friBeanList, Context context) {
        this.friBeanList = friBeanList;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return friBeanList.size();
    }
    @Override
    public Object getItem(int position) {
        return friBeanList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {
        Holder holder = new Holder();

            //加载右边布局，也就是用户对应的布局信息
            contentView = layoutInflater.inflate(R.layout.fri_list_item, null);

        holder.tv_userId = (TextView) contentView.findViewById(R.id.tv_userid);
        holder.btn_delete=(Button)contentView.findViewById(R.id.btn_delete);
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EMClient.getInstance().contactManager().deleteContact(friBeanList.get(position).getUserId());
                    if(friBeanList.size()>0){
                        friBeanList.remove(position);
                        notifyDataSetChanged();
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
        holder.tv_userId.setText(friBeanList.get(position).getUserId());
        return contentView;
    }
    class Holder {
        public TextView tv_userId;
        public Button btn_delete;
    }
}

