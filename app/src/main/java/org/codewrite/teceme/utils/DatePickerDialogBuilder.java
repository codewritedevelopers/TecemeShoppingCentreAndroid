package org.codewrite.teceme.utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TimePicker;

import java.util.Calendar;

public class DatePickerDialogBuilder {

    public static DatePickerDialog build(final Context context,
                                         DatePickerDialog.OnDateSetListener dateSetListener) {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(
                context, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateSetListener, year, month, day);

        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }


    public static TimePickerDialog build2(final Context context,
                                         TimePickerDialog.OnTimeSetListener timeSetListener) {

        TimePicker timePicker = new TimePicker(context);
        int hr = timePicker.getCurrentHour();
        int min = timePicker.getCurrentMinute();

        TimePickerDialog dialog = new TimePickerDialog(context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                timeSetListener,hr,min,false);

        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }
}
