/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriftfilesystem;

import thriftfilesystem.controller.HelloImpl;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import thriftclasses.FileSystem;

public class teste {
    
    //static int identifier;
    //static int[] allServers;

    public static void main(String[] args) {
        //int[] allServers = serversPorts;
        int i = 0;
        //for (i = 0; i < serversNumber; i++) {
            try {
                TServerSocket serverTransport = new TServerSocket(9000);
                FileSystem.Processor processor = new FileSystem.Processor(new HelloImpl());
                TServer server = new TThreadPoolServer(
                        new TThreadPoolServer.Args(serverTransport).processor(processor));
                System.out.println("Starting server on port 9000 ...");
                server.serve();
                TServerSocket serverTransport2 = new TServerSocket(9001);
                FileSystem.Processor processor2 = new FileSystem.Processor(new HelloImpl());
                TServer server2 = new TThreadPoolServer(
                        new TThreadPoolServer.Args(serverTransport2).processor(processor2));
                System.out.println("Starting server on port 9001 ...");
                server2.serve();
            } catch (TTransportException e) {
                e.printStackTrace();
                //return false;
            }
        //}
//        if(i == serversNumber)
//            return true;
//        else
//            return false;
    }
    
}
