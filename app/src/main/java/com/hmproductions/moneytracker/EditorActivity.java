package com.hmproductions.moneytracker;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.hmproductions.moneytracker.data.ExtrasContract.ExtrasEntry;

import java.text.SimpleDateFormat;
import java.util.Date;


public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText mItemName1, mItemCost;
    private Button dateButton;
    private Switch mNameSwitch;
    private Spinner mSpinner;
    private int mItemName2, no_rows_updated;
    private int mItemNameSelector = 0;

    Uri uri, mCurrentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mItemName1 = (EditText)findViewById(R.id.name_editText);
        mItemCost = (EditText)findViewById(R.id.cost_editText);
        dateButton = (Button)findViewById(R.id.date_button);

        Intent intent  = getIntent();
        mCurrentUri = intent.getData();

        if(mCurrentUri==null)
            setTitle("Add an Item");
        else
        {
            setTitle("Edit an Item");
            getLoaderManager().initLoader(0, null, this);
        }
        setupItemNameSpinner();
        setupSwitch();
    }

    public void setupSwitch()
    {
        mNameSwitch = (Switch)findViewById(R.id.name_switch);
        mItemName1.setEnabled(false);

        mNameSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemNameSelector = (mItemNameSelector==1)?0:1;

                if(mItemNameSelector==0)
                    mItemName1.setEnabled(false);
                else
                    mItemName1.setEnabled(true);
            }
        });
    }

    public void setupItemNameSpinner()
    {
        mSpinner = (Spinner)findViewById(R.id.spinner);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(EditorActivity.this,R.array.array_itemName_options,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mSpinner.setAdapter(adapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selection = (String)mSpinner.getItemAtPosition(position);
                switch (selection) {
                    case "French Fries":
                        mItemName2 = ExtrasEntry.ITEM_FRENCHFRIES;
                        break;
                    case "Gobi Manchurian":
                        mItemName2 = ExtrasEntry.ITEM_GOBIMANCHURIAN;
                        break;
                    case "Mushroom Masala":
                        mItemName2 = ExtrasEntry.ITEM_MUSHROOMMASALA;
                        break;
                    case "Paneer Butter Masala":
                        mItemName2 = ExtrasEntry.ITEM_PANEERBUTTERMASALA;
                        break;
                    case "Paneer Fried Rice":
                        mItemName2 = ExtrasEntry.ITEM_PANEERFRIEDRICE;
                        break;
                    case "Kadai Paneer":
                        mItemName2 = ExtrasEntry.ITEM_KADAIPANEER;
                        break;
                    case "Palak Paneer":
                        mItemName2 = ExtrasEntry.ITEM_PALAKPANEER;
                        break;
                    case "Gobi 65":
                        mItemName2 = ExtrasEntry.ITEM_GOBI65;
                        break;
                    case "Aloo 65":
                        mItemName2 = ExtrasEntry.ITEM_ALOO65;
                        break;
                    case "Paneer 65":
                        mItemName2 = ExtrasEntry.ITEM_PANEER65;
                        break;
                    case "Chicken Masala":
                        mItemName2 = ExtrasEntry.ITEM_CHICKENMASALA;
                        break;
                    case "Chicken Fried Rice":
                        mItemName2 = ExtrasEntry.ITEM_CHICKENFRIEDRICE;
                        break;
                    case "Fish Fry":
                        mItemName2 = ExtrasEntry.ITEM_FISHFRY;
                        break;
                    case "Egg Curry":
                        mItemName2 = ExtrasEntry.ITEM_EGGCURRY;
                        break;
                    case "Omelet":
                        mItemName2 = ExtrasEntry.ITEM_OMELET;
                        break;
                    case "Others":
                        mItemName2 = ExtrasEntry.ITEM_OTHERS;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mItemName2 = 15;
            }
        });
    }

    public boolean insertData()
    {
        ContentValues contentValues = new ContentValues();

        if(mItemNameSelector ==0)
            contentValues.put(ExtrasEntry.COLUMN_EXTRAS_NAME,String.valueOf(mItemName2));
        else
            contentValues.put(ExtrasEntry.COLUMN_EXTRAS_NAME, mItemName1.getText().toString().trim());
        contentValues.put(ExtrasEntry.COLUMN_EXTRAS_DATE,String.valueOf(System.currentTimeMillis()));
        contentValues.put(ExtrasEntry.COLUMN_EXTRAS_COST,mItemCost.getText().toString().trim());

        if(TextUtils.isEmpty(mItemName1.getText().toString()) && mItemNameSelector==1)
        {
            Toast.makeText(this,"Please enter the Item Name.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(TextUtils.isEmpty(mItemCost.getText().toString()))
        {
            Toast.makeText(this,"Please enter the cost.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(mCurrentUri == null)
            uri = getContentResolver().insert(ExtrasEntry.CONTENT_URI,contentValues);
        else
            no_rows_updated = getContentResolver().update(mCurrentUri,contentValues,null,null);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu options from the res/menu/editor_menu.xml file.
        // This adds menu items to the app bar.

        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(mCurrentUri == null)
        {
            MenuItem menuItem = menu.findItem(R.id.delete_icon);
            menuItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.add_icon :
                boolean state = insertData();
                if (state)
                    startActivity(new Intent(EditorActivity.this,MainActivity.class));
                return true;

            case R.id.delete_icon :
                int no_of_rows_deleted = getContentResolver().delete(mCurrentUri,null,null);
                Intent intent = new Intent(EditorActivity.this,MainActivity.class);
                startActivity(intent);
                break;

            case android.R.id.home:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Discard your changes or keeping editing?\nTo save changes, press the Tick icon above. ").setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    }
                }).setNegativeButton("Keep Editing", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ExtrasEntry._ID,
                ExtrasEntry.COLUMN_EXTRAS_NAME,
                ExtrasEntry.COLUMN_EXTRAS_DATE,
                ExtrasEntry.COLUMN_EXTRAS_COST
        };

        return new CursorLoader(this,mCurrentUri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(ExtrasEntry.COLUMN_EXTRAS_NAME));
            Long dateLong = cursor.getLong(cursor.getColumnIndex(ExtrasEntry.COLUMN_EXTRAS_DATE));
            int cost = cursor.getInt(cursor.getColumnIndex(ExtrasEntry.COLUMN_EXTRAS_COST));

            if(name.equalsIgnoreCase("0") || name.equalsIgnoreCase("1") || name.equalsIgnoreCase("2") || name.equalsIgnoreCase("3") || name.equalsIgnoreCase("4") || name.equalsIgnoreCase("5") || name.equalsIgnoreCase("6") || name.equalsIgnoreCase("7") || name.equalsIgnoreCase("8") || name.equalsIgnoreCase("9") || name.equalsIgnoreCase("10") || name.equalsIgnoreCase("11") || name.equalsIgnoreCase("12") || name.equalsIgnoreCase("13") || name.equalsIgnoreCase("14") || name.equalsIgnoreCase("15"))
            {
                int nameNumber = Integer.parseInt(name);
                switch (nameNumber)
                {
                    case ExtrasEntry.ITEM_FRENCHFRIES : mSpinner.setSelection(0); break;
                    case ExtrasEntry.ITEM_GOBIMANCHURIAN : mSpinner.setSelection(1); break;
                    case ExtrasEntry.ITEM_MUSHROOMMASALA : mSpinner.setSelection(2); break;
                    case ExtrasEntry.ITEM_PANEERBUTTERMASALA : mSpinner.setSelection(3); break;
                    case ExtrasEntry.ITEM_PANEERFRIEDRICE : mSpinner.setSelection(3); break;
                    case ExtrasEntry.ITEM_KADAIPANEER : mSpinner.setSelection(5); break;
                    case ExtrasEntry.ITEM_PALAKPANEER : mSpinner.setSelection(6); break;
                    case ExtrasEntry.ITEM_GOBI65 : mSpinner.setSelection(7); break;
                    case ExtrasEntry.ITEM_ALOO65 : mSpinner.setSelection(8); break;
                    case ExtrasEntry.ITEM_PANEER65 : mSpinner.setSelection(9); break;
                    case ExtrasEntry.ITEM_CHICKENFRIEDRICE : mSpinner.setSelection(10); break;
                    case ExtrasEntry.ITEM_CHICKENMASALA : mSpinner.setSelection(11); break;
                    case ExtrasEntry.ITEM_FISHFRY : mSpinner.setSelection(12); break;
                    case ExtrasEntry.ITEM_EGGCURRY : mSpinner.setSelection(13); break;
                    case ExtrasEntry.ITEM_OMELET : mSpinner.setSelection(14); break;
                    case ExtrasEntry.ITEM_OTHERS : mSpinner.setSelection(15); break;
                }
            }

            else
                mItemName1.setText(name);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            dateButton.setText(formatter.format(new Date(dateLong)));
            mItemCost.setText(String.valueOf(cost));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //YOLO
    }
}
