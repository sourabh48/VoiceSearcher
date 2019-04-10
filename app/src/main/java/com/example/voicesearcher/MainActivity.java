package com.example.voicesearcher;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.*;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends Activity
{
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private String derived_text;
    private static final String TAG = MainActivity.class.getSimpleName();
    private RelativeLayout coordinatorLayout;
    DatabaseReference mTasksDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnSpeak.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                promptSpeechInput();
            }
        });
        coordinatorLayout = (RelativeLayout) findViewById(R.id.relativelayout);
        FirebaseApp.initializeApp(this);
        mTasksDatabaseReference = FirebaseDatabase.getInstance().getReference("Places");
    }


    private void promptSpeechInput()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        }
        catch (ActivityNotFoundException a)
        {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case REQ_CODE_SPEECH_INPUT:
            {
                if (resultCode == RESULT_OK && null != data)
                {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d(TAG, result.get(0));

                    derived_text = result.get(0);

                    txtSpeechInput.setText(derived_text);

                    derived_text = derived_text.toLowerCase();

                    Pattern pattern  = Pattern.compile("^find the places around [a-z ]*$");
                    Matcher matcher = pattern.matcher(derived_text);

                    if(matcher.matches())
                    {
                        final String substring = derived_text.substring(23).trim();


                        mTasksDatabaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if(dataSnapshot.hasChild(substring))
                                {
                                    Intent list = new Intent(MainActivity.this, ListViewActivity.class);
                                    list.putExtra("value", substring);
                                    startActivity(list);
                                }
                                else
                                {
                                    Snackbar snackbar = Snackbar.make(coordinatorLayout, substring + " I'm Sorry No Data Found", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    else
                    {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "I'm Sorry not the Valid Input", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }


                }
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    private static final int TIME_INTERVAL = 1000;
    private long mBackPressed;

    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }
}