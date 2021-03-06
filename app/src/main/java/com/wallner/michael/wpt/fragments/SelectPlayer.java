package com.wallner.michael.wpt.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.wallner.michael.wpt.R;

public class SelectPlayer extends DialogFragment {

    public interface NoticeDialogListener {
        void onClickPlayer(DialogFragment dialog, String selectedPlayer);
    }

    public NoticeDialogListener mListener;
    private String[] playerlist;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        playerlist = getArguments().getStringArray("playerlist");
        builder.setTitle(R.string.erster_geber)
                .setItems(getArguments().getStringArray("playerlist"),
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onClickPlayer(SelectPlayer.this, playerlist[which]);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}

