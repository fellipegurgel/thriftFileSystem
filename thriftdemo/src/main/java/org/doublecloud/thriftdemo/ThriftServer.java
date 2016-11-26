/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.doublecloud.thriftdemo;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

public class ThriftServer {
    
    //static int identifier;
    static int[] allServers;

    public static void StartServers(int serversNumber, int[] serversPorts) {
        allServers = serversPorts;
        for (int i = 0; i < serversNumber; i++) {
            try {
                TServerSocket serverTransport = new TServerSocket(serversPorts[i]);
                Hello.Processor processor = new Hello.Processor(new HelloImpl());
                TServer server = new TThreadPoolServer(
                        new TThreadPoolServer.Args(serverTransport).processor(processor));
                System.out.println("Starting server on port " + serversPorts[i] + " ...");
                server.serve();
            } catch (TTransportException e) {
                e.printStackTrace();
            }
        }
    }
    
}
