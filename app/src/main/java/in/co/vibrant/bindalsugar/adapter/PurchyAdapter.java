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
import in.co.vibrant.bindalsugar.model.PurchyModel;
import in.co.vibrant.bindalsugar.view.supervisor.PurchyListReport;


public class PurchyAdapter extends RecyclerView.Adapter<PurchyAdapter.MyHolder> {

    private Context context;
    List <PurchyModel> arrayList;


    public PurchyAdapter(Context context, List <PurchyModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(parent.getContext()).inflate(R.layout.purchydesing,null);
        MyHolder myHolder =new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        try {
            holder.sno.setText(String.valueOf(position+1));
            holder.mode.setText("Mode : " +arrayList.get(position).getMode());
            holder.Category.setText("Category : " +arrayList.get(position).getCategory());
            holder.Purchyno.setText("Purchy No. : "+arrayList.get(position).getPurchyno());
            holder.gcode.setText("Grower : " +arrayList.get(position).getGrower_code()+" / "+arrayList.get(position).getGrower_name());
            //holder.grower_name.setText("Grower Name : "+arrayList.get(position).getGrower_name());
            holder.v_code.setText("Village  : "+arrayList.get(position).getVillcode()+" / "+arrayList.get(position).getVillname());


        } catch (Exception e) {
            e.printStackTrace();
        }
        //-----------------click the list--------------------------------------------------------

        holder.show_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PurchyListReport.class);
                intent.putExtra("Purchy_no", PurchyAdapter.this.arrayList.get(position).getPurchyno());
                intent.putExtra("Mode", PurchyAdapter.this.arrayList.get(position).getMode());
                intent.putExtra("Category", PurchyAdapter.this.arrayList.get(position).getCategory());
                intent.putExtra("vill_code", PurchyAdapter.this.arrayList.get(position).getVillcode());
                intent.putExtra("vill_name", PurchyAdapter.this.arrayList.get(position).getVillname());
                intent.putExtra("grower_code", PurchyAdapter.this.arrayList.get(position).getGrower_code());
                intent.putExtra("grower_name", PurchyAdapter.this.arrayList.get(position).getGrower_name());
                intent.putExtra("grower_father", PurchyAdapter.this.arrayList.get(position).getGfather());

                context.startActivity(intent );
            }
        });



    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        CardView show_details;
        TextView mode,Category,Purchyno,sno,gcode,grower_name;
        TextView v_code,v_name;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            sno=itemView.findViewById(R.id.sno);
            mode=itemView.findViewById(R.id.mode);
            Category=itemView.findViewById(R.id.Category);
            Purchyno=itemView.findViewById(R.id.Purchyno);
            show_details=itemView.findViewById(R.id.show_details);
            grower_name=itemView.findViewById(R.id.grower_name);
            gcode=itemView.findViewById(R.id.gcode);
            v_code=itemView.findViewById(R.id.v_code);
            v_name=itemView.findViewById(R.id.v_name);

        }


    }

}
