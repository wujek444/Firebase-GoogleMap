package pl.jwojcik.gascompanion.services.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

import pl.jwojcik.gascompanion.models.GasStation;
import pl.jwojcik.gascompanion.models.Price;
import pl.jwojcik.gascompanion.services.ObjectResultListener;
import pl.jwojcik.gascompanion.services.ResultListener;

public class GasStationService extends FirebaseService{

    private static GasStationService shared = new GasStationService();
    private DatabaseReference gasStationsRef;

    private GasStationService() {
        this.gasStationsRef = getGasStationsRef();
    }

    public static GasStationService getInstance(){
        return shared;
    }

    private void createGasStation(GasStation gasStation, ObjectResultListener listener) {
        String newId = gasStation.getPlace_id();
        getGasStationsRef().child(newId).setValue(gasStation.firebaseDetails());
        listener.onResult(true, null, null);
    }

    public void createGasStationIfNotPresent(final GasStation gasStationToCheck){
        getGasStations(new ResultListener() {
            @Override
            public void onResult(boolean isSuccess, String error, List object) {
                List<GasStation> gasStations = new ArrayList<>();
                if (object != null) {
                    for (int i = 0; i < object.size(); i++) {
                        GasStation item = (GasStation) object.get(i);
                        gasStations.add(item);
                    }
                }
                boolean found = false;
                for(GasStation gS : gasStations){
                    if(gS.getPlace_id().equals(gasStationToCheck.getPlace_id())) {
                        found = true;
                        break;
                    }
                }
                if(!found){
                    createGasStation(gasStationToCheck, new ObjectResultListener() {
                        @Override
                        public void onResult(boolean isSuccess, String error, Object object) {

                        }
                    });
                }
            }
        });
    }

    public void getGasStations(final ResultListener listener) {

        gasStationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<GasStation> list = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    GasStation gasStation = snapshot.getValue(GasStation.class);
                    list.add(gasStation);
                }
                listener.onResult(true, null, list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onResult(false, databaseError.getMessage(), null);
            }
        });
    }

    public void addPrice(GasStation gasStation, Price price, ObjectResultListener listener){
        gasStationsRef.child(gasStation.getPlace_id()).child(KEY_PRICES).child(generateRandomId()).setValue(price);
        listener.onResult(true, null, null);
    }



    public String generateRandomId(){
        return RandomStringUtils.randomAlphanumeric(20).toUpperCase();
    }
}
