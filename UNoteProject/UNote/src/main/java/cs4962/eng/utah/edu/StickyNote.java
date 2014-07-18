package cs4962.eng.utah.edu;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Thomas Gonsor on 11/27/13.
 *
 * A sticky note will contain the following:
 *  1. A current background color (required)
 *  2. A title (required)
 *  3. A date created (required)
 *  4. A date due (optional)
 *  5. A time due (optional, requires 4?)
 *  6. A body (required)
 *  7. An image (optional)
 */
public class StickyNote implements Serializable
{
    private int currentColor;
    private String title;
    private String dateCreated;
    private String dateDue;
    private String timeDue;
    private String body;

    public StickyNote()
    {
        this(-1, "", "", "", "");

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        dateCreated = sdf.format(c.getTime());
    }

    public StickyNote(int currentColor, String title, String dateDue, String timeDue, String body)
    {
        this.currentColor       = currentColor;
        this.title              = title;
        this.dateDue            = dateDue;
        this.timeDue            = timeDue;
        this.body               = body;
    }

    /**
     * Getters
     */
    int getCurrentColor()       { return currentColor; }
    String getTitle()           { return title; }
    String getDateCreated()     { return dateCreated; }
    String getDateDue()         { return dateDue; }
    String getTimeDue()         { return timeDue; }
    String getBody()            { return body; }

    /**
     * Individual Setters
     */
    void setCurrentColor(int currentColor)          { this.currentColor = currentColor; }
    void setTitle(String title)                     { this.title = title; }
    void setDateDue(String dateDue)                 { this.dateDue = dateDue; }
    void setTimeDue(String timeDue)                 { this.timeDue = timeDue; }
    void setBody(String body)                       { this.body = body; }
}
