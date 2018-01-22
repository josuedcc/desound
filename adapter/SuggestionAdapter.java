package cc.devjo.desound.adapter;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import cc.devjo.desound.models.SuggestGetSet;
import cc.devjo.desound.utils.JsonParse;

/**
 * Created by Josue on 20/12/2015.
 */
public class SuggestionAdapter extends ArrayAdapter<String>{
    protected static final String TAG = "SuggestionAdapter";
    private List<String> suggestions;
    public SuggestionAdapter(Activity context, String nameFilter){
        super(context, android.R.layout.simple_dropdown_item_1line);
        suggestions = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public String getItem(int position) {
        return suggestions.get(position);
    }

    @Override
    public Filter getFilter() {

        Filter myFylter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                JsonParse jp = new JsonParse();

                if (constraint != null){
                    List<SuggestGetSet> new_suggestions = jp.getParseJsonWCF(constraint.toString());
                    suggestions.clear();
                    for (int i = 0 ; i<new_suggestions.size();i++){
                        suggestions.add(new_suggestions.get(i).getName());
                    }
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count>0){
                    notifyDataSetChanged();
                }else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return myFylter;
    }
}
