package in.co.vibrant.bindalsugar.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.POCPlotListDataModel;


public class PUCPlotListSaveAdapter extends RecyclerView.Adapter<PUCPlotListSaveAdapter.holder> {
    private Context context;
    List<POCPlotListDataModel> arrayList;
    AlertDialog AlertdialogBox;




    public PUCPlotListSaveAdapter(Context context, List<POCPlotListDataModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.puc_plots_save_reportlist, null);
        holder holder = new holder(layout);


        return holder;




    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        try {

            final POCPlotListDataModel item = arrayList.get(position);
            holder.tx_single_poc.setText(item.getPOCNAME());

            holder.rbNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        holder.rbYes.setChecked(false);

                    }
                }
            });
            holder.rbYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        holder.rbNo.setChecked(false);

                    }
                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class holder extends RecyclerView.ViewHolder {
        TextView tx_single_poc;
        RadioButton rbYes,rbNo;

        public holder(@NonNull View itemView) {
            super(itemView);

            tx_single_poc = itemView.findViewById(R.id.tx_single_poc);
            rbYes = itemView.findViewById(R.id.rbYes);
            rbNo = itemView.findViewById(R.id.rbNo);




        }
    }
}
