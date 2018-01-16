package main;

import java.util.List;

import org.eclipse.rdf4j.query.MalformedQueryException;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;

import com.google.common.collect.Lists;

final class QueryConstants {

	private static final String firstQuery = "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "PREFIX dc:      <http://purl.org/dc/elements/1.1/> "
			+ "PREFIX dcterms: <http://purl.org/dc/terms/> "
			+ "PREFIX bench:   <http://localhost/vocabulary/bench/> "
			+ "PREFIX xsd:     <http://www.w3.org/2001/XMLSchema#> "
			+ "SELECT ?yr "
			+ "WHERE {"
			+ "  ?journal rdf:type bench:Journal ."
			+ "  ?journal dc:title \"Journal 1 (1940)\"^^xsd:string ."
			+ "  ?journal dcterms:issued ?yr "
			+ "}";

	private static final String secondQuery = "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "PREFIX rdfs:    <http://www.w3.org/2000/01/rdf-schema#> "
			+ "PREFIX swrc:    <http://swrc.ontoware.org/ontology#> "
			+ "PREFIX foaf:    <http://xmlns.com/foaf/0.1/> "
			+ "PREFIX bench:   <http://localhost/vocabulary/bench/> "
			+ "PREFIX dc:      <http://purl.org/dc/elements/1.1/> "
			+ "PREFIX dcterms: <http://purl.org/dc/terms/> "
			+ "SELECT ?inproc ?author ?booktitle ?title  "
			+ "       ?proc ?ee ?page ?url ?yr ?abstract "
			+ "WHERE { "
			+ "  ?inproc rdf:type bench:Inproceedings . "
			+ "  ?inproc dc:creator ?author . "
			+ "  ?inproc bench:booktitle ?booktitle . "
			+ "  ?inproc dc:title ?title . "
			+ "  ?inproc dcterms:partOf ?proc . "
			+ "  ?inproc rdfs:seeAlso ?ee . "
			+ "  ?inproc swrc:pages ?page . "
			+ "  ?inproc foaf:homepage ?url . "
			+ "  ?inproc dcterms:issued ?yr  "
			+ "  OPTIONAL { "
			+ "    ?inproc bench:abstract ?abstract "
			+ "  } "
			+ "} "
			+ "ORDER BY ?yr";

	private static final String thirdQuery = "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "PREFIX bench: <http://localhost/vocabulary/bench/> "
			+ "PREFIX swrc:  <http://swrc.ontoware.org/ontology#> "
			+ "SELECT ?article "
			+ "WHERE { "
			+ "  ?article rdf:type bench:Article . "
			+ "  ?article ?property ?value  "
			+ "  FILTER (?property=swrc:pages)  "
			+ "}";

	private static final String fourthQuery = "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "PREFIX bench:   <http://localhost/vocabulary/bench/> "
			+ "PREFIX dc:      <http://purl.org/dc/elements/1.1/> "
			+ "PREFIX dcterms: <http://purl.org/dc/terms/> "
			+ "PREFIX foaf:    <http://xmlns.com/foaf/0.1/> "
			+ "PREFIX swrc:    <http://swrc.ontoware.org/ontology#> "
			+ "SELECT DISTINCT ?name1 ?name2  "
			+ "WHERE { "
			+ "  ?article1 rdf:type bench:Article . "
			+ "  ?article2 rdf:type bench:Article . "
			+ "  ?article1 dc:creator ?author1 . "
			+ "  ?author1 foaf:name ?name1 . "
			+ "  ?article2 dc:creator ?author2 . "
			+ "  ?author2 foaf:name ?name2 . "
			+ "  ?article1 swrc:journal ?journal . "
			+ "  ?article2 swrc:journal ?journal "
			+ "  FILTER (?name1<?name2) "
			+ "}";

	private static final String fifthQuery = "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "PREFIX foaf:  <http://xmlns.com/foaf/0.1/> "
			+ "PREFIX bench: <http://localhost/vocabulary/bench/> "
			+ "PREFIX dc:    <http://purl.org/dc/elements/1.1/> "
			+ "SELECT DISTINCT ?person ?name "
			+ "WHERE { "
			+ "  ?article rdf:type bench:Article . "
			+ "  ?article dc:creator ?person . "
			+ "  ?inproc rdf:type bench:Inproceedings . "
			+ "  ?inproc dc:creator ?person2 . "
			+ "  ?person foaf:name ?name . "
			+ "  ?person2 foaf:name ?name2 "
			+ "  FILTER (?name=?name2) "
			+ "}";

	private static final String sixthQuery = "SELECT ?yr ?name ?document "
			+ "WHERE { "
			+ "  ?class rdfs:subClassOf foaf:Document . "
			+ "  ?document rdf:type ?class . "
			+ "  ?document dcterms:issued ?yr . "
			+ "  ?document dc:creator ?author . "
			+ "  ?author foaf:name ?name "
			+ "  OPTIONAL { "
			+ "    ?class2 rdfs:subClassOf foaf:Document . "
			+ "    ?document2 rdf:type ?class2 . "
			+ "    ?document2 dcterms:issued ?yr2 . "
			+ "    ?document2 dc:creator ?author2  "
			+ "    FILTER (?author=?author2 && ?yr2<?yr) "
			+ "  } FILTER (!bound(?author2)) "
			+ "}";

	private static final String seventhQuery = "PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "PREFIX rdfs:    <http://www.w3.org/2000/01/rdf-schema#> "
			+ "PREFIX foaf:    <http://xmlns.com/foaf/0.1/> "
			+ "PREFIX dc:      <http://purl.org/dc/elements/1.1/> "
			+ "PREFIX dcterms: <http://purl.org/dc/terms/> "
			+ "SELECT DISTINCT ?title "
			+ "WHERE { "
			+ "  ?class rdfs:subClassOf foaf:Document . "
			+ "  ?doc rdf:type ?class . "
			+ "  ?doc dc:title ?title . "
			+ "  ?bag2 ?member2 ?doc . "
			+ "  ?doc2 dcterms:references ?bag2 "
			+ "  OPTIONAL { "
			+ "    ?class3 rdfs:subClassOf foaf:Document . "
			+ "    ?doc3 rdf:type ?class3 . "
			+ "    ?doc3 dcterms:references ?bag3 . "
			+ "    ?bag3 ?member3 ?doc "
			+ "    OPTIONAL { "
			+ "      ?class4 rdfs:subClassOf foaf:Document . "
			+ "      ?doc4 rdf:type ?class4 . "
			+ "      ?doc4 dcterms:references ?bag4 . "
			+ "      ?bag4 ?member4 ?doc3 "
			+ "    } FILTER (!bound(?doc4)) "
			+ "  } FILTER (!bound(?doc3)) "
			+ "}";

	private static final String eighthQuery = "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>  "
			+ "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
			+ "PREFIX dc:   <http://purl.org/dc/elements/1.1/> "
			+ "SELECT DISTINCT ?name "
			+ "WHERE { "
			+ "  ?erdoes rdf:type foaf:Person . "
			+ "  ?erdoes foaf:name \"Paul Erdoes\"^^xsd:string . "
			+ "  { "
			+ "    ?document dc:creator ?erdoes . "
			+ "    ?document dc:creator ?author . "
			+ "    ?document2 dc:creator ?author . "
			+ "    ?document2 dc:creator ?author2 . "
			+ "    ?author2 foaf:name ?name "
			+ "    FILTER (?author!=?erdoes && "
			+ "            ?document2!=?document && "
			+ "            ?author2!=?erdoes && "
			+ "            ?author2!=?author) "
			+ "  } UNION { "
			+ "    ?document dc:creator ?erdoes. "
			+ "    ?document dc:creator ?author. "
			+ "    ?author foaf:name ?name "
			+ "    FILTER (?author!=?erdoes) "
			+ "  } "
			+ "}";

	private static final String ninthQuery = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
			+ "SELECT DISTINCT ?predicate "
			+ "WHERE { "
			+ "  { "
			+ "    ?person rdf:type foaf:Person . "
			+ "    ?subject ?predicate ?person "
			+ "  } UNION { "
			+ "    ?person rdf:type foaf:Person . "
			+ "    ?person ?predicate ?object "
			+ "  } "
			+ "}";

	private static final String tenthQuery = "PREFIX person: <http://localhost/persons/> "
			+ "SELECT ?subject ?predicate "
			+ "WHERE { "
			+ "  ?subject ?predicate person:Paul_Erdoes "
			+ "}";

	private static final String eleventhQuery = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
			+ "SELECT ?ee "
			+ "WHERE { "
			+ "  ?publication rdfs:seeAlso ?ee "
			+ "} "
			+ "ORDER BY ?ee "
			+ "LIMIT 10 "
			+ "OFFSET 50";

	static final List<String> queryList = Lists.newArrayList(
			firstQuery,
			secondQuery,
			thirdQuery,
			fourthQuery,
			fifthQuery,
			// sixthQuery,
			// seventhQuery,
			eighthQuery,
			ninthQuery,
			tenthQuery,
			eleventhQuery);

	static TupleQueryResult prepareAndEvaluate(RepositoryConnection connection, String query)
			throws MalformedQueryException,
			RepositoryException,
			QueryEvaluationException {
		return connection.prepareTupleQuery(QueryLanguage.SPARQL, query).evaluate();
	}
}
