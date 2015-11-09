package com.kiskiarea.databasessampleprogram;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBAdapter db = new DBAdapter(this);

        try{
            String destPath = "/data/data/" + getPackageName() +
                    "/databases";

            File f = new File(destPath);

            if (!f.exists())
            {
                f.mkdirs();
                f.createNewFile();

                //---copy the db from the assets folder into
                //the databases folder---
                CopyDB(getBaseContext().getAssets().open("mydb"),
                        new FileOutputStream((destPath + "/MyDB")));
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        /*
        //---Add a contact
        db.open();
        long id = db.insertContact("Desantis, Fran", "fran.desantis@kiskiarea.com");
        id = db.insertContact("Schaeffer, Melissa", "melissa.schaeffer@kiskiarea.com");
        db.close();
        */


        //---Get all contacts---
        db.open();
        Cursor c = db.getAllContacts();
        if(c.moveToFirst())
        {
            do{
                DisplayContact(c);
            }while (c.moveToNext());


        }
        db.close();


        /*
        //---Delete a contact---
        db.open();
        if(db.deleteContact(1)){
            Toast.makeText(this, "Delete successful.",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this,"Delete failed", Toast.LENGTH_LONG).show();
        }
        db.close();
        */

    }


    public void DisplayContact(Cursor c)
    {
        Toast.makeText(this,
                "id: " + c.getString(0) + "\n" +
                        "Name: " + c.getString(1) + "\n" +
                        "Email: " + c.getString(2),
                Toast.LENGTH_LONG).show();
    }


    public void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException{
        //---Copy 1K bytes at a time---
        byte[] buffer = new byte[1024];
        int length;

        while ((length = inputStream.read(buffer)) > 0)
        {
            outputStream.write(buffer, 0, length);
        }

        inputStream.close();
        outputStream.close();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
