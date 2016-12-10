/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriftfilesystem.controller;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private TreeMap<String, FakeFile> fileSyetem;

    public ServerImpl(int[] servers, int serverID) {
        this.otherServers = servers;
        this.serverID = serverID;
        this.serversNumber = servers.length;
        this.fileSyetem = new TreeMap();
    }

    public ServerImpl() {
    }

    public String hi() throws TException {
        TSocket transport = new TSocket("localhost", otherServers[0]);
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        FileSystem.Client client = new FileSystem.Client(protocol);
        return client.hi2();
    }

    public String hi2() throws TException {
        return "Gente entrou aqui. deus seja louvado!\n Hello " + otherServers[serverID];
    }

    @Override
    public String getFile(String path) throws TException {

        int serverHost = path.hashCode() % serversNumber;
        String result = "";
        FakeFile file = null;

        if (serverHost == this.serverID) {
            if (this.fileSyetem.containsKey(path)) {
                file = fileSyetem.get(path);
                try {
                    result = "- Name: " + file.getName()
                            + "\n- Creation: " + file.getCreation()
                            + "\n- Modification: " + file.getModification()
                            + "\n- Version: " + file.getVersion()
                            + "\n- Data: \n" + new String(file.getData(), "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                return result;
            } else {
                return "";
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
    public String listChildren(String path) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String addFile(String path, ByteBuffer data) throws TException {
        
        String fileName = "";
        
        if (path.equals("/")) {
            fileName = "/";
        } else {
            String[] splitededPath = path.split("/");
            fileName = splitededPath[splitededPath.length - 1];
        }
        int serverHost = path.hashCode() % serversNumber;
        String result = "";
        if (serverHost == this.serverID) {
            if (!this.fileSyetem.containsKey(fileName)) {
                if (checkParents(path, fileName)) {
                    String date = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss").format(new Date());
                    FakeFile file;
                    file = new FakeFile(fileName, date, date, 1, data, new TreeSet<String>());
                    this.fileSyetem.put(path, file);
                    result = "File '" + fileName + "' successfully added!";
                    return result;
                } else {
                    return "Parent folder(s) not found!";
                }
            } else {
                return "";
            }
        } else {
            TSocket transport = new TSocket("localhost", otherServers[serverHost]);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            FileSystem.Client client = new FileSystem.Client(protocol);
            return client.addFile(path, data);
        }
    }

    @Override
    public String updateFile(String path, ByteBuffer data) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String deleteFile(String path) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String updateByVersion(String path, ByteBuffer data, int version) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String deleteByVersion(String path, int version) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean checkParents(String path, String fileName) throws TTransportException, TException {
        if (!path.equals("/")) {
            String parent = path.substring(0, path.indexOf(fileName));
            int serverHost = parent.hashCode() % serversNumber;
            if (serverHost == this.serverID) {
                if (!this.fileSyetem.containsKey(parent)) {
                    return false;
                }
            } else {
                TSocket transport = new TSocket("localhost", otherServers[serverHost]);
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                FileSystem.Client client = new FileSystem.Client(protocol);
                if (!client.checkFile(parent)) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }
    
    @Override
    public boolean checkFile(String file){
        return this.fileSyetem.containsKey(file);
    }
}
