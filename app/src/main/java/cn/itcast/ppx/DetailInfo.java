package cn.itcast.ppx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import cn.itcast.ppx.domain.BooksDetailTab;
import cn.itcast.ppx.ui.HomeFragment;
import cn.itcast.ppx.utils.CacheUtils;


public class DetailInfo extends AppCompatActivity {

    private ImageButton mBack;
    private ImageView mImg;
    private TextView mName;
    private TextView mAuthor;
    private TextView mDate;
    private TextView mPrice;
    private TextView mBookIntroduction;
    private TextView mAuthorIntroduction;
    private TextView mCatalogue;

    private BitmapUtils mBitmapUtils;

    private BooksDetailTab mBooksDetailTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);

        mBack=(ImageButton)findViewById(R.id.btn_back);
        mImg=(ImageView)findViewById(R.id.iv_icon);
        mName=(TextView)findViewById(R.id.tv_title);
        mAuthor=(TextView)findViewById(R.id.tv_author);
        mDate=(TextView)findViewById(R.id.tv_date);
        mPrice=(TextView)findViewById(R.id.tv_price);
        mBookIntroduction=(TextView)findViewById(R.id.tv_authorIntroduction);
        mAuthorIntroduction=(TextView)findViewById(R.id.tv_bookIntroduction);
        mCatalogue=(TextView)findViewById(R.id.tv_table);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.btn_back:
                        finish();
                        break;
                    default:
                        break;
                }
            }
        });

        String author=getIntent().getStringExtra("Book_Author");
        String name=getIntent().getStringExtra("Book_Name");
        String date=getIntent().getStringExtra("Book_Date");
        String price=getIntent().getStringExtra("Book_Price");
        String img=getIntent().getStringExtra("Book_Img");

        mName.setText(name);
        mAuthor.setText(author);
        mDate.setText(date);
        mPrice.setText(price);

        mBitmapUtils=new BitmapUtils(this);
        String topimage=img;//图片的下载链接
        mImg.setScaleType(ImageView.ScaleType.FIT_XY);//设置缩放模式，图片宽高匹配
        mBitmapUtils.display(mImg,topimage);

        getDataFromServer();

    }


    private void getDataFromServer(){
        HttpUtils utils=new HttpUtils();
        String id=getIntent().getStringExtra("Book_Id");

            utils.send(HttpRequest.HttpMethod.GET, "http://106.52.239.252:9988/test?p=detail&id=" + id,
                    new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            String result = responseInfo.result;
                            System.out.println("服务器数据:" + result);
                            if (result.equals("null")) {
                                Toast.makeText(getApplicationContext(), "数据暂时为空哦", Toast.LENGTH_SHORT).show();
                            } else {
                                processData(result);
                                mAuthorIntroduction.setText(mBooksDetailTab.getAuthorIntroduction());
                                mBookIntroduction.setText(mBooksDetailTab.getBookIntroduction());
                                mCatalogue.setText(mBooksDetailTab.getTable());
                            }
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {

                        }
                    });
    }

    private void processData(String json) {
        Gson gson=new Gson();
        mBooksDetailTab=gson.fromJson(json,BooksDetailTab.class);
        System.out.println("详情页解析的结果："+mBooksDetailTab);
    }

}
