package cn.itcast.ppx.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

import cn.itcast.ppx.SearchActivity;
import cn.itcast.ppx.domain.BooksTab;
import cn.itcast.ppx.DetailInfo;
import cn.itcast.ppx.utils.JsonParse;
import cn.itcast.ppx.R;
import cn.itcast.ppx.utils.CacheUtils;


public class HomeFragment extends Fragment {

    private List<BooksTab> mBooksTabs;
    private BooksTab mBooksTab;
    private BitmapUtils mBitmapUtils;
    private TextView mTitle;
    private ImageView mIcon;

    private GridView mGridView1;
    private GridView mGridView2;
    private EditText mSearchEdit;
    private ImageButton mSearchBtn;
    private LinearLayout mHome;
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_home, container, false);

        mGridView1=(GridView) view.findViewById(R.id.gv_list);
        mGridView2=(GridView) view.findViewById(R.id.gv_list2);
        mSearchEdit=(EditText)view.findViewById(R.id.et_search);
        mSearchBtn=(ImageButton) view.findViewById(R.id.ib_search);
        mHome=(LinearLayout)view.findViewById(R.id.ll_home);
        mTitle=(TextView)view.findViewById(R.id.tv_title);
        mIcon=(ImageView)view.findViewById(R.id.iv_pic);

        mHome.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(mSearchEdit.getWindowToken(),0);
            }
        });

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search_content=mSearchEdit.getText().toString().trim();
                Intent intent=new Intent(getContext(), SearchActivity.class);
                intent.putExtra("search_content",search_content);
                startActivity(intent);
            }
        });

        String cache=CacheUtils.getCache(getContext(),"http://106.52.239.252:9988/test?p=getbook");
//        if(!TextUtils.isEmpty(cache)){
//            System.out.println("发现缓存");
//            System.out.println(cache);
//            //有缓存
//            processData(cache);
//        }
        //继续请求服务器数据，保存缓存最新
        getDataFromServer();

        return view;
    }


    private void getDataFromServer(){
        HttpUtils utils_getbook=new HttpUtils();
        utils_getbook.send(HttpRequest.HttpMethod.GET, "http://106.52.239.252:9988/test?p=getbook",
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result=responseInfo.result;
                        System.out.println("服务器数据:"+result);
                        if(result==null){
                            Toast.makeText(getContext(),"解析失败",Toast.LENGTH_SHORT).show();
                        }else{
                            processData(result);

                            MyBaseAdapter1 myBaseAdapter1=new MyBaseAdapter1();
                            MyBaseAdapter2 myBaseAdapter2=new MyBaseAdapter2();

                            mGridView1.setAdapter(myBaseAdapter1);
                            mGridView2.setAdapter(myBaseAdapter2);

                            mTitle.setText(mBooksTabs.get(3).getName());
                            mBitmapUtils=new BitmapUtils(getContext());
                            String topimage=mBooksTabs.get(3).getImg();//图片的下载链接
                            mIcon.setScaleType(ImageView.ScaleType.FIT_XY);//设置缩放模式，图片宽高匹配
                            mBitmapUtils.display(mIcon,topimage);
                            mIcon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(getContext(), DetailInfo.class);
                                    BooksTab booksTab=mBooksTabs.get(3);
                                    intent.putExtra("Book_Id",booksTab.getId().trim());
                                    intent.putExtra("Book_Author",booksTab.getAuthor());
                                    intent.putExtra("Book_Date",booksTab.getDate());
                                    intent.putExtra("Book_Name",booksTab.getName());
                                    intent.putExtra("Book_Price",booksTab.getPrice());
                                    intent.putExtra("Book_Img",booksTab.getImg());
                                    startActivity(intent);
                                }
                            });

                            mGridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    System.out.println("当前位置"+ position);
                                    Intent intent=new Intent(getContext(), DetailInfo.class);
                                    BooksTab booksTab=mBooksTabs.get(position);
                                    intent.putExtra("Book_Id",booksTab.getId().trim());
                                    intent.putExtra("Book_Author",booksTab.getAuthor());
                                    intent.putExtra("Book_Date",booksTab.getDate());
                                    intent.putExtra("Book_Name",booksTab.getName());
                                    intent.putExtra("Book_Price",booksTab.getPrice());
                                    intent.putExtra("Book_Img",booksTab.getImg());
                                    startActivity(intent);
                                }
                            });

                            mGridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    System.out.println("当前位置"+ position);
                                    Intent intent=new Intent(getContext(), DetailInfo.class);
                                    BooksTab booksTab=mBooksTabs.get(position);
                                    intent.putExtra("Book_Id",booksTab.getId().trim());
                                    intent.putExtra("Book_Author",booksTab.getAuthor());
                                    intent.putExtra("Book_Date",booksTab.getDate());
                                    intent.putExtra("Book_Name",booksTab.getName());
                                    intent.putExtra("Book_Price",booksTab.getPrice());
                                    intent.putExtra("Book_Img",booksTab.getImg());
                                    startActivity(intent);
                                }
                            });
                            //Toast.makeText(getContext(),"解析成功",Toast.LENGTH_SHORT).show();
                            //写缓存
                           //CacheUtils.setCache(getContext(),"http://106.52.239.252:9988/test?p=getbook",result);
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
        mBooksTabs= JsonParse.getBooksTab(json);
        System.out.println("解析的结果："+mBooksTabs);
    }

    class MyBaseAdapter1 extends BaseAdapter {

        @Override
        public int getCount() {
            return 6;
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
                holder.mPublish=convertView.findViewById(R.id.tv_publish);
                holder.mStar=convertView.findViewById(R.id.tv_star);
                holder.imageView=convertView.findViewById(R.id.iv_pic);
                mBooksTab=mBooksTabs.get(position);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder) convertView.getTag();
            }
            holder.mTexTView.setText(mBooksTab.getName());
            holder.mPublish.setText(mBooksTab.getPublish().trim());
            holder.mStar.setText("豆瓣评分："+mBooksTab.getStar());
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
            TextView mPublish;
            TextView mStar;
        }
    }

    class MyBaseAdapter2 extends BaseAdapter {

        @Override
        public int getCount() {
            return 6;
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
                convertView = View.inflate(getContext(), R.layout.list_item_photo, null);
                holder = new ViewHolder();
                holder.mTexTView = convertView.findViewById(R.id.tv_title);
                holder.mPublish = convertView.findViewById(R.id.tv_publish);
                holder.mStar = convertView.findViewById(R.id.tv_star);
                holder.imageView = convertView.findViewById(R.id.iv_pic);
                mBooksTab = mBooksTabs.get(position);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mTexTView.setText(mBooksTab.getName());
            holder.mPublish.setText(mBooksTab.getDate());
            holder.mStar.setText("豆瓣评分：" + mBooksTab.getStar());
            //holder.imageView.setBackgroundResource(icons[position]);
            mBitmapUtils = new BitmapUtils(getActivity());
            String topimage = mBooksTab.getImg();//图片的下载链接
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);//设置缩放模式，图片宽高匹配
            mBitmapUtils.display(holder.imageView, topimage);
            return convertView;
        }

        class ViewHolder {
            TextView mTexTView;
            ImageView imageView;
            TextView mPublish;
            TextView mStar;
        }
    }
}
