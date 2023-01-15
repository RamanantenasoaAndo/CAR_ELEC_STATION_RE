package com.example.car_elec_station_res;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class userAdapter extends RecyclerView.Adapter<userAdapter.UserViewHolder> {
    private List<user> mlistUser;
    public void  setData (List<user>list){
        this.mlistUser=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users,parent,false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        user user =mlistUser.get(position);
        if (user==null){
            return;
        }
        holder.imgAvatar.setImageResource(user.getResourceId());
        holder.tvName.setText(user.getName());
        holder.tvAdresse.setText(user.getAddress());

    }

    @Override
    public int getItemCount() {
        if (mlistUser!=null){
            return mlistUser.size();
        }
        return 0;
    }

    public  class  UserViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgAvatar;
        private TextView tvName;
        private TextView tvAdresse;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAdresse = itemView.findViewById(R.id.tv_address);
        }
    }
}
