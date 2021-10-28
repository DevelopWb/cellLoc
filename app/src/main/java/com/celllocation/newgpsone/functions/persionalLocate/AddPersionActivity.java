package com.celllocation.newgpsone.functions.persionalLocate;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.celllocation.R;
import com.celllocation.newgpsone.Utils.ObjectBox;
import com.celllocation.newgpsone.base.BaseAppActivity;
import com.celllocation.newgpsone.bean.PeopleLocateUserBean;
import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.basecomponent.utils.RuleTools;
import com.juntai.disabled.basecomponent.utils.ToastUtils;

import java.util.List;

/**
 * @aouther tobato
 * @description 描述  添加成员
 * @date 2021/10/23 20:21
 */
public class AddPersionActivity extends BaseAppActivity implements View.OnClickListener {

    /**
     * 请输入成员名称
     */
    private EditText mUserNameTv;
    /**
     * 请输入手机号码
     */
    private EditText mDevMobileTv;
    /**
     * 手机
     */
    private RadioButton mMobileDevRb;
    /**
     * GPS设备
     */
    private RadioButton mGpsDevRb;
    private RadioGroup mDevTypeRg;
    private ImageView mSelectHeadIconIv;
    /**
     * 确定
     */
    private TextView mCommitTv;
    private PeopleLocateUserBean peopleLocateDetailBean;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_add_persion;
    }

    @Override
    public void initView() {
        peopleLocateDetailBean = new PeopleLocateUserBean();
        setTitleName("成员添加");
        mUserNameTv = (EditText) findViewById(R.id.user_name_tv);
        mDevMobileTv = (EditText) findViewById(R.id.dev_mobile_tv);
        mMobileDevRb = (RadioButton) findViewById(R.id.mobile_dev_rb);
        mGpsDevRb = (RadioButton) findViewById(R.id.gps_dev_rb);
        mDevTypeRg = (RadioGroup) findViewById(R.id.dev_type_rg);
        mSelectHeadIconIv = (ImageView) findViewById(R.id.select_head_icon_iv);
        mSelectHeadIconIv.setOnClickListener(this);
        mCommitTv = (TextView) findViewById(R.id.commit_tv);
        mCommitTv.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }


    @Override
    public void onSuccess(String tag, Object o) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.select_head_icon_iv:
                choseImage(0, AddPersionActivity.this, 1);
                break;
            case R.id.commit_tv:
                if (TextUtils.isEmpty(getTextViewValue(mUserNameTv))) {
                    ToastUtils.toast(mContext,"请输入成员名称");
                    return;
                }
                if (!RuleTools.isMobileNO(getTextViewValue(mDevMobileTv))) {
                    ToastUtils.toast(mContext,"请输入正确手机号码");
                    return;
                }
                peopleLocateDetailBean.setPeopleName(getTextViewValue(mUserNameTv));
                peopleLocateDetailBean.setPeopleMobile(getTextViewValue(mDevMobileTv));
                peopleLocateDetailBean.setLocType(0);
                ObjectBox.get().boxFor(PeopleLocateUserBean.class).put(peopleLocateDetailBean);
                finish();
                break;
        }
    }
    @Override
    protected void selectedPicsAndEmpressed(List icons) {
        if (icons.size() > 0) {
            String path = (String) icons.get(0);
            ImageLoadUtil.loadImage(mContext,path,mSelectHeadIconIv);
            peopleLocateDetailBean.setHeadPicPath(path);
        }
    }

}
