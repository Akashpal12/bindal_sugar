package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.co.vibrant.bindalsugar.R;


public class FactoryListAdapter extends RecyclerView.Adapter<FactoryListAdapter.MyHolder> {

    private Context context;

    int datasize = 0;
    public  FactoryListAdapter(Context context,int datasize) {
        this.context = context;
        this.datasize = datasize;

   /* private Context context;
    List<FactoryModel> arrayList;


    public FactoryListAdapter(Context context, List<FactoryModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }*/
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_factory,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        /*holder.code.setText(arrayList.get(position).getFactoryCode());
        holder.name.setText(arrayList.get(position).getFactoryName());
        holder.status.setText(arrayList.get(position).getStatus());

        holder.rl_text.setBackgroundColor(Color.parseColor(arrayList.get(position).getBackgroundColor()));
        holder.code.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.name.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.status.setTextColor(Color.parseColor(arrayList.get(position).getColor()));*/
    }


    @Override
    public int getItemCount() {
        return datasize;
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        TextView code,name,status;
        RelativeLayout rl_text;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            code=itemView.findViewById(R.id.code);
            name=itemView.findViewById(R.id.name);
            status=itemView.findViewById(R.id.status);
            rl_text=itemView.findViewById(R.id.rl_text);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
