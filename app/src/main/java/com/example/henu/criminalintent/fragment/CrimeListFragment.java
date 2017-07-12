package com.example.henu.criminalintent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.henu.criminalintent.Crime;
import com.example.henu.criminalintent.CrimeLab;
import com.example.henu.criminalintent.R;
import com.example.henu.criminalintent.activity.CrimePagerActivity;

import java.util.List;

/**
 * Created by hppc on 2017/3/20.
 * 行为列表项
 */

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list,container,false);

        mCrimeRecyclerView = (RecyclerView)view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();//当模型层的数据有变化时，返回到CrimeListFragment时，刷新列表项
    }

    /*
        将Adapter和RecyclerView联系在一起，设置CrimeListFragment的用户界面的方法
         */
    private void updateUI()
    {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if(mAdapter == null)
        {
            mAdapter = new CrimeAdapter(crimes);//创建CrimeAdapter
            mCrimeRecyclerView.setAdapter(mAdapter);
        }else
        {
            mAdapter.notifyDataSetChanged();//更新数据
        }
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
            mDateTextView.setText(mCrime.getDate().toString());
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
    }
}
