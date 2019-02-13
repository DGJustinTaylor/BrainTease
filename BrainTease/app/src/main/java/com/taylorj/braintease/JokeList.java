package com.taylorj.braintease;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

//This will be nearly identical to the FactList class.
//Could have made one standard class, but this was easier to keep track of.
public class JokeList extends AppCompatActivity
{
    DatabaseManager dbManager;
    ArrayAdapter<String> mAdapter;
    ListView lstJoke;
    SearchView svSearchJoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joke_list);


        dbManager = new DatabaseManager(this);
        svSearchJoke = (SearchView) findViewById(R.id.svSearchJoke);
        lstJoke = (ListView)findViewById(R.id.lstJokes);

        svSearchJoke.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                lstJoke.setAdapter(mAdapter);

                return false;
            }
        });

        svSearchJoke.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> jokeList = new ArrayList<>();
                ArrayList<String> curList = dbManager.getAllJokes();

                for(String joke : curList)
                {
                    if(joke.toLowerCase().contains(newText.toLowerCase()))
                    {
                        jokeList.add(joke);
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(JokeList.this, R.layout.row_joke, R.id.jokeTitle,jokeList);
                lstJoke.setAdapter(adapter);

                return true;
            }
        });

        loadList();
    }
    private void loadList()
    {
        ArrayList<String> jokeList = dbManager.getAllJokes();

        if(mAdapter == null)
        {
            mAdapter = new ArrayAdapter<String>(this, R.layout.row_joke, R.id.jokeTitle, jokeList);
            lstJoke.setAdapter(mAdapter);
        }
        else
        {
            mAdapter.clear();
            mAdapter.addAll(jokeList);
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
                final EditText etxJokeEdit = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("What funny joke do you have?")
                        .setView(etxJokeEdit)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String joke = String.valueOf(etxJokeEdit.getText());
                                if(joke.isEmpty())
                                {
                                    Toast.makeText(JokeList.this, "Must enter something!", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Boolean check = dbManager.checkDups("Jokes", joke);

                                    if(check == true)
                                    {
                                        Toast.makeText(JokeList.this, "That already exists within the database!", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        dbManager.insertNewJoke(joke);
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

    public void deleteJoke(View view)
    {
        View parent = (View) view.getParent();
        TextView jokeTextView = (TextView) parent.findViewById(R.id.jokeTitle);
        String joke = String.valueOf(jokeTextView.getText());
        dbManager.deleteJoke(joke);
        loadList();
    }

    public void updateJoke(View view)
    {
        TextView jokeTextView = (TextView) findViewById(R.id.jokeTitle);
        final String oldJoke = String.valueOf(jokeTextView.getText());

        final EditText etxFactEdit = new EditText(this);
        etxFactEdit.setText(oldJoke);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Edit this fact")
                .setView(etxFactEdit)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String joke = String.valueOf(etxFactEdit.getText());
                        dbManager.editJoke(joke, oldJoke);
                        loadList();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
}
