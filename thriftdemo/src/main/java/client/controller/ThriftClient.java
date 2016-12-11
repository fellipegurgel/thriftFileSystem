/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import thriftclasses.FileSystem;
import client.view.ClientResultsView;
import client.view.ClientResultsView;

public class ThriftClient {

//    public static void main(String args[]){
//        ProcessClientRequest("localhost", 9003, "", "", 0, null);
//    }
    public static void ProcessClientRequest(String host, int port, String request, String path, int version, byte[] data) {
        try {
            TSocket transport = new TSocket(host, port);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);

            ByteBuffer newData = ByteBuffer.wrap(data);
//            List<String> partiotionedPath = new ArrayList<>(Arrays.asList(path.split("/")));
//            if (partiotionedPath.isEmpty()) {
//                partiotionedPath.add("/");
//            } else {
//                partiotionedPath.set(0, "/");
//            }

            FileSystem.Client client = new FileSystem.Client(protocol);
            String result = "";
            switch (request) {
                case "ADD":
                    result = client.addFile(path, newData);
                    break;
                case "GET":
                    result = client.getFile(path);
                    break;
                case "LIST":
                    result = client.listChildren(path);
                    break;
                case "UPDATE":
                    result = client.updateFile(path, newData, false, version);
                    break;
                case "DELETE":
                    result = client.deleteFile(path, false, version);
                    if (result == null) {
                        result = "The file " + path + " does not exist!";
                    }
                    break;
                case "DELETE+VERSION":
                    result = client.deleteFile(path, true, version);
                    if (result == null) {
                        result = "The file " + path + " does not exist or it has a different version!";
                    }
                    break;
                case "UPDATE+VERSION":
                    result = client.updateFile(path, newData, true, version);
                    if (result == null) {
                        result = "The file " + path + " does not exist or it has a different version!";
                    }
                    break;
                default:
                    result = "Error!";
                    break;
            }

            //result = client.hi();
            String[] a = null;
            new ClientResultsView("").show(result);
            //System.out.println("Return from server: " + result);

            transport.close();
        } catch (TException e) {
            e.printStackTrace();
        }
    }
}
