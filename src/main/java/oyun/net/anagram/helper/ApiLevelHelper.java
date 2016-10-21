package oyun.net.anagram.helper;

import android.os.Build;

public class ApiLevelHelper {
    private ApiLevelHelper() {}


    public static boolean isAtLeastLollipop() {
        return isAtLeast(Build.VERSION_CODES.LOLLIPOP);
    }
    public static boolean isLowerThanLollipop() {
        return isLowerThan(Build.VERSION_CODES.LOLLIPOP);
    }

    public static boolean isAtLeast(int apiLevel) {
        return Build.VERSION.SDK_INT >= apiLevel;
    }

    public static boolean isLowerThan(int apiLevel) {
        return Build.VERSION.SDK_INT < apiLevel;
    }
}
