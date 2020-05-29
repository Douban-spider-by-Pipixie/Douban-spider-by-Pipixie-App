package cn.itcast.ppx.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cn.itcast.ppx.R;

public class UserRegister extends AppCompatActivity {

    String TAG = UserRegister.class.getCanonicalName();

    private EditText et_data_uname;
    private EditText et_data_upass1;
    private EditText et_data_upass2;
    private RelativeLayout rl_activity_register;
    private HashMap<String, String> stringHashMap;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_data_uname = (EditText) findViewById(R.id.et_registeractivity_username);
        et_data_upass1 = (EditText) findViewById(R.id.et_registeractivity_password1);
        et_data_upass2 = (EditText) findViewById(R.id.et_registeractivity_password2);
        rl_activity_register=(RelativeLayout)findViewById(R.id.rl_activity_register);
        stringHashMap = new HashMap<>();

        rl_activity_register.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
    }

    public void RegisterGET(View view) {
        stringHashMap.put("name", et_data_uname.getText().toString());
        stringHashMap.put("password", et_data_upass1.getText().toString());
        new Thread(getRun).start();
    }

    public void LoginPage(View view) {
        Timer time = new Timer();
        TimerTask tk = new TimerTask() {
            Intent intent = new Intent(UserRegister.this, UserLogin.class);
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        };time.schedule(tk,500);
    }

    /**
     * get请求线程
     */
    Runnable getRun = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            requestGet(stringHashMap);
        }
    };


    /**
     * get提交数据
     *
     * @param paramsMap
     */
    private void requestGet(HashMap<String, String> paramsMap) {
        try {
            if((et_data_upass1.getText().toString().equals(et_data_upass2.getText().toString()))&&!TextUtils.isEmpty(et_data_uname.getText().toString())&&!TextUtils.isEmpty(et_data_upass1.getText().toString())){
                String baseUrl = "http://106.52.239.252:9988/reg?";
                StringBuilder tempParams = new StringBuilder();
                int pos = 0;
                for (String key : paramsMap.keySet()) {
                    if (pos > 0) {
                        tempParams.append("&");
                    }
                    tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                    pos++;
                }

                Log.e(TAG,"params--get-->>"+tempParams.toString());
                String requestUrl = baseUrl + tempParams.toString();
                // 新建一个URL对象
                URL url = new URL(requestUrl);
                // 打开一个HttpURLConnection连接
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                // 设置连接主机超时时间
                urlConn.setConnectTimeout(5 * 1000);
                //设置从主机读取数据超时
                urlConn.setReadTimeout(5 * 1000);
                // 设置是否使用缓存  默认是true
                urlConn.setUseCaches(true);
                // 设置为Post请求
                urlConn.setRequestMethod("GET");
                //urlConn设置请求头信息
                //设置请求中的媒体类型信息。
                urlConn.setRequestProperty("Content-Type", "application/json");
                //设置客户端与服务连接类型
                urlConn.addRequestProperty("Connection", "Keep-Alive");
                // 开始连接
                urlConn.connect();
                // 判断请求是否成功
                if (urlConn.getResponseCode() == 200) {
                    // 获取返回的数据
                    String result = streamToString(urlConn.getInputStream());
                    Log.e(TAG, "Get方式请求成功，result--->" + result);
                    if(result.contentEquals("true")){
                        try{
                            EMClient.getInstance().createAccount(et_data_uname.getText().toString().trim(),et_data_upass1.getText().toString().trim());
                            Log.e("ppx","注册成功");
                        }catch (HyphenateException e){
                            e.printStackTrace();
                            Log.e("ppx","注册失败"+e.getErrorCode()+","+e.getMessage());
                        }
                        Looper.prepare();
                        Toast.makeText(this,"注册成功",Toast.LENGTH_LONG).show();
                        Timer time = new Timer();
                        TimerTask tk = new TimerTask() {
                            Intent intent = new Intent(UserRegister.this, UserLogin.class);
                            @Override
                            public void run() {
                                startActivity(intent);
                                finish();
                            }
                        };time.schedule(tk,2000);
                        Looper.loop();
                    }else{
                        Looper.prepare();
                        Toast.makeText(this,"账号已被注册",Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } else {
                    Looper.prepare();
                    Toast.makeText(this,"网络出现错误",Toast.LENGTH_LONG).show();
                    Looper.prepare();
                    Log.e(TAG, "Get方式请求失败");
                }
                // 关闭连接
                urlConn.disconnect();
            }else if((!et_data_upass1.getText().toString().equals(et_data_upass2.getText().toString()))&&!TextUtils.isEmpty(et_data_uname.getText().toString())){
                Looper.prepare();
                Toast.makeText(this,"输入密码不一致，请重新输入",Toast.LENGTH_LONG).show();
                Looper.loop();
            }else if(TextUtils.isEmpty(et_data_uname.getText().toString())){
                Looper.prepare();
                Toast.makeText(this,"请输入用户名",Toast.LENGTH_LONG).show();
                Looper.loop();
            }else if(TextUtils.isEmpty(et_data_upass1.getText().toString())&&!TextUtils.isEmpty(et_data_uname.getText().toString())) {
                Looper.prepare();
                Toast.makeText(this, "请输入密码", Toast.LENGTH_LONG).show();
                Looper.loop();
            }else if(TextUtils.isEmpty(et_data_upass2.getText().toString())&&!TextUtils.isEmpty(et_data_uname.getText().toString())&&!TextUtils.isEmpty(et_data_upass1.getText().toString())) {
                Looper.prepare();
                Toast.makeText(this, "请再次输入密码", Toast.LENGTH_LONG).show();
                Looper.loop();
            }else{
                Looper.prepare();
                Toast.makeText(this, "未知错误", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }



    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
    public String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
}
