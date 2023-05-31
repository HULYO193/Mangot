package com.example.mangot;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
    private Spinner spinnerstatus;
    private Spinner spinnerchapters;
    private EditText editTextPassword;
    private Button button;
    private DashboardDialogListener listener;
    private String mName;
    private String mStatus;
    private int mChapters;
    private ArrayList<MangaStatus> usermangas;
    private String baseStatus = "";
    private Context c;
    private static final ArrayList<String> statusChoicesArr = new ArrayList<>();
    private  static final ArrayList<String> statusChaptersArr = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth =  FirebaseAuth.getInstance();

                        // manga name  manga status  maxchapters        all the mangas of the user       DashboardActivity
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
        statusChaptersArr.clear();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_for_dashboard,null);

        builder.setView(view)
                .setTitle("Options")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getDialog().dismiss();

                    }
                })
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        getDialog().dismiss();


                    }
                });
        spinnerstatus = (Spinner) view.findViewById(R.id.statusSpinner);
        statusChoicesArr.add(baseStatus);

        statusChoicesArr.add("Reading");
        statusChoicesArr.add("On Hold");
        statusChoicesArr.add("Completed");
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,statusChoicesArr);
        statusAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        spinnerstatus.setAdapter(statusAdapter);


        spinnerstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                   String value =statusChoicesArr.get(i);
                   if(value.equals(baseStatus))
                       return;

                   mStatus = value;

                db.collection("MangaStatus").document(""+mAuth.getCurrentUser().getEmail())
                        .collection("userMangas")
                        .document(""+mName)
                        .update("status",mStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    usermangas = updateselectedMangaStatus(usermangas, mName,mStatus);
                                    ((DashboardActivity)(DashboardDialog.this.c)).refreshAdapter();
                                }
                            }
                        });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //mStatus = statusChoicesArr.get(0);

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
                                  //  MangaAdapter mangaAdapter = new MangaAdapter(usermangas,(DashboardActivity)c);


                                    ((DashboardActivity)(DashboardDialog.this.c)).refreshAdapter();
                                    getDialog().dismiss();

                                }
                            }
                        });



            }
        });
            //////
        spinnerchapters = (Spinner) view.findViewById(R.id.chapterspinner);
        statusChaptersArr.add(baseStatus);
        for (int i = 1; i <= mChapters; i++) {
            statusChaptersArr.add("Chapter "+i);
        }
        ArrayAdapter<String> statusAdapterchapters = new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,statusChaptersArr);
        statusAdapterchapters.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerchapters.setAdapter(statusAdapterchapters);
        spinnerchapters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String value = statusChaptersArr.get(i);
                if(value.equals(baseStatus))
                    return;
                String[] res = value.split(" ");

                int chap = Integer.valueOf(res[1]);

                db.collection("MangaStatus").document(""+mAuth.getCurrentUser().getEmail())
                        .collection("userMangas")
                        .document(""+mName)
                        .update("currChapter",chap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    usermangas = updateselectedMangaChapter(usermangas, mName,chap);
                                    ((DashboardActivity)(DashboardDialog.this.c)).refreshAdapter();
                                }
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
    public ArrayList<MangaStatus> updateselectedMangaStatus(ArrayList<MangaStatus> mangaStatuses, String mNAME, String mstatus){
        ArrayList<MangaStatus> tmp = mangaStatuses ;
        for (int i = 0; i < tmp.size(); i++) {
            if(tmp.get(i).getMangaName() == mNAME)
                tmp.get(i).setStatus(mstatus);
        }
        return tmp;

    }
    public ArrayList<MangaStatus> updateselectedMangaChapter(ArrayList<MangaStatus> mangaStatuses, String mNAME, int chapt){
        ArrayList<MangaStatus> tmp = mangaStatuses ;
        for (int i = 0; i < tmp.size(); i++) {
            if(tmp.get(i).getMangaName() == mNAME)
                tmp.get(i).setCurrChapter(chapt);
        }
        return tmp;

    }
}
