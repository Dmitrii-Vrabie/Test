package hfad.com.test;

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
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class Tab2Fragment extends Fragment {
    private RecyclerView mRecyclerView;
    private Button mButton;

    @Override
    public void onStart() {
        super.onStart();
        new FetchAsyncFollowers().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.tab2_rview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mButton = (Button) view.findViewById(R.id.btn_tab2_common);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.setAdapter(new CommonItemsAdapter(compareData()));
            }
        });
        return view;
    }

    private RealmResults<Followers> compareData() {
        Realm realm  = Realm.getDefaultInstance();


        return realm.where(Followers.class).findAll();
    }

    private class CommonItemsHolder extends RecyclerView.ViewHolder {
        private TextView mCommonTV;

        CommonItemsHolder(View itemView) {
            super(itemView);
            mCommonTV = (TextView) itemView.findViewById(R.id.tab2_row);
        }
    }

    private class CommonItemsAdapter extends RecyclerView.Adapter<CommonItemsHolder> {
        private RealmResults<Followers> mCommonsAdapter;

        CommonItemsAdapter(RealmResults<Followers> commonsAdapter) {
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

    private class FetchAsyncFollowers extends AsyncTask<Void, Void, RealmResults<Followers>> {

        @Override
        protected RealmResults<Followers> doInBackground(Void... params) {
            return new FetchFollowers().getFollowers();
        }
    }
}
