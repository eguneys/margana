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
import oyun.net.anagram.model.quiz.AnagramQuiz;
import oyun.net.anagram.widget.LetterView;

public class AnagramQuizAdapter extends BaseAdapter {

    private final Context mContext;
    private final AnagramQuiz mAnagramQuiz;

    public AnagramQuizAdapter(Context context, AnagramQuiz anagramQuiz) {
        mContext = context;
        mAnagramQuiz = anagramQuiz;
    }

    public AnagramQuiz getAnagram() {
        return mAnagramQuiz;
    }

    @Override
    public int getCount() {
        return mAnagramQuiz.size();
    }

    @Override
    public String getItem(int position) {
        return mAnagramQuiz.get(position).toUpperCase();
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
