package com.hmproductions.moneytracker;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hmproductions.moneytracker.data.ExtrasContract;
import com.hmproductions.moneytracker.data.ExtrasProvider;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    ListView listView;
    FloatingActionButton fab;
    ExtrasCursorAdapter adapter;
    public static int balance = 2000, total = 2000;
    TextView balance_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        balance_textView = (TextView)findViewById(R.id.balance_textView);
        View emptyView = findViewById(R.id.emptyView);
        listView = (ListView)findViewById(R.id.list_view);
        adapter = new ExtrasCursorAdapter(this,null);

        listView.setAdapter(adapter);
        listView.setEmptyView(emptyView);

        ListItemClickListener();
        FloatingActionButtonListener();
        total = getTotal();

        balance = total - ExtrasProvider.getCostSum();

        checkBalance();

        balance_textView.setText("Balance Rs." + String.valueOf(balance));
        getLoaderManager().initLoader(0,null,this);
    }

    public void ListItemClickListener()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);

                Uri mCurrentUri = ContentUris.withAppendedId(ExtrasContract.ExtrasEntry.CONTENT_URI,id);

                intent.setData(mCurrentUri);

                startActivity(intent);
            }
        });
    }

    public void FloatingActionButtonListener()
    {
        fab = (FloatingActionButton)findViewById(R.id.add_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,EditorActivity.class));
            }
        });
    }

    public void checkBalance()
    {
        if(balance<0)
        {
            fab.setEnabled(false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Current balance is less than Zero. Add extras feature is disabled. Delete or edit extras from the list to enable add feature.")
                    .setTitle("Balance too low")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .show();

        }
        else
            fab.setEnabled(true);
    }

    public void saveTotal(int save)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("balanceInfo", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("total",save);
        editor.apply();
    }

    public int getTotal()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("balanceInfo", Context.MODE_PRIVATE);

        return sharedPreferences.getInt("total",2000);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ExtrasContract.ExtrasEntry._ID,
                ExtrasContract.ExtrasEntry.COLUMN_EXTRAS_NAME,
                ExtrasContract.ExtrasEntry.COLUMN_EXTRAS_DATE,
                ExtrasContract.ExtrasEntry.COLUMN_EXTRAS_COST
        };

        return new CursorLoader(this, ExtrasContract.ExtrasEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu options from the res/menu/editor_menu.xml file.
        // This adds menu items to the app bar.

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(total == balance)
        {
            MenuItem menuItem = menu.findItem(R.id.erase_all_data);
            menuItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.about :
                    startActivity(new Intent(MainActivity.this,InfoActivity.class));
                return true;

            case R.id.erase_all_data:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Permanent Deletion").setMessage("Do you want to erase all the data ?").setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("Erase", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int no_of_rows_deleted = getContentResolver().delete(ExtrasContract.ExtrasEntry.CONTENT_URI,null,null);
                        saveTotal(2000);
                        Toast.makeText(MainActivity.this,"Please restart the Application", Toast.LENGTH_LONG).show();
                    }
                }).show();
                return true;

            case R.id.change_balance:
                builder = new AlertDialog.Builder(this);
                final EditText input = new EditText(this);
                input.setMaxLines(1);
                input.setHint("Eg. 1000");
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setTitle("New Balance")
                        .setView(input)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int checker =  Integer.parseInt(input.getText().toString());
                        if(checker>0)
                        {
                            total = checker;
                            balance = total - ExtrasProvider.getCostSum();
                            balance_textView.setText("Balance Rs." + String.valueOf(balance));
                            saveTotal(total);
                            checkBalance();
                        }
                        else
                            Toast.makeText(MainActivity.this,"Please enter a valid amount",Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
