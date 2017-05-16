package com.android.project.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.android.barball_library.Tkrefreshlayout.TwinklingRefreshLayout;
import com.android.barball_library.Tkrefreshlayout.footer.BallPulseView;
import com.android.barball_library.Tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.android.project.R;


/**
 * 作　　者：Leon（黄长亮）
 * 创建日期：2017/4/7
 */

public class RefreshLayout extends TwinklingRefreshLayout {

    public RefreshLayout(Context context) {
        this(context,null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        ProgressLayout progressLayout = new ProgressLayout(context);
        progressLayout.setColorSchemeResources(R.color.white);
        progressLayout.setProgressBackgroundColorSchemeResource(R.color.colorTheme);
        setHeaderView(progressLayout);

        BallPulseView ballPulseView = new BallPulseView(context);
        ballPulseView.setAnimatingColor(Color.parseColor("#ff4d4d"));
        setBottomView(ballPulseView);
    }

}
