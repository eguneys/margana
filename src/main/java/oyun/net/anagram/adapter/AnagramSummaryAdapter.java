package oyun.net.anagram.adapter;

import java.util.List;
import android.util.Log;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.BaseAdapter;
import android.widget.TextView;

import oyun.net.anagram.R;
import oyun.net.anagram.model.Anagram;

public class AnagramSummaryAdapter extends BaseAdapter {

    private final Context mContext;
    private List<Anagram> mAnagrams;

    public AnagramSummaryAdapter(Context context, List<Anagram> anagrams) {
        mContext = context;
        mAnagrams = anagrams;
    }

    @Override
    public int getCount() {
        return mAnagrams.size();
    }

    @Override
    public Anagram getItem(int position) {
        return mAnagrams.get(position);
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
        final Anagram anagram = getItem(position);

        TextView anagramTitle = (TextView) convertView.findViewById(R.id.anagram_title);
        anagramTitle.setText(anagram.getQuestion());

        return convertView;
    }

    private View createView(ViewGroup parent) {
        return LayoutInflater.from(mContext)
            .inflate(R.layout.item_anagram_summary, parent, false);
    }
}
