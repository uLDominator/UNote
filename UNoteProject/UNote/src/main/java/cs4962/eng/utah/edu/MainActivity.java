package cs4962.eng.utah.edu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Thomas Gonsor on 11/27/2013.
 */
public class MainActivity extends Activity
{
    //TODO: animate sorting
    public final static int NEW_STICKY_ACTION = 10;
    public final static int SORT_METHOD = 11;

    private StickyModel stickyModel;
    private GridView stickyGrid;
    private StickyAdapter gridAdapter;
    private static Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        myContext = this;

        //Initialize model
        stickyModel = StickyModel.getInstance();

        //Create the main layout & add view
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setBackgroundResource(R.drawable.wood_background);

        //Create grid and adaper
        stickyGrid = new GridView(this);
        stickyGrid.setId(74);
        gridAdapter = new StickyAdapter(this, stickyModel.getStickyNoteCollection());
        stickyGrid.setAdapter(gridAdapter);

        //Customize grid
        stickyGrid.setNumColumns(-1);
        stickyGrid.setColumnWidth(250);
        stickyGrid.setStretchMode(3);
        stickyGrid.setVerticalSpacing(30);

        //Item listener
        stickyGrid.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                StickyNote sticky = ((MainStickyView)v).getSticky();

                //Create new intent
                Intent startIntent = new Intent(myContext, StickyEditActivity.class);

                //Put sticky into the intent extras
                startIntent.putExtra("sticky", sticky);
                startIntent.putExtra("dataPos", stickyModel.getStickyPosition(sticky));

                startActivityForResult(startIntent, 1);
            }
        });

        mainLayout.addView(stickyGrid);

        //Set the content
        setContentView(mainLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuItem newSticky = menu.add(Menu.NONE, NEW_STICKY_ACTION, Menu.NONE, "NEW");
        newSticky.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        /*
        MenuItem sortMethod = menu.add(Menu.NONE, SORT_METHOD, Menu.NONE, "SORT");
        sortMethod.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        */

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case NEW_STICKY_ACTION:
                //Create new intent
                Intent startIntent = new Intent(this, StickyEditActivity.class);

                //Create a new sticky note
                StickyNote sticky = new StickyNote();

                //Put sticky into the intent extras
                startIntent.putExtra("sticky", sticky);
                startIntent.putExtra("dataPos", stickyModel.getStickyPosition(sticky));

                startActivityForResult(startIntent, 0);
                break;

            /*
            case SORT_METHOD:
                //TODO: sort the sticky notes
                AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
                builder.setTitle("Pick sorting method");
                String[] colors = {"Alphabetical by Title", "Nearest Due Date"};
                builder.setItems(colors, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position)
                    {
                        stickyModel.sortStickyCollection(position);
                        gridAdapter.notifyDataSetChanged();
                    }
                });

                builder.create().show();
                break;
            */
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        //Request code = 0, means we launched the intent for a NEW sticky
        //Request code = 1, means we're editing a previous sticky
        if (requestCode == 0 || requestCode == 1)
        {
            /* -1 = RESULT_OK (SAVE)
             *  0 = RESULT_CANCELLED (CANCEL)
             *  1 = RESULT_FIRST_USER (DELETE)
             */
            if (resultCode == RESULT_OK)
            {
                //Only add if it's a new sticky
                if(requestCode != 1)
                {
                    //Add to model
                    stickyModel.addSticky((StickyNote)data.getSerializableExtra("sticky"));
                }

                //Update the view
                StickyNote sticky = (StickyNote)data.getSerializableExtra("sticky");
                int dataPos = data.getIntExtra("dataPos", -1);

                if(dataPos != -1)
                    stickyModel.updateSticky(sticky, dataPos);

                gridAdapter.notifyDataSetChanged();
            }
            else if(resultCode == RESULT_FIRST_USER)
            {
                //Remove from model
                int dataPos = data.getIntExtra("dataPos", -1);

                if(dataPos != -1)
                    stickyModel.removeSticky(dataPos);

                //Update the view
                gridAdapter.notifyDataSetChanged();
            }
            else if(resultCode == RESULT_CANCELED)
                return;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        ArrayList<StickyNote> stickyCollection = stickyModel.getStickyNoteCollection();

        try
        {
            FileOutputStream outputStream = openFileOutput("UNote-Test1.dat", MODE_PRIVATE);
            ObjectOutputStream writer = new ObjectOutputStream(outputStream);

            //Record model's state
            writer.writeObject(stickyCollection);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        try
        {
            FileInputStream inputStream = openFileInput("UNote-Test1.dat");
            ObjectInputStream reader = new ObjectInputStream(inputStream);

            ArrayList<StickyNote> readNotes = (ArrayList<StickyNote>)reader.readObject();

            //Re-initialize model and refresh view
            if(readNotes.size() == (stickyModel.size()+1) || readNotes.size() == (stickyModel.size()-1))
                stickyModel.setStickNoteCollection(readNotes);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
