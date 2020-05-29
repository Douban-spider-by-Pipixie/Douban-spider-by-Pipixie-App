package cn.itcast.ppx;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

import cn.itcast.ppx.utils.CacheUtils;
import cn.itcast.ppx.view.RefreshLishView;


public class HomeFragment extends Fragment {

    private List<BooksTab> mBooksTabs;
    private BooksTab mBooksTab;
    private BitmapUtils mBitmapUtils;

    private GridView mGridView1;
    private GridView mGridView2;

    protected Activity mActivity;


    private String[] names={"登单于台","酬曹侍御","春风动春心","落叶","春夜喜雨","常恒阁"};

    private String[] titles={"中国小说史略","谁在收藏中国","爆发"};

    private int[] icons={R.drawable.book1,R.drawable.book2,R.drawable.book3,R.drawable.book4,R.drawable.book5,R.drawable.book6};

    public HomeFragment(){
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mActivity=(Activity)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_home, container, false);

        mGridView1=(GridView) view.findViewById(R.id.gv_list);
        mGridView2=(GridView) view.findViewById(R.id.gv_list2);

        //CacheUtils.setCache(getContext(),"http://106.52.239.252:9988/test?p=getbook","result");

        String cache=CacheUtils.getCache(getContext(),"http://106.52.239.252:9988/test?p=getbook");
        if(!TextUtils.isEmpty(cache)){
            System.out.println("发现缓存");
            System.out.println(cache);
            //有缓存
            processData(cache);
        }
        //继续请求服务器数据，保存缓存最新
        getDataFromServer();

        MyBaseAdapter1 myBaseAdapter1=new MyBaseAdapter1();
        MyBaseAdapter2 myBaseAdapter2=new MyBaseAdapter2();

        mGridView1.setAdapter(myBaseAdapter1);
        mGridView2.setAdapter(myBaseAdapter2);
        return view;
    }


    private void getDataFromServer(){
        HttpUtils utils=new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, "http://106.52.239.252:9988/test?p=getbook",
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result=responseInfo.result;
                        System.out.println("服务器数据:"+result);
                        if(result==null){
                            Toast.makeText(getContext(),"解析失败",Toast.LENGTH_SHORT).show();
                        }else{
                            processData(result);
                            //Toast.makeText(getContext(),"解析成功",Toast.LENGTH_SHORT).show();
                            //写缓存
                           CacheUtils.setCache(getContext(),"http://106.52.239.252:9988/test?p=getbook",result);
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        error.printStackTrace();
                        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
                    }
                });
    }

    protected void processData(String json){
        mBooksTabs=JsonParse.getBooksTab(json);
        System.out.println("解析的结果："+mBooksTabs);
    }

    class MyBaseAdapter1 extends BaseAdapter {

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return null;
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
                holder.mDate=convertView.findViewById(R.id.tv_time);
                holder.imageView=convertView.findViewById(R.id.iv_pic);
                mBooksTab=mBooksTabs.get(position);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder) convertView.getTag();
            }
            holder.mTexTView.setText(mBooksTab.getName());
            holder.mDate.setText(mBooksTab.getDate());
            //holder.imageView.setBackgroundResource(icons[position]);
            mBitmapUtils=new BitmapUtils(getActivity());
            String topimage=mBooksTab.getImg();//图片的下载链接
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);//设置缩放模式，图片宽高匹配
            mBitmapUtils.display(holder.imageView,topimage);
            return convertView;
        }
         class ViewHolder{
            TextView mTexTView;
            ImageView imageView;
            TextView mDate;
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
