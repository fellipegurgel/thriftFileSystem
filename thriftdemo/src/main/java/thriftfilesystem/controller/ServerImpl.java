/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriftfilesystem.controller;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;
import thriftclasses.FileSystem;
import thriftclasses.FakeFile;

public class ServerImpl implements FileSystem.Iface {

    private int[] otherServers;
    private int serverID;
    private int serversNumber;
    private TreeMap<String, FakeFile> fileSyetem = new TreeMap<String, FakeFile>();

    public ServerImpl(int[] servers, int serverID) {
        this.otherServers = servers;
        this.serverID = serverID;
        this.serversNumber = servers.length;
    }

    public ServerImpl() {
    }

    public String hi() throws TException {
        return "Hello " + otherServers[serverID];
    }

    @Override
    public String getFile(List<String> path) throws TException {
        int pathCount = path.size();
        String fileName = path.get(pathCount - 1);
        int serverHost = path.get(pathCount - 1).hashCode() % serversNumber;
        String result = "";
        FakeFile file = null;
        //return "ate aqui foi ein";
        if (serverHost == this.serverID) {
            if (this.fileSyetem.containsKey(fileName)) {
                file = fileSyetem.get(fileName);
                try {
                    result = "Name: " + file.getName()
                            + "\nCreation: " + file.getCreation()
                            + "\nModification: " + file.getModification()
                            + "\nVersion: " + file.getVersion()
                            + "\nData: \n" + new String(file.getData(), "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                return result;
            } else {
                return null;
            }
        } else {
            TSocket transport = new TSocket("localhost", otherServers[serverHost]);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            FileSystem.Client client = new FileSystem.Client(protocol);
            return client.getFile(path);
        }
    }

    @Override
    public String listChildren(List<String> path) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String addFile(List<String> path, ByteBuffer data) throws TException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        int pathCount = path.size();
        String fileName = path.get(pathCount - 1);
        int serverHost = path.get(pathCount - 1).hashCode() % serversNumber;
        String result = "";
        FakeFile file = null;

        if (serverHost == this.serverID) {
            if (!this.fileSyetem.containsKey(fileName)) {
                if (checkParents(path)) {
                    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                    file = new FakeFile(fileName, date, date, 1, data, new TreeSet<String>());
                    this.fileSyetem.put(fileName, file);
                    result = "File " + fileName + " successfully added!";
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            TSocket transport = new TSocket("localhost", otherServers[serverHost]);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            FileSystem.Client client = new FileSystem.Client(protocol);
            return client.addFile(path, data);
        }
        return null;
    }

    @Override
    public String updateFile(List<String> path, ByteBuffer data) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String deleteFile(List<String> path) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String updateByVersion(List<String> path, ByteBuffer data, int version) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String deleteByVersion(List<String> path, int version) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean checkParents(List<String> path) throws TTransportException, TException {
        for (int i = 0; i < path.size() - 1; i++) {
            String parents = path.get(i);
            int serverHost = parents.hashCode() % serversNumber;
            TSocket transport = new TSocket("localhost", otherServers[serverHost]);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            FileSystem.Client client = new FileSystem.Client(protocol);
            List<String> parent = new ArrayList();
            parent.add(parents);
            if (client.getFile(parent) == null) {
                return false;
            }
        }
        return true;
    }

}
