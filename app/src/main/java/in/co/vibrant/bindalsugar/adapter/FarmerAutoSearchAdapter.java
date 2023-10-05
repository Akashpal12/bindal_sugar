package in.co.vibrant.bindalsugar.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.model.FarmerModel;


public class FarmerAutoSearchAdapter extends ArrayAdapter<FarmerModel> implements Filterable {
    private Context context;
    private int resourceId;
    private List<FarmerModel> items, tempItems, suggestions;

    public FarmerAutoSearchAdapter(@NonNull Context context, int resourceId, List<FarmerModel> items) {
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
            FarmerModel mModel = getItem(position);
            TextView name = (TextView) view.findViewById(R.id.tv_name);
            name.setText(mModel.getFarmerCode()+" - "+mModel.getFarmerName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Nullable
    @Override
    public FarmerModel getItem(int position) {
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
            FarmerModel model = (FarmerModel) resultValue;
            return model.getVillageCode();
        }
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                String SearchData=charSequence.toString().toLowerCase();
                for (FarmerModel model: tempItems) {
                    if (model.getFarmerCode().startsWith(SearchData) || model.getFarmerName().toLowerCase().contains(SearchData)) {
                        suggestions.add(model);
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
            try {
                ArrayList<FarmerModel> tempValues = (ArrayList<FarmerModel>) filterResults.values;
                if (filterResults != null && filterResults.count > 0) {
                    clear();
                    for (FarmerModel mMode : tempValues) {
                        add(mMode);
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
