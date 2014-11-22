/**
 * 
 */
package ch.epfl.smartmap.map;

import java.util.List;

import android.content.Context;
import ch.epfl.smartmap.cache.Displayable;
import ch.epfl.smartmap.cache.User;

import com.google.android.gms.maps.model.Marker;

/**
 * A generic manager for markers, that keeps track of the displayed markers
 * 
 * @author hugo-S
 */
public interface MarkerManager<T extends Displayable> {

    /**
     * This method updates the markers on the map with the given list of items
     * 
     * @param context
     * @param mGoogleMap
     *            the map where we want to update markers
     * @param friendsToDisplay
     *            the updated friends
     */
    void updateMarkers(Context context, List<Displayable> itemsToDisplay);

    /**
     * @param item
     * @return true if the item is displayed
     */
    boolean isDisplayedItem(Displayable Item);

    /**
     * @param marker
     * @return true if the marker is displayed
     */
    boolean isDisplayedMarker(Marker marker);

    /**
     * @return the list of the markers that are displayed
     */
    List<Marker> getDisplayedMarkers();

    /**
     * @return the list of items that are displayed
     */
    List<Displayable> getDisplayedItems();

    /**
     * Add a marker to the map
     * 
     * @param event
     *            the item for which we want to add a marker
     */
    Marker addMarker(Displayable item, Context context);

    /**
     * Remove a marker from the map
     * 
     * @param event
     *            the item for which we want to remove a marker
     */
    Marker removeMarker(Displayable item);

    /**
     * @param marker
     * @return the item that the marker represents
     */
    User getItemForMarker(Marker marker);

    /**
     * @param event
     * @return the marker that represents the given item
     */
    Marker getMarkerForItem(Displayable item);

}
