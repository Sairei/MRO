import java.io.*;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class WCSP
{
    String path;

    public WCSP(String p){ path = p; }

    int[] regions;
    Station[] stations;
    Interference[] interferences;
    Liaison[] liaisons;

    class Station {
        int num;
        int region;
        int delta;
        int[] emetteur;
        int[] recepteur;
    }

    class Interference {
        int x;
        int y;
        int Delta;
    }

    class Liaison {
        int x;
        int y;
    }

    public static void write() throws Exception
    {
        String str = "Hello";
        BufferedWriter writer = new BufferedWriter(new FileWriter(".txt"));
        writer.write(str);
        writer.append("\n"+str);

        writer.close();
    }


    public void init(JSONObject obj)
    {
        Iterator itr;
        int i;

        // JSONArray
        JSONArray station = (JSONArray) obj.get("stations");
        JSONArray region = (JSONArray) obj.get("regions");
        JSONArray interference = (JSONArray) obj.get("interferences");
        JSONArray liaison = (JSONArray) obj.get("liaisons");

        i = 0;
        itr = station.iterator();
        while (itr.hasNext())
        {
            i++;
            itr.next();
        }
        stations = new Station[i];

        i = 0;
        itr = region.iterator();
        while (itr.hasNext())
        {
            i++;
            itr.next();
        }
        regions = new int[i];

        i = 0;
        itr = interference.iterator();
        while (itr.hasNext())
        {
            i++;
            itr.next();
        }
        interferences = new Interference[i];

        i = 0;
        itr = liaison.iterator();
        while (itr.hasNext())
        {
            i++;
            itr.next();
        }
        liaisons = new Liaison[i];
    }

    public void construct_stations(JSONArray obj)
    {
        int i;
        Iterator<Map.Entry> itr1;
        Iterator itr2;

        itr2 = obj.iterator();
        while (itr2.hasNext())
        {
            itr1 = ((Map) itr2.next()).entrySet().iterator();

            Station s = new Station();
            while (itr1.hasNext()) {
                Map.Entry pair = itr1.next();
                String key = pair.getKey().toString();
                Object value = pair.getValue();

                //System.out.println(key+": "+value);

                if(key.equals("num"))
                    s.num = (int) ((long) value);
                else if(key.equals("region"))
                    s.region = (int) ((long) value);
                else if(key.equals("delta"))
                    s.delta = (int) ((long) value);
                else if(key.equals("emetteur"))
                    System.out.println(value.getClass());
            }
            i = s.num;
            stations[i] = s;
        }
    }

    public void construct_regions(JSONArray obj)
    {

    }

    public void construct_interferences(JSONArray obj)
    {

    }

    public void construct_liaisons(JSONArray obj)
    {

    }


    public void readFile(String path) throws Exception
    {
        // parsing file "JSONExample.json"
        Object obj = new JSONParser().parse(new FileReader(path));

        // casting obj to JSONObject
        JSONObject json_obj = (JSONObject) obj;

        init(json_obj);

        // JSONArray
        JSONArray station = (JSONArray) json_obj.get("stations");
        JSONArray region = (JSONArray) json_obj.get("regions");
        JSONArray interference = (JSONArray) json_obj.get("interferences");
        JSONArray liaison = (JSONArray) json_obj.get("liaisons");

        construct_stations(station);
    }
}
