package org.codewrite.teceme.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import org.codewrite.teceme.R;

public  class ContentLoadingDialog {

    public static AlertDialog create(Context context, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_content_loading,null);
        builder.setView(view);
        TextView textMessage = view.findViewById(R.id.textMessage);
        textMessage.setText(message);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        return dialog;
    }
}
