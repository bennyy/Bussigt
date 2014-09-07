package com.bom.bussig.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.bom.bussig.BussigApplication;
import com.bom.bussig.Helpers.AlarmManagerBroadcastReceiver;
import com.bom.bussig.R;

/**
 * Created by Mackan on 2014-09-07.
 */
public class SetAlarmDialogActivity extends DialogFragment {
    public interface SetAlarmListener {
        public void onSetAlarm(DialogFragment dialog);
        public void onCancelSelectAlarm(DialogFragment dialog);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int minutesToDeparture = (int)getArguments().getInt("Departure");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Use the Builder class for convenient dialog construction
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_select_minutes, null);

        final NumberPicker np = (NumberPicker) view.findViewById(R.id.alarm_minutes_before_departure);
        np.setMaxValue(minutesToDeparture);
        np.setMinValue(0);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        AlarmManagerBroadcastReceiver alarm = new AlarmManagerBroadcastReceiver();
                        alarm.setOnetimeTimer(BussigApplication.getContext(), np.getValue());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }
}