package com.datastax.ca.java;

import com.datastax.driver.core.*;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.LatencyAwarePolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;
import com.datastax.driver.core.policies.RoundRobinPolicy;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by scotthendrickson.
 */
public class ConnectionTest {

    public static void main(String[] args) {

        String cp = "";
        String pword = "";
        String dc = "";

        try {
            if (args.length > 2) { // I arguments provided
                System.out.println(args[0]);
                cp = args[0];
                pword = args[1];
                dc = args[2];
            } else {
                System.out.println("run.sh [node IP] [db password] [datacenter]");
            }
            //DCAwareRoundRobinPolicy dcAwarePolicy = new DCAwareRoundRobinPolicy.Builder().build();
            //LatencyAwarePolicy policy = new LatencyAwarePolicy().Builder().build();
            Cluster cluster = Cluster.builder()
                    .addContactPoint(cp)
                    .withCredentials("cassandra", pword)
                    .withLoadBalancingPolicy(new RoundRobinPolicy())
                    .build();
            System.out.println("\n");

            System.out.printf("Connected to cluster: %s%n", cluster.getMetadata().getClusterName());

            System.out.println("\n");

            System.out.printf("All hosts for cluster: %s%n", cluster.getMetadata().getAllHosts());

            System.out.println("\n");

            Set<Host> hosts = cluster.getMetadata().getAllHosts();
            Iterator itr = hosts.iterator();
            while ( itr.hasNext()) {

                Host host = (Host) itr.next();

                System.out.printf("Token range: %s %50.50s . . . %n", host.toString(), cluster.getMetadata().getTokenRanges("simplex", host ));
                System.out.println("\n");
            }

            Session session = cluster.connect();

            session.execute("DROP KEYSPACE IF EXISTS simplex");

            session.execute("CREATE KEYSPACE IF NOT EXISTS simplex WITH replication " +
                    "= {'class':'NetworkTopologyStrategy', '"+dc+"':3};");


            session.execute(
                    "CREATE TABLE IF NOT EXISTS simplex.playlists (" +
                            "id text," +
                            "title text," +
                            "album text, " +
                            "artist text," +
                            "PRIMARY KEY (id, title, album, artist)" +
                            ");");

            session.execute(
                    "INSERT INTO simplex.playlists (id,  title, album, artist) " +
                            "VALUES (" +
                            "'3cc9ccb7-6221-4ccb-8387-f22b6a1b354d'," +
                            "'The Ocean'," +
                            "'Houses of The Holy'," +
                            "'Led Zeppelin'" +
                            ");");

            session.execute(
                    "INSERT INTO simplex.playlists (id,  title, album, artist) " +
                            "VALUES (" +
                            "'2cc9ccb7-6221-4ccb-8387-f22b6a1b354d'," +
                            "'La Petite Tonkinoise'," +
                            "'Bye Bye Blackbird'," +
                            "'Joséphine Baker'" +
                            ");");

            //ResultSet results = session.execute(
            // "SELECT * FROM simplex.playlists " +
            //"WHERE id = 2cc9ccb7-6221-4ccb-8387-f22b6a1b354d;");

            ResultSet results = session.execute(
                    "SELECT * FROM simplex.playlists ");

            //System.out.println(results.all().size());
            System.out.printf("%-30s\t%-20s\t%-20s%n", "title", "album", "artist");
            System.out.println("-------------------------------+-----------------------+--------------------");

            for (Row row : results) {

                System.out.printf("%-30s\t%-20s\t%-20s%n",
                        row.getString("title"),
                        row.getString("album"),
                        row.getString("artist"));

            }

            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
