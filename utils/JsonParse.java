package cc.devjo.desound.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import cc.devjo.desound.models.SuggestGetSet;

/**
 * Created by Josue on 19/12/2015.
 */
public class JsonParse {
    double current_latitude,current_longitude;
    public JsonParse(){}
    public JsonParse(double current_latitude, double current_longitude){
        this.current_latitude = current_latitude;
        this.current_longitude = current_longitude;
    }
    public List<SuggestGetSet> getParseJsonWCF(String sName){
        List<SuggestGetSet> ListData = new ArrayList<SuggestGetSet>();
        try {
            String temp=sName.replace(" ", "%20");
            URL js = new URL("http://192.168.1.53/find/filter.php?q="+temp);
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONObject jsonResponse = new JSONObject(line);
            JSONArray jsonArray = jsonResponse.getJSONArray("resultados");
            JSONArray jsonArray2 = jsonArray.getJSONArray(0);
            Log.e("Suggestion", String.valueOf(jsonArray));
            Log.e("Suggestion", String.valueOf(jsonArray2.length()));

            for (int i = 0 ; i< jsonArray2.length(); i++){
                JSONObject r = jsonArray2.getJSONObject(i);
                ListData.add(new SuggestGetSet(r.getString("id"),r.getString("name")));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ListData;
    }
}
