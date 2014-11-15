package ch.epfl.smartmap.cache;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Objects that can be displayed on the bottom menu
 * @author ritterni
 */
public interface Displayable {
    
    /**
     * @param context The application's context, needed to access the memory
     * @return The object's picture
     */
    Bitmap getPicture(Context context);
    
    /**
     * @return The object's name (e.g. username)
     */
    String getName();
    
    /**
     * @return Text containing various information (description, last seen, etc.)
     */
    String getInfo();
}
