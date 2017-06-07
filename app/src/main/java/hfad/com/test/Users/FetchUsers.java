package hfad.com.test.Users;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

class FetchUsers {
    private Realm mRealm = Realm.getDefaultInstance();
    public static final String FETCH_MODE = "followers";
    private final String TAG = getClass().getSimpleName();
    private HttpURLConnection mConnection;


    private void getData(String number) {
        try {

            mConnection =
                    (HttpURLConnection) new URL(buildUrlUsers(number)).openConnection();
            try {
                InputStream inputStream = mConnection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuilder buffer = new StringBuilder();
                while ((line = bReader.readLine()) != null) {
                    buffer.append(line);
                }

                String resultJson = buffer.toString();
//                resultJson = resultJson.substring(resultJson.indexOf("{"));

                JSONObject dataJson = new JSONObject(resultJson);
                JSONArray users = dataJson.getJSONArray("response");

                mRealm.beginTransaction();

                for (int i = 0; i < users.length(); i++) {
                    User mRealmObject = mRealm.createObject(User.class);

                    JSONObject user = users.getJSONObject(i);

                    mRealmObject.setId(user.getString("id"));
                    mRealmObject.setName(user.getString("name"));
                    mRealmObject.setLikes(Integer.parseInt(user.getString("likes")));
                }
                Log.d(TAG, "added object # " + number);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mRealm.commitTransaction();
        } catch (IOException e) {
            Log.e(TAG, "Exception parsing JSON: " + e);

        } finally {
            mConnection.disconnect();
        }
    }

    private String buildUrlUsers(String number) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.nomadroot.com")
                .appendPath("appstore")
                .appendPath("test.php")
                .appendQueryParameter("mode", "user")
                .appendQueryParameter("page", number)
                .build();
        return builder.toString();
    }

    RealmResults<User> donwloadAllPages() {
        for (int i = 1; i < 20; i++) {
            getData(String.valueOf(i));
        }
        RealmQuery<User> query = mRealm.where(User.class);
        final RealmResults<User> results = query.findAll();
        Log.d(TAG, "donwloadAllPages: " + String.valueOf(results.size()));
        results.sort("likes", Sort.DESCENDING);
        return results;
    }

}