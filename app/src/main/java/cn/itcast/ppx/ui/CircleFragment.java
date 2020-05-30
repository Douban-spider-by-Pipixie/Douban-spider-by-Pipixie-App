package cn.itcast.ppx.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.ppx.item.ItemFragmentRemen;
import cn.itcast.ppx.item.ItemFragmentGuanzhu;
import cn.itcast.ppx.item.ItemFragmentHuati;
import cn.itcast.ppx.R;


public class CircleFragment extends Fragment {

    private static final String[] CONTENT = new String[]{"热门", "关注", "话题"};

    private List<Fragment> mFragment = new ArrayList<Fragment>();

    public ViewPager mViewPager;

    public TabPageIndicator mIndicator;

    public CircleFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final Context context = new ContextThemeWrapper(getActivity(), R.style.MyTheme);
        LayoutInflater  localInflater=inflater.cloneInContext(context);

        View view = localInflater.inflate(R.layout.fragment_circle, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_circle);
        mIndicator = view.findViewById(R.id.indicator);

        mFragment.add(new ItemFragmentRemen());
        mFragment.add(new ItemFragmentGuanzhu());
        mFragment.add(new ItemFragmentHuati());

        FragmentPagerAdapter adapter=new CircleAdapter(getFragmentManager(),mFragment);

        mViewPager.setAdapter(adapter);

        mIndicator.setViewPager(mViewPager);

        return view;
    }

    class CircleAdapter extends FragmentPagerAdapter {

        private List<Fragment> views;

       public CircleAdapter(FragmentManager fm,List<Fragment> views){
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
