package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView totalcall, incomingncall, outgoingcall, missedcall, receivedcall;
    //Button refresh;
    String in, out, total, missed, received;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        totalcall = findViewById(R.id.tvTotalCallCount);
        outgoingcall = findViewById(R.id.tvTotalOutgoingCallCount);
        incomingncall = findViewById(R.id.tvTotalIncomingCallCount);
        missedcall = findViewById(R.id.tvTotalMissedCallCount);
        receivedcall = findViewById(R.id.tvTotalReceievdCallCount);
        //refresh = findViewById(R.id.button);


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG)!=PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CALL_LOG)){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
            }else{
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
            }
        }else{
            updateCallCounts();
        }

//        refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateCallCounts();
//            }
//        });
//        <Button
//        android:id="@+id/button"
//        android:layout_width="wrap_content"
//        android:layout_height="wrap_content"
//        android:text="REFRESH DATA"
//        android:layout_marginTop="30dp"
//        android:textStyle="bold"
//        android:textSize="25dp"
//        android:textColor="@color/white"
//        app:layout_constraintEnd_toEndOf="parent"
//        app:layout_constraintHorizontal_bias="0.5"
//        app:layout_constraintStart_toStartOf="parent"
//        app:layout_constraintTop_toBottomOf="@+id/tvTotalReceievdCallCount" />
        // butto code

        updateCallCounts();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:{
                {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG)==PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "Please restart the app.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(this, "No Permission Granted", Toast.LENGTH_SHORT).show();
                        }
                    }return;
                }
            }
        }
    }

    private void updateCallCounts() {
        String[] projectionMissed = { CallLog.Calls.CACHED_NAME, CallLog.Calls.CACHED_NUMBER_LABEL, CallLog.Calls.TYPE };
        String whereMissed = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE;
        Cursor m = getContentResolver().query(CallLog.Calls.CONTENT_URI, projectionMissed, whereMissed, null, null);

        if (m != null) {
            try {
                ((Cursor) m).moveToFirst();
                missed = String.valueOf(m.getCount());
                missedcall.setText("Missed Calls: " + missed);
            } finally {
                m.close(); // Close the Cursor to avoid memory leaks
            }
        }

        String[] projectionOutgoing = { CallLog.Calls.CACHED_NAME, CallLog.Calls.CACHED_NUMBER_LABEL, CallLog.Calls.TYPE };
        String whereOutgoing = CallLog.Calls.TYPE + "=" + CallLog.Calls.OUTGOING_TYPE;
        Cursor o = getContentResolver().query(CallLog.Calls.CONTENT_URI, projectionOutgoing, whereOutgoing, null, null);

        if (o != null) {
            try {
                o.moveToFirst();
                out = String.valueOf(o.getCount());
                outgoingcall.setText("Outgoing Calls: " + out);
            } finally {
                o.close(); // Close the Cursor to avoid memory leaks
            }
        }

        String[] projectionIncoming = { CallLog.Calls.CACHED_NAME, CallLog.Calls.CACHED_NUMBER_LABEL, CallLog.Calls.TYPE };
        String whereOIcoming = CallLog.Calls.TYPE + "=" + CallLog.Calls.INCOMING_TYPE;
        Cursor k = getContentResolver().query(CallLog.Calls.CONTENT_URI, projectionIncoming, whereOIcoming, null, null);

        if (k != null) {
            try {
                k.moveToFirst();
                in = String.valueOf(k.getCount());
                incomingncall.setText("Incoming Calls: " + in);
            } finally {
                k.close(); // Close the Cursor to avoid memory leaks
            }
        }

        int rec = Integer.parseInt(in)-Integer.parseInt(missed);
        received = String.valueOf(rec);
        receivedcall.setText("Received Calls:" + received);

        int net = Integer.parseInt(in)+Integer.parseInt(out);
        total = String.valueOf(net);
        totalcall.setText("Total Calls:" + total);

    }

}