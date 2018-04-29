package com.example.pankaj.new_additem;

/**
 * Created by pankaj on 7/4/18.
 */



import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class SearchAdaptor extends RecyclerView.Adapter<SearchAdaptor.ViewHolder> {

    private Context context;
    private ArrayList<String> names;
    private String sub;

    public SearchAdaptor(Search_ItemActivity mainActivity, ArrayList<String> names) {
        this.names = names;
        context=mainActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.option_look, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String[] arr=names.get(position).split(" ");
        sub="";
        for(int i=1;i<arr.length;i++){

            if(i<arr.length-1) {
                sub += arr[i] + " ";
            }
            else
                sub+=arr[i];
        }
        holder.textViewCategory.setText(arr[0]);
        holder.textViewSubCategory.setText(sub);
        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("checj "+sub);
                Intent intent=new Intent(context,MainActivity.class);
                intent.putExtra("From_Search",sub);
                context.startActivity(intent);

            }
        });
    }
    @Override
    public int getItemCount() {
        return names.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCategory;
        TextView textViewSubCategory;
        public View main;

        ViewHolder(View itemView) {
            super(itemView);
            main=itemView;

            textViewCategory = (TextView) itemView.findViewById(R.id.Maincategory);
            textViewSubCategory=itemView.findViewById(R.id.subCategory);
        }
    }
}
