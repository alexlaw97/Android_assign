package com.student.alex.android_assign;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nispok.snackbar.Snackbar;

import java.util.Map;

public class text_note extends AppCompatActivity {

    private FirebaseDatabase fb = FirebaseDatabase.getInstance();
    private DatabaseReference dbrg;
    private EditText contents, titles;
    private String uid;
    private String inputtxt, inputtitle;
    String tit ,ky2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_note);
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));

        contents = findViewById(R.id.txt_content);
        titles = findViewById(R.id.txt_title);
        //Get User Current ID
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        uid = currentFirebaseUser.getUid();
        dbrg = fb.getReference().child(uid).child("Note");
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_btn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValue();
            }
        });


    }
    @Override
    public void onStart(){
        super.onStart();
        ky2 = null;
        if(getIntent()!=null && getIntent().getExtras()!=null){
            Bundle bundle = getIntent().getExtras();
            if(!bundle.getString("Key2").equals(null)){
                ky2= bundle.getString("Key2");
                edit();
            }
        }
    }

    @Override
    public void onBackPressed(){
        getValue();
        return;
    }
    private void getValue(){

        inputtxt = contents.getText().toString();
        inputtitle = titles.getText().toString();
        if(inputtxt.trim().equals("") && inputtitle.trim().equals("")) {

            Toast.makeText(text_note.this, "No data to insert" ,Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),Main_page.class));
        }
        else if(inputtitle.length() > 1 && inputtxt.equals("")){
           new AlertDialog.Builder(text_note.this)
                   .setTitle("Exit")
                   .setMessage("Do you want to close this?\n Notice! It will not save the data")
                   .setPositiveButton("YES",new DialogInterface.OnClickListener(){
                       public void onClick(DialogInterface dialog, int id){

                           Toast.makeText(text_note.this, "No data to insert" ,Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(getApplicationContext(),Main_page.class));
                       }
            })
                   .setNegativeButton("NO", new DialogInterface.OnClickListener(){
                       public void onClick(DialogInterface dialog, int id){
                           dialog.cancel();
                       }
                   }).show();
        }
        else {
            if(ky2 != null){
                note_inp input = new note_inp(inputtitle,inputtxt);
                dbrg.child(ky2).setValue(input);
                Toast.makeText(text_note.this, "Update Data Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Main_page.class));
            }
            else {
                String id = dbrg.push().getKey();
                note_inp input = new note_inp(inputtitle, inputtxt);
                dbrg.child(id).setValue(input);
                Toast.makeText(text_note.this, "Insert Data Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Main_page.class));
            }
        }
    }

    private void edit(){
        dbrg.child(ky2).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    tit = dataSnapshot.child("title").getValue(String.class);
                    String cont = dataSnapshot.child("content").getValue(String.class);
                    titles.setText(tit);
                    contents.setText(cont);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error");
            }
        });
    }
}
