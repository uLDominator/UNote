package cs4962.eng.utah.edu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Author: Thomas Gonsor
 * Date: 12/7/13.
 */
public class StickyAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<StickyNote> stickies;

    public StickyAdapter(Context c, ArrayList<StickyNote> collection)
    {
        mContext = c;
        stickies = collection;
    }

    @Override
    public int getCount()
    {
        return stickies.size();
    }

    @Override
    public Object getItem(int i)
    {
        return null;
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        MainStickyView stickyView;

        if(convertView == null)
        {
            stickyView = new MainStickyView(mContext, stickies.get(position));
            stickyView.setLayoutParams(new GridView.LayoutParams(250, 250));
        }
        else
            stickyView = (MainStickyView) convertView;

        return stickyView;
    }
}
