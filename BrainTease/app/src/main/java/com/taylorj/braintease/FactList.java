package com.taylorj.braintease;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

//Handles all options and functions on the fact_list activity
public class FactList extends AppCompatActivity {

    DatabaseManager dbManager;
    ArrayAdapter<String> mAdapter;
    ListView lstFact;
    SearchView svSearchFact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fact_list);


        dbManager = new DatabaseManager(this);
        svSearchFact = (SearchView) findViewById(R.id.svSearchFact);
        lstFact = (ListView)findViewById(R.id.lstFacts);

        svSearchFact.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                lstFact.setAdapter(mAdapter);

                return false;
            }
        });

        svSearchFact.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> factList = new ArrayList<>();
                ArrayList<String> curList = dbManager.getAllFacts();

                for(String fact : curList)
                {
                    if(fact.toLowerCase().contains(newText.toLowerCase()))
                    {
                        factList.add(fact);
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(FactList.this, R.layout.row_fact, R.id.factTitle, factList);
                lstFact.setAdapter(adapter);

                return true;
            }
        });

        loadList();
    }
    private void loadList()
    {
        ArrayList<String> factList = dbManager.getAllFacts();

        if(mAdapter == null)
        {
            mAdapter = new ArrayAdapter<String>(this, R.layout.row_fact, R.id.factTitle, factList);
            lstFact.setAdapter(mAdapter);
        }
        else
        {
            mAdapter.clear();
            mAdapter.addAll(factList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.add_item, menu);
        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.itmAddFact:
                final EditText etxFactEdit = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("What interesting fact do you have?")
                        .setView(etxFactEdit)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String fact = String.valueOf(etxFactEdit.getText());
                                if(fact.isEmpty())
                                {
                                    Toast.makeText(FactList.this, "Must enter something!", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Boolean check = dbManager.checkDups("Facts", fact);

                                    if(check == true)
                                    {
                                        Toast.makeText(FactList.this, "That already exists within the database!", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        dbManager.insertNewFact(fact);
                                        loadList();
                                    }
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteFact(View view)
    {
        View parent = (View) view.getParent();
        TextView factTextView = (TextView) parent.findViewById(R.id.factTitle);
        String fact = String.valueOf(factTextView.getText());
        dbManager.deleteFact(fact);
        loadList();
    }

    public void updateFact(View view)
    {
        TextView factTextView = (TextView) findViewById(R.id.factTitle);
        final String oldFact = String.valueOf(factTextView.getText());

        final EditText etxFactEdit = new EditText(this);
        etxFactEdit.setText(oldFact);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Edit this fact")
                .setView(etxFactEdit)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fact = String.valueOf(etxFactEdit.getText());
                        dbManager.editFact(fact, oldFact);
                        loadList();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
}
