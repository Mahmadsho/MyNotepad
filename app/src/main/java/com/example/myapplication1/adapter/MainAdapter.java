package com.example.myapplication1.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.EditActivity;
import com.example.myapplication1.R;
import com.example.myapplication1.db.MyConstants;
import com.example.myapplication1.db.MyDbManger;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    private Context context;
    private List<ListItem> maimArray;

    public MainAdapter(Context context) {
        this.context = context;
        maimArray = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_list_layoult, parent, false);
        return new MyViewHolder(view, context, maimArray);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    holder.setData(maimArray.get(position).getTitle());
    }

    @Override
    public int getItemCount() {

        return maimArray.size();
    }

  static   class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private  TextView tvTitle;
       private Context context;
       private List<ListItem> mainArray;

    public MyViewHolder(@NonNull View itemView, Context context, List<ListItem> mainArray) {
        super(itemView);
        this.mainArray = mainArray;
        this.context = context;
        tvTitle = itemView.findViewById(R.id.tvTitle);
        itemView.setOnClickListener(this);

    }
    public void setData(String title){
        tvTitle.setText(title);
    }

      @Override
      public void onClick(View view) {
          Intent i = new Intent(context, EditActivity.class);
          i.putExtra(MyConstants.LIST_ITEM_INTENT, mainArray.get(getAdapterPosition()));
          i.putExtra(MyConstants.EDIT_STATE, false);
          context.startActivity(i);
      }
  }
    public void updateAdapter (List<ListItem> newList){
        maimArray.clear();
        maimArray.addAll(newList);
        notifyDataSetChanged();
}
public void removeItem(int pos, MyDbManger myDbManger){
myDbManger.delete(maimArray.get(pos).getId());
maimArray.remove(pos);
notifyItemRangeChanged(0, maimArray.size());
notifyItemRemoved(pos);

}
}
