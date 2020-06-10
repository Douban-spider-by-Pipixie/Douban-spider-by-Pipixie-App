package cn.itcast.ppx.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;
import cn.itcast.ppx.ChatActivity;
import cn.itcast.ppx.adapter.FrilistAdapter;
import cn.itcast.ppx.R;
import cn.itcast.ppx.domain.ChatBean;
import cn.itcast.ppx.domain.FriListBean;

public class FriFragment extends Fragment {

    private static final int COMPLETED = 0;
    private ListView mListView;
    private EditText mAdduserId;
    private Button mAddfribtn;
    private FrilistAdapter adpter;
    private Button mFindfribtn;

    private List<String> usernames;

    private List<FriListBean> friBeanList;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                //new Thread(networkTask).start();
                friBeanList = new ArrayList<FriListBean>();
                ChatBean chatBean = new ChatBean();
                for(String s:usernames){
                    FriListBean friListBean = new FriListBean();
                    System.out.println("userid:"+s);
                    friListBean.setUserId(s);
                    friBeanList.add(friListBean);
                }
                adpter = new FrilistAdapter(friBeanList, getContext());
                mListView.setAdapter(adpter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //String chatId=usernames.get(position);
                        String chatId=friBeanList.get(position).getUserId();
                        Intent intent = new Intent(getContext(), ChatActivity.class);
                        intent.putExtra("ec_chat_id", chatId);
                        startActivity(intent);
                    }
                });

                EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {


                    @Override
                    public void onContactAdded(String username) {
                       //增加了联系人
                        System.out.println(username+"成为你的好友");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("runonuiThread启动了");
                                ChatBean chatBean = new ChatBean();
                                FriListBean friListBean=new FriListBean();
                                friListBean.setUserId(username);
                                friBeanList.add(friListBean);        //将发送的信息添加到chatBeanList集合中
                                adpter.notifyDataSetChanged();    //更新ListView列表
//                                Message msg = new Message();
//                                msg.what = COMPLETED;
//                                handler.sendMessage(msg);
                            }
                        });
                    }

                    @Override
                    public void onContactDeleted(String username) {
                        //删除了联系人
                        System.out.println(username+"已被删除");
                    }

                    @Override
                    public void onContactInvited(String username, String reason) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("收到了好友请求");
                                //收到好友邀请
                                AlertDialog.Builder bb=new AlertDialog.Builder(getContext());

                                bb.setPositiveButton("接受", (dialog, which) -> {
                                    try {
                                        EMClient.getInstance().contactManager().acceptInvitation(username);

                                    } catch (HyphenateException e) {
                                        e.printStackTrace();
                                    }
                                    dialog.dismiss();
                                });

                                bb.setNegativeButton("拒绝", (dialog, which) -> {
                                    try {
                                        EMClient.getInstance().contactManager().declineInvitation(username);

                                    } catch (HyphenateException e) {
                                        e.printStackTrace();
                                    }
                                    dialog.dismiss();
                                });

                                bb.setMessage(username+"请求添加你为好友");
                                bb.setTitle("好友请求");
                                bb.show();
                            }
                        });

                    }

                    @Override
                    public void onFriendRequestAccepted(String username) {
                        //好友请求被同意

//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
//
//                                } catch (HyphenateException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }).start();
                    }

                    @Override
                    public void onFriendRequestDeclined(String username) {
                        //好友请求被拒绝
                        Looper.prepare();
                        Toast.makeText(getContext(),username+"拒绝了你的好友请求",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                });
            }
        }
    };

    public FriFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final Context context = new ContextThemeWrapper(getActivity(), R.style.MyTheme);
        LayoutInflater  localInflater=inflater.cloneInContext(context);

        View view = localInflater.inflate(R.layout.fragment_fri,container,false);

        mAdduserId=(EditText)view.findViewById(R.id.et_fri_id);
        mAddfribtn=(Button)view.findViewById(R.id.btn_addfri);
        mFindfribtn=(Button)view.findViewById(R.id.btn_findfri);
        mListView=(ListView)view.findViewById(R.id.lv_friList);

        mAddfribtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toAddUsername=mAdduserId.getText().toString().trim();
                if(!TextUtils.isEmpty(toAddUsername)){
                    String currUsername = EMClient.getInstance().getCurrentUser();
                    if(toAddUsername.equals(currUsername)){
                        Toast.makeText(getContext(), "不能添加自己为好友", Toast.LENGTH_SHORT).show();
                        return;
                    }
                        try {
                            AddFri(toAddUsername);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),"错误:"+e,Toast.LENGTH_LONG).show();
                        }
                }else{
                    Toast.makeText(getContext(), "ID不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });

        mFindfribtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("ec_chat_id", "panxiongjian");
                startActivity(intent);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = COMPLETED;
                handler.sendMessage(msg);
            }
        }).start();
        return view;
    }

    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            try {
                usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
            } catch (HyphenateException e) {
                e.printStackTrace();
            }

            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", "请求结果");
            msg.setData(data);
            handler2.sendMessage(msg);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i("mylog", "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
        }
    };

    private void AddFri(String toAddUsername) throws HyphenateException {
        String reason="nice to meet you!";
        EMClient.getInstance().contactManager().addContact(toAddUsername, reason);
    }

}
