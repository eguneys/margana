package oyun.net.anagram.model;

import oyun.net.anagram.R;

public enum Theme {
    anagram(R.color.anagram_primary,
            R.color.anagram_background,
            R.color.anagram_primary_dark,
            R.color.anagram_text,
            R.color.anagram_accent,
            R.style.Anagram),
    red(R.color.theme_red_primary,
        R.color.theme_red_background,
        R.color.theme_red_primary_dark,
        R.color.theme_red_text,
        R.color.theme_red_accent,
        R.style.Anagram_Red);


    private final int mColorPrimaryId;
    private final int mWindowBackgroundColorId;
    private final int mColorPrimaryDarkId;
    private final int mTextColorPrimaryId;
    private final int mAccentColorId;
    private final int mStyleId;

    Theme(final int colorPrimaryId,
          final int windowBackgroundColorId,
          final int colorPrimaryDarkId,
          final int textColorPrimaryId,
          final int accentColorId,
          final int styleId) {
        mColorPrimaryId = colorPrimaryId;
        mWindowBackgroundColorId = windowBackgroundColorId;
        mColorPrimaryDarkId= colorPrimaryDarkId;
        mTextColorPrimaryId = textColorPrimaryId;
        mAccentColorId = accentColorId;
        mStyleId = styleId;
    }

    public int getPrimaryColor() {
        return mColorPrimaryId;
    }

    public int getWindowBackgroundColor() {
        return mWindowBackgroundColorId;
    }

    public int getColorPrimaryDark() {
        return mColorPrimaryDarkId;
    }

    public int getTextPrimaryColor() {
        return mTextColorPrimaryId;
    }

    public int getAccentColor() {
        return mAccentColorId;
    }

    public int getStyleId() {
        return mStyleId;
    }

}
