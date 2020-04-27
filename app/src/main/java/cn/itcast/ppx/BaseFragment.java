package cn.itcast.ppx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;

public class BaseFragment extends AppCompatActivity implements View.OnClickListener {

    private HomeFragment f1;
    private CircleFragment f2;
    private FriFragment f3;
    private MineFragment f4;

    private RadioButton mImgHome;
    private RadioButton mImgCircle;
    private RadioButton mImgFriend;
    private RadioButton mImgMine;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mylayout);

        mImgHome = (RadioButton)findViewById(R.id.rb_home);
        mImgCircle = (RadioButton)findViewById(R.id.rb_circle);
        mImgFriend = (RadioButton)findViewById(R.id.rb_fri);
        mImgMine = (RadioButton)findViewById(R.id.rb_mine);

        mImgHome.setOnClickListener(this);
        mImgCircle.setOnClickListener(this);
        mImgFriend.setOnClickListener(this);
        mImgMine.setOnClickListener(this);

        initFragment1();
    }

    private void initFragment1(){
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //方式（add），初始化fragment并添加到事务中，如果为null就new一个
        if(f1 == null){
            f1 = new HomeFragment();
            transaction.add(R.id.main_frame_layout,f1);
        }
        //mImgHome.setImageResource(R.drawable.bottom_home2);
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(f1);
        //提交事务
        transaction.commit();
    }
    private void initFragment2(){
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
        if(f2 == null){
            f2 = new CircleFragment();
            transaction.add(R.id.main_frame_layout, f2);
        }
        //mImgCircle.setImageResource(R.drawable.bottom_circle2);
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(f2);

        transaction.commit();
    }
    private void initFragment3(){
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
        if(f3 == null){
            f3 = new FriFragment();
            transaction.add(R.id.main_frame_layout, f3);
        }
        //mImgFriend.setImageResource(R.drawable.bottom_friend2);
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(f3);
        //提交事务
        transaction.commit();
    }
    private void initFragment4(){
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
        if(f4 == null){
            f4 = new MineFragment();
            transaction.add(R.id.main_frame_layout, f4);
        }
        //mImgMine.setImageResource(R.drawable.bottom_mine2);
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(f4);
        //提交事务
        transaction.commit();
    }
    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction){
        if(f1 != null){
            transaction.hide(f1);
        }
        if(f2 != null){
            transaction.hide(f2);
        }
        if(f3 != null){
            transaction.hide(f3);
        }
        if(f4 != null){
            transaction.hide(f4);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == mImgHome){
            initFragment1();
        }else if(v == mImgCircle){
            initFragment2();
        }else if(v == mImgFriend){
            initFragment3();
        }else if(v == mImgMine){
            initFragment4();
        }
    }

}
