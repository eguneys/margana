package oyun.net.anagram;

import oyun.net.anagram.helper.FontsOverride;

public final class Application extends android.app.Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        String regularFont = "Rubik-Regular.ttf";

        FontsOverride.setDefaultFont(this, "DEFAULT", regularFont);
        FontsOverride.setDefaultFont(this, "SERIF", regularFont);
        FontsOverride.setDefaultFont(this, "SANS_SERIF", regularFont);
    }
    
}
