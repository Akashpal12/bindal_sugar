package in.co.vibrant.bindalsugar.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.AgriInput;

public class AgreeInpuAdapter extends RecyclerView.Adapter<AgreeInpuAdapter.MyHolder> {

    private Context context;
    List<AgriInput> arrayList;
    AlertDialog AlertdialogBox;
    FusedLocationProviderClient fusedLocationClient;


    public AgreeInpuAdapter(Context context, List<AgriInput> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.agree_input_items, null);
        MyHolder MyHolder = new MyHolder(layout);
        return MyHolder;
    }


    private boolean isLastThreeItems(int position, int itemCount) {
        return (itemCount - position) <= 3;
    }


    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        try {

            holder.date.setText("" + arrayList.get(position).getDate().replace("null", ""));
            holder.vilageName.setText(arrayList.get(position).getVillCode().replace("null", "") + "  " + arrayList.get(position).getVillName().replace("null", ""));
            holder.growerName.setText(arrayList.get(position).getGroCode().replace("null", "") + "  " + arrayList.get(position).getGroName());
            holder.bleachingP.setText("" + arrayList.get(position).getBleachingPowder());
            holder.hexa.setText("" + arrayList.get(position).getHexaStop());
            holder.TrichoP.setText("" + arrayList.get(position).getTrichormePowder());
            holder.TrichoL.setText("" + arrayList.get(position).getTrichormeLiquid());
            holder.emida.setText("" + arrayList.get(position).getEmida());
            holder.corajen.setText("" + arrayList.get(position).getCorajen());
            holder.ferrous.setText("" + arrayList.get(position).getFerrosSulphate());


            if (position >= getItemCount() - 3) {
                // Get the position relative to the last 3 items
                int relativePosition = getItemCount() - position;

                // Determine the color based on the relative position
                int colorResId = getColorForRelativePosition(relativePosition);

                holder.date.setTypeface(null, Typeface.BOLD);
                holder.vilageName.setTypeface(null, Typeface.BOLD);
                holder.growerName.setTypeface(null, Typeface.BOLD);
                holder.bleachingP.setTypeface(null, Typeface.BOLD);
                holder.TrichoP.setTypeface(null, Typeface.BOLD);
                holder.TrichoL.setTypeface(null, Typeface.BOLD);
                holder.emida.setTypeface(null, Typeface.BOLD);
                holder.corajen.setTypeface(null, Typeface.BOLD);
                holder.ferrous.setTypeface(null, Typeface.BOLD);


                // Set the background color of the item view to the determined color
               // holder.itemView.setBackgroundColor(ContextCompat.getColor(context, colorResId));
                holder.date.setBackgroundColor(ContextCompat.getColor(context, colorResId));
                holder.vilageName.setBackgroundColor(ContextCompat.getColor(context, colorResId));
                holder.growerName.setBackgroundColor(ContextCompat.getColor(context, colorResId));
                holder.bleachingP.setBackgroundColor(ContextCompat.getColor(context, colorResId));
                holder.hexa.setBackgroundColor(ContextCompat.getColor(context, colorResId));
                holder.TrichoP.setBackgroundColor(ContextCompat.getColor(context, colorResId));
                holder.TrichoL.setBackgroundColor(ContextCompat.getColor(context, colorResId));
                holder.emida.setBackgroundColor(ContextCompat.getColor(context, colorResId));
                holder.corajen.setBackgroundColor(ContextCompat.getColor(context, colorResId));
                holder.ferrous.setBackgroundColor(ContextCompat.getColor(context, colorResId));




            } else {
                // Set the background color of the item view to the default color
               // holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
            }


            //StaffCommunityHistoryList

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        TextView date, vilageName, growerName, bleachingP, hexa, TrichoP, TrichoL, emida, corajen, ferrous;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            vilageName = itemView.findViewById(R.id.vilageName);
            growerName = itemView.findViewById(R.id.growerName);
            bleachingP = itemView.findViewById(R.id.bleachingP);
            hexa = itemView.findViewById(R.id.hexa);
            TrichoP = itemView.findViewById(R.id.TrichoP);
            TrichoL = itemView.findViewById(R.id.TrichoL);
            emida = itemView.findViewById(R.id.emida);
            corajen = itemView.findViewById(R.id.corajen);
            ferrous = itemView.findViewById(R.id.ferrous);

        }

    }


    private int getColorForRelativePosition(int relativePosition) {
        switch (relativePosition) {
            case 1:
                return R.color.Variance;
            case 2:
                return R.color.Traget;
            case 3:
                return R.color.Total;
            default:
                return R.color.bluee;
        }

    }
}
