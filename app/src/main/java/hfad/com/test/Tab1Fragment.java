package hfad.com.test;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import hfad.com.test.Users.User;
import hfad.com.test.Users.UserAPI;
import hfad.com.test.Users.UserController;
import hfad.com.test.Users.UserGSON;
import hfad.com.test.Users.UsersListGSON;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tab1Fragment extends Fragment {
    private Realm mRealm;
    private ProgressDialog mProgressDialog;
    private RecyclerView mRecyclerView;
    private boolean isDownloaded = false;
    private UserAPI mUserAPI;
    private String TAG = getClass().getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(getContext());
        fetchUsers();

    }

    @Override
    public void onStart() {
        super.onStart();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Making Request...");
        mProgressDialog.show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.tab1_fragment_items);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Button button = (Button) view.findViewById(R.id.btn_request);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mRecyclerView.setAdapter(new ItemsAdapter(mRealm.where(User.class).findAllSorted("likes", Sort.DESCENDING)));
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void fetchUsers() {
        mUserAPI = UserController.getApi();

        mRealm = Realm.getDefaultInstance();

        for (int i = 1; i < 200; i++) {
            final int finalI = i;
            mUserAPI.getUsers("user", i).enqueue(new Callback<UsersListGSON>() {
                @Override
                public void onResponse(@NonNull Call<UsersListGSON> call, @NonNull Response<UsersListGSON> response) {

                    UsersListGSON list = response.body();
                    assert list != null;
                    List<UserGSON> listUsers = list.getResponse();
                    mRealm.beginTransaction();
                    for (UserGSON user : listUsers) {
                        User mRealmObject = mRealm.createObject(User.class);
                        mRealmObject.setId(user.getId());
                        mRealmObject.setName(user.getName());
                        mRealmObject.setLikes(user.getLikes());

                    }
                    mRealm.commitTransaction();

                    RealmResults<User> query = mRealm.where(User.class).findAll();

                    Log.e(TAG, "onResponse: " + query.size());
                    if (finalI == 199) mProgressDialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<UsersListGSON> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailue: " + t.toString());
                }
            });
        }


    }

}

