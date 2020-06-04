package cn.itcast.ppx;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import cn.itcast.ppx.domain.BooksTab;
import cn.itcast.ppx.utils.JsonParse;

public class SearchActivity extends AppCompatActivity {

    private GridView mSearchList;
    private List<BooksTab> mBooksTabs;
    private BitmapUtils mBitmapUtils;
    private BooksTab mBooksTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchList=(GridView)findViewById(R.id.gv_searchlist);

        getDataFromServer();

    }

    private void getDataFromServer() {
        HttpUtils utils_getbook = new HttpUtils();
        String search_content=getIntent().getStringExtra("search_content");
        utils_getbook.send(HttpRequest.HttpMethod.GET, "http://106.52.239.252:9988/test?p=search&s="+search_content,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        System.out.println("服务器数据:" + result);
                        if (result == null) {
                            Toast.makeText(SearchActivity.this, "好像什么也没有哦", Toast.LENGTH_SHORT).show();
                        } else {
                            processData(result);

                            SearchListAdapter adapter = new SearchListAdapter();

                            mSearchList.setAdapter(adapter);

                            mSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    System.out.println("当前位置" + position);
                                    Intent intent = new Intent(SearchActivity.this, DetailInfo.class);
                                    BooksTab booksTab = mBooksTabs.get(position);
                                    intent.putExtra("Book_Id", booksTab.getId().trim());
                                    intent.putExtra("Book_Author", booksTab.getAuthor());
                                    intent.putExtra("Book_Date", booksTab.getDate());
                                    intent.putExtra("Book_Name", booksTab.getName());
                                    intent.putExtra("Book_Price", booksTab.getPrice());
                                    intent.putExtra("Book_Img", booksTab.getImg());
                                    startActivity(intent);
                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        error.printStackTrace();
                        Toast.makeText(SearchActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    protected void processData(String json){
        mBooksTabs= JsonParse.getBooksTab(json);
        System.out.println("解析的结果："+mBooksTabs);
    }

    class SearchListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mBooksTabs.size();
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
                convertView=View.inflate(SearchActivity.this,R.layout.list_item_photo,null);
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
            mBitmapUtils=new BitmapUtils(SearchActivity.this);
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
}
