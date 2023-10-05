package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.RemedialJsonPendingModel;
import in.co.vibrant.bindalsugar.model.RemedialJsonPendingNewModel;
import in.co.vibrant.bindalsugar.view.supervisor.AddRemadialActivity;


public class RemedialJsonPendingDataAdapter extends RecyclerView.Adapter<RemedialJsonPendingDataAdapter.MYholder> {
    private Context context;
    List<RemedialJsonPendingModel> arrayList;
    List<RemedialJsonPendingNewModel> arrayNewList;

    public RemedialJsonPendingDataAdapter(Context context, List<RemedialJsonPendingModel> arrayList,List<RemedialJsonPendingNewModel> arrayNewList) {
        this.context = context;
        this.arrayList = arrayList;
        this.arrayNewList = arrayNewList;
    }


    @NonNull
    @Override
    public MYholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_remedial_pending_json, null);
        MYholder holder = new MYholder(layout);
        return holder;


    }

    @Override
    public void onBindViewHolder(@NonNull MYholder holder, int position) {

        holder.remedial_Code.setText(arrayList.get(position).getREMICODE());
        holder.remedial_Name.setText(arrayList.get(position).getREMIDIALNAME());
        holder.days.setText(arrayList.get(position).getDAYES());
        holder.status.setText(arrayList.get(position).getSTATUS());

        holder.opendetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddRemadialActivity.class);
            intent.putExtra("V_CODE", arrayNewList.get(position).getVillageCode());
            intent.putExtra("V_NAME", arrayNewList.get(position).getVillageName());
            intent.putExtra("G_CODE", arrayNewList.get(position).getGrowerCode());
            intent.putExtra("PLOT_SR_NO", arrayNewList.get(position).getPlotNo());
            intent.putExtra("PLOT_VILL_CODE", arrayNewList.get(position).getPlotVillageCode());
            intent.putExtra("G_NAME", arrayNewList.get(position).getGrowerName());
            intent.putExtra("LAT", arrayNewList.get(position).getLAT());
            intent.putExtra("LNG", arrayNewList.get(position).getLONG());
            intent.putExtra("D_CODE", arrayNewList.get(position).getDeseaseCode());
            intent.putExtra("D_NAME", arrayNewList.get(position).getDisease());
            context.startActivity(intent);

        });

    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MYholder extends RecyclerView.ViewHolder {
        TextView remedial_Code,remedial_Name,days,status;
        CardView opendetails;


        public MYholder(@NonNull View itemView) {
            super(itemView);

            remedial_Code = itemView.findViewById(R.id.remedial_Code);
            remedial_Name = itemView.findViewById(R.id.remedial_Name);
            days = itemView.findViewById(R.id.days);
            status = itemView.findViewById(R.id.status);
            opendetails=itemView.findViewById(R.id.opendetails);


        }
    }
}
