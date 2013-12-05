package com.zzy.xiaoyacz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class AboutDialog extends SherlockDialogFragment{
	private static final String msg="“熟读唐诗三百首，不会吟诗也会吟。”<br/>愿这款小软件能给您的唐诗学习带来一点帮助。<br/>作者Email: zhangzuoyi@163.com，欢迎提出意见和建议！";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        /*builder.setMessage(R.string.about_message)
               .setPositiveButton(R.string.btn_accept, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                   }
               })
               .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                   }
               });*/
        builder.setMessage(Html.fromHtml(msg));
        builder.setTitle(R.string.about);
        builder.setIcon(R.drawable.notebook_icon);
        // Create the AlertDialog object and return it
        return builder.create();
	}
	
}
