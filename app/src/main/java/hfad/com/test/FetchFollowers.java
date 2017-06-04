package hfad.com.test;

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


class FetchFollowers {
    private final String TAG = getClass().getSimpleName();
    private Realm mRealm = Realm.getDefaultInstance();
    private HttpURLConnection mConnection;
    private RealmResults<Followers> mResults;
    private boolean mDone = false;

    private void getData() {
        try {

            mConnection =
                    (HttpURLConnection) new URL(buildUrlUsers()).openConnection();
            try {
                InputStream inputStream = mConnection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuilder buffer = new StringBuilder();
                while ((line = bReader.readLine()) != null) {
                    buffer.append(line);
                }

                String resultJson = buffer.toString();
                resultJson = resultJson.substring(resultJson.indexOf("{"));

                JSONObject dataJson = new JSONObject(resultJson);
                JSONArray users = dataJson.getJSONArray("response");

                mRealm.beginTransaction();

                for (int i = 0; i < users.length(); i++) {
                    Followers mRealmObject = mRealm.createObject(Followers.class);
                    JSONObject user = users.getJSONObject(i);

                    mRealmObject.setId(user.getString("id"));
                    mRealmObject.setName(user.getString("name"));
                    mRealmObject.setLikes(Integer.parseInt(user.getString("likes")));

                }
                mRealm.commitTransaction();
                RealmQuery<Followers> query = mRealm.where(Followers.class);
                mResults = query.findAll();
                mDone = true;

            } catch (IOException e) {
                Log.e(TAG, "Exception parsing JSON: " + e);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            mConnection.disconnect();
        }
    }

    private String buildUrlUsers() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.nomadroot.com")
                .appendPath("appstore")
                .appendPath("test.php")
                .appendQueryParameter("mode", "followers")
                .build();
        return builder.toString();
    }

    RealmResults<Followers> getFollowers() {
        do {
            getData();
        } while (!mDone);
        RealmQuery<Followers> query = mRealm.where(Followers.class);
        final RealmResults<Followers> results = query.findAll();
        Log.d(TAG, "donwloadAllPages: " + String.valueOf(results.size()));
        mDone = false;
        return results;
    }
}

