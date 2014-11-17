package ch.epfl.smartmap.cache;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ch.epfl.smartmap.R;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;

/**
 * An event that can be seen on the map
 * @author ritterni
 */
public class UserEvent implements Event, Searchable, Displayable {
    private String mEvtName;
    private final long mEvtCreator; //the user who created the event
    private final GregorianCalendar mStartDate;
    private final GregorianCalendar mEndDate;
    private long mID;
    private final Location mLocation;
    private String mPositionName;
    private String mCreatorName;
    private String mDescription;
    public final static long DEFAULT_ID = -1;
    public final static int EVENT_ICON = R.drawable.ic_launcher;
    
    /**
     * UserEvent constructor
     * @param name The name of the event
     * @param creator The id of the user who created the event
     * @param creatorName The name of the user who created the event
     * @param startDate The date at which the event starts
     * @param endDate The date at which the event ends
     * @param p The event's location on the map
     */
    public UserEvent(String name, long creator, String creatorName,
        GregorianCalendar startDate, GregorianCalendar endDate, Location p) {
        mEvtName = name;
        mEvtCreator = creator;
        mStartDate = new GregorianCalendar(startDate.get(Calendar.YEAR),
                startDate.get(Calendar.MONTH),
                startDate.get(Calendar.DATE),
                startDate.get(Calendar.HOUR),
                startDate.get(Calendar.MINUTE));

        mStartDate.set(GregorianCalendar.HOUR_OF_DAY, startDate.get(GregorianCalendar.HOUR_OF_DAY));
        mEndDate = new GregorianCalendar(endDate.get(Calendar.YEAR),
                endDate.get(Calendar.MONTH),
                endDate.get(Calendar.DATE),
                endDate.get(Calendar.HOUR),
                endDate.get(Calendar.MINUTE));

        mEndDate.set(GregorianCalendar.HOUR_OF_DAY, endDate.get(GregorianCalendar.HOUR_OF_DAY));

        mLocation = new Location(p);
        mPositionName = "";
        mCreatorName = creatorName;
        mDescription = "";
        mID = DEFAULT_ID;
    }

    @Override
    public String getName() {
        return mEvtName;
    }

    @Override
    public long getCreator() {
        return mEvtCreator;
    }

    @Override
    public GregorianCalendar getStartDate() {
        return mStartDate;
    }

    @Override
    public GregorianCalendar getEndDate() {
        return mEndDate;
    }

    @Override
    public void setName(String newName) {
        mEvtName = newName;
    }

    @Override
    public void setStartDate(GregorianCalendar newDate) {
        mStartDate.set(newDate.get(Calendar.YEAR),
                newDate.get(Calendar.MONTH),
                newDate.get(Calendar.DATE),
                newDate.get(Calendar.HOUR),
                newDate.get(Calendar.MINUTE));
    }

    @Override
    public void setEndDate(GregorianCalendar newDate) {
        mEndDate.set(newDate.get(Calendar.YEAR),
                newDate.get(Calendar.MONTH),
                newDate.get(Calendar.DATE),
                newDate.get(Calendar.HOUR),
                newDate.get(Calendar.MINUTE));
    }

    @Override
    public long getID() {
        return mID;
    }

    @Override
    public void setID(long newID) {
        mID = newID;
    }

    @Override
    public Location getLocation() {
        return mLocation;
    }

    @Override
    public void setLocation(Location p) {
    	mLocation.set(p);
    }

	@Override
	public LatLng getLatLng() {
		return new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
	}

	@Override
	public void setLongitude(double x) {
		mLocation.setLongitude(x);

	}

	@Override
	public void setLatitude(double y) {
		mLocation.setLatitude(y);
	}

    @Override
    public String getPositionName() {
        return mPositionName;
    }

    @Override
    public void setPositionName(String posName) {
        mPositionName = posName;
    }

	@Override
	public String getCreatorName() {
		return mCreatorName;
	}

	@Override
	public void setCreatorName(String name) {
		mCreatorName = name;
	}

	@Override
	public String getDescription() {
		return mDescription;
	}

	@Override
	public void setDescription(String desc) {
		mDescription = desc;
	}

    @Override
    public Bitmap getPicture(Context context) {
        //Returns a generic event picture
        return BitmapFactory.decodeResource(context.getResources(),
                EVENT_ICON);
    }

    @Override
    public String getInfo() {
        return mDescription;
    }
}
