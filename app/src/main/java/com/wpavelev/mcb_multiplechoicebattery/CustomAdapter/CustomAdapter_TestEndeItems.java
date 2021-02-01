package com.wpavelev.mcb_multiplechoicebattery.CustomAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wpavelev.mcb_multiplechoicebattery.R;

import java.util.List;

public class CustomAdapter_TestEndeItems extends ArrayAdapter<String> {

    Context context;
    int id;
    List<String> selectedItems, correctItems, items;

    TextView tvTaskId, tvCorrect, tvSelected;


    public CustomAdapter_TestEndeItems(Context context, int resource, List<String> items, List<String> correctItems, List<String> selectedItems) {
        super(context, resource, items);
        this.context = context;
        id = resource;
        this.items = items;
        this.correctItems = correctItems;
        this.selectedItems = selectedItems;

    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(id, null);
        }

        tvTaskId = view.findViewById(R.id.test_ende_list_tv0);
        tvCorrect = view.findViewById(R.id.test_ende_list_tv1);
        tvSelected = view.findViewById(R.id.test_ende_list_tv2);

        int  cRed, cGreen;


        cGreen = context.getResources().getColor(R.color.green);
        cRed = context.getResources().getColor(R.color.red);



        //selectedItems markiert?
        if (items.get(position) != null) {

            String text = "" + items.get(position);
            tvTaskId.setText(text);

            text = "" + selectedItems.get(position);
            tvSelected.setText(text);

            text = "" + correctItems.get(position);
            tvCorrect.setText(text);


            if (selectedItems.get(position).equals(correctItems.get(position))) {
                textViewSetColor(cGreen);
            } else {
                textViewSetColor(cRed);
            }



        }


        return view;


    }


    void textViewSetColor(int color) {
        tvCorrect.setBackgroundColor(color);
        tvSelected.setBackgroundColor(color);
        tvTaskId.setBackgroundColor(color);
    }


}
