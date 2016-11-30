/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriftfilesystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import thriftfilesystem.controller.ServerImpl;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;
import thriftclasses.FileSystem;

public class teste {
    
    //static int identifier;
    //static int[] allServers;

    public static void main(String[] args) throws TTransportException, TException {
        //int[] allServers = serversPorts;
        List<String> partiotionedPath = new ArrayList<>(Arrays.asList("/a".split("/")));
        int i = 0;
            TSocket transport = new TSocket("localhost", 9003);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            FileSystem.Client client = new FileSystem.Client(protocol);
            String a = client.getFile(partiotionedPath);
            System.out.println(a);

        //for (i = 0; i < serversNumber; i++) {
//            try {
//                TServerSocket serverTransport = new TServerSocket(9000);
//                FileSystem.Processor processor = new FileSystem.Processor(new ServerImpl());
//                TServer server = new TThreadPoolServer(
//                        new TThreadPoolServer.Args(serverTransport).processor(processor));
//                System.out.println("Starting server on port 9000 ...".hashCode()%5);
//                server.serve();
//                TServerSocket serverTransport2 = new TServerSocket(9001);
//                FileSystem.Processor processor2 = new FileSystem.Processor(new ServerImpl());
//                TServer server2 = new TThreadPoolServer(
//                        new TThreadPoolServer.Args(serverTransport2).processor(processor2));
//                System.out.println("Starting server on port 9001 ...");
//                server2.serve();
//            } catch (TTransportException e) {
//                e.printStackTrace();
//                //return false;
//            }
        //}
//        if(i == serversNumber)
//            return true;
//        else
//            return false;
    }
    
}
