package com.android.project.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.project.R;
import com.android.project.common.NavigationActivity;
import com.android.project.databinding.ActivitySearchBinding;
import com.android.project.databinding.ViewSearch2Binding;
import com.android.project.utils.KeyboardUtils;
import com.android.project.utils.UIUtils;


/**
 * 搜索页Activity
 *
 * @author Leon(黄长亮)
 * @date 2017/5/15.
 */
public class SearchActivity extends NavigationActivity<ActivitySearchBinding> {

    private ViewSearch2Binding mSearch2Binding;
    private int mWidth,mCurrWidth;
    private int mLeftMargin,mCurrLeftMargin;

    @Override
    public void init() {
        setHideNavBarOwnView(true);
        advanceInitSuccessView(true);
        mWidth = getIntent().getIntExtra("width",0);
        mLeftMargin = getIntent().getIntExtra("leftMargin",0);
    }

    @Override
    public void initData() {

    }

    @Override
    public int initSuccessView() {
        return R.layout.activity_search;
    }

    @Override
    public void displayPage(final ActivitySearchBinding viewBinding) {
        inputAnimator(viewBinding);
    }

    @Override
    public void initListener(final ActivitySearchBinding viewBinding) {
        mSearch2Binding.btnNavBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputAnimator(viewBinding);
            }
        });
    }


    /**
     * 进场动画
     * @param viewBinding
     */
    public void inputAnimator(final ActivitySearchBinding viewBinding){
        mDetailsBinding.contentContainer.setBackgroundResource(R.color.transparent);
        viewBinding.rvList.getBackground().setAlpha(0);
        View searchView = LayoutInflater.from(this).inflate(R.layout.view_search2, getNavigationBar().mNavigationBinding.placeholderContainer, false);
        mSearch2Binding = DataBindingUtil.bind(searchView);
        addViewOnNavBar(searchView);
        final RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mSearch2Binding.searchContainer.getLayoutParams();
        final LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) mSearch2Binding.layoutSearch.getLayoutParams();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat( 0f,1.0f);
        valueAnimator.setDuration(250);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float money= (float) animation.getAnimatedValue();
                mSearch2Binding.btnNavBack.setAlpha(money);
                mSearch2Binding.tvSearch.setAlpha(money);
                viewBinding.rvList.getBackground().setAlpha((int) (255 * money));
                lp.width = (int)(mWidth - (mWidth * 0.25 * money));
                mCurrWidth = lp.width;
                mSearch2Binding.searchContainer.setLayoutParams(lp);
                mCurrLeftMargin = (int) (mLeftMargin * money);
                lp2.setMargins(- mCurrLeftMargin,0,0,0);
                mSearch2Binding.layoutSearch.setLayoutParams(lp2);
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                KeyboardUtils.openKeyboard(mSearch2Binding.etSearch, UIUtils.getContext());
            }
        });
        //设置尺寸变化动画对象
        valueAnimator.start();
    }


    /**
     * 退场动画
     * @param viewBinding
     */
    public void outputAnimator(final ActivitySearchBinding viewBinding){
        final RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mSearch2Binding.searchContainer.getLayoutParams();
        final LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) mSearch2Binding.layoutSearch.getLayoutParams();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat( 1.0f,0.0f);
        valueAnimator.setDuration(250);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float money= (float) animation.getAnimatedValue();
                mSearch2Binding.btnNavBack.setAlpha(money);
                mSearch2Binding.tvSearch.setAlpha(money);
                viewBinding.rvList.getBackground().setAlpha((int) (255 * money));
                lp.width = (int)(mCurrWidth + (mWidth * 0.25 * (1- money)));
                mSearch2Binding.searchContainer.setLayoutParams(lp);
                lp2.setMargins((int) (-mCurrLeftMargin + mLeftMargin * (1- money)),0,0,0);
                mSearch2Binding.layoutSearch.setLayoutParams(lp2);
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                KeyboardUtils.closeKeyboard(mSearch2Binding.etSearch,UIUtils.getContext());
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        //设置尺寸变化动画对象
        valueAnimator.start();
    }

}
