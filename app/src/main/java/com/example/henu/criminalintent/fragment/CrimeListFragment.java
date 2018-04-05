package com.example.henu.criminalintent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.henu.criminalintent.Crime;
import com.example.henu.criminalintent.CrimeLab;
import com.example.henu.criminalintent.R;
import com.example.henu.criminalintent.activity.CrimePagerActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by hppc on 2017/3/20.
 * 行为列表项
 */

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;//记录子标题状态
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    public TextView emptyView;

    //创建项目的视图并返回给调用者
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list,container,false);

        mCrimeRecyclerView = (RecyclerView)view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        emptyView = (TextView)view.findViewById(R.id.empty_textView);

        if(savedInstanceState != null)
        {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        updateUI();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }

    //创建该fragment，可以做除了view之外的初始化工作
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Report that this fragment would like to participate in populating the options menu by
        // receiving a call toonCreateOptionsMenu(Menu, MenuInflater) and related methods.
        setHasOptionsMenu(true);
    }

    /**
     * 创建选项菜单
     * 按照本应用的设计选项菜单到的回调应该在fragment中而不是activity中，而在fragment中onCreateOptionsMenu
     * 是由fragmentManager负责调用的，因此，当activity接收到操作系统的onCreateOptionsMenu(...)方法回调请求
     * 时，我们必须明确的告诉fragmentManager，其管理的fragment应该接收此方法的调用指令
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crim_list_menu,menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible)
        {
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(),crime.getId());
                startActivity(intent);
                return true;//完成处理后，返回true表示全部任务已经完成
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();//下次显示的时候重建选项菜单
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 设置工具栏子标题，显示当前记录的数量
     */
    private void updateSubtitle(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format,crimeCount);

        if(!mSubtitleVisible)
        {
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);

    }
    @Override
    public void onResume() {
        super.onResume();
        updateUI();//当模型层的数据有变化时，返回到CrimeListFragment时，刷新列表项
    }

    /*
        将Adapter和RecyclerView联系在一起，设置CrimeListFragment的用户界面的方法
        刷新界面
         */
    private void updateUI()
    {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        emptyView.setVisibility(crimes.size() > 0 ? View.GONE : View.VISIBLE);
        if(mAdapter == null)
        {
            mAdapter = new CrimeAdapter(crimes);//创建CrimeAdapter
            mCrimeRecyclerView.setAdapter(mAdapter);
        }else
        {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();//更新数据
        }
        updateSubtitle();//刷新菜单
    }

    /**
     * CrimeHolder保存Crime的布局信息，绑定数据
     */
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;
        public TextView mTitleTextView;
        private Crime mCrime;

        public CrimeHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);//响应用户的触摸点击列表项事件
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_textView);
            mDateTextView  = (TextView)itemView.findViewById(R.id.list_item_crime_data_textView);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_checkBox);
        }
        /*
        绑定数据
         */
        public void bindCrime(Crime crime)
        {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            DateFormat dateFormat = new SimpleDateFormat(CrimeFragment.DATE_FORMAT);
            DateFormat timeFormat = new SimpleDateFormat(CrimeFragment.TIME_FORMAT);
            mDateTextView.setText(dateFormat.format(mCrime.getDate()) +" "+timeFormat.format(mCrime.getTime()));
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        /**
         * 从Fragment中启动Activity点击Crime列表项时启动对应的Activity
         * @param v
         */
        @Override
        public void onClick(View v) {
            //利用CrimeActivity的新建Intent的方法创建Intent 并传递指定的Crime的Id
            //Intent intent = CrimeActivity.newIntent(getActivity(),mCrime.getId());
            Intent intent = CrimePagerActivity.newIntent(getActivity(),mCrime.getId());
            startActivity(intent);//开启

        }
    }
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;
        public CrimeAdapter(List<Crime> crimes)
        {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater= LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime,parent,false);
            return new CrimeHolder(view);
        }
        /*
        该方法将ViewHolder的view视图和模型层数据绑定在一起，收到viewHolder和列表项在数据集中的位置后
        ，通过索引位置找到要显示的数据进行绑定
         */
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);//刷新显示的Crime数据
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
        //更新视图
        public void setCrimes(List<Crime> crimes)
        {
            mCrimes = crimes;
        }
    }
}
