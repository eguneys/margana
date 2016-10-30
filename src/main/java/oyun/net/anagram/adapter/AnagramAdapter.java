package oyun.net.anagram.adapter;

import java.util.List;
import android.util.Log;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.AbsListView;

import oyun.net.anagram.R;
import oyun.net.anagram.model.Anagram;
import oyun.net.anagram.widget.LetterView;

public class AnagramAdapter extends BaseAdapter {

    private final Context mContext;
    private Anagram mAnagram;

    public AnagramAdapter(Context context, Anagram anagram) {
        mContext = context;
        mAnagram = anagram;
    }

    public Anagram getAnagram() {
        return mAnagram;
    }

    public void setAnagram(Anagram anagram) {
        mAnagram = anagram;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAnagram.size();
    }

    @Override
    public String getItem(int position) {
        return mAnagram.get(position).toUpperCase();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = createView(parent);
        }
        final String letter = getItem(position);
        ((LetterView)convertView).setLetter(letter);
        return convertView;
    }

    private LetterView createView(ViewGroup parent) {
        return (LetterView)LayoutInflater.from(mContext)
            .inflate(R.layout.view_letter, parent, false);
    }
}
