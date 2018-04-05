package uk.co.reallysmall.cordova.plugin.firestore;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class InitialiseHandler implements ActionHandler {

    public static final String PERSIST = "persist";
    public static final String DATE_PREFIX = "datePrefix";
    public static final String FIELDVALUE_DELETE = "fieldValueDelete";
    public static final String FIELDVALUE_SERVERTIMESTAMP = "fieldValueServerTimestamp";
    private FirestorePlugin firestorePlugin;

    public InitialiseHandler(FirestorePlugin firestorePlugin) {
        this.firestorePlugin = firestorePlugin;
    }

    @Override
    public boolean handle(final JSONArray args, CallbackContext callbackContext) {

        try {
            if (firestorePlugin.getDatabase() == null) {
                Log.d(FirestorePlugin.TAG, "Initialising Firestore...");

                final JSONObject options = args.getJSONObject(0);

                FirebaseFirestore.setLoggingEnabled(true);
                firestorePlugin.setDatabase(FirebaseFirestore.getInstance());

                boolean persist = false;

                if (options.has(PERSIST) && options.getBoolean(PERSIST)) {
                    persist = true;
                }

                if (options.has(DATE_PREFIX)) {
                    JSONDateWrapper.setDatePrefix(options.getString(DATE_PREFIX));
                }

                if (options.has(FIELDVALUE_DELETE)) {
                    FieldValueHelper.setDeletePrefix(options.getString(FIELDVALUE_DELETE));
                }

                if (options.has(FIELDVALUE_SERVERTIMESTAMP)) {
                    FieldValueHelper.setServerTimestampPrefix(options.getString(FIELDVALUE_SERVERTIMESTAMP));
                }

                Log.d(FirestorePlugin.TAG, "Setting Firestore persistance to " + persist);

                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setPersistenceEnabled(persist)
                        .build();
                firestorePlugin.getDatabase().setFirestoreSettings(settings);

                callbackContext.success();
            }
        } catch (JSONException e) {
            Log.e(FirestorePlugin.TAG, "Error initialising Forestore", e);
            callbackContext.error(e.getMessage());
        }

        return true;
    }
}
