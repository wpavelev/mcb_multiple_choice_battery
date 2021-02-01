package com.wpavelev.mcb_multiplechoicebattery.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wpavelev.mcb_multiplechoicebattery.R;

import java.util.List;

public class CustomAdapter_FileChooserItems extends ArrayAdapter<String> {
    
    private Context mContext;
    private int id, lengthFileList, lengthDirList;
    private List<String> fileList;
    
    
    public CustomAdapter_FileChooserItems(Context context, int textViewResourceId, List<String> fileList, int lengthDirList) {
        super(context, textViewResourceId,fileList);
        mContext = context;
        id = textViewResourceId;
        this.fileList = fileList;
           
    
        this.lengthDirList = lengthDirList;
       
        
    }
    
    @Override
    public View getView(int position, View v, ViewGroup parent) {
        View mView = v;
        if (mView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }
        
        TextView text =  mView.findViewById(R.id.tv_FileDirText);
        ImageView image =  mView.findViewById(R.id.iv_IconFileDir);
        int cRed = mContext.getResources().getColor(R.color.red);
        int cPrimaryDark = mContext.getResources().getColor(R.color.colorPrimaryDark);

        text.setTextColor(cPrimaryDark);
    
       
    
            if (fileList.get(position) != null) {
                text.setText(fileList.get(position));
                
                if (position == 0) {
                    image.setImageResource(R.drawable.back_icon);
                } else if (position < lengthDirList) {
                    image.setImageResource(R.drawable.folder_icon);
                } else {
                    image.setImageResource(R.drawable.textfile_icon);
                }
    
    
            }
    
        
        return mView;
    }

private String getExtension(String file) {
    String extension = "";
    String fileArray[] = file.split("\\.");
    extension = fileArray[fileArray.length - 1];
    return extension;
}
    
}
