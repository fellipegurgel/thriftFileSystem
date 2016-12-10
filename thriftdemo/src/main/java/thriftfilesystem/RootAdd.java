/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriftfilesystem;

import java.util.ArrayList;
import java.util.List;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;
import thriftclasses.FileSystem;

/**
 *
 * @author Fellipe G
 */
public class RootAdd {

    public static void main(String[] args) throws TTransportException, TException {
        String root = "/";
        TSocket transport = new TSocket("localhost", 9003);
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        FileSystem.Client client = new FileSystem.Client(protocol);
        String a = client.addFile("/a", null);
        System.out.println(a);
    }

}
