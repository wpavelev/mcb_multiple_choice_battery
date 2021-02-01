package com.wpavelev.mcb_multiplechoicebattery.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.wpavelev.mcb_multiplechoicebattery.CustomAdapter.CustomAdapter_StackViewItems;
import com.wpavelev.mcb_multiplechoicebattery.Data.Mode;
import com.wpavelev.mcb_multiplechoicebattery.Helper.DatabaseHelper;
import com.wpavelev.mcb_multiplechoicebattery.Helper.FileChooserHelper;
import com.wpavelev.mcb_multiplechoicebattery.R;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    //KEYS
    private static final String KEY_PREF_LAST_FILE = "com.wpavelev.mcq.key.LASTFILE";
    private static final String KEY_STATE_FILES = "com.wpavelev.mcq.key.FILES";
    public static final String KEY_INTENT_STACK_ID = "com.wpavelev.mcq.key.stackID";
    public static final String KEY_INTENT_MODE = "com.wpavelev.mcq.key.mode";
    public static final String KEY_INTENT_FILES = "com.wpavelev.mcq.key.files";


    //TAG für den LOG
    public static final String TAG = "APP-LOG";

    public static Context context;

    ArrayList<String> adapterItems = new ArrayList<>();

    ListView listView;

    CustomAdapter_StackViewItems adapter;
    boolean permissionsGranted = false;
    private String fileIOname = "openedFiles.txt";


    DatabaseHelper db;
    File fileIO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack_view);
        listView =  findViewById(R.id.stackList);
        context = getApplicationContext();
        db = new DatabaseHelper(getApplicationContext());
        fileIO = new File(context.getFilesDir(), fileIOname);
        Stetho.initializeWithDefaults(this);
        checkPermission();

        updateAdapter();

        //<editor-fold desc="Longclick: ListView">
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

                Log.d(TAG, "onItemLongClick: " + String.valueOf(position));
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.stack_popup, popup.getMenu());
                popup.show();


                //Starte Longclick-Menu-Oberfläche
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        Log.d(TAG, "onMenuItemClick: selectedItems:" + String.valueOf(item));
                        switch (item.getItemId()) {
                            case R.id.menuItemDelete:

                            /*delete anweisung*/


                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                builder.setTitle(R.string.delete);
                                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String stackNameToDelete = adapterItems.get(position);
                                        int idStackToDelete = db.findStackByName(stackNameToDelete);
                                        adapterItems.remove(position);
                                        db.deleteStack(idStackToDelete);
                                        updateAdapter();
                                    }
                                });

                                builder.setNegativeButton("Abbruch", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });

                                AlertDialog dialoag = builder.create();
                                dialoag.show();

                                break;

                            case R.id.menuItemChangeName:


                                AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                                builder1.setTitle("Input new Filename");
                                final EditText input = new EditText(context);
                                builder1.setView(input);

                                builder1.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String nameOld = adapterItems.get(position);
                                        String nameNew = input.getText().toString();

                                        String newFilePath = nameOld.substring(0, nameOld.lastIndexOf("/") + 1) +
                                                input.getText().toString();


                                        adapterItems.set(position, newFilePath);

                                        db.updateStack(nameOld, newFilePath);
                                        updateAdapter();


                                    }
                                });

                                builder1.setNegativeButton("Abbruch", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });

                                AlertDialog dialog1 = builder1.create();
                                dialog1.show();


                                break;
                        }


                        return true;

                    }
                });


                return true;

            }
        });
        //</editor-fold>

        //<editor-fold desc="Click: ListView">
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "onItemClick: " + String.valueOf(position));
                int stackId = db.findStackByName(adapterItems.get(position));
                selectMode(stackId);


            }
        });


    }

    private void startActivityTaskView(int stackId, Mode mode) {
        Intent startTaskView = new Intent();

        switch (mode) {
            case learn:
                startTaskView = new Intent(MainActivity.this, TaskViewLearn.class);
                 break;

            case practise:
                startTaskView = new Intent(MainActivity.this, TaskViewPractise.class);

                break;

            case test:
                startTaskView = new Intent(MainActivity.this, TaskViewTest.class);

                break;


        }


        startTaskView.putExtra(KEY_INTENT_STACK_ID, stackId);
        startActivity(startTaskView);
    }


    private void selectMode(final int stackId) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_mode);

        ImageButton buttonLearn = dialog.findViewById(R.id.learnMode);
        ImageButton buttonPractise = dialog.findViewById(R.id.practiseMode);
        ImageButton buttonTest = dialog.findViewById(R.id.testMode);

        buttonLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityTaskView(stackId, Mode.learn);
                dialog.dismiss();
            }
        });
        buttonPractise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityTaskView(stackId, Mode.practise);
                dialog.dismiss();
            }
        });
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityTaskView(stackId, Mode.test);
                dialog.dismiss();
            }
        });


        dialog.show();



    }


    /**
     * überprüft, ob die Permission für Dateizugriff erteilt wurde
     */
    private void checkPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    permissionsGranted = true;
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    Toast.makeText(context, "Can't open File without permission!", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(KEY_STATE_FILES, adapterItems);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();

    }

    public static Context getContext() {
        return context;
    }

    private void keepAdapterItem(File file) {

        int counter = 0;

        for (String adapterItem : adapterItems) {
            if (adapterItem == file.getAbsolutePath()) {
                counter++;
            }
        }

        String name = file.getAbsolutePath();

        name = name.substring(0, name.indexOf("."));
        name += " - " + counter;

        Intent openXLSFileSplashScreen = new Intent(MainActivity.this, SplashScreen.class);
        openXLSFileSplashScreen.putExtra("file", file.getAbsolutePath());
        openXLSFileSplashScreen.putExtra("name", name);
        startActivity(openXLSFileSplashScreen);

        db.insertStack(name);
        updateAdapter();


    }

    /**
     * Wird ausgeführt, wenn Button aktiviert wird.
     * öffnet FileChooserHelper zum aussuchen der Datei
     * @param view
     */
    public void setFile(final View view) {

        if (permissionsGranted) {

            Log.d(TAG, "setFile: ");

            new FileChooserHelper(this).setFileListener(new FileChooserHelper.FileSelectedListener() {
                @Override
                public void fileSelected(final File file) {


                    if ( adapterItems.contains(file.getAbsolutePath()) ) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        String title = getString(R.string.addStack) + file.getName();
                        builder.setTitle(title);


                        builder.setPositiveButton(getString(R.string.addnewstack), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                keepAdapterItem(file);
                            }
                        });

                        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Do nothing
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    } else {

                        String name = file.getAbsolutePath();
                        name = name.substring(name.lastIndexOf("/"), name.lastIndexOf("."));

                        Intent openXLSFileSplashScreen = new Intent(MainActivity.this, SplashScreen.class);
                        openXLSFileSplashScreen.putExtra("file", file.getAbsolutePath());
                        openXLSFileSplashScreen.putExtra("name", name);
                        startActivity(openXLSFileSplashScreen);

                        db.insertStack(name);

                        updateAdapter();

                    }


                }
            }).showDialog();


        } else {

            checkPermission();

        }


    }

    public void showTests(View view) {
        Intent showTests = new Intent(this, TestView.class);
        startActivity(showTests);
    }

    /**
     * erneuert die Daten im Adapter für die Stacks-Liste
     */
    private void updateAdapter() {
              if (db.dataExist(DatabaseHelper.TABLE_STACKS)) {

            adapterItems = new ArrayList<>(db.getStacks());

        } else {
            adapterItems = new ArrayList<>();
        }

        adapter = new CustomAdapter_StackViewItems(
                this,
                R.layout.list_item_stack_view_items,
                adapterItems

        );
        listView.setAdapter(adapter);

    }


}


