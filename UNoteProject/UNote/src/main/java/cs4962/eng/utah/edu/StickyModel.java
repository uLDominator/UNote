package cs4962.eng.utah.edu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Thomas Gonsor on 11/27/13.
 *
 * This class represents the collection of all sticky notes for the application.
 * This class will respond to inquiries/requests about the data.
 */
public class StickyModel
{
    private static StickyModel instance;
    private ArrayList<StickyNote> stickyNoteCollection;

    public static synchronized StickyModel getInstance()
    {
        if(instance == null)
            instance = new StickyModel();

        return instance;
    }

    private StickyModel()
    {
        stickyNoteCollection = new ArrayList<StickyNote>();
    }

    /**
     * Getter/Setter for entire collection
     */
    public ArrayList<StickyNote> getStickyNoteCollection()                  { return stickyNoteCollection; }
    public void setStickNoteCollection(ArrayList<StickyNote> collection)    { stickyNoteCollection = collection; }

    /**
     * Add/Remove a single sticky note
     */
    public void addSticky(StickyNote sticky) { stickyNoteCollection.add(sticky); }
    public void removeSticky(int i) { stickyNoteCollection.remove(i); }

    public int getStickyPosition(StickyNote s)
    {
        return stickyNoteCollection.indexOf(s);
    }

    public void updateSticky(StickyNote s, int i)
    {
        stickyNoteCollection.set(i, s);
    }

    public void sortStickyCollection(int i)
    {
        switch(i)
        {
            //Alphabetical title
            case 0:
                Collections.sort(stickyNoteCollection, new Comparator<StickyNote>()
                {
                    @Override
                    public int compare(StickyNote stickyNote, StickyNote stickyNote2)
                    {
                        String title1 = stickyNote.getTitle();
                        String title2 = stickyNote2.getTitle();

                        return title1.compareTo(title2);
                    }
                });
                break;

            //Nearest Due date
            case 1:
                break;
        }
    }

    public boolean containsSticky(StickyNote s)
    {
        return stickyNoteCollection.contains(s);
    }

    public int size()
    {
        return stickyNoteCollection.size();
    }

}
