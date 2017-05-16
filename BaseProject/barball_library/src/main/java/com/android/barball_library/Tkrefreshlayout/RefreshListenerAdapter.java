package com.android.barball_library.Tkrefreshlayout;

public abstract class RefreshListenerAdapter implements PullListener {
        @Override
        public void onPullingDown(float fraction) {
        }

        @Override
        public void onPullingUp(float fraction) {
        }

        @Override
        public void onPullDownReleasing( float fraction) {
        }

        @Override
        public void onPullUpReleasing( float fraction) {
        }

        @Override
        public void onRefresh() {
        }

        @Override
        public void onLoadMore() {
        }

        @Override
        public void onFinishRefresh() {

        }

        @Override
        public void onFinishLoadMore() {

        }

        @Override
        public void onRefreshCanceled() {

        }

        @Override
        public void onLoadMoreCanceled() {

        }
}