package in.co.vibrant.bindalsugar.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.CommunityChatDataModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.view.supervisor.ImageShowActivity;


public class ListCommunityChatAdapter extends RecyclerView.Adapter<ListCommunityChatAdapter.MyHolder> {

    private Context context;
    List<CommunityChatDataModel> list;
    AlertDialog AlertdialogBox;


    public ListCommunityChatAdapter(Context context, List<CommunityChatDataModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_chat_data_box,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        //,title,class_room,posted_date,time_ago
        try{
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = sdf.format(c.getTime());
            SimpleDateFormat sdfDate = new SimpleDateFormat("hh:mm:ss a");
            SimpleDateFormat sdfDate1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
            Date dateObj2 = sdf.parse(list.get(position).getCreatedAt());
            Date dateObj1 = sdf.parse(dateString);
            long diff = dateObj2.getTime() - dateObj1.getTime();
            int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
            if(list.get(position).getChatContentType().equalsIgnoreCase("Image"))
            {
                holder.chat_text.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.VISIBLE);
                holder.videoView.setVisibility(View.GONE);
                Glide.with(this.context)
                        .load(list.get(position).getImagePath())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(holder.imageView);
            }
            else if(list.get(position).getChatContentType().equalsIgnoreCase("Video"))
            {
                //holder.chat_text.setText("Click here to open video");
                holder.chat_text.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.GONE);
                holder.videoView.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.chat_text.setText(list.get(position).getChatText());
                holder.chat_text.setVisibility(View.VISIBLE);
                holder.imageView.setVisibility(View.GONE);
                holder.videoView.setVisibility(View.GONE);
            }
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ImageShowActivity.class);
                    intent.putExtra("ImageUrl", list.get(position).getImagePath());
                    context.startActivity(intent);
                }
            });
            holder.videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Intent intent = new Intent(context, VideoShowActivity.class);
                    ShowVideo(list.get(position).getVideoPath());
                    //context.startActivity(intent);
                }
            });
            if(diffDays==0)
            {
                holder.created_at.setText("Sent at "+sdfDate.format(dateObj2));
            }
            else
            {
                holder.created_at.setText("Sent at "+sdfDate1.format(dateObj2));
            }
            if(list.get(position).getSender().equalsIgnoreCase(list.get(position).getSenderId()))
            {
                holder.chat_text.setBackgroundColor(Color.parseColor("#FFFFFF"));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.card_view_text.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                holder.card_view_text.setLayoutParams(params);
                holder.created_at.setGravity(Gravity.RIGHT);
                holder.chat_text.setGravity(Gravity.RIGHT);
            }
            else
            {
                holder.chat_text.setBackgroundColor(Color.parseColor("#F3E8B5"));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.card_view_text.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                holder.card_view_text.setLayoutParams(params);
                holder.created_at.setGravity(Gravity.LEFT);
                holder.chat_text.setGravity(Gravity.LEFT);
            }
        }
        catch (Exception e)
        {
            holder.chat_text.setText(e.toString());
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
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        CardView card_view_text;
        RelativeLayout main_layout;
        ImageView imageView,videoView;
        TextView chat_text,created_at;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            chat_text=itemView.findViewById(R.id.chat_text);
            created_at=itemView.findViewById(R.id.created_at);
            card_view_text=itemView.findViewById(R.id.card_view_text);
            main_layout=itemView.findViewById(R.id.main_layout);
            imageView=itemView.findViewById(R.id.imageView);
            videoView=itemView.findViewById(R.id.videoView);

        }

        //public ImageView getImage(){ return this.imageView;}
    }

}
