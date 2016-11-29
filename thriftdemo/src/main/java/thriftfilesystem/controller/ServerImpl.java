/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriftfilesystem.controller;

import java.nio.ByteBuffer;
import java.util.TreeMap;
import org.apache.thrift.TException;
import thriftclasses.FileSystem;
import thriftclasses.FakeFile;

public class ServerImpl implements FileSystem.Iface {
    
    private int[] otherServers;
    private int serverID;
    private TreeMap<String, FakeFile> fileSyetem = new TreeMap<String, FakeFile>();

    public ServerImpl(int[] servers) {
        this.otherServers = servers;
    }

    public ServerImpl() {
    }
    
    public String hi() throws TException {
        return "Hello "+otherServers[2];
    }

    @Override
    public String getFile(String path) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String listChildren(String path) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String addFile(String path, ByteBuffer data) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
}
