package in.co.vibrant.bindalsugar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.SuperviserAttendanceReportModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;

public class SuperVisorAttendanceAdapter extends RecyclerView.Adapter<SuperVisorAttendanceAdapter.MyHolder> {

    private Context context;
    List<SuperviserAttendanceReportModel> arrayList;




    public SuperVisorAttendanceAdapter(Context context, List<SuperviserAttendanceReportModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.superviserattendance_reportlist, null);
        MyHolder myHolder = new MyHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {

       try {
            if (position == 0) {
                holder.date.setText("Date");
                holder.checkIn.setText("Check In");
                holder.checkOut.setText("Check Out");
                holder.workingHour.setText("Working Hour");
                holder.Attendance.setText("Attendence");

                holder.date.setTypeface(Typeface.DEFAULT_BOLD);
                holder.checkIn.setTypeface(Typeface.DEFAULT_BOLD);
                holder.checkOut.setTypeface(Typeface.DEFAULT_BOLD);
                holder.workingHour.setTypeface(Typeface.DEFAULT_BOLD);
                holder.Attendance.setTypeface(Typeface.DEFAULT_BOLD);

                holder.date.setBackgroundColor(Color.parseColor("#FFF37404"));
                holder.checkIn.setBackgroundColor(Color.parseColor("#FFF37404"));
                holder.checkOut.setBackgroundColor(Color.parseColor("#FFF37404"));
                holder.workingHour.setBackgroundColor(Color.parseColor("#FFF37404"));
                holder.Attendance.setBackgroundColor(Color.parseColor("#FFF37404")); //FFF37404 // blue 07A5AC

                holder.date.setTextColor(Color.parseColor("#FFFFFF"));
                holder.checkIn.setTextColor(Color.parseColor("#FFFFFF"));
                holder.checkOut.setTextColor(Color.parseColor("#FFFFFF"));
                holder.workingHour.setTextColor(Color.parseColor("#FFFFFF"));
                holder.Attendance.setTextColor(Color.parseColor("#FFFFFF"));

            } else

            {
                final int pos = position - 1;
                holder.date.setText("" + arrayList.get(pos).getDATE());
                holder.checkIn.setText("" + arrayList.get(pos).getCHECKIN());
                holder.checkOut.setText("" + arrayList.get(pos).getCHECKOUT());
                holder.workingHour.setText("" + arrayList.get(pos).getWORKHOUR());
                holder.Attendance.setText("" + arrayList.get(pos).getATTENDANCE());



            }
        }






        catch (Exception e)
        {
            new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
        }


    }




    @Override
    public int getItemCount() {
        return arrayList.size() + 1;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        TextView date, checkIn, checkOut, workingHour, Attendance;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            checkIn = itemView.findViewById(R.id.checkIn);
            checkOut = itemView.findViewById(R.id.checkOut);
            workingHour = itemView.findViewById(R.id.workingHour);
            Attendance = itemView.findViewById(R.id.Attendance);



        }


    }


}
