package oyun.net.anagram.helper;

import android.util.Property;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.FrameLayout;

public class ViewUtils {
    
    public static final Property<FrameLayout, Integer> FOREGROUND_COLOR =
        new IntProperty<FrameLayout>("foregroundColor") {

            @Override
            public void setValue(FrameLayout layout, int value) {
                if (layout.getForeground() instanceof ColorDrawable) {
                    ((ColorDrawable)layout.getForeground().mutate()).setColor(value);
                } else {
                    layout.setForeground(new ColorDrawable(value));
                }
            }

            @Override
            public Integer get(FrameLayout layout) {
                if (layout.getForeground() instanceof ColorDrawable) {
                    return ((ColorDrawable) layout.getForeground()).getColor();
                } else {
                    return Color.TRANSPARENT;
                }
            }
        };

    public static abstract class IntProperty<T> extends Property<T, Integer> {

        public IntProperty(String name) {
            super(Integer.class, name);
        }

        public abstract void setValue(T object, int value);

        @Override
        final public void set(T object, Integer value) {
            setValue(object, value.intValue());
        }
    }
}
