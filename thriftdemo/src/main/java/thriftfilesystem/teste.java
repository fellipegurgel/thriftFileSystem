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
    public static void main(String[] args) throws TTransportException, TException {
        String t = "/a";
        String b = t.substring(0, t.indexOf("/"));
        int i = 0;
        TSocket transport = new TSocket("localhost", 9003);
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        FileSystem.Client client = new FileSystem.Client(protocol);
        String a = client.addFile("/c", null);
        System.out.println(a);

    }

}
