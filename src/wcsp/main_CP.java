public class main_CP
{
    public static void main(String[] args) throws Exception
    {
        String[] files = {
                "celar_50_7_10_5_0.800000_0.json",
                "celar_50_7_10_5_0.800000_1.json",
                "celar_50_7_10_5_0.800000_4.json",
                "celar_50_7_10_5_0.800000_9.json",
                "celar_50_8_10_5_0.800000_8.json",
                "celar_150_13_15_5_0.800000_2.json",
                "celar_150_13_15_5_0.800000_9.json",
                "celar_150_13_15_5_0.800000_18.json",
                "celar_150_13_15_5_0.800000_20.json",
                "celar_150_13_15_5_0.800000_25.json",
                "celar_250_25_15_5_0.820000_0.json",
                "celar_250_25_15_5_0.820000_6.json",
                "celar_250_25_15_5_0.820000_8.json",
                "celar_250_25_15_5_0.820000_17.json",
                "celar_250_25_15_5_0.820000_29.json",
                "celar_500_30_20_5_0.870000_0.json",
                "celar_500_30_20_5_0.870000_8.json",
                "celar_500_30_20_5_0.870000_25.json",
                "celar_500_30_20_5_0.870000_30.json",
                "celar_500_30_20_5_0.870000_49.json"
        };


        for(String str : files)
        {
            CP problem = new CP();
            problem.readFile(args[0]+str);

            System.out.println("\ndo: " + str);

            problem.makeCP();

            System.out.println("done");
        }
    }
}
