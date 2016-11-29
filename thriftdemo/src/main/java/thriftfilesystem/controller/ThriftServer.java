/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriftfilesystem.controller;

import thriftfilesystem.controller.ServerImpl;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import thriftclasses.FileSystem;

public class ThriftServer {

    //static int identifier;
    //static int[] allServers;
    public static void StartServer(int serverPort, int[] serversPorts) {
        //int[] allServers = serversPorts;
        try {
            TServerSocket serverTransport = new TServerSocket(serverPort);
            FileSystem.Processor processor = new FileSystem.Processor(new ServerImpl(serversPorts));
            TServer server = new TThreadPoolServer(
                    new TThreadPoolServer.Args(serverTransport).processor(processor));
            System.out.println("Starting server on port " + serverPort + " ...");
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
            //return false;
        }

    }
}
