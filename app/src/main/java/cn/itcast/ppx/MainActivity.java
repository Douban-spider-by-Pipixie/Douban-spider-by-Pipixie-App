package cn.itcast.ppx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private EditText et_username;
    private EditText et_pwd;
    private CheckBox cb_isSave;
    private Button btn_login;
    private Button btn_lost;
    private Button btn_register;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_username=(EditText) findViewById(R.id.et_username);
        et_pwd=(EditText) findViewById(R.id.et_password);
        cb_isSave=(CheckBox) findViewById(R.id.cb_isSave);
        btn_login=(Button)findViewById(R.id.btn_login);
        btn_lost=(Button)findViewById(R.id.btn_lost);
        btn_lost.getBackground().setAlpha(0);
        btn_register=(Button)findViewById(R.id.btn_register);
        btn_register.getBackground().setAlpha(0);
        btn_login.setOnClickListener(this);
        //获取sp对象
        sp=getSharedPreferences("info",MODE_PRIVATE);

        boolean isSave=sp.getBoolean("isChecked",false);
        if(isSave){
            String username=sp.getString("username","");
            String pwd=sp.getString("pwd","");
            et_username.setText(username);
            et_pwd.setText(pwd);
            cb_isSave.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        String username=et_username.getText().toString().trim();
        String pwd=et_pwd.getText().toString().trim();
        if(TextUtils.isEmpty(pwd)||TextUtils.isEmpty(username)){
            Toast.makeText(this,"用户名密码不能为空",Toast.LENGTH_SHORT).show();;
        }else{
            SharedPreferences.Editor editor=sp.edit();
            boolean checked= cb_isSave.isChecked();
            if(checked){
                //通过sp对象获取编辑器
                editor.putString("username",username);
                editor.putString("pwd",pwd);
            }
            editor.putBoolean("isChecked",checked);
            //提交
            editor.commit();
        }
        Intent intent=new Intent(getApplicationContext(),BaseFragment.class);
        startActivity(intent);
    }
}
