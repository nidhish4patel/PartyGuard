package com.example.s525339.partygaurd_androidnachos;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**

 * It is where the user can select the university name.Based on that fraternity list would be populated.
 */

public class UniversityDialogFragment extends DialogFragment {


    University theUniversity;
    String[] universitiesArray = {"Northwest Missouri State University","Georgia Tech","University of Nebraska","University of Arkansas"};

    public interface University
    {
        void getUniversityName(String universityName);
    }



    public void onAttach(Activity act)
    {
        super.onAttach(act);
        theUniversity =  (University) act;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose_university)
                .setItems(universitiesArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        theUniversity.getUniversityName(universitiesArray[which]);
                    }
                });
        return builder.create();
    }
}
