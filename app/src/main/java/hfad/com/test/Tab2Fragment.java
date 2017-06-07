package hfad.com.test;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hfad.com.test.Followers.FetchFollowers;
import hfad.com.test.Followers.Follower;
import hfad.com.test.Users.User;
import io.realm.Realm;
import io.realm.RealmResults;

public class Tab2Fragment extends Fragment {
    private Realm mRealm;
    private String TAG = getClass().getSimpleName();
    private RecyclerView mRecyclerView;
    private ProgressDialog mProgressDialog;
    private Button mRequestButton;
    private List<String> mCommonNames = new ArrayList<>();
    private List<String> mFollowersNames = new ArrayList<>();
    private List<String> mUsersNames = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_fragment, container, false);
        mProgressDialog = new ProgressDialog(getContext());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.tab2_rview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        mRequestButton = (Button) view.findViewById(R.id.btn_tab2_make_request);
        mRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchAsyncFollowers().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        return view;
    }


    class CommonItemsHolder extends RecyclerView.ViewHolder {
        private TextView mCommonTV;

        CommonItemsHolder(View itemView) {
            super(itemView);
            mCommonTV = (TextView) itemView.findViewById(R.id.tab2_row);
        }
    }

    private class CommonItemsAdapter extends RecyclerView.Adapter<CommonItemsHolder> {
        private List<String> mCommonsAdapter;

        CommonItemsAdapter(List<String> commonsAdapter) {
            mCommonsAdapter = commonsAdapter;
        }

        @Override
        public CommonItemsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.row_one_tv, parent, false);
            return new CommonItemsHolder(view);
        }

        @Override
        public void onBindViewHolder(CommonItemsHolder holder, int position) {
            holder.mCommonTV.setText(String.valueOf(mCommonsAdapter.get(position)));
        }

        @Override
        public int getItemCount() {
            return mCommonsAdapter.size();
        }
    }

    private class FetchAsyncFollowers extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Fetching followers...");
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            FetchFollowers ff = new FetchFollowers();
            ff.getFollowers();
            fetchNames();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
            mRecyclerView.setAdapter(new CommonItemsAdapter(mCommonNames));
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    private void fetchNames() {

        mRealm = Realm.getDefaultInstance();

        RealmResults<User> query = mRealm.where(User.class).findAll();
        RealmResults<Follower> followerQuery = mRealm.where(Follower.class).findAll();


        for (User user : query) {
            String userName = user.getName();
            mUsersNames.add(userName);
        }
        mCommonNames = new ArrayList<>(mUsersNames);
        for (Follower follower : followerQuery) {
            String followerName = follower.getName();
            mFollowersNames.add(followerName);
        }

        mCommonNames.retainAll(mFollowersNames);
    }

}
