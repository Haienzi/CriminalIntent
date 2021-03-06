package com.example.henu.criminalintent.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.henu.criminalintent.R;
import com.example.henu.criminalintent.bean.Crime;
import com.example.henu.criminalintent.bean.CrimeLab;
import com.example.henu.criminalintent.util.PictureUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hppc on 2017/3/18.
 * Crime的详细信息碎片
 */

public class CrimeFragment extends Fragment{
    private static final String ARG_CRIME_ID ="crime_id"; //Crime条目的Id
    private static final String DIALOG_DATE = "DialogDate";//DatePickerFragment的tag
    private static final String DIALOG_TIME = "DialogTime";//TimePickerFragment的tag
    private static final String DIALOG_PHOTO = "DialogPhoto";//PhotoDetailFragment的tag
    private static final int REQUEST_DATE = 0;//DatePickerFragment的请求代码
    private static final int REQUEST_TIME = 1;//TimePickerFragment的请求代码
    private static final int REQUEST_CONTACT = 2;//获取联系人信息的请求码
    private static final int REQUEST_PHOTO = 3;//获取相片的请求码
    public static String DATE_FORMAT = "EEE MMM dd yyyy";
    public static String TIME_FORMAT = "hh:mm a";
    private Crime mCrime;
    private File mPhotoFile;//图片文件
    @BindView(R.id.crime_title)
    EditText mTitleField;
    @BindView(R.id.crime_date)
    Button mDateButton;
    @BindView(R.id.crime_time)
    Button mTimeButton;
    @BindView(R.id.crime_solved)
    CheckBox mSolvedCheckBox;
    @BindView(R.id.crime_report)
    Button mReportButton;
    @BindView(R.id.crime_suspect)
    Button mSuspectButton;
    @BindView(R.id.crime_camera)
    ImageButton mPhotoButton;//拍照按钮
    @BindView(R.id.crime_photo)
    ImageView mPhotoView;//照片视图

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
        ButterKnife.bind(this,v);
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
              if(TextUtils.isEmpty(s))
              {
                  Toast.makeText(getActivity(),"标题不能为空",Toast.LENGTH_SHORT).show();
              }
            }
        });
        //设置犯罪时间
        /*String date;
        date = (String) DateFormat.format("EEEE,MMMM dd,yyyy kk:mm",mCrime.getDate());
        mDateButton.setText(date);*/
        updateDate();
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
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getTime());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_TIME);
                dialog.show(manager,DIALOG_TIME);
            }
        });
        //设置是否解决
        mSolvedCheckBox.setChecked(mCrime.isSolved());//
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);//更新Crime类的mSolved变量值
            }
        });
        //发送报告
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐式Intent
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,getCrimeReport());//内容
                i.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject));//主题
                i = Intent.createChooser(i,getString(R.string.send_report));
                startActivity(i);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact,REQUEST_CONTACT);
            }
        });
        if(mCrime.getSuspect() != null)
        {
            mSuspectButton.setText(mCrime.getSuspect());
        }
        PackageManager packageManager = getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContact,packageManager.MATCH_DEFAULT_ONLY) == null)
        {
            mSuspectButton.setEnabled(false);
        }

        //打开相机应用默认只能拍摄缩略图类似的低分辨率图片
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager)!=null;
        mPhotoButton.setEnabled(canTakePhoto);
        if(canTakePhoto)
        {
            //获取存储路径的URI
            Uri uri = Uri.fromFile(mPhotoFile);
            //使用文件系统存储数据
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        }
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage,REQUEST_PHOTO);
            }
        });

        updatePhotoView();

        //点击缩略图显示详细的图片
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPhotoFile == null || !mPhotoFile.exists()) return;//no photo
                FragmentManager manager = getFragmentManager();
                PhotoDetailFragment dialog = PhotoDetailFragment.newInstance(mPhotoFile);
                dialog.show(manager,DIALOG_PHOTO);
            }
        });

        return v;
    }

    //Fragment的onCreate()方法可以在其中初始化除了View之外的所有东西，在onCreateView之前调用
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
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(mCrime);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity()).updateCrime(mCrime);
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
        if(requestCode == REQUEST_TIME)
        {
            Date time = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setTime(time);
            updateTime();
        }else if (requestCode == REQUEST_PHOTO)
        {
            updatePhotoView();
        }
    }
    //为日期按钮设置日期
    private void updateDate() {
        java.text.DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        mDateButton.setText(dateFormat.format(mCrime.getDate()));
    }
    //为时间按钮设置事件
    private void updateTime(){
       java.text.DateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
        mTimeButton.setText(timeFormat.format(mCrime.getTime()));
    }
    //获取犯罪报告
    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect);
        }
        //完善报告
        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);
        return report;
    }
    private void updatePhotoView()
    {
        if(mPhotoFile == null || !mPhotoFile.exists())
        {
            mPhotoView.setImageDrawable(null);
        }else{
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}
