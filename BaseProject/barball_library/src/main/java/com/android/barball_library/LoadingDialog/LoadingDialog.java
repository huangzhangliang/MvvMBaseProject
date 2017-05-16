package com.android.barball_library.LoadingDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.android.barball_library.R;


/**
 * Created by cy on 2016/7/27.
 * 等待框
 */
@SuppressLint("ValidFragment")
public class LoadingDialog extends DialogFragment {

    View view;
    Context context;
    DialogInterface.OnCancelListener onCancelListener;

    public LoadingDialog(Context context) {
        this.context=context;
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener){
        this.onCancelListener=onCancelListener;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (onCancelListener!=null)
            onCancelListener.onCancel(dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        getDialog().getWindow().setGravity(Gravity.CENTER);

        getDialog().setCanceledOnTouchOutside(false);

        view=inflater.inflate(R.layout.dialog_loading,container);

        return view;
    }

   /* //全屏宽度操作
    @Override
    public void onStart() {
        super.onStart();
        ViewGroup.LayoutParams lp= view.getLayoutParams();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );
        lp.width=dm.widthPixels;
        view.setLayoutParams(lp);
        this.isVisible()
    }
*/

    public boolean isShowing(){
        if (getDialog()!=null  && getDialog().isShowing())
            return true;
        return false;
    }

    public void show() {
        try {
           show(((FragmentActivity)context).getFragmentManager(),"");
        }catch (Exception e){
            show(((Activity)context).getFragmentManager(),"");

        }

    }


}

