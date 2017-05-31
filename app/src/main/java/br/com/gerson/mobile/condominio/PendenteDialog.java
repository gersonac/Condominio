package br.com.gerson.mobile.condominio;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by gerson on 30/05/2017.
 */

public class PendenteDialog extends DialogFragment {

    public interface PendenteDialogListener {
        public void onItemClick(DialogFragment dialog, int which);
    }

    PendenteDialogListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (PendenteDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " deve implementar PendenteDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_pendentes_dialog).setItems(R.array.pendentes_array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onItemClick(PendenteDialog.this, which);
            }
        });
        return builder.create();
    }
}
