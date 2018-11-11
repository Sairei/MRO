import java.io.*;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class CP
{
    String file;

    ArrayList<ArrayList<Integer>> frequenciesByRegions;

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


    public String findFile(String path)
    {
        String[] tmp = path.split("/");
        String file = tmp[tmp.length-1];

        file = file.split(".json")[0];

        return file;
    }

    public int[] getIntTab(JSONArray ar)
    {
        int[] tab = new int[ar.size()];
        int i = 0;

        for (Object e : ar)
        {
            tab[i] = (int) ((long) e);
            i++;
        }

        return tab;
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

        i = station.size();
        stations = new Station[i];

        i = region.size();
        regions = new int[i];

        i = interference.size();
        interferences = new Interference[i];

        i = liaison.size();
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

                if(key.equals("num"))
                    s.num = (int) ((long) value);
                else if(key.equals("region"))
                    s.region = (int) ((long) value);
                else if(key.equals("delta"))
                    s.delta = (int) ((long) value);
                else if(key.equals("emetteur"))
                    s.emetteur = getIntTab((JSONArray) value);
                else if(key.equals("recepteur"))
                    s.recepteur = getIntTab((JSONArray) value);
            }
            i = s.num;
            stations[i] = s;
        }
    }

    public void construct_regions(JSONArray obj)
    {
        regions = getIntTab(obj);
    }

    public void construct_interferences(JSONArray obj)
    {
        int i = 0;
        Iterator<Map.Entry> itr1;
        Iterator itr2;

        itr2 = obj.iterator();
        while (itr2.hasNext())
        {
            itr1 = ((Map) itr2.next()).entrySet().iterator();

            Interference in = new Interference();
            while (itr1.hasNext()) {
                Map.Entry pair = itr1.next();
                String key = pair.getKey().toString();
                Object value = pair.getValue();

                if(key.equals("x"))
                    in.x = (int) ((long) value);
                else if(key.equals("y"))
                    in.y = (int) ((long) value);
                else if(key.equals("Delta"))
                    in.Delta = (int) ((long) value);
            }
            interferences[i] = in;
            i++;
        }
    }

    public void construct_liaisons(JSONArray obj)
    {
        int i = 0;
        Iterator<Map.Entry> itr1;
        Iterator itr2;

        itr2 = obj.iterator();
        while (itr2.hasNext())
        {
            itr1 = ((Map) itr2.next()).entrySet().iterator();

            Liaison L = new Liaison();
            while (itr1.hasNext()) {
                Map.Entry pair = itr1.next();
                String key = pair.getKey().toString();
                Object value = pair.getValue();

                if(key.equals("x"))
                    L.x = (int) ((long) value);
                else if(key.equals("y"))
                    L.y = (int) ((long) value);
            }
            liaisons[i] = L;
            i++;
        }
    }


    public void readFile(String path) throws Exception
    {
        this.file = findFile(path);

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
        construct_regions(region);
        construct_interferences(interference);
        construct_liaisons(liaison);

        frequenciesByRegions = new ArrayList<>();
        for (int i = 0; i < regions.length; i++)
            frequenciesByRegions.add(new ArrayList<>());

        for (int i = 0; i < stations.length; i++) {
            int r = stations[i].region;
            for(int eme : stations[i].emetteur)
                if(!frequenciesByRegions.get(r).contains(eme))
                    frequenciesByRegions.get(r).add(eme);
            for(int rec : stations[i].recepteur)
                if(!frequenciesByRegions.get(r).contains(rec))
                    frequenciesByRegions.get(r).add(rec);
        }
        for (int i = 0; i < regions.length; i++)
            Collections.sort(frequenciesByRegions.get(i));
    }


    public String pb_name()
    {
        String name = "# problem name\n";

        name += file+"\n\n";

        return name;
    }

    public String pb_var()
    {
        String var = "# variables with explicit domains";

        for(int i=0; i<stations.length; i++)
        {
            var += "\neme" + i;
            for(int e : stations[i].emetteur)
                var += " " + e;

            var += "\nrec" + i;
            for(int e : stations[i].recepteur)
                var += " " + e;
        }
        var += "\n\n";

        return var;
    }

    public String pb_contraint()
    {
        int cost;
        String contraint = "# contraints";

        // dist(eme[i], rec[i]) == stations[i].delta
        contraint += "\n\n# abs(eme - rec) == delta  -> INF";
        for(int i=0; i<stations.length; i++)
        {
            contraint += "\nhard(";
            contraint += "abs(";
            contraint += "eme"+i+"-"+"rec"+i;
            contraint += ")==";
            contraint += stations[i].delta;
            contraint += ")";
        }

        // dist(eme[x], eme[y]) >= interference[i].Delta
        contraint += "\n\n# abs(eme[x] - eme[y]) == delta  -> 1000";
        cost = 1_000;
        for(Interference i : interferences)
        {
            int x = i.x;
            int y = i.y;
            int D = i.Delta;

            contraint += "\nsoft(";
            contraint += cost;
            contraint += ", abs(";
            contraint += "eme"+x+"-"+"eme"+y;
            contraint += ")==";
            contraint += D;
            contraint += ")";
        }

        // dist(eme[x], rec[y]) >= interference[i].Delta
        contraint += "\n\n# abs(eme[x] - rec[y]) == delta  -> 1000";
        cost = 1_000;
        for(Interference i : interferences)
        {
            int x = i.x;
            int y = i.y;
            int D = i.Delta;

            contraint += "\nsoft(";
            contraint += cost;
            contraint += ", abs(";
            contraint += "eme"+x+"-"+"rec"+y;
            contraint += ")==";
            contraint += D;
            contraint += ")";
        }

        // dist(rec[x], eme[y]) >= interference[i].Delta
        contraint += "\n\n# abs(rec[x] - eme[y]) == delta  -> 1000";
        cost = 1_000;
        for(Interference i : interferences)
        {
            int x = i.x;
            int y = i.y;
            int D = i.Delta;

            contraint += "\nsoft(";
            contraint += cost;
            contraint += ", abs(";
            contraint += "rec"+x+"-"+"eme"+y;
            contraint += ")==";
            contraint += D;
            contraint += ")";
        }

        // dist(rec[x], rec[y]) >= interference[i].Delta
        contraint += "\n\n# abs(rec[x] - rec[y]) == delta  -> 1000";
        cost = 1_000;
        for(Interference i : interferences)
        {
            int x = i.x;
            int y = i.y;
            int D = i.Delta;

            contraint += "\nsoft(";
            contraint += cost;
            contraint += ", abs(";
            contraint += "rec"+x+"-"+"rec"+y;
            contraint += ")==";
            contraint += 1D;
            contraint += ")";
        }

        // eme[x] == rec[y]
        cost = 1_000;
        contraint += "\n\n# eme[x] == rec[y]  -> "+cost;
        for(Liaison L : liaisons)
        {
            int x = L.x;
            int y = L.y;

            contraint += "\nsoft(";
            contraint += cost;
            contraint += ", ";
            contraint += "eme"+x;
            contraint += "==";
            contraint += "rec"+y;
            contraint += ")";
        }

        // rec[x] == eme[y]
        cost = 1_000;
        contraint += "\n\n# eme[x] == rec[y]  -> "+cost;
        for(Liaison L : liaisons)
        {
            int x = L.x;
            int y = L.y;

            contraint += "\nsoft(";
            contraint += cost;
            contraint += ", ";
            contraint += "rec"+x;
            contraint += "==";
            contraint += "eme"+y;
            contraint += ")";
        }
        return contraint;
    }

    public void makeCP() throws Exception
    {
        String str = "";

        str += pb_name();
        str += pb_var();
        str += pb_contraint();
        str += "\n\n# end " + file;

        BufferedWriter writer = new BufferedWriter(new FileWriter(this.file+".cp"));
        writer.append(str);

        writer.close();
    }
}
