package oyun.net.anagram.adapter;

import java.util.List;
import android.util.Log;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;

import android.graphics.drawable.Drawable;

import android.support.v4.content.ContextCompat;

import oyun.net.anagram.R;
import oyun.net.anagram.model.Anagram;
import oyun.net.anagram.helper.ResourceUtil;

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
        TextView anagramSolveTime = (TextView) convertView.findViewById(R.id.anagram_solve_time);
        ImageView anagramScore = (ImageView) convertView.findViewById(R.id.anagram_score);

        anagramTitle.setText(anagram.getQuestion());
        anagramSolveTime.setText(formatSolveTime(anagram.getTimeSpent()));

        if (anagram.isSolved()) {
            anagramScore.setImageResource(R.drawable.bg_score_check);
        } else {
            anagramScore.setImageResource(R.drawable.bg_score_close);
        }

        return convertView;
    }

    private View createView(ViewGroup parent) {
        return LayoutInflater.from(mContext)
            .inflate(R.layout.item_anagram_summary, parent, false);
    }

    private String formatSolveTime(int seconds) {
        int minutes = seconds / 60;
        seconds = seconds % 60;

        return String.format("%1$02d:%2$02d", minutes, seconds);
    }
}
