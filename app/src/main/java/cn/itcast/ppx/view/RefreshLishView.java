package cn.itcast.ppx.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.itcast.ppx.R;


public class RefreshLishView extends ListView implements AbsListView.OnScrollListener {

    private View mHeaderView;
    private int mHeaderViewHeight;
    private int mFooterViewHeight;

    private static final int STATE_PULL_TO_REFRESH=0;//下拉刷新状态
    private static final int STATE_RELEASE_TO_REFRESH=1;//松开刷新
    private static final int STATE_REFRESHING=2;//正在刷新

    private int mCurrentState=STATE_PULL_TO_REFRESH;//当前状态，默认是下拉刷新

    private RotateAnimation animUp;
    private RotateAnimation animDown;

    private TextView tvState;
    private TextView tvTime;
    private ImageView ivArrow;
    private ProgressBar pbLoading;

    private View mFooterView;

    private int startY=-1;

    private boolean isLoadMore=false;//是否加载更多数据

    public RefreshLishView(Context context) {
        this(context,null);
    }

    public RefreshLishView(Context context, AttributeSet attrs) {
        this(context,attrs,-1);
    }

    public RefreshLishView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    //初始化头布局
    private void initHeaderView(){
        mHeaderView= View.inflate(getContext(), R.layout.pull_to_refresh,null);
        addHeaderView(mHeaderView);//给Listview添加头布局

        tvState=mHeaderView.findViewById(R.id.tv_state);
        tvTime=mHeaderView.findViewById(R.id.tv_time);
        ivArrow=mHeaderView.findViewById(R.id.iv_arrow);
        pbLoading=mHeaderView.findViewById(R.id.pb_loading);

        //隐藏头布局
        //获取当前头布局高度，然后设置负paddingTop，布局就会向上走

        //int height=mHeaderView.getHeight(); 不能这样拿，控件没有回执完成 获取不到宽高
        mHeaderView.measure(0,0);//手动测量,宽高传0 表示不参与具体宽高的设定，全由系统决定
        mHeaderViewHeight=mHeaderView.getMeasuredHeight();

        mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);

        initArrowAnim();
        setRefreshTime();
    }


    //初始化脚布局
    private void initFooterView(){
        mFooterView=View.inflate(getContext(),R.layout.pull_to_refresh_footer,null);
        addFooterView(mFooterView);

        mFooterView.measure(0,0);
        mFooterViewHeight=mFooterView.getMeasuredHeight();
        //隐藏脚布局
        mFooterView.setPadding(0,-mFooterViewHeight,0,0);

        //设置滑动监听
        setOnScrollListener(this);
    }

    //滑动显示下拉刷新 事件处理
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY=(int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(startY==-1){//没有获取到按下的事件(按住头条新闻滑动时，按下事件被BiewPager消费了)
                    startY=(int) ev.getY();//重新获取一次起点位置
                }

                int endY=(int)ev.getY();
                int dy=endY-startY;

                //如果正在刷新，什么都不做
                if(mCurrentState==STATE_REFRESHING){
                    break;
                }

                //当现实的第一个item的位置
                int firstVisiblePosition=this.getFirstVisiblePosition();

                if(dy>0&&firstVisiblePosition==0){
                    //下拉动作 在ListView的顶部
                    int padding=-mHeaderViewHeight+dy;

                    if(padding>0&&mCurrentState!=STATE_RELEASE_TO_REFRESH){
                        //切换到松开刷新状态
                        mCurrentState=STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    }else if(padding<=0&&mCurrentState!=STATE_PULL_TO_REFRESH){
                        //切换到下拉刷新状态
                        mCurrentState=STATE_PULL_TO_REFRESH;
                        refreshState();
                    }

                    //通过修改padding来设置当前刷新控件的最新位置
                    mHeaderView.setPadding(0,padding,0,0);

                    return true;//消费此事件，处理下拉刷新控件的滑动，不需要listview原生滑动效果参与
                }
                break;
            case MotionEvent.ACTION_UP:
                startY=-1;//起始坐标归零

                if(mCurrentState==STATE_RELEASE_TO_REFRESH){
                    //切换成正在刷新
                    mCurrentState=STATE_REFRESHING;

                    //完整显示刷新控件
                    mHeaderView.setPadding(0,0,0,0);

                    refreshState();

                }else if(mCurrentState==STATE_PULL_TO_REFRESH){
                    //隐藏刷新控件
                    mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);
                }

                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);//要返回super，方便listview原生滑动处理
    }

    //初始化箭头动画
    private void initArrowAnim(){
        //箭头向上动画
        animUp=new RotateAnimation(0,-180, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animUp.setDuration(300);
        animUp.setFillAfter(true);//保持动画结束的状态
        //箭头向下
        animDown=new RotateAnimation(-180,0, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animDown.setDuration(300);
        animDown.setFillAfter(true);//保持动画结束的状态
    }

    //根据当前状态刷新界面
    private void refreshState() {
        switch(mCurrentState){
            case STATE_PULL_TO_REFRESH:
                tvState.setText("下拉刷新");
                pbLoading.setVisibility(View.INVISIBLE);
                ivArrow.setVisibility(View.VISIBLE);

                ivArrow.startAnimation(animDown);
                break;
            case STATE_RELEASE_TO_REFRESH:
                tvState.setText("松开刷新");
                pbLoading.setVisibility(View.INVISIBLE);
                ivArrow.setVisibility(View.VISIBLE);

                ivArrow.startAnimation(animUp);
                break;
            case STATE_REFRESHING:
                tvState.setText("正在刷新");

                ivArrow.clearAnimation();//清理动画才能隐藏
                pbLoading.setVisibility(View.VISIBLE);
                ivArrow.setVisibility(View.INVISIBLE);

                //回调下拉刷新
                if(mListener!=null){
                    mListener.onRefresh();
                }
                break;
            default:
                break;
        }
    }

    //刷新结束隐藏控件
    public void onRefreshComplete(){
        if(!isLoadMore){
            //隐藏控件
            mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);

            //所有状态初始化
            tvState.setText("下拉刷新");
            pbLoading.setVisibility(View.INVISIBLE);
            ivArrow.setVisibility(View.VISIBLE);
            mCurrentState=STATE_PULL_TO_REFRESH;

            //更新刷新时间
            setRefreshTime();
        }else{
            //隐藏加载跟过的控件
            mFooterView.setPadding(0,-mFooterViewHeight,0,0);
            isLoadMore=false;
        }
    }

    private OnRefreshListener mListener;

    //设置刷新回调监听
    public void setOnRefreshListener(OnRefreshListener listener){
        mListener=listener;
    }

    //滑动状态发生变化 脚布局
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState==SCROLL_STATE_IDLE){
            //空闲状态
            int lastVisiblePosition=getLastVisiblePosition();//当前显示的最后一个item
            if(lastVisiblePosition==getCount()-1&&!isLoadMore){
                //到最底部了
                //加载更多
                isLoadMore=true;

                //显示加载中布局
                mFooterView.setPadding(0,0,0,0);

                setSelection(getCount()-1);//显示在最后一个item的位置(展示加载中布局)

                //加载更多数据
                if(mListener!=null){
                    mListener.onLoadMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    //定义一个回调接口，通知刷新状态
    public interface OnRefreshListener{
        //下拉刷新的回调
        public void onRefresh();

        //更多数据回调
        public void onLoadMore();
    }

    //设置刷新时间
    private void setRefreshTime(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=format.format(new Date());
        tvTime.setText(time);
    }
}
