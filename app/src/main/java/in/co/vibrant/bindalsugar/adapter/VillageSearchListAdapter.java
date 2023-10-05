package in.co.vibrant.bindalsugar.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.VillageModal;


public class VillageSearchListAdapter extends ArrayAdapter<VillageModal> {
    private Context context;
    private int resourceId;
    private List<VillageModal> items, tempItems, suggestions;

    public VillageSearchListAdapter(@NonNull Context context, int resourceId, List<VillageModal> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
        this.resourceId = resourceId;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                view = inflater.inflate(resourceId, parent, false);
            }
            VillageModal vModel = getItem(position);
            TextView name = (TextView) view.findViewById(R.id.tv_name);
            name.setText(vModel.getCode()+" - "+vModel.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Nullable
    @Override
    public VillageModal getItem(int position) {
        return items.get(position);
    }
    @Override
    public int getCount() {
        return items.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return fruitFilter;
    }

    private Filter fruitFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            VillageModal villModel = (VillageModal) resultValue;
            return villModel.getCode();
        }
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                String SearchData=charSequence.toString().toLowerCase();
                for (VillageModal villgModel: tempItems) {
                    if (villgModel.getCode().startsWith(SearchData) || villgModel.getName().toLowerCase().startsWith(SearchData)) {
                        suggestions.add(villgModel);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            try{
                ArrayList<VillageModal> tempValues = (ArrayList<VillageModal>) filterResults.values;
                if (filterResults != null && filterResults.count > 0) {
                    clear();
                    for (VillageModal vMode : tempValues) {
                        add(vMode);
                        notifyDataSetChanged();
                    }
                } else {
                    clear();
                    notifyDataSetChanged();
                }
            }
            catch (Exception e)
            {

            }
        }
    };
}

