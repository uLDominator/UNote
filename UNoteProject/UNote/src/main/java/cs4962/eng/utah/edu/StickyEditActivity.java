package cs4962.eng.utah.edu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.File;

/**
 * Created by Thomas Gonsor on 11/27/13.
 */
public class StickyEditActivity extends Activity
{
    public final static int SAVE_STICKY = 11;
    public final static int DELETE_STICKY = 12;
    public final static int CANCEL_STICKY = 13;
    public final static int GALLERY_ACTION = 14;

    //Global variables
    private StickyNote givenSticky;
    private int dataPos;
    private Context myContext;

    //Layout & views
    private LinearLayout mainLayout;
    private EditText title;
    private TextView dateCreated;
    private TextView dateDueText;
    private TextView timeDueText;
    private EditText body;
    private DatePicker dateDue;
    private TimePicker timeDue;
    private ImageView imageView;
    private Button select, setDue, changeColor;
    private Dialog picker;
    private String selectedDate;
    private String selectedTime;
    private int currentBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        myContext = this;

        //Get any extras that are sent through Intent
        Intent receivedIntent = getIntent();
        givenSticky = (StickyNote)receivedIntent.getSerializableExtra("sticky");
        dataPos = receivedIntent.getIntExtra("dataPos", -1);

        //Create main layout
        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        currentBackground = givenSticky.getCurrentColor();

        if(currentBackground == -1)
            currentBackground = R.drawable.yellow_sticky;

        mainLayout.setBackgroundResource(currentBackground);

        selectedDate = givenSticky.getDateDue();
        selectedTime = givenSticky.getTimeDue();

        //Create views
        createTitleText();
        createDateCreatedText();
        createDateDueText();
        createTimeDueText();
        createSetDateTimeButton();
        createBody();
        createChangeColorView();

        setContentView(mainLayout);
    }

    private void createTitleText()
    {
        title = new EditText(this);
        title.setText(givenSticky.getTitle());
        title.setHint("Title");
        title.setId(0xDEADBEEE);
        title.setSingleLine(true);
        mainLayout.addView(title);
    }

    private void createDateCreatedText()
    {
        //Date created view
        dateCreated = new TextView(this);
        dateCreated.setText("Date Created: " + givenSticky.getDateCreated());
        dateCreated.setKeyListener(null);
        mainLayout.addView(dateCreated);
    }

    private void createDateDueText()
    {
        dateDueText = new TextView(this);
        dateDueText.setText("Date Due: " + givenSticky.getDateDue());
        dateDueText.setSingleLine(true);
        mainLayout.addView(dateDueText);
    }

    private void createTimeDueText()
    {
        timeDueText = new TextView(this);
        timeDueText.setText("Time Due: " + givenSticky.getTimeDue());
        timeDueText.setSingleLine(true);
        mainLayout.addView(timeDueText);
    }

    private void createSetDateTimeButton()
    {
        setDue = new Button(this);
        setDue.setText(givenSticky.getDateDue().equals("") ? "Select Date and Time Due" : "Edit Date and Time Due");
        setDue.setGravity(Gravity.CENTER);
        setDue.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        setDue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                picker = new Dialog(myContext);
                picker.setContentView(R.layout.picker_frag);
                picker.setTitle("Select Date and Time Due");

                dateDue = (DatePicker)picker.findViewById(R.id.datePicker);
                timeDue = (TimePicker)picker.findViewById(R.id.timePicker);
                select  = (Button)picker.findViewById(R.id.btnSet);

                select.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        String month    = dateDue.getMonth()        +"";
                        String day      = dateDue.getDayOfMonth()   +"";
                        String year     = dateDue.getYear()         +"";
                        String hour     = timeDue.getCurrentHour()  +"";
                        String minute   = timeDue.getCurrentMinute()+"";
                        String time = " AM";

                        if(timeDue.getCurrentHour() > 12)
                        {
                            hour = (timeDue.getCurrentHour() % 12) + "";
                            time = " PM";
                        }

                        if(minute.length() == 1)
                            minute = "0" + minute;

                        selectedDate = day + "/" + month + "/" + year;
                        selectedTime = hour + ":" + minute + time;

                        dateDueText.setText("Date Due: " + selectedDate);
                        timeDueText.setText("Time Due: " + selectedTime);
                        setDue.setText("Edit Date and Time Due");

                        picker.dismiss();
                    }
                });

                picker.show();
            }
        });

        mainLayout.addView(setDue);
    }

    private void createBody()
    {
        body = new EditText(this);
        body.setText(givenSticky.getBody());
        body.setHint("Body");
        body.setId(0xDEADBEEF);
        body.setMaxLines(4);
        mainLayout.addView(body);
    }

    private void createChangeColorView()
    {
        changeColor = new Button(this);
        changeColor.setText("Change Sticky Color");
        changeColor.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        changeColor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
                builder.setTitle("Pick sticky color");
                String[] colors = {"Yellow", "Orange", "Green", "Purple"};
                builder.setItems(colors, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position)
                    {
                        switch(position)
                        {
                            case 0:
                                mainLayout.setBackgroundResource(R.drawable.yellow_sticky);
                                currentBackground = R.drawable.yellow_sticky;
                                break;
                            case 1:
                                mainLayout.setBackgroundResource(R.drawable.orange_sticky);
                                currentBackground = R.drawable.orange_sticky;
                                break;
                            case 2:
                                mainLayout.setBackgroundResource(R.drawable.green_sticky);
                                currentBackground = R.drawable.green_sticky;
                                break;
                            case 3:
                                mainLayout.setBackgroundResource(R.drawable.purple_sticky);
                                currentBackground = R.drawable.purple_sticky;
                                break;
                        }
                    }
                });

                builder.create().show();
            }
        });

        mainLayout.addView(changeColor);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(requestCode)
        {
            case GALLERY_ACTION:
                Uri imageUri = data.getData();
                if(imageUri == null)
                    return;
                try
                {
                    Uri uri = Uri.parse(imageUri.toString());

                    String[] projection = { MediaStore.Images.Media.DATA };
                    Cursor cur = this.getContentResolver().query(uri, projection, null, null, null);
                    cur.moveToFirst();
                    String path = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA));

                    File mediaFile = new File(path);
                    if(mediaFile.exists())
                        imageView.setImageBitmap(BitmapFactory.decodeFile(path));
                }
                catch (Exception e) { e.printStackTrace(); }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuItem saveSticky = menu.add(Menu.NONE, SAVE_STICKY, Menu.NONE, "SAVE");
        saveSticky.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        MenuItem deleteSticky = menu.add(Menu.NONE, DELETE_STICKY, Menu.NONE, "DELETE");
        deleteSticky.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        MenuItem cancelSticky = menu.add(Menu.NONE, CANCEL_STICKY, Menu.NONE, "CANCEL");
        cancelSticky.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent returnIntent = new Intent();

        switch (item.getItemId())
        {
            case SAVE_STICKY:
                if(!title.getText().toString().matches(".*\\w.*"))
                {
                    showError();
                    return super.onOptionsItemSelected(item);
                }

                updateSticky();

                //Create new return intent with all given fields from the view
                returnIntent.putExtra("sticky", givenSticky);
                returnIntent.putExtra("dataPos", dataPos);
                setResult(RESULT_OK, returnIntent);
                finish();
                break;

            case DELETE_STICKY:
                //Create new return intent with the only result being the sticky to delete
                returnIntent.putExtra("sticky", givenSticky);
                returnIntent.putExtra("dataPos", dataPos);
                setResult(RESULT_FIRST_USER, returnIntent);
                finish();
                break;

            case CANCEL_STICKY:
                //Create new return intent with no results
                setResult(RESULT_CANCELED, returnIntent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showError()
    {
        AlertDialog.Builder dlg = new AlertDialog.Builder(myContext);
        dlg.setTitle("Missing Title");
        dlg.setMessage("Please provide at least a title.");
        dlg.setPositiveButton("OK", null);
        dlg.setCancelable(true);
        dlg.create().show();
    }

    private void updateSticky()
    {
        givenSticky.setTitle(title.getText().toString());
        givenSticky.setDateDue(selectedDate);
        givenSticky.setTimeDue(selectedTime);
        givenSticky.setBody(body.getText().toString());
        givenSticky.setCurrentColor(currentBackground);
    }
}
