package com.example.henu.criminalintent.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.henu.criminalintent.Crime;
import com.example.henu.criminalintent.CrimeLab;
import com.example.henu.criminalintent.R;

import java.util.Date;
import java.util.UUID;

/**
 * Created by hppc on 2017/3/18.
 * Crime的详细信息碎片
 */

public class CrimeFragment extends Fragment{
    private static final String ARG_CRIME_ID ="crime_id"; //Crime条目的Id
    private static final String DIALOG_DATE = "DialogDate";//DatePickerFragment的tag
    private static final int REQUEST_DATE = 0;//DatePickerFragment的请求代码
    private static String DATE_FORMAT = "EEE MMM dd yyyy";
    private static String TIME_FORMAT = "hh:mm a z";
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton,mTimeButton;
    private CheckBox mSolvedCheckBox;

    /**
     * 完成fragment示例及bundle对象的创建，然后将argument放入bundle对象中，最后
     * 再附加给fragment
     * 托管activity需要fragment实例时调用
     * @param crimeId 要启动的crime的Id
     * @return 返回fragment实例
     */
    public static CrimeFragment newInstance(UUID crimeId)
    {
        Bundle args = new Bundle();
        //附加数据
        args.putSerializable(ARG_CRIME_ID,crimeId);

        CrimeFragment fragment = new CrimeFragment();
        //附加argument bundle给fragment 必须在fragment创建后 添加给activity之前完成
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 创建并返回与该片段相关联的视图的层次结构
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*
        第二个参数是视图的父视图，我们通常需要父视图来正确配置组件，第三个参数告知布局生成器是否将生成的视图
        添加给父视图 我们以代码的方式添加生成的视图 所以选择false
         */
        View v = inflater.inflate(R.layout.fragment_crime,container,false);

        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());//通过对应的Crime设置标题
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //This space intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //设置犯罪时间
        mDateButton = (Button)v.findViewById(R.id.crime_date);
        String date;
        date = (String) DateFormat.format("EEEE,MMMM dd,yyyy kk:mm",mCrime.getDate());
        mDateButton.setText(date);
        //点击日期按钮展现DatePickerFragment界面
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                //为DatePickerFragment设置目标Fragment
                dialog.setTargetFragment(CrimeFragment.this,0);
                dialog.show(manager,DIALOG_DATE);//将DialogFragment添加给FragmentManager并显示到屏幕上
            }
        });
        //设置是否解决
        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());//
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);//更新Crime类的mSolved变量值
            }
        });
        return v;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取extra数据并取得crime对象 有两种方式
        //方式1 从托管的Activity中 简单粗暴但缺少灵活性 fragment
       /* UUID crimeId = (UUID)getActivity().getIntent().getSerializableExtra(CrimeActivity.
                EXTRA_CRIME_ID);*/
        //通过获得的crimeId从CrimeLab单例中调取Crime对象

        //方法二 从fragment中获取crimeId
        UUID crimeId = (UUID)getArguments().getSerializable(ARG_CRIME_ID);

        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK)
        {
            return;
        }
        if(requestCode == REQUEST_DATE)
        {
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            //获取选择的日期
            mCrime.setDate(date);
            //为日期按钮设置日期
            updateDate();
        }
    }
    //为日期按钮设置日期
    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }
}
