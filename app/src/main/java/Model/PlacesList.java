package Model;

/**
 * Created by MNT on 30-Mar-15.
 */
import com.google.api.client.util.Key;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class PlacesList implements Serializable{
    @Key
    public String status;
    @Key
    public ArrayList<Place> results;

    public int resultSize()
    {
        return results.size();
    }
}
