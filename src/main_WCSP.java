public class main_WCSP
{
    public static void main(String[] args) throws Exception
    {
        WCSP wcsp = new WCSP(args[0]);
        wcsp.readFile(args[0]);
        for(WCSP.Station s : wcsp.stations)
        {
            String str = "";
            str += "| "+s.num+" |";
            str += "\tregion: "+s.region;
            str += "\tdelta: "+s.delta;
            str += "\n\temetteur: "+s.emetteur;
            str += "\n\trecepteur: "+s.recepteur;
            System.out.println(str);
        }
    }
}
