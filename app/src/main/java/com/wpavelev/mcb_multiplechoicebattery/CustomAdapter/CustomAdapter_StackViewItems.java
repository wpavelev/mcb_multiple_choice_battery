package com.wpavelev.mcb_multiplechoicebattery.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wpavelev.mcb_multiplechoicebattery.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter_StackViewItems extends ArrayAdapter<String> {

    private Context context;
    private int id;
    private List<String> files;


    public CustomAdapter_StackViewItems(Context context,int textViewResourceId,List<String> files) {

        super(context, textViewResourceId, files);

        this.context = context;
        id = textViewResourceId;

        if (files == null) {
            this.files = new ArrayList<>();
        } else {
            this.files = new ArrayList<>(files);
        }


    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        View mView = v;
        if (mView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView tv_path = mView.findViewById(R.id.path);
        TextView tv_name = mView.findViewById(R.id.name);


        String name = "";
        String path = files.get(position);
        if (path.contains("/")) {

            name = path.substring(path.lastIndexOf("/") + 1, path.length());
            //name = name.substring(0, name.lastIndexOf("."));
        }


        if (files.get(position) != null) {
            tv_path.setText(path);
            tv_name.setText(name);

        }




        return mView;
    }

}
