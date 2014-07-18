package cs4962.eng.utah.edu;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Thomas Gonsor on 11/28/13.
 */
public class MainStickyView extends LinearLayout
{
    private StickyNote sticky;
    private TextView titleText;
    private TextView dateDue;
    private ImageView imageView;

    public MainStickyView(Context context, StickyNote stickyNote)
    {
        super(context);
        sticky = stickyNote;
        setOrientation(VERTICAL);

        setBackgroundResource(sticky.getCurrentColor());

        titleText = new TextView(context);
        titleText.setText(sticky.getTitle());
        titleText.setGravity(Gravity.CENTER_HORIZONTAL);
        titleText.setSingleLine(true);
        addView(titleText);

        dateDue = new TextView(context);
        if(!sticky.getDateDue().equals("") && !sticky.getTimeDue().equals(""))
            dateDue.setText(sticky.getDateDue() + "\n@ " + sticky.getTimeDue());
        dateDue.setGravity(Gravity.CENTER_HORIZONTAL);
        addView(dateDue);
    }

    public StickyNote getSticky(){return sticky;}
}
