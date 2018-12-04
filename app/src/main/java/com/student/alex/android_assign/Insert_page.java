package com.student.alex.android_assign;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.internal.FirebaseAppHelper;

import java.lang.reflect.Array;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;

public class Insert_page extends AppCompatActivity {

    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private EditText input;
    private String[] items;
    private Button add;
    private ListView listView;
    private String uid;
    private String newItem;
    String smtg, ky;

    private FirebaseDatabase fb = FirebaseDatabase.getInstance();
    private DatabaseReference dbrg, ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_page);


        listView = findViewById(R.id.listview);
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,arrayList);
        input = findViewById(R.id.inputtxt);

//      Get Current User ID
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        uid = currentFirebaseUser.getUid();
        add = findViewById(R.id.btnadd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newItem = input.getText().toString().trim();
                if(newItem == null || newItem.trim().equals("")){
                    Snackbar.make(findViewById(R.id.container),"Write Somethings", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    //Get value from list view push into array List
                    arrayList.add(newItem);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    ((EditText) findViewById(R.id.inputtxt)).setText("");
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(Insert_page.this)
                        .setTitle("Delete")
                        .setMessage("Do you want to delete this?")
                        .setPositiveButton("YES",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                arrayList.remove(position);
                                listView.setAdapter(adapter);
                                Toast.makeText(Insert_page.this, "Deleted", Toast.LENGTH_SHORT).show();

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
        dbrg = fb.getReference().child(uid).child("List");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_btn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Get a reference to our posts
    }

    @Override
    public void onStart(){
        super.onStart();
        ky = null;
        if(getIntent()!=null && getIntent().getExtras()!=null){
            Bundle bundle = getIntent().getExtras();
            if(!bundle.getString("Key").equals(null)){
                ky= bundle.getString("Key");
                edit();
            }
        }
    }

    @Override
    public void onBackPressed(){
        getValue2();
        return;
    }

    private void getValue2(){
        String text = input.getText().toString();
        if(!arrayList.isEmpty()){
            if(ky != null)
            {
                for (String tbl_login : arrayList) {
                    dbrg.child(ky).setValue(arrayList);
                }
                Toast.makeText(Insert_page.this, "Update Data Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Main_page.class));
            }
            else {
                ky = dbrg.push().getKey();
                //arrayList.add(uid);
                for (String tbl_login : arrayList) {
                    dbrg.child(ky).setValue(arrayList);
                }
                Toast.makeText(Insert_page.this, "Insert Data Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Main_page.class));
            }
        }

        else if(text.equals("")) {
            Toast.makeText(Insert_page.this, "No data to insert" ,Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),Main_page.class));
        }
        else if(text.length() > 1){
            new AlertDialog.Builder(Insert_page.this)
                    .setTitle("Exit")
                    .setMessage("Do you want to close this?")
                    .setPositiveButton("YES",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            Toast.makeText(Insert_page.this, "No data to insert" ,Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Main_page.class));
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            dialog.cancel();
                        }
                    }).show();
        }
    }

    private void edit(){
//        Intent i = getIntent();
//        ky = i.getStringExtra("Key").trim();
        dbrg.child(ky).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String items = ds.getValue().toString();
                    arrayList.add(items);
                }
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
