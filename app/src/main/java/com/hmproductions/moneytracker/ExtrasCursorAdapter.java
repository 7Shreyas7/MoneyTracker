package com.hmproductions.moneytracker;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hmproductions.moneytracker.data.ExtrasContract.ExtrasEntry;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Harsh Mahajan on 28/1/2017.
 */

public class ExtrasCursorAdapter extends CursorAdapter {

    public ExtrasCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView name_text = (TextView)view.findViewById(R.id.itemName_textView);
        TextView date_text = (TextView)view.findViewById(R.id.date_textView);
        TextView cost_text = (TextView)view.findViewById(R.id.cost_textView);

        String name = cursor.getString(cursor.getColumnIndexOrThrow(ExtrasEntry.COLUMN_EXTRAS_NAME));

        if(name.equalsIgnoreCase("0") || name.equalsIgnoreCase("1") || name.equalsIgnoreCase("2") || name.equalsIgnoreCase("3") || name.equalsIgnoreCase("4") || name.equalsIgnoreCase("5") || name.equalsIgnoreCase("6") || name.equalsIgnoreCase("7") || name.equalsIgnoreCase("8") || name.equalsIgnoreCase("9") || name.equalsIgnoreCase("10") || name.equalsIgnoreCase("11") || name.equalsIgnoreCase("12") || name.equalsIgnoreCase("13") || name.equalsIgnoreCase("14") || name.equalsIgnoreCase("15"))
        {
            int nameNumber = Integer.parseInt(name);
            switch(nameNumber)
            {
                case 0: name_text.setText(R.string.french_fries); break;
                case 1: name_text.setText(R.string.gobi_machurian); break;
                case 2: name_text.setText(R.string.mushroom_masala); break;
                case 3: name_text.setText(R.string.paneer_butter_masala); break;
                case 4: name_text.setText(R.string.paneer_fried_rice); break;
                case 5: name_text.setText(R.string.kadai_paneer); break;
                case 6: name_text.setText(R.string.palak_paneer); break;
                case 7: name_text.setText(R.string.gobi_65); break;
                case 8: name_text.setText(R.string.aloo_65); break;
                case 9: name_text.setText(R.string.paneer_65); break;
                case 10: name_text.setText(R.string.chicken_fried_rice); break;
                case 11: name_text.setText(R.string.chicken_masala); break;
                case 12: name_text.setText(R.string.fish_fry); break;
                case 13: name_text.setText(R.string.egg_curry); break;
                case 14: name_text.setText(R.string.omelet); break;
                case 15: name_text.setText(R.string.others); break;
            }
        }

        else
            name_text.setText(name);


        Long dateLong = cursor.getLong(cursor.getColumnIndexOrThrow(ExtrasEntry.COLUMN_EXTRAS_DATE));
        String cost = cursor.getString(cursor.getColumnIndexOrThrow(ExtrasEntry.COLUMN_EXTRAS_COST));

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        date_text.setText(formatter.format(new Date(dateLong)));
        cost_text.setText(cost);

    }
}
