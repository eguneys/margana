package oyun.net.anagram.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class MainFragment extends Fragment
{
    public static MainFragment newInstance(boolean thing) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // if (savedInstanceState == null) {
        //     getSupportFragmentManager().beginTransaction()
        //         .replace(R.id.main_container, MainFragment.newInstance(false))
        //         .commit();
        // }
        super.onCreate(savedInstanceState);
    }
}
