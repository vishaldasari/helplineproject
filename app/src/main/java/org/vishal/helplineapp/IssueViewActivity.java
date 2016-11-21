package org.vishal.helplineapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.vishal.helplineapp.model.Issue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class IssueViewActivity extends AppCompatActivity {

    private EditText m_nameET;
    private EditText m_reporterET;
    private EditText m_descET;
    private EditText m_issueNumberET;

    private Button m_editButton;

    private FirebaseDatabase database;

    private HashMap<String, Issue> issueMap;

    private static final String TAG = "HelpLineApp";
    private Spinner m_issueSpinner;

    private Boolean newIssueFlag = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setAlpha((float) .25);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG);
                snackbar.setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((FloatingActionButton) findViewById(R.id.fab)).setAlpha((float) .25);
                            }
                        });
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {
                        super.onShown(snackbar);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((FloatingActionButton) findViewById(R.id.fab)).setAlpha((float) 1);
                            }
                        });
                    }
                });
                snackbar.setAction("Action", null).show();
            }
        });
        m_issueSpinner = (Spinner) findViewById(R.id.issueSpinner);
        m_issueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                populateFieldsWithIssue((String) parent.getItemAtPosition(position));
                m_editButton.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        m_nameET = (EditText) findViewById(R.id.nameET);
        m_reporterET = (EditText) findViewById(R.id.reporterET);
        m_descET = (EditText) findViewById(R.id.descET);
        m_issueNumberET = (EditText) findViewById(R.id.issueNumberEditText);

        m_editButton = (Button) findViewById(R.id.editButton);

        database = FirebaseDatabase.getInstance();
        setUpDatabase();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }




    private void populateFieldsWithIssue(String itemAtPosition) {

        String issueNumber = itemAtPosition.substring(0,6);

        m_nameET.setText(issueMap.get(issueNumber).getName());
        m_nameET.setEnabled(false);
        m_reporterET.setText(issueMap.get(issueNumber).getReporter());
        m_reporterET.setEnabled(false);
        m_descET.setText(issueMap.get(issueNumber).getDescription());
        m_descET.setEnabled(false);
        m_issueNumberET.setText(issueNumber);
        m_issueNumberET.setEnabled(false);



    }

    private void setUpDatabase() {
        // Read from the database
        DatabaseReference myRef = database.getReference("issues/" + getCurrentUserUID() + "/");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                GenericTypeIndicator<HashMap<String, Issue>> t = new GenericTypeIndicator<HashMap<String, Issue>>() {
                };
                issueMap = dataSnapshot.getValue(t);
                if(issueMap!= null) {
                    Log.d(TAG, "Value is: " + issueMap.toString());
                    setUpSpinner();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void setUpSpinner() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                genSpinnerItemsArray());
        int currentPosition = m_issueSpinner.getSelectedItemPosition();
        m_issueSpinner.setAdapter(adapter);
        m_issueSpinner.setSelection(currentPosition);

    }


    //will return spinner items in the format XXXXXX - name - reporter
    private String[] genSpinnerItemsArray() {
        Set<String> setOfIssueNumbers = issueMap.keySet();
        List<String> spinnerItems = new ArrayList<String>();
       for (String issueNumber : setOfIssueNumbers) {
           Issue issue = issueMap.get(issueNumber);
           spinnerItems.add("" + issue.getNumber() + " - " + issue.getName());
       }
       return spinnerItems.toArray(new String[spinnerItems.size()]);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_volunteer_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_new_issue) {
            Issue newIssue = prepNewIssue();
            saveIssueInDatabse(newIssue);
        }

        if( id == R.id.action_delete_issue) {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Issue")
                    .setMessage("Are you sure you want to delete this issue?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(deleteIssueInDatabase(getCurrentIssue())){
                                Toast.makeText(IssueViewActivity.this, "Issue successfully deleted!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(IssueViewActivity.this, "There was a problem deleting this issue", Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        }

        if (id == R.id.action_signout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }



    private Issue prepNewIssue() {
        m_issueNumberET.setText("");
        m_descET.setText("");
        m_descET.setEnabled(true);
        m_reporterET.setText("");
        m_reporterET.setEnabled(true);
        m_nameET.setText("");
        m_nameET.setEnabled(true);


        Issue newIssue = new Issue("", generateNumber(), "", "");
        m_issueNumberET.setText(newIssue.getNumber());
        m_editButton.setEnabled(false);

        newIssueFlag = true;

        return newIssue;

    }

    private void saveIssueInDatabse(Issue toBeSaved) {
        DatabaseReference issueDataRef = database.getReference("issues/" + getCurrentUserUID() + "/" + toBeSaved.getNumber());
        issueDataRef.setValue(toBeSaved);

    }

    private boolean deleteIssueInDatabase(Issue toBeDeleted) {
        if(toBeDeleted != null) {
            DatabaseReference issueDataRef = database.getReference("issues/" + getCurrentUserUID() + "/" + toBeDeleted.getNumber());
            issueDataRef.setValue(null);
            return true;
        }
        return false;

    }

    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }


    public void onClickSaveButton(View view) {
        String issueName = m_nameET.getText().toString();
        String reporterName = m_reporterET.getText().toString();
        Issue toBeSaved = new Issue(issueName, getCurrentIssue().getNumber(), reporterName, m_descET.getText().toString());

        saveIssueInDatabse(toBeSaved);

        m_descET.setEnabled(false);
        m_reporterET.setEnabled(false);
        m_nameET.setEnabled(false);

    }


    public void onClickEditButton(View view) {
        Button editButton = (Button) view;
        if( editButton.getText() == getString(R.string.discard))
        {

        }
        else
        {
            m_nameET.setEnabled(true);
            m_reporterET.setEnabled(true);
            m_descET.setEnabled(true);
        }

    }


    public String generateNumber() {

        int number;
        if(issueMap != null) {
            do {
                Random rnd = new Random();
                number = 100000 + rnd.nextInt(900000);
            } while (issueMap.get(number) != null);
        }
        else {
            Random rnd = new Random();
            number = 100000 + rnd.nextInt(900000);
        }

        return Integer.toString(number);
    }


    private String getCurrentUserUID () {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getUid();
    }


    private Issue getCurrentIssue() {
        if(issueMap!= null) {
            if(issueMap.size() > 0) {
                String issueNumber = ((String)m_issueSpinner.getSelectedItem()).substring(0,6);
                return issueMap.get(issueNumber);
            }
        }
        return null;


    }
}
