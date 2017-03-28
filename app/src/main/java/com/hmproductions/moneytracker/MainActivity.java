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
import android.support.v7.preference.EditTextPreference;
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
import com.hmproductions.moneytracker.settings.SettingsActivity;

/* Making a database App :-

    1. Create Contract class containing content authorities
    2. Create it's sub class Entry class for each table containing table and column names ( implement BaseColumns class )

    3. Create DbHelper class extending SQLiteOpenHelper class containing CREATE & DELETE statements and DATABASE name

    4a. Create Provider class extending from ContentProvider
    4b. Create URI matcher and add URIs
    5. Create DbHelper object
    6. Override & define all functions
    7. Add provider in manifest

    8. In MainActivity implement LoaderManager.LoaderCallbacks<Cursor>
    9. Kick off loader in onCreate()
    10. Define all the three functions
 */

public class MainActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {

    ListView listView;
    FloatingActionButton fab;
    ExtrasCursorAdapter adapter;
    public static int balance = 2000, total = 2000;
    boolean costSortOrder;
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
        SetupPreferences();

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

    public void SetupPreferences()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        preferences.registerOnSharedPreferenceChangeListener(this);

        costSortOrder = preferences.getBoolean("cost_sort", true);
        total = Integer.parseInt(preferences.getString("balance", "2000"));
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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        String[] projection = {
                ExtrasContract.ExtrasEntry._ID,
                ExtrasContract.ExtrasEntry.COLUMN_EXTRAS_NAME,
                ExtrasContract.ExtrasEntry.COLUMN_EXTRAS_DATE,
                ExtrasContract.ExtrasEntry.COLUMN_EXTRAS_COST
        };

        String sortOrder = (costSortOrder?" ASC":" DESC");

        return new CursorLoader(this, ExtrasContract.ExtrasEntry.CONTENT_URI,projection,null,null, ExtrasContract.ExtrasEntry.COLUMN_EXTRAS_COST + sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
    {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
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
                    }
                }).show();
                return true;

            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if(key.equals("balance"))
        {
            total = Integer.parseInt(sharedPreferences.getString("balance", "2000"));
            balance = total - ExtrasProvider.getCostSum();
            balance_textView.setText("Balance Rs." + String.valueOf(balance));
            saveTotal(total);
            checkBalance();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}
