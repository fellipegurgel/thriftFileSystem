/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriftfilesystem.controller;

import org.apache.thrift.TException;
import thriftclasses.FileSystem;
import thriftclasses.FakeFile;

public class HelloImpl implements FileSystem.Iface {
    
    private int[] otherServers;

    public HelloImpl(int[] servers) {
        this.otherServers = servers;
    }

    public HelloImpl() {
    }
    

    public FakeFile GetFile(String hi) throws TException {
        return new FakeFile();
    }

    public String hi() throws TException {
        return "Hello DoubleCloud.org";
    }
}
