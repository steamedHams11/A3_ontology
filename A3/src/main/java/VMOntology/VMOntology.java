package VMOntology;

import java.io.File;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

public class VMOntology {

	public static void main(String[] args) {
	
		String PATH_QUERY = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
				+ File.separator + "resources" + File.separator + "queries" + File.separator + "temp_Q.sparql";
		
		String ENDPOINT = "http://localhost:3030/ontology/query";
		
		String query = QueryFactory.read(PATH_QUERY).toString();
	

		executeQuery(query, ENDPOINT);
		
		
	}
	
	
	
	
	
	
	
	
	

	

	public static void executeQuery(String query, String endPoint) {

		if(query.contains("SELECT"))select_remote(query, endPoint);
		
		else if(query.contains("CONSTRUCT")) {
			Model graph = construct_remote(query, endPoint);
			RDFDataMgr.write(System.out, graph, Lang.TURTLE);
		} 
		else if (query.contains("ASK")) System.out.println(ask_remote(query, endPoint));
	
		else if (query.contains("DESCRIBE")) {
			Model graph = describe_remote(query, endPoint);
			RDFDataMgr.write(System.out, graph, Lang.TURTLE);
		}

	}
	
	
	
	
	
	

	
	
	
	
	
	
	public static ResultSet select_remote(String query_string, String ENDPOINT) {
		ResultSet results = null;
		Query query = QueryFactory.create(query_string);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(ENDPOINT, query);
		try {
			results = qexec.execSelect();
		} finally {
			if (results != null)
				ResultSetFormatter.out(System.out, results, query);
			else
				System.out.println("no results were found!");
			qexec.close();
		}
		return results;
	}

	public static Model construct_remote(String query_string, String ENDPOINT) {
		Model graph = null;
		Query query = QueryFactory.create(query_string);
		
		QueryExecution qexec = QueryExecutionFactory.sparqlService(ENDPOINT, query);
		
		try {
			graph = qexec.execConstruct();
		} finally {
			qexec.close();
		}
		
		return graph;
	}
	
	public static Boolean ask_remote(String query_string, String ENDPOINT){
		Boolean results = null;
		Query query = QueryFactory.create(query_string);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(ENDPOINT, query);
		try {
			results = qexec.execAsk();
		} finally {
			qexec.close();
		}
		return results;
		
	}

	public static Model describe_remote(String query_string, String ENDPOINT) {
		Model results = null;
		Query query = QueryFactory.create(query_string);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(ENDPOINT, query);
		try {
			results = qexec.execDescribe();
		} finally {
			qexec.close();
		}
		return results;
	}
}