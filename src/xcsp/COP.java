import org.xcsp.modeler.api.ProblemAPI;
import org.xcsp.common.IVar.Var;

import java.util.ArrayList;

public class COP implements ProblemAPI {
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


    @Override
    public void model() {
        int n_stations = stations.length;
        int n_interfer = interferences.length;
        int n_liaisons = liaisons.length;
        int n_regions = regions.length;

        int f_max = -1;
        for (int i = 0; i < n_stations; i++)
        {
            for (int j = 0; j < stations[i].emetteur.length; j++)
            {
                int tmp = stations[i].emetteur[j];
                f_max = Math.max(f_max, tmp);
            }

            for (int j = 0; j < stations[i].recepteur.length; j++)
            {
                int tmp = stations[i].recepteur[j];
                f_max = Math.max(f_max, tmp);
            }
        }


        // Variables obligatoire au problème
        Var[] eme = array("eme", size(n_stations), i -> dom(stations[i].emetteur),
                "eme[i]: frequence d'emission de la station i");
        Var[] rec = array("rec", size(n_stations), i -> dom(stations[i].recepteur),
                "rec[i]: frequence de reception de la station i");


        // Contraintes du problème de base
        // 1_ Ecart de frequence entre eme et rec d'une station.
        forall(range(n_stations), i -> equal(dist(eme[i], rec[i]), stations[i].delta)).note("Ecart de frequence entre eme et rec d'une station");  // | eme[i] - rec[i] |  ==  station[i].delta

        // 2_ Ecart de frequence necessaire entre stations proches
        forall(range(n_interfer), i ->
        {
            int x = interferences[i].x;
            int y = interferences[i].y;
            int D = interferences[i].Delta;

            greaterEqual(dist(eme[x], eme[y]), D);  // |eme[x] - eme[y]| <= D
            greaterEqual(dist(eme[x], rec[y]), D);  // |eme[x] - rec[y]| <= D
            greaterEqual(dist(rec[x], eme[y]), D);  // |rec[x] - eme[y]| <= D
            greaterEqual(dist(rec[x], rec[y]), D);  // |rec[x] - rec[y]| <= D
        }).note("Ecart de frequence necessaire entre stations proches");

        // 3_ Liaisons entre stations
        forall(range(n_liaisons), i ->
        {
            int x = liaisons[i].x;
            int y = liaisons[i].y;

            equal(eme[x], rec[y]);  // eme[i] == rec[j]
            equal(rec[x], eme[y]);  // rec[i] == eme[j]
        }).note("Liaisons entre stations");

        // 4_ nombre de frequences par regions
        //Initialisation du tableau pour sauvegarder les variables par région
        ArrayList<ArrayList<Var>> frequenciesByRegions = new ArrayList<ArrayList<Var>>();
        for (int i = 0; i < n_regions; i++)
            frequenciesByRegions.add(new ArrayList<Var>());

        for (int i = 0; i < n_stations; i++) {
            int r = stations[i].region;
            frequenciesByRegions.get(r).add(eme[i]);
            frequenciesByRegions.get(r).add(rec[i]);
        }
        for (int i = 0; i < n_regions; i++) {
            ArrayList<Var> arr = frequenciesByRegions.get(i);
            Var[] frequencies_region_i = arr.toArray(new Var[arr.size()]);
            nValues(frequencies_region_i, LE, regions[i]).note("Nombre de frequences differentes utilisees pour la region " + i);
        }


        //minimiser le nombre de frequences utilisees
        if (modelVariant("m1"))
        {
            Var[] allVar = vars(eme, rec);

            int sum = 0;
            for (int i = 0; i < n_regions; i++) {
                // Le nombre maximum de fréquence utilisable
                // est majoré par la somme du nombre de
                // fréquence utilisable par chaque région
                sum += regions[i];
            }

            Var mini = var("mini", dom(range(sum+1)),
                    "min est le nombre de fréquence différentes utilisées");
            nValues(allVar, EQ, mini).note("Nombre de frequences differentes utilisees");
            minimize(mini);
        }
        //utiliser les frequences les plus basses possibles,
        if (modelVariant("m2"))
        {
            minimize(SUM, vars(eme, rec));
        }
        //minimiser la largeur de la bande de frequences utilisées
        if (modelVariant("m3"))
        {
            Var mini = var("mini", dom(range(f_max+1)),
                    "Frequence minimum utilisee");
            Var maxi = var("maxi", dom(range(f_max+1)),
                    "Frequence maximum utilisee");
            Var d = var("d", dom(range(f_max+1)),
                    "Ecart entre la plus haute et la plus basse frequence utilisees");

            Var[] allVar = vars(eme, rec);
            minimum(allVar, mini).note("Calcul la frequence minimale utilisée");
            maximum(allVar, maxi).note("Calcul la frequence maximale utilisée");
            equal(dist(mini, maxi), d).note("Calcul l'ecart entre min et max");

            minimize(d);
        }
    }
}
