package com.wpavelev.mcb_multiplechoicebattery.Helper;

import android.app.Activity;
import android.app.Dialog;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wpavelev.mcb_multiplechoicebattery.CustomAdapter.CustomAdapter_FileChooserItems;
import com.wpavelev.mcb_multiplechoicebattery.R;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileChooserHelper {
    
    
    private static final String PARENT_DIR = "..";
    
    private final Activity activity;
    private ListView list;
    private Dialog dialog;
    private File currentPath;
    
    // filter on file extension
    private String extension = "xlsx";
    
    // file selection event handling
    private FileSelectedListener fileListener;
    
   /* public void setExtension(String extension) {
        this.extension = (extension == null) ? null :
                extension.toLowerCase();
    }*/
    public interface FileSelectedListener {
        void fileSelected(File file);
    
    }
    
    public FileChooserHelper setFileListener(FileSelectedListener fileListener) {
        this.fileListener = fileListener;
        return this;
    }
    
    public FileChooserHelper(Activity activity) {
        this.activity = activity;
        dialog = new Dialog(activity);
        list = new ListView(activity);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int which, long id) {
                String fileChosen = (String) list.getItemAtPosition(which);
                File chosenFile = getChosenFile(fileChosen);
                if (chosenFile.isDirectory()) {
                    refresh(chosenFile);
                } else {
                    if (fileListener != null) {
                        fileListener.fileSelected(chosenFile);
                    }
                    dialog.dismiss();
                }
            }
        });
        dialog.setContentView(list);
        dialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        refresh(Environment.getExternalStorageDirectory());
    }
    
    public void showDialog() {
        dialog.show();
    }
    
    
    /**
     * Sort, filter and display the files for the given path.
     */
    private void refresh(File path) {
        this.currentPath = path;
        if (path.exists()) {
            File[] dirs = path.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return (file.isDirectory() && file.canRead());
                }
            });
            File[] files = path.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    
                    if (!file.isDirectory()) {
                        if (!file.canRead()) {
                            return false;
                        } else if (extension == null) {
                            return true;
                        } else {
                            return file.getName().toLowerCase().endsWith(extension);
                        }
                    } else {
                        return false;
                    }
                    
                }
                
            });
            
            // add to List(Adapterlist)
            List<String> fileList = new ArrayList<>();
            
            fileList.add(PARENT_DIR);
            
            Arrays.sort(dirs);
            Arrays.sort(files);
            for (File dir : dirs) {
                fileList.add(dir.getName());
            }
            for (File file : files) {
                fileList.add(file.getName());
            }
            
            // refreshStats the user interface
            dialog.setTitle(currentPath.getPath());
    
    
            list.setAdapter(new CustomAdapter_FileChooserItems(activity, R.layout.dialog_file_chooser, fileList, dirs.length));
        }
    }
    
    
    /**
     * Convert a relative filename into an actual File object.
     */
    private File getChosenFile(String fileChosen) {
        if (fileChosen.equals(PARENT_DIR)) {

            return currentPath.getParentFile();
        } else {
            return new File(currentPath, fileChosen);
        }
    }
    
    
}
