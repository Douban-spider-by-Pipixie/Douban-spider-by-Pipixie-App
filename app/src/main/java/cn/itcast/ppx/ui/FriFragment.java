package cn.itcast.ppx.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.viewpagerindicator.TabPageIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.itcast.ppx.ChatActivity;
import cn.itcast.ppx.R;

public class FriFragment extends Fragment {

    private static final String[] CONTENT = new String[]{"消息", "好友"};

    private List<Fragment> mFragment = new ArrayList<Fragment>();

    public ViewPager mViewPager;

    public TabPageIndicator mIndicator;

    private TextView mTime;

    private ListView mListView;

    // 发起聊天 username 输入框
    private EditText mChatIdEdit;
    // 发起聊天
    private Button mStartChatBtn;

    public FriFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final Context context = new ContextThemeWrapper(getActivity(), R.style.MyTheme);
        LayoutInflater  localInflater=inflater.cloneInContext(context);

        View view = localInflater.inflate(R.layout.fragment_fri,container,false);



        mChatIdEdit = (EditText) view.findViewById(R.id.ec_edit_chat_id);

        mStartChatBtn = (Button) view.findViewById(R.id.ec_btn_start_chat);
        mStartChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取我们发起聊天的者的username
                String chatId = mChatIdEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(chatId)) {
                    // 获取当前登录用户的 username
                    String currUsername = EMClient.getInstance().getCurrentUser();
                    if (chatId.equals(currUsername)) {
                        Toast.makeText(getContext(), "不能和自己聊天", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // 跳转到聊天界面，开始聊天
                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    intent.putExtra("ec_chat_id", chatId);
                    startActivity(intent);
                    //将当前fragment加入到返回栈中
                } else {
                    Toast.makeText(getContext(), "Username 不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    public class TimeThread extends Thread{
        @Override
        public void run() {
            super.run();
            do{
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);
        }
    }
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    mTime.setText(new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis())));
                    break;
            }
            return false;
        }
    });


    class FriAdapter extends FragmentPagerAdapter {

        private List<Fragment> views;

        public FriAdapter(FragmentManager fm, List<Fragment> views){
            super(fm);
            this.views=views;
        }

        @Override
        public Fragment getItem(int position) {
            return views.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length];
        }

        @Override
        public int getCount() {
            return views.size();
        }

    }

}
