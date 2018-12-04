package com.student.alex.android_assign;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class Tab1Note extends Fragment {
    private ListView listView2;
    private ArrayList<note_inp> note = new ArrayList<>();
    private ArrayList<String> list3;
    private DatabaseReference ref;
    MyAdapter adapter;
    String uid,key2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.activity_tab1,container,false);
        list3 = new ArrayList<String>();
        listView2 = rootView.findViewById(R.id.listv);
        adapter = new MyAdapter(this.getActivity(),note);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentFirebaseUser.getUid();
        ref = FirebaseDatabase.getInstance().getReference().child(uid).child("Note");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    key2 = dataSnapshot1.getKey();
                    note_inp notes = dataSnapshot1.getValue(note_inp.class);
                    note.add(notes);
                    list3.add(key2);
                    listView2.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String z = list3.get(position);
                Intent i = new Intent(rootView.getContext(), text_note.class);
                i.putExtra("Key2", z);
                startActivity(i);
            }
        });

        listView2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(Tab1Note.this.getActivity())
                        .setTitle("Delete")
                        .setMessage("Do you want to delete this?")
                        .setPositiveButton("YES",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                String z = list3.get(position);
                                list3.remove(position);
                                listView2.setAdapter(adapter);
                                ref.child(z).removeValue();
                                Toast.makeText(rootView.getContext(), "Note had been deleted", Toast.LENGTH_SHORT).show();
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
