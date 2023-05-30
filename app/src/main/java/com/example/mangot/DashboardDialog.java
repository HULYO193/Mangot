package com.example.mangot;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DashboardDialog extends AppCompatDialogFragment  {
    private Spinner spinner;
    private EditText editTextPassword;
    private Button button;
    private DashboardDialogListener listener;
    private String mName;
    private String mStatus;
    private int mChapters;
    private ArrayList<MangaStatus> usermangas;

    private Context c;
    private static final ArrayList<String> statusChoicesArr = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth =  FirebaseAuth.getInstance();

    public DashboardDialog(String mName, String mStatus, int mChapters, ArrayList<MangaStatus> usermanga,Context c){
        this.mName = mName;
        this.mStatus = mStatus;
        this.mChapters = mChapters;
        this.usermangas = usermanga;
        this.c = c;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        statusChoicesArr.clear();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_for_dashboard,null);

        builder.setView(view)
                .setTitle("Options")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                    }
                });
        spinner = (Spinner) view.findViewById(R.id.statusSpinner);

        statusChoicesArr.add("Reading");
        statusChoicesArr.add("On Hold");
        statusChoicesArr.add("Completed");
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,statusChoicesArr);
        statusAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        spinner.setAdapter(statusAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mStatus = statusChoicesArr.get(i);

                db.collection("MangaStatus").document(""+mAuth.getCurrentUser().getEmail())
                        .collection("userMangas")
                        .document(""+mName)
                        .update("status",mStatus);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mStatus = statusChoicesArr.get(0);
            }
        });
        //still not working after putting it in dialog , maybe need to update the array of the adapter for it to work
        button = (Button) view.findViewById(R.id.deletebutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("MangaStatus")
                        .document(""+ mAuth.getCurrentUser().getEmail())
                        .collection("userMangas")
                        .document(""+mName).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    usermangas = deleteselectedManga(usermangas, mName);
                                    MangaAdapter mangaAdapter = new MangaAdapter(usermangas,(DashboardActivity)c);
                                    mangaAdapter.notifyDataSetChanged();
                                }
                            }
                        });



            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener =(DashboardDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException((context.toString() + "must implement DashboardDialogListener"));
        }
    }

    public interface  DashboardDialogListener{
        void applyText(String username, String password);
    }
    public ArrayList<MangaStatus> deleteselectedManga(ArrayList<MangaStatus> mangaStatuses,String mNAME)
    {
        ArrayList<MangaStatus> tmp = mangaStatuses ;
        for (int i = 0; i < tmp.size(); i++) {
            if(tmp.get(i).getMangaName() == mNAME)
                tmp.remove(i);
        }
        return tmp;
    }
}
