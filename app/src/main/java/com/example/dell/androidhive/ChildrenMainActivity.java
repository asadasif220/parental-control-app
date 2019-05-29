package com.example.dell.androidhive;

import android.Manifest;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChildrenMainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth auth;
    private Button signOut;
    private FirebaseAuth.AuthStateListener authListener;
    private Button dashBoard;
    String userID;

    private TextView currentEmail;
//    private String cEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        auth = FirebaseAuth.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(ChildrenMainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        setContentView(R.layout.activity_children_main);

//        currentEmail = (TextView) findViewById(R.id.current_child_email);
        signOut = findViewById(R.id.sign_out);
        dashBoard = findViewById(R.id.button_dashbord);

//        cEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

//        currentEmail.setText(cEmail);

//        getPermission();

        sendCallLog();
        sendHistory();
        sendMsgLog();


        dashBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("DASHBOARD");
                Intent intent = new Intent(ChildrenMainActivity.this, ChildDashOptions.class);
                startActivity(intent);
                finish();


            }
        });

        /**firebaseAuth = FirebaseAuth.getInstance();
         if (firebaseAuth.getCurrentUser()==null)
         {
         finish();
         startActivity( new Intent(this,LoginActivity.class));
         }*/

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidhive-124c5.firebaseio.com/Users/Current Child").setValue(null);

                firebaseAuth.signOut();
                Intent intent = new Intent(ChildrenMainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    public void sendCallLog() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            Log.i("", "===============Permission NOT granted..");
            requestPermission();
        } else {
            Log.i("", "===============Permission has been granted. Displaying CALL LOG preview.");
        }

        Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);

        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {

                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }

            FirebaseDatabase.getInstance().getReference().child("Users").child("Children").child(userID).child("Call Log").push().setValue(new tempCall(phNumber, dir, callDayTime.toString(), callDuration));
        }
        managedCursor.close();
        return;
    }

    public void sendMsgLog() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.i("", "===============Permission NOT granted..");
            requestPermission();
        } else {
            Log.i("", "===============Permission has been granted. Displaying CALL LOG preview.");
        }

        Uri inboxURI = Uri.parse("content://sms/inbox");

        String[] reqTags = new String[]{"address", "body"};

        Cursor cursor = getContentResolver().query(inboxURI, reqTags, null, null, null);

        if(cursor.moveToFirst())

        { // must check the result to prevent exception
            do {
                String msgData = "";

                Map<String, TempClass> myMsg = new HashMap<>();
                Map<String, String> m2 = new HashMap<>();


                for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                    m2.put(cursor.getColumnName(idx), cursor.getString(idx));

                    FirebaseDatabase.getInstance().getReference().child("Users").child("Children").child(userID).child("Msg Log").push().setValue(m2);
                }
                // use msgData
            } while (cursor.moveToNext());
        } else

        {
            // empty box, no SMS
        }
    }




    public void sendHistory(){

    }
    public void getPermission(){
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
//            Log.i("", "===============Permission NOT granted..");
//            requestPermission();
//        } else {
//            Log.i("", "===============Permission has been granted. Displaying CALL LOG preview.");
//        }
    }
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALL_LOG)) {
            // you can show dialog here for grant permission (call log) and handle dialog event according to your need

        } else {
            // call log permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, 0);
        }
    }
}
