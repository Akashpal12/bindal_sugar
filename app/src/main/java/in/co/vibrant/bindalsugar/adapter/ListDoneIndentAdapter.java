package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.IndentModel;


public class ListDoneIndentAdapter extends RecyclerView.Adapter<ListDoneIndentAdapter.MyHolder> {

    private Context context;
    List<IndentModel> arrayList;


    public ListDoneIndentAdapter(Context context, List<IndentModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_pending_indent,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        if(position==0)
        {
            holder.tv_village_code.setText("Vill Code");
            holder.tv_village_name.setText("Farmer Name");
            holder.tv_code.setText("Plot Vill Code");
            holder.tv_name.setText("Area");
            holder.tv_server_status.setText("Server Status");
            holder.rl_text_1.setBackgroundColor(Color.parseColor("#000000"));

            holder.tv_village_code.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tv_village_name.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tv_code.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tv_name.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tv_server_status.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
            final int pos=position-1;
            holder.tv_village_code.setText(arrayList.get(pos).getVillage());
            holder.tv_village_name.setText(arrayList.get(pos).getGrower());
            holder.tv_code.setText(arrayList.get(pos).getPLOTVillage());
            holder.tv_name.setText(arrayList.get(pos).getArea());
            holder.tv_server_status.setText(arrayList.get(pos).getServerStatus());
            holder.rl_text_1.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.tv_village_code.setTextColor(Color.parseColor("#000000"));
            holder.tv_village_name.setTextColor(Color.parseColor("#000000"));
            holder.tv_code.setTextColor(Color.parseColor("#000000"));
            holder.tv_name.setTextColor(Color.parseColor("#000000"));
            holder.tv_server_status.setTextColor(Color.parseColor("#000000"));
            holder.rl_text_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //AlertPopUp(arrayList.get(pos).getRemark());
                    //new ImageUploadTask().execute();
                }
            });
        }



    }


    private void AlertPopUp(String msg) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
                context);
        alertDialog.setTitle(context.getString(R.string.app_name));
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return arrayList.size()+1;
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        ///ImageView upload_image;
        RelativeLayout rl_text_1;
        TextView tv_village_code,tv_village_name,tv_code,tv_name,tv_server_status;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_village_code=itemView.findViewById(R.id.tv_village_code);
            tv_village_name=itemView.findViewById(R.id.tv_village_name);
            tv_code=itemView.findViewById(R.id.tv_code);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_server_status=itemView.findViewById(R.id.tv_server_status);
            //upload_image=itemView.findViewById(R.id.upload_image);
            //rlLayout=itemView.findViewById(R.id.rlLayout);
            rl_text_1=itemView.findViewById(R.id.rl_text_1);
        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
