package cn.itcast.ppx;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.ppx.adapter.ChatAdapter;
import cn.itcast.ppx.domain.ChatBean;

public class ChatActivity extends AppCompatActivity implements EMMessageListener {

    //private TextView mContentView;
    private TextView mUserId;
    private ListView mListView;
    private EditText mInputView;
    private Button mSendBtn;
    private LinearLayout mllchat;

    private ChatAdapter adpter;

    private String sendMsg;    //发送的信息

    // 消息监听器
    private EMMessageListener mMessageListener;
    // 当前聊天的 ID
    private String mChatId;
    // 当前会话对象
    private EMConversation mConversation;

    private List<ChatBean> chatBeanList; //存放所有聊天数据的集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        // 获取当前会话的username(如果是群聊就是群id)
        mChatId = getIntent().getStringExtra("ec_chat_id");
        mMessageListener = this;
        initView();
        initConversation();
    }


    /**
     * 初始化界面
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        chatBeanList = new ArrayList<ChatBean>();
        mListView=(ListView)findViewById(R.id.list);
        mInputView = (EditText) findViewById(R.id.et_send_msg);
        mSendBtn = (Button) findViewById(R.id.btn_send);
        mUserId=(TextView) findViewById(R.id.tv_userid);
        mllchat=(LinearLayout)findViewById(R.id.ll_chat);
        mUserId.setText(mChatId);
        //mContentView = (TextView) findViewById(R.id.ec_text_content);
        // 设置textview可滚动，需配合xml布局设置
        //mContentView.setMovementMethod(new ScrollingMovementMethod());

        mllchat.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        adpter = new ChatAdapter(chatBeanList, this);
        mListView.setAdapter(adpter);
        mInputView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() ==
                        KeyEvent.ACTION_DOWN) {
                    sendData();//点击Enter键也可以发送信息
                }
                return false;
            }
        });


        // 设置发送按钮的点击事件
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });
    }


    private void sendData() {
        String content = mInputView.getText().toString().trim();
        if (!TextUtils.isEmpty(content)) {
            mInputView.setText("");
            // 创建一条新消息，第一个参数为消息内容，第二个为接受者username
            EMMessage message = EMMessage.createTxtSendMessage(content, mChatId);
            // 将新的消息内容和时间加入到下边
            //mContentView.setText(mContentView.getText() + "\n" + content + " -> " + message.getMsgTime());
            // 调用发送消息的方法
            ChatBean chatBean = new ChatBean();
            chatBean.setMessage(content);
            chatBean.setState(chatBean.SEND); //SEND表示自己发送的信息
            chatBeanList.add(chatBean);        //将发送的信息添加到chatBeanList集合中
            adpter.notifyDataSetChanged();    //更新ListView列表
            EMClient.getInstance().chatManager().sendMessage(message);
            // 为消息设置回调
            message.setMessageStatusCallback(new EMCallBack() {
                @Override
                public void onSuccess() {
                    // 消息发送成功，打印下日志，正常操作应该去刷新ui
                    Log.i("lzan13", "send message on success");
                }

                @Override
                public void onError(int i, String s) {
                    // 消息发送失败，打印下失败的信息，正常操作应该去刷新ui
                    Log.i("lzan13", "send message on error " + i + " - " + s);
                }

                @Override
                public void onProgress(int i, String s) {
                    // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调
                }
            });
        }
    }

    /**
     * 初始化会话对象，并且根据需要加载更多消息
     */
    private void initConversation() {

        /**
         * 初始化会话对象，这里有三个参数么，
         * 第一个表示会话的当前聊天的 useranme 或者 groupid
         * 第二个是绘画类型可以为空
         * 第三个表示如果会话不存在是否创建
         */
        mConversation = EMClient.getInstance().chatManager().getConversation(mChatId, null, true);
        // 设置当前会话未读数为 0
        mConversation.markAllMessagesAsRead();
        int count = mConversation.getAllMessages().size();
        if (count < mConversation.getAllMsgCount() && count < 20) {
            // 获取已经在列表中的最上边的一条消息id
            String msgId = mConversation.getAllMessages().get(0).getMsgId();
            // 分页加载更多消息，需要传递已经加载的消息的最上边一条消息的id，以及需要加载的消息的条数
            mConversation.loadMoreMsgFromDB(msgId, 20 - count);
        }
        // 打开聊天界面获取最后一条消息内容并显示
        if (mConversation.getAllMessages().size() > 0) {
            //EMMessage messge = mConversation.getLastMessage();
            List<EMMessage> msgs=mConversation.getAllMessages();
            //String who=messge.getFrom();
            //Log.e("消息","消息来自"+who);
            for(EMMessage messge:msgs){
                EMTextMessageBody body = (EMTextMessageBody) messge.getBody();
                // 将消息内容和时间显示出来
                //mContentView.setText(body.getMessage() + " - " + mConversation.getLastMessage().getMsgTime());
                ChatBean chatBean = new ChatBean();
                chatBean.setMessage(body.getMessage());
                if(messge.getFrom().equals(mChatId)){
                    chatBean.setState(chatBean.RECEIVE); //SEND表示自己发送的信息
                }else{
                    chatBean.setState(chatBean.SEND); //SEND表示自己发送的信息
                }
                chatBeanList.add(chatBean);        //将发送的信息添加到chatBeanList集合中
                adpter.notifyDataSetChanged();    //更新ListView列表
            }
        }
    }

    /**
     * 自定义实现Handler，主要用于刷新UI操作
     */
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    EMMessage message = (EMMessage) msg.obj;
                    // 这里只是简单的demo，也只是测试文字消息的收发，所以直接将body转为EMTextMessageBody去获取内容
                    EMTextMessageBody body = (EMTextMessageBody) message.getBody();
                    // 将新的消息内容和时间加入到下边
                    //mContentView.setText(mContentView.getText() + "\n" + body.getMessage() + " <- " + message.getMsgTime());
                    ChatBean chatBean = new ChatBean();
                    chatBean.setMessage(body.getMessage());
                    if(message.getFrom().equals(mChatId)){
                        chatBean.setState(chatBean.RECEIVE); //SEND表示自己发送的信息
                    }else{
                        chatBean.setState(chatBean.SEND); //SEND表示自己发送的信息
                    }
                    chatBeanList.add(chatBean);        //将发送的信息添加到chatBeanList集合中
                    adpter.notifyDataSetChanged();    //更新ListView列表
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // 添加消息监听
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 移除消息监听
        EMClient.getInstance().chatManager().removeMessageListener(mMessageListener);
    }
    /**
     * --------------------------------- Message Listener -------------------------------------
     * 环信消息监听主要方法
     */
    /**
     * 收到新消息
     *
     * @param list 收到的新消息集合
     */
    @Override
    public void onMessageReceived(List<EMMessage> list) {
        // 循环遍历当前收到的消息
        for (EMMessage message : list) {
            if (message.getFrom().equals(mChatId)) {
                // 设置消息为已读
                mConversation.markMessageAsRead(message.getMsgId());

                // 因为消息监听回调这里是非ui线程，所以要用handler去更新ui
                Message msg = mHandler.obtainMessage();
                msg.what = 0;
                msg.obj = message;
                mHandler.sendMessage(msg);
            } else {
                // 如果消息不是当前会话的消息发送通知栏通知
            }
        }
    }

    /**
     * 收到新的 CMD 消息
     *
     * @param list
     */
    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {
        for (int i = 0; i < list.size(); i++) {
            // 透传消息
            EMMessage cmdMessage = list.get(i);
            EMCmdMessageBody body = (EMCmdMessageBody) cmdMessage.getBody();
            Log.i("lzan13", body.action());
        }
    }

    @Override
    public void onMessageRead(List<EMMessage> list) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {

    }

    @Override
    public void onMessageRecalled(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage message, Object object) {
    }
}
