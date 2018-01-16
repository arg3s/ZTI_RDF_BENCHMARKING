package main;

public class CommonDBTest {
    public static void main(String[] args) {
        try {
            System.out.println("#################################################");
            System.out.println("################# GraphDB tests #################");
            System.out.println("#################################################");
            GraphDBBenchmarkTest.runTest();
            System.out.println("###############################################");
            System.out.println("################# RDF4J tests #################");
            System.out.println("###############################################");
            RDF4JDBBenchmarkTest.runTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
