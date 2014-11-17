/**
 * 
 */
package ch.epfl.smartmap.gui;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import ch.epfl.smartmap.R;
import ch.epfl.smartmap.cache.Displayable;
import ch.epfl.smartmap.cache.MockDB;

/**
 * @author jfperren
 *
 */
public class InformationViewExtended extends LinearLayout {

    private static final String TAG = "INFORMATION VIEW EXTENDED";
    private InformationPanel mPanel;

    private int mWidth;
    private int mHeight;

    public InformationViewExtended(Context context, Displayable item,
        InformationPanel panel) {
        super(context);
        // Layout Settings
        this.setOrientation(HORIZONTAL);
        this.setLayoutParams(new LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        this.setBackgroundResource(R.color.light_blue);
        this.addView(new TextView(context));
        mPanel = panel;
    }

    /**
     * @param context
     */
    public InformationViewExtended(Context context, InformationPanel panel) {
        this(context, MockDB.ALAIN, panel);
    }
}
