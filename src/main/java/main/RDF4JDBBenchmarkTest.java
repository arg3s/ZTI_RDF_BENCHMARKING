package main;

import org.eclipse.rdf4j.RDF4J;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import static main.QueryConstants.prepareAndEvaluate;
import static main.QueryConstants.queryList;

import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class RDF4JDBBenchmarkTest {
	private static Stopwatch stopwatch = new Stopwatch();

	private RepositoryConnection connection;

	public static void runTest() throws Exception {
	    //tworzenie repozytorium
        Repository repo = new SailRepository( new MemoryStore() );
        repo.initialize();
        //tworzenie połączenia
        RepositoryConnection con = repo.getConnection();

        try {
            con.clear();
            stopwatch.start();
            con.begin();
            con.add(RDF4JDBBenchmarkTest.class.getResourceAsStream("/data/sp2b.n3"), "urn:base", RDFFormat.N3);
            stopwatch.stopForInsert();

            System.out.println("# RUNNING QUERIES");
            IntStream.range(0, 8).forEach(i -> {
                stopwatch.start();
                TupleQueryResult queryResult = prepareAndEvaluate(con, queryList.get(i));
                stopwatch.stop(i);
                queryResult.close();
            });
            System.out.println("Complete map of times: ");
            stopwatch.timesMap.forEach((integer, aLong) -> System.out.println(integer + " : " + aLong));
        } finally {
			con.close();
		}

	}
}
