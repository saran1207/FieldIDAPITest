package com.fieldid.index;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.sql.*;

public class Validate {

	public static void main(String[] args) throws Exception {
		if (args.length < 4) {
			System.err.println("Usage: <db_host> <db_user> <db_pass> <tenant_name>");
			System.exit(1);
		}
		String dbHost = args[0];
		String dbUser = args[1];
		String dbPass = args[2];
		String tenantName = args[3];

		Directory dir = null;
		IndexReader reader = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(String.format("jdbc:mysql://%s/fieldid?user=%s&password=%s", dbHost, dbUser, dbPass));

			Long tenantId = loadTenantId(con, tenantName);

			dir = FSDirectory.open(new File(String.format("/var/fieldid/private/indexes/%s/assets", tenantName)));
			reader = DirectoryReader.open(dir);

			stmt = con.prepareStatement("SELECT COUNT(*) FROM assets WHERE tenant_id = ? AND id = ?");
			stmt.setLong(1, tenantId);

			IndexSearcher searcher = new IndexSearcher(reader);
			TopDocs docs = searcher.search(new MatchAllDocsQuery(), reader.numDocs());

			int i = 0;
			int totalDocs = docs.scoreDocs.length;
			int corruptDocs = 0;
			Long assetId;
			Document doc;

			printStatus(0, totalDocs);
			for (ScoreDoc scoreDoc: docs.scoreDocs) {
				doc = searcher.doc(scoreDoc.doc);
				assetId = Long.valueOf(doc.get("_id"));
				stmt.setLong(2, assetId);
				rs = stmt.executeQuery();
				rs.next();

				if (i % totalDocs == 1000) {
					printStatus(i, totalDocs);
				}

				if (rs.getInt(1) == 0) {
					printDoc(doc);
					corruptDocs++;
				}
				rs.close();
				i++;
			}
			printStatus(i, totalDocs);
			if (totalDocs > 0) {
				System.out.println(String.format("%s: %d/%d (%.2f%%)", tenantName, corruptDocs, totalDocs, ((float) corruptDocs / totalDocs * 100.0f)));
			} else {
				System.out.println(String.format("%s: 0/0 (0%%)", tenantName));
			}
		} finally {
			if (rs != null) 	{ try { rs.close(); 	} catch (Exception e) {} }
			if (stmt != null) 	{ try { stmt.close(); 	} catch (Exception e) {} }
			if (con != null) 	{ try { con.close(); 	} catch (Exception e) {} }
			if (reader != null) { try { reader.close(); } catch (Exception e) {} }
			if (dir != null) 	{ try { dir.close(); 	} catch (Exception e) {} }
		}
	}

	private static void printStatus(int i, int totalDocs) {
		System.out.println("-----------------------------------------");
		System.out.println(String.format("Scanned: %d/%d", i, totalDocs));
		System.out.println("-----------------------------------------");
	}

	private static void printDoc(Document doc) {
		System.out.println("================================================================================");
		for (IndexableField field: doc.getFields()) {
			System.out.println(field.name() + ": " + field.stringValue());
		}
	}

	private static Long loadTenantId(Connection con, String tenantName) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT id FROM tenants WHERE name = '" + tenantName + "'");
			if (!rs.next()) {
				throw new Exception("Tenant " + tenantName + " not found");
			}
			return rs.getLong(1);
		} finally {
			if (rs != null) 	{ try { rs.close(); 	} catch (Exception e) {} }
			if (stmt != null) 	{ try { stmt.close(); 	} catch (Exception e) {} }
		}
	}
}
