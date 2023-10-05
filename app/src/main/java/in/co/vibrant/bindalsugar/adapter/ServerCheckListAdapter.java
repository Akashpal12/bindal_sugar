package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.PrintPurcheyModel;


public class ServerCheckListAdapter extends RecyclerView.Adapter<ServerCheckListAdapter.MyHolder> {

    private Context context;
    List<PrintPurcheyModel> arrayList;


    public ServerCheckListAdapter(Context context, List<PrintPurcheyModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_print_purchey,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        try {
            //,,,,,,,
            //                ,,,,,,,;
            double shareArea=(Double.parseDouble(arrayList.get(position).getCaneArea())* Double.parseDouble(arrayList.get(position).getPlotSharePer()))/100;
            holder.tv_srno.setText(""+(position+1));
            holder.tv_survey_village.setText(arrayList.get(position).getPlotVillageCode());
            holder.tv_survey_village_name.setText(arrayList.get(position).getPlotVillageName());
            holder.tv_plot_serial_number.setText(arrayList.get(position).getPlotSrNo());
            holder.tv_grower_village.setText(arrayList.get(position).getVillageCode()+"/"+arrayList.get(position).getVillageName());
            holder.tv_unique_code.setText(arrayList.get(position).getGrowerCode());
            holder.tv_grower_name.setText(arrayList.get(position).getGrowerName());
            holder.tv_grower_father.setText(arrayList.get(position).getGrowerFather());
            holder.tv_east.setText(arrayList.get(position).getEastDistacne());
            holder.tv_west.setText(arrayList.get(position).getWestDistance());
            holder.tv_north.setText(arrayList.get(position).getNorthDistance());
            holder.tv_south.setText(arrayList.get(position).getSouthDistance());
            holder.tv_plot_share.setText(arrayList.get(position).getPlotSharePer());
            holder.tv_variety.setText(arrayList.get(position).getVarietyName());
            holder.tv_canetype.setText(arrayList.get(position).getCaneTypeName());
            holder.tv_canearea.setText(new DecimalFormat("0.000").format(Double.parseDouble(arrayList.get(position).getCaneArea())));
            holder.tv_sharearea.setText(new DecimalFormat("0.000").format(shareArea));
        }
        catch (Exception e)
        {
            //new GenerateLogFile(context).writeToFile("ServerPendingShareListAdapter.java"+e.toString());
        }

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        TextView tv_srno,tv_survey_village,tv_survey_village_name,tv_plot_serial_number,tv_unique_code,tv_grower_name,tv_grower_father,
                tv_east,tv_west,tv_north,tv_south,tv_plot_share,tv_variety,tv_canetype,tv_canearea,tv_sharearea,tv_grower_village;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_srno=itemView.findViewById(R.id.tv_srno);
            tv_survey_village=itemView.findViewById(R.id.tv_survey_village);
            tv_survey_village_name=itemView.findViewById(R.id.tv_survey_village_name);
            tv_plot_serial_number=itemView.findViewById(R.id.tv_plot_serial_number);
            tv_unique_code=itemView.findViewById(R.id.tv_unique_code);
            tv_grower_name=itemView.findViewById(R.id.tv_grower_name);
            tv_grower_father=itemView.findViewById(R.id.tv_grower_father);
            tv_east=itemView.findViewById(R.id.tv_east);
            tv_west=itemView.findViewById(R.id.tv_west);
            tv_north=itemView.findViewById(R.id.tv_north);
            tv_south=itemView.findViewById(R.id.tv_south);
            tv_plot_share=itemView.findViewById(R.id.tv_plot_share);
            tv_variety=itemView.findViewById(R.id.tv_variety);
            tv_canetype=itemView.findViewById(R.id.tv_canetype);
            tv_canearea=itemView.findViewById(R.id.tv_canearea);
            tv_sharearea=itemView.findViewById(R.id.tv_sharearea);
            tv_grower_village=itemView.findViewById(R.id.tv_grower_village);

        }

        //public ImageView getImage(){ return this.imageView;}
    }



}
