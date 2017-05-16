package com.android.barball_library.Tkrefreshlayout;

public interface PullListener {
        /**
         * 下拉中
         *
         * @param fraction
         */
        void onPullingDown(float fraction);

        /**
         * 上拉
         */
        void onPullingUp(float fraction);

        /**
         * 下拉松开
         *
         * @param fraction
         */
        void onPullDownReleasing(float fraction);

        /**
         * 上拉松开
         */
        void onPullUpReleasing(float fraction);

        /**
         * 刷新中。。。
         */
        void onRefresh();

        /**
         * 加载更多中
         */
        void onLoadMore();

        /**
         * 手动调用finishRefresh或者finishLoadmore之后的回调
         */
        void onFinishRefresh();

        void onFinishLoadMore();

        /**
        * 正在刷新时向上滑动屏幕，刷新被取消
        */
        void onRefreshCanceled();

        /**
         * 正在加载更多时向下滑动屏幕，加载更多被取消
         */
        void onLoadMoreCanceled();
    }