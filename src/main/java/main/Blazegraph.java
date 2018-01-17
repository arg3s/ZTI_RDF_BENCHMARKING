package main;

import java.util.Properties;
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

import com.bigdata.journal.Options;
import com.bigdata.rdf.sail.BigdataSail;
import com.bigdata.rdf.sail.BigdataSailRepository;

public class Blazegraph {
    private static Stopwatch stopwatch = new Stopwatch();

    public static void main(String[] args) throws Exception {
        final Properties props = new Properties();
        props.put(Options.BUFFER_MODE, "DiskRW"); // persistent file system located journal
        props.put(Options.FILE, "/tmp/blazegraph/test.jnl"); // journal file location

        final BigdataSail sail = new BigdataSail(props); // instantiate a sail
        final Repository repo  = (Repository) new BigdataSailRepository(sail); // create a Sesame repository

        repo.initialize();
        RepositoryConnection con = repo.getConnection();

        try {
            con.clear();
            stopwatch.start();
            con.begin();
            con.add(Blazegraph.class.getResourceAsStream("/data/sp2b.n3"), "urn:base", RDFFormat.N3);
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
