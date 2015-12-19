package aditya.cloudstuff.www.check;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Mukesh on 12/17/2015.
 */
public class FontManager {
    public static final String ROOT = "fonts/",
            FONTAWESOME = ROOT + "fontawesome-webfont.ttf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }
}
