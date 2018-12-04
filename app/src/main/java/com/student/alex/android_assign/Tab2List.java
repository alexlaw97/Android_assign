package com.student.alex.android_assign;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Tab2List extends Fragment {
    private ListView mListView2;
    private ArrayAdapter<String> adapter2;
    private ArrayList<String> list1, list2;
    private DatabaseReference ref;
    String uid, smtg, key;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.activity_tab2,container,false);
        mListView2 = rootView.findViewById(R.id.listv2);
        list1 = new ArrayList<String>();
        list2 = new ArrayList<String>();
        adapter2 = new ArrayAdapter<String>(this.getActivity(),R.layout.support_simple_spinner_dropdown_item,list2);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentFirebaseUser.getUid();
        ref = FirebaseDatabase.getInstance().getReference().child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String bla = dataSnapshot1.getKey();
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                        if (bla.equals("List")) {
                            key = dataSnapshot2.getKey();
                            smtg = dataSnapshot2.getValue().toString();
                            smtg = smtg.replace("[", "");
                            smtg = smtg.replace("]", "");
                            smtg = smtg.replace(",", "");
                            smtg = smtg.replace("{", "");
                            smtg = smtg.replace("}", "");
                            smtg = smtg.replace("c", "C");
                            list2.add(smtg);
                            list1.add(key);
                            mListView2.setAdapter(adapter2);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String x = list1.get(position).toString();
                    Intent i = new Intent(rootView.getContext(), Insert_page.class);
                    i.putExtra("Key", x);
                    startActivity(i);
            }
        });

        mListView2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(Tab2List.this.getActivity())
                        .setTitle("Delete")
                        .setMessage("Do you want to delete this?")
                        .setPositiveButton("YES",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                String x = list1.get(position);
                                list1.remove(position);
                                mListView2.setAdapter(adapter2);
                                ref.child("List").child(x).removeValue();
                                Toast.makeText(rootView.getContext(), "List had been deleted", Toast.LENGTH_SHORT).show();
                                startActivity(getActivity().getIntent());
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                dialog.cancel();
                            }
                        }).show();
                return true;
            }
        });
        return rootView;
    }

}
