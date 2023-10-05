package in.co.vibrant.bindalsugar.view.admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.LivetrackingModel;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.view.supervisor.LiveLocationMap;

public class LiveTrackingAdapter extends RecyclerView.Adapter<LiveTrackingAdapter.holder> {

    AlertDialog AlertdialogBox;
    List<LivetrackingModel> arrayList;
    BottomSheetDialog sheetDialog;
    String selectedFilter = "ATM";
    private Context context;

    public LiveTrackingAdapter(Context context, List<LivetrackingModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.livetrackdesign, null);
        holder holder = new holder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, @SuppressLint("RecyclerView") int position) {
        try {
            holder.name.setText("" + arrayList.get(position).getNAME());
            holder.date.setText("" + arrayList.get(position).getCREATEDAT());
            holder.address.setText("" + arrayList.get(position).getADDRESS());
            holder.lastAddress.setText("Current Status : " + arrayList.get(position).getLAST_ADDRESS());
            holder.speed.setText("SPEED : " + arrayList.get(position).getSPEED());
            holder.accuracy.setText("ACCURACY : " + arrayList.get(position).getACCURACY());
            holder.gpsstatus.setText("GPS STATUS : " + arrayList.get(position).getGPSSTATUS());

            if (arrayList.get(position).getGPSSTATUS().equalsIgnoreCase("")) {

                holder.charger_icons.setImageResource(R.drawable.gps);

            }

            //---------------battery logic------------------------------------------------
            if (arrayList.get(position).getBATTERY() < 25) {
                holder.buttery.setText("  " + arrayList.get(position).getBATTERY());
                holder.battery_icon.setImageResource(R.drawable.ic_baseline_battery_0_bar_24);

            } else if (arrayList.get(position).getBATTERY() < 50) {
                holder.buttery.setText("" + arrayList.get(position).getBATTERY());
                holder.battery_icon.setImageResource(R.drawable.ic_baseline_battery_4_bar_24);
            } else if (arrayList.get(position).getBATTERY() > 50) {
                holder.buttery.setText("" + arrayList.get(position).getBATTERY());
                holder.battery_icon.setImageResource(R.drawable.ic_baseline_battery_5_bar_24);
            } else if (arrayList.get(position).getBATTERY() < 75) {
                holder.buttery.setText("" + arrayList.get(position).getBATTERY());
                holder.battery_icon.setImageResource(R.drawable.ic_baseline_battery_6_bar_24);
            } else if (arrayList.get(position).getBATTERY() > 100) {
                holder.buttery.setText("" + arrayList.get(position).getBATTERY());
                holder.battery_icon.setImageResource(R.drawable.ic_baseline_battery_std_24);
            }
            //-----------------------------------------------------------------------------------
            if (arrayList.get(position).getCHARGING().equalsIgnoreCase("Charger Not Connected")) {
                // holder.buttery.setText("  " + arrayList.get(position).getBATTERY());
                holder.charger_icons.setImageResource(R.drawable.ic_baseline_battery_charging_full_24);

            } else {
                holder.charger_icons.setImageResource(R.drawable.ic_baseline_battery_std_24);
            }


            if (arrayList.get(position).getINTERNETSTATUS().equalsIgnoreCase("true")) {
                // holder.buttery.setText("  " + arrayList.get(position).getBATTERY());
                holder.internat_icon.setImageResource(R.drawable.ic_baseline_signal_cellular_alt_24);

            } else {
                holder.internat_icon.setImageResource(R.drawable.ic_baseline_signal_cellular_alt_2_bar_24);
            }

            holder.provider.setText("PROVIDER : " + arrayList.get(position).getPROVIDER());
            holder.internat.setText("INTERNETSTATUS : " + arrayList.get(position).getINTERNETSTATUS());
            holder.CREATEDAT.setText("" + arrayList.get(position).getCREATEDAT());


        } catch (Exception e) {

        }


        //--------------click listener------------------------------------------
        holder.linear_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sheetDialog = new BottomSheetDialog(context, R.style.BottomSheet);
                    View view1 = LayoutInflater.from(context).inflate(R.layout.bottonsheet, null);
                    sheetDialog.setContentView(view1);
                    ImageView live_tracking = view1.findViewById(R.id.live_tracking);
                    live_tracking.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LiveLocationMap.class);
                            intent.putExtra("Name", arrayList.get(position).getNAME());
                            intent.putExtra("Address", arrayList.get(position).getADDRESS());
                            intent.putExtra("Speed", arrayList.get(position).getSPEED());
                            intent.putExtra("Accuracy", arrayList.get(position).getACCURACY());
                            intent.putExtra("Appversion", arrayList.get(position).getAPPVERSION());
                            intent.putExtra("Buttery", arrayList.get(position).getBATTERY());
                            intent.putExtra("Bearing", arrayList.get(position).getBEARING());
                            intent.putExtra("charger", arrayList.get(position).getCHARGING());
                            intent.putExtra("Dates", arrayList.get(position).getCREATEDAT());
                            intent.putExtra("GPS", arrayList.get(position).getGPSSTATUS());
                            intent.putExtra("InternetStatus", arrayList.get(position).getINTERNETSTATUS());
                            intent.putExtra("Provider", arrayList.get(position).getPROVIDER());
                            intent.putExtra("USERCODE", arrayList.get(position).getUSERCODE());
                            intent.putExtra("Date", arrayList.get(position).getCORDINATEDATE());
                            intent.putExtra("lat", arrayList.get(position).getLAT());
                            intent.putExtra("lag", arrayList.get(position).getLNG());
                            context.startActivity(intent);
                        }
                    });


                    ImageView history_tracking = view1.findViewById(R.id.history_tracking);
                    history_tracking.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HistoryPage.class);
                            intent.putExtra("USERCODE", arrayList.get(position).getUSERCODE());
                            context.startActivity(intent);
                        }
                    });


//                    ImageView notification_tracking=view1.findViewById(R.id.notification_tracking);
//                    notification_tracking.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(context, NotificationActivity.class);
//                            context.startActivity(intent);
//                        }
//                    });


                    ImageView nearby = view1.findViewById(R.id.nearbynearby);
                    nearby.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sheetDialog.dismiss();
                          /* Intent intent = new Intent(context, NearByActivity.class);
                            context.startActivity(intent);*/

                            try {
                                AlertDialog.Builder dialogbilder = new AlertDialog.Builder(context);
                                View mView = LayoutInflater.from(context).inflate(R.layout.nearlocation_altbox, null);
                                dialogbilder.setView(mView);
                                AlertdialogBox = dialogbilder.create();

                                //------------open alt box nearby place--------------------------
                                LinearLayout hospital = mView.findViewById(R.id.hospital);
                                hospital.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertdialogBox.dismiss();
                                        String uri = "https://www.google.com/maps/search/" + "Hospital" + "/@" + arrayList.get(position).getLAT() + "," + arrayList.get(position).getLNG();
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                        context.startActivity(intent);
                                    }
                                });

                                LinearLayout hotals = mView.findViewById(R.id.hotals);
                                hotals.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertdialogBox.dismiss();
                                        String uri = "https://www.google.com/maps/search/" + "Hotels" + "/@" + arrayList.get(position).getLAT() + "," + arrayList.get(position).getLNG();
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                        context.startActivity(intent);


                                    }
                                });


                                LinearLayout petrol_pumps = mView.findViewById(R.id.petrol_pumps);
                                petrol_pumps.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertdialogBox.dismiss();
                                        String uri = "https://www.google.com/maps/search/" + "Petrol" + "/@" + arrayList.get(position).getLAT() + "," + arrayList.get(position).getLNG();
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                        context.startActivity(intent);

                                    }
                                });


                                LinearLayout restaurants = mView.findViewById(R.id.restaurants);
                                restaurants.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertdialogBox.dismiss();
                                        String uri = "https://www.google.com/maps/search/" + "Restaurants" + "/@" + arrayList.get(position).getLAT() + "," + arrayList.get(position).getLNG();
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                        context.startActivity(intent);


                                    }
                                });


                                LinearLayout atm = mView.findViewById(R.id.atm);
                                atm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertdialogBox.dismiss();
                                        String uri = "https://www.google.com/maps/search/" + "ATM" + "/@" + arrayList.get(position).getLAT() + "," + arrayList.get(position).getLNG();
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                        context.startActivity(intent);
                                    }
                                });


                                LinearLayout grocerie = mView.findViewById(R.id.groceries);
                                grocerie.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String uri = "https://www.google.com/maps/search/" + "Groceries" + "/@" + arrayList.get(position).getLAT() + "," + arrayList.get(position).getLNG();
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                        context.startActivity(intent);

                                    }
                                });


                                LinearLayout coffee = mView.findViewById(R.id.coffee);
                                coffee.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertdialogBox.dismiss();
                                        String uri = "https://www.google.com/maps/search/" + "Coffee" + "/@" + arrayList.get(position).getLAT() + "," + arrayList.get(position).getLNG();
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                        context.startActivity(intent);

                                    }
                                });


                                LinearLayout shopping = mView.findViewById(R.id.shopping);
                                shopping.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertdialogBox.dismiss();
                                        String uri = "https://www.google.com/maps/search/" + "Shopping" + "/@" + arrayList.get(position).getLAT() + "," + arrayList.get(position).getLNG();
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                        context.startActivity(intent);

                                    }
                                });


                                AlertdialogBox.show();
                                AlertdialogBox.setCancelable(false);
                                AlertdialogBox.setCanceledOnTouchOutside(true);
                            } catch (Exception e) {
                                new AlertDialogManager().RedDialog(context, "Error:" + e.toString());
                            }


                        }
                    });


                    sheetDialog.show();


                } catch (Exception e) {
                    // new AlertDialogManager().RedDialog(context,"Error:"+e.toString());
                }


            }
        });
        //--------------------------------------------------------------------


    }


    @Override
    public int getItemCount() {

        return arrayList.size();
    }


    public class holder extends RecyclerView.ViewHolder {

        RelativeLayout rl_text_1;
        LinearLayout linear_main;
        TextView name, speed, date, address, lastAddress, buttery, CHARGING, CREATEDAT, gpsstatus, accuracy;
        TextView internat, provider;
        ImageView battery_icon, charger_icons, gps_icon, internat_icon;

        public holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            speed = itemView.findViewById(R.id.speed);
            date = itemView.findViewById(R.id.date);
            address = itemView.findViewById(R.id.address);
            lastAddress = itemView.findViewById(R.id.lastAddress);
            buttery = itemView.findViewById(R.id.buttery);
            //CHARGING=itemView.findViewById(R.id.CHARGING);
            //  CREATEDAT=itemView.findViewById(R.id.CREATEDAT);
            gpsstatus = itemView.findViewById(R.id.GPS);
            linear_main = itemView.findViewById(R.id.linear_main);
            battery_icon = itemView.findViewById(R.id.battery_icon);
            accuracy = itemView.findViewById(R.id.accuracy);
            charger_icons = itemView.findViewById(R.id.charger_icons);
            internat = itemView.findViewById(R.id.internat);
            provider = itemView.findViewById(R.id.provider);
            gps_icon = itemView.findViewById(R.id.gps_icon);
            internat_icon = itemView.findViewById(R.id.internat_icon);


        }
    }
}
