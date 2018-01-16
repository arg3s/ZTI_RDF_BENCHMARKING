package main;

import static main.QueryConstants.prepareAndEvaluate;
import static main.QueryConstants.queryList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;

import Util.EmbeddedGraphDB;

class GraphDBBenchmarkTest {

	private static Stopwatch stopwatch = new Stopwatch();

	private RepositoryConnection connection;

	private GraphDBBenchmarkTest(RepositoryConnection connection) {
		this.connection = connection;
	}

	public static void main(String[] args) throws Exception {
		// Utworzenie polaczenia z repo do bazy GraphDB

		RepositoryConnection connection = EmbeddedGraphDB.openConnectionToTemporaryRepository("owl2-rl-optimized");

		connection.clear();

		GraphDBBenchmarkTest graphDbBenchmark = new GraphDBBenchmarkTest(connection);

		try {
			stopwatch.start();
			graphDbBenchmark.loadData();
			stopwatch.stopForInsert();

			graphDbBenchmark.runQueries();
		} finally {
			// zamkniecie polaczenia
			connection.close();
		}

	}

	private void loadData() throws RepositoryException, IOException, RDFParseException {
		System.out.println("Loading /data/sp2b.n3 to db...");

		// Start transakcji
		connection.begin();

		// Pobranie z resources/data/sp2b.n3 danych do transakcji
		connection.add(GraphDBBenchmarkTest.class.getResourceAsStream("/data/sp2b.n3"), "urn:base", RDFFormat.N3);

		// Zapisanie danych w bazie
		connection.commit();
		System.out.println("Successfully committed");

	}

	private void runQueries() throws Exception {
		System.out.println("# RUNNING QUERIES");
		IntStream.range(0, 8).forEach(i -> {
			stopwatch.start();
			TupleQueryResult queryResult = prepareAndEvaluate(connection, queryList.get(i));
			stopwatch.stop(i);
			queryResult.close();
		});
		System.out.println("Complete map of times: ");
		stopwatch.timesMap.forEach((integer, aLong) -> System.out.println(integer + " : " + aLong));
	}

	public static final class Stopwatch {

		private Long start;

		private Map<Integer, Double> timesMap = new HashMap<>();

		public void start() {
			start = System.currentTimeMillis();
		}

		public void stop(int queryId) {
			long now = System.currentTimeMillis();
			double estimatedTime = (now - start);
			timesMap.put(queryId, estimatedTime);
			System.out.println("Query nr: " + queryId + " took: " + estimatedTime);
		}

		public void stopForInsert() {
			long now = System.currentTimeMillis();
			double estimatedTime = (now - start);
			System.out.println("Insert took: " + estimatedTime + " milliseconds.");

		}

	}
}
