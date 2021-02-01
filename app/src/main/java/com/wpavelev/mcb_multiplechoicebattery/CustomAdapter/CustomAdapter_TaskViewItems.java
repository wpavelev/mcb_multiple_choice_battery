package com.wpavelev.mcb_multiplechoicebattery.CustomAdapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wpavelev.mcb_multiplechoicebattery.R;

import java.util.List;
import java.util.Set;

public class CustomAdapter_TaskViewItems extends ArrayAdapter<String> {

    private Context context;
    private int id;
    private List<String> items;
    private Set<Integer> selectedItems;
    private List<Integer> correctItems;
    private static final String TAG = "CustomAdapter_TaskViewItems";
    boolean hintAvailable = false;

    TextView text;
    CardView card;

    public CustomAdapter_TaskViewItems(Context context,
                                       int textViewResourceId,
                                       List<String> list,
                                       List<Integer> correctItems,
                                       Set<Integer> selectedItems,
                                       boolean hintAvailable) {

        super(context, textViewResourceId, list);
        this.context = context;
        id = textViewResourceId;
        items = list;
        this.correctItems = correctItems;
        this.selectedItems = selectedItems;
        this.hintAvailable = hintAvailable;


    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        View mView = v;
        if (mView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        text = mView.findViewById(R.id.sc_item_text);
        card = mView.findViewById(R.id.card_taskviewItem);

        int cWhite, cBlack, cPrimary, cPrimaryDark, cAccent, cRed, cGreen, cYellow;

        //<editor-fold desc="Colors">
        cWhite = context.getResources().getColor(R.color.white);
        cBlack = context.getResources().getColor(R.color.black);
        cPrimary = context.getResources().getColor(R.color.colorPrimary);
        cPrimaryDark = context.getResources().getColor(R.color.colorPrimaryDark);
        cAccent = context.getResources().getColor(R.color.colorPrimaryLight);
        cGreen = context.getResources().getColor(R.color.green);
        cRed = context.getResources().getColor(R.color.red);
        cYellow = context.getResources().getColor(R.color.yellow);
        //</editor-fold>


        text.setTextColor(cBlack);


        //fortfahren, wenn ein Item markiert wurde
        if (items.get(position) != null) {
            text.setText(items.get(position));
            setItemColor(cWhite);

            //Wenn der Durchlauf beim Ausgewählten Item ankommt, dann
            if (selectedItems.contains(position)) {
                setItemColor(cAccent);

                if (hintAvailable) {
                    if (correctItems.contains(position)) {//Wenn das Item Richtig ist, grün markieren
                        setItemColor(cGreen);

                    } else {//Wenn das Item sonst falsch ist, Rot markieren
                        setItemColor(cRed);
                    }
                }
            } else if (correctItems.contains(position) && !selectedItems.contains(position)) {
                if (hintAvailable) {
                    setItemColor(cYellow);
                }
            } else {
                setItemColor(cWhite);

            }


        } else {
            //Wenn nichts angewählt wurde, solle alle weiß bleben
            //text.setBackgroundColor(Color.WHITE);

        }

        return mView;
    }

    private void setItemColor(int color) {
        text.setBackgroundColor(color);
//        card.setBackgroundColor(color);
        card.setCardBackgroundColor(color);
    }

}
