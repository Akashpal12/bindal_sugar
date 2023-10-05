package in.co.vibrant.bindalsugar.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.RemedialJsonDataModel;


public class RemedialJsonDataAdapter extends RecyclerView.Adapter<RemedialJsonDataAdapter.MYholder> {
    private Context context;
    List<RemedialJsonDataModel> arrayList;
    AlertDialog AlertdialogBox;


    public RemedialJsonDataAdapter(Context context, List<RemedialJsonDataModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public MYholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_remedial_json, null);
        MYholder holder = new MYholder(layout);
        return holder;


    }

    @Override
    public void onBindViewHolder(@NonNull MYholder holder, int position) {

        holder.remedial_Code.setText(arrayList.get(position).getREMICODE());
        holder.remedial_Name.setText(arrayList.get(position).getREMIDIALNAME());
        holder.days.setText(arrayList.get(position).getDAYES());
        holder.status.setText(arrayList.get(position).getSTATUS());

    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MYholder extends RecyclerView.ViewHolder {
        TextView remedial_Code,remedial_Name,days,status;


        public MYholder(@NonNull View itemView) {
            super(itemView);

            remedial_Code = itemView.findViewById(R.id.remedial_Code);
            remedial_Name = itemView.findViewById(R.id.remedial_Name);
            days = itemView.findViewById(R.id.days);
            status = itemView.findViewById(R.id.status);


        }
    }
}
