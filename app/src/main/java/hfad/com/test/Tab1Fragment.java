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

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class Tab1Fragment extends Fragment {
    private RealmResults<Users> mUsers;
    private ProgressDialog mProgressDialog;
    private RecyclerView mRecyclerView;
    private boolean isDownloaded = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(getContext());
        new FetchData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.tab1_fragment_items);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Button mRequest = (Button) view.findViewById(R.id.btn_request);
        mRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getDefaultInstance();
                RealmQuery<Users> query = realm.where(Users.class);
                mUsers = query.findAllSorted("likes", Sort.DESCENDING);
                mRecyclerView.setAdapter(new ItemsAdapter(mUsers));
            }
        });


        return view;
    }

    private class FetchData extends AsyncTask<Void, Void, RealmResults<Users>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMessage("Обработка...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected RealmResults<Users> doInBackground(Void... params) {

            return new FetchItems().donwloadAllPages();
        }

        @Override
        protected void onPostExecute(RealmResults<Users> users) {
            mProgressDialog.dismiss();
        }
    }

    private class ItemsHolder extends RecyclerView.ViewHolder {
        TextView mUser;
        TextView mLikes;

        ItemsHolder(View itemView) {
            super(itemView);
            mUser = (TextView) itemView.findViewById(R.id.label);
            mLikes = (TextView) itemView.findViewById(R.id.value);
        }

    }

    private class ItemsAdapter extends RecyclerView.Adapter<ItemsHolder> {
        private RealmResults<Users> mUsersAdapter;

        ItemsAdapter(RealmResults<Users> users) {
            mUsersAdapter = users;
        }

        @Override
        public ItemsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.row_2_tv, parent, false);
            return new ItemsHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemsHolder holder, int position) {
            holder.mUser.setText(mUsersAdapter.get(position).getName());
            holder.mLikes.setText(String.valueOf(mUsersAdapter.get(position).getLikes()));
        }

        @Override
        public int getItemCount() {
            return mUsersAdapter.size();
        }
    }
}
