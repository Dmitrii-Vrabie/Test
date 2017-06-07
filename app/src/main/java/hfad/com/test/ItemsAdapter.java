package hfad.com.test;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hfad.com.test.Users.User;
import io.realm.RealmResults;


class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsHolder> {
    private RealmResults<User> mUsersAdapter;

    ItemsAdapter(RealmResults<User> users) {
        mUsersAdapter = users;
    }

    @Override
    public ItemsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
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


    class ItemsHolder extends RecyclerView.ViewHolder {
        TextView mUser;
        TextView mLikes;

        ItemsHolder(View itemView) {
            super(itemView);
            mUser = (TextView) itemView.findViewById(R.id.label);
            mLikes = (TextView) itemView.findViewById(R.id.value);
        }

    }
}

