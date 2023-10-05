package in.co.vibrant.bindalsugar.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.ExpertHistoryModal;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.view.supervisor.ImageShowActivity;
import in.co.vibrant.bindalsugar.view.supervisor.StaffCommunityHistoryFormSupervisor;


public class ExpertAnserAdapter extends RecyclerView.Adapter<ExpertAnserAdapter.MyHolder> {

    private Context context;
    List<ExpertHistoryModal> arrayList;
    AlertDialog AlertdialogBox;
    FusedLocationProviderClient fusedLocationClient;


    public ExpertAnserAdapter(Context context, List<ExpertHistoryModal> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_ask_expert_history,null);
        MyHolder MyHolder =new MyHolder(layout);
        return MyHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        try {
            holder.tvCount.setText(arrayList.get(position).getId());
            holder.tv_name.setText(arrayList.get(position).getIssueType());
            holder.created_at.setText(arrayList.get(position).getCreatedAt());
            holder.last_update.setText(arrayList.get(position).getLastUpdate());
            holder.answer.setText(arrayList.get(position).getDescription());

            if(arrayList.get(position).getVideo().length()<5)
            {
                holder.ivVideo.setVisibility(View.GONE);
            }

            if(arrayList.get(position).getImage().length()<5)
            {
                holder.ivimage.setVisibility(View.GONE);
            }

            holder.ivimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ImageShowActivity.class);
                    intent.putExtra("ImageUrl", arrayList.get(position).getImagePath());
                    context.startActivity(intent);
                }
            });

            holder.ivVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Intent intent = new Intent(context, VideoShowActivity.class);
                    ShowVideo(arrayList.get(position).getVideoPath());
                    //context.startActivity(intent);
                }
            });

            holder.main_relative_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, StaffCommunityHistoryFormSupervisor.class);
                    intent.putExtra("chatId",arrayList.get(position).getId()+"");
                    intent.putExtra("villCode",arrayList.get(position).getVillCode());
                    intent.putExtra("villName",arrayList.get(position).getVillName());
                    intent.putExtra("growCode",arrayList.get(position).getGrowerCode());
                    intent.putExtra("growerName",arrayList.get(position).getGrowerName());
                    intent.putExtra("growerFatherName",arrayList.get(position).getGrowerFather());
                    context.startActivity(intent);
                }
            });
            //StaffCommunityHistoryList

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ShowVideo(String imageUrl)
    {
        try {
            AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
            View mView = LayoutInflater.from(context).inflate(R.layout.dialogue_single_video_play_ext, null);
            dialogbilder.setView(mView);
            AlertdialogBox = dialogbilder.create();
            WebView videoView = mView.findViewById(R.id.video);
            videoView.loadUrl(imageUrl);

            Button closeMap = mView.findViewById(R.id.closeMap);
            closeMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertdialogBox.dismiss();
                }
            });
            AlertdialogBox.show();
            AlertdialogBox.setCancelable(false);
            AlertdialogBox.setCanceledOnTouchOutside(true);
        }
        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }

    }





    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        ImageView ivimage,ivVideo;
        RelativeLayout main_relative_layout;
        TextView tvCount,tv_name,created_at,last_update,answer;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvCount=itemView.findViewById(R.id.tvCount);
            tv_name=itemView.findViewById(R.id.tv_name);
            created_at=itemView.findViewById(R.id.created_at);
            last_update=itemView.findViewById(R.id.last_update);
            answer=itemView.findViewById(R.id.answer);
            ivimage=itemView.findViewById(R.id.ivimage);
            ivVideo=itemView.findViewById(R.id.ivVideo);
            main_relative_layout=itemView.findViewById(R.id.main_relative_layout);

        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
