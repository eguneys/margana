package oyun.net.anagram.helper;

import android.content.Context;

public final class ResourceUtil {

    // http://stackoverflow.com/questions/3656371/dynamic-string-using-string-xml
    public static String getDynamicString(Context context, int resId, String arg1) {
        String text = String.format(context.getResources().getString(resId), arg1);
        return text;
    }

    public static String getDynamicString2(Context context, int resId, String arg1, String arg2) {
        String text = String.format(context.getResources().getString(resId), arg1, arg2);
        return text;
    }

    // http://stackoverflow.com/questions/6583843/how-to-access-resource-with-dynamic-name-in-my-case
    public static String getResourceString(String name, Context context) {

        int nameResourceId = context.getResources().getIdentifier(name,
                                                                  "string",
                                                                  context.getApplicationInfo().packageName);
        return context.getString(nameResourceId);
        
    }
   
}
