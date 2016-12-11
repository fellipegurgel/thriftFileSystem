/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesystemserver.controller;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
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

    @Override
    public String hi() throws TException {
        TSocket transport = new TSocket("localhost", otherServers[0]);
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        FileSystem.Client client = new FileSystem.Client(protocol);
        return client.hi2();
    }

    @Override
    public String hi2() throws TException {
        return "Gente entrou aqui. deus seje louvado!\n Hello " + otherServers[serverID];
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
                return "The file '" + path + "' does not exist!";
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
        String result = "File(s) under " + path + ":\n";
        int serverHost = path.hashCode() % serversNumber;
        if (serverHost == this.serverID) {
            if (this.fileSyetem.containsKey(path)) {
                FakeFile file = this.fileSyetem.get(path);
                for (String s : file.getChildren()) {
                    result += "- " + s + "\n";
                }
                return result;
            } else {
                return "The file '" + path + "' does not exist!";
            }
        } else {
            TSocket transport = new TSocket("localhost", otherServers[serverHost]);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            FileSystem.Client client = new FileSystem.Client(protocol);
            return client.listChildren(path);
        }
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
            if (!this.fileSyetem.containsKey(path)) {
                if (checkParents(path, fileName)) {
                    if (commitChanges(path, "adding")) {
                        if (addChild(path, fileName)) {
                            String date = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss").format(new Date());
                            FakeFile file;
                            file = new FakeFile(fileName, date, date, 1, data, new TreeSet<String>());
                            this.fileSyetem.put(path, file);
                            result = "File '" + fileName + "' successfully added!";
                            System.out.println(result);
                        } else {
                            result = "Unable to add child";
                        }
                    } else {
                        result = "Unable to commit changes!";
                        return result;
                    }
                    return result;
                } else {
                    return "Parent folder(s) not found!";
                }
            } else {
                return "The file '" + path + "' already exists!";
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
    public String updateFile(String path, ByteBuffer data, boolean checkVersion, int version) throws TException {
        int serverHost = path.hashCode() % serversNumber;
        String result = "";
        if (serverHost == this.serverID) {
            if (this.fileSyetem.containsKey(path)) {
                FakeFile file = this.fileSyetem.get(path);
                if (checkVersion) {
                    if (file.getVersion() != version) {
                        return "The version of '" + path + "' is not '" + version + "'";
                    }
                }
                file.setData(data);
                file.setVersion(file.getVersion() + 1);
                file.setModification(new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss").format(new Date()));
                result = "'" + path + "' UPDATED\n" + getFile(path);
                return result;
            } else {
                return "The file '" + path + "' does not exist!";
            }
        } else {
            TSocket transport = new TSocket("localhost", otherServers[serverHost]);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            FileSystem.Client client = new FileSystem.Client(protocol);
            return client.updateFile(path, data, checkVersion, version);
        }
    }

    @Override
    public String deleteFile(String path, boolean checkVersion, int version) throws TException {
        int serverHost = path.hashCode() % serversNumber;
        String result = "";
        if (serverHost == this.serverID) {
            if (this.fileSyetem.containsKey(path)) {
                FakeFile tempFile = this.fileSyetem.get(path);
                if (checkVersion) {
                    if (tempFile.getVersion() != version) {
                        return "The version of '" + path + "' is not '" + version + "'";
                    }
                }
                if (tempFile.getChildren().isEmpty()) {
                    if (commitChanges(path, "deleting")) {
                        String fileName = "";
                        if (path.equals("/")) {
                            fileName = "/";
                        } else {
                            String[] splitededPath = path.split("/");
                            fileName = splitededPath[splitededPath.length - 1];
                        }
                        if (deleteChild(path, fileName)) {
                            this.fileSyetem.remove(path);
                            result = "File '" + fileName + "' successfully deleted!";
                            System.out.println(result);
                        } else {
                            result = "Unable to delete child!";
                        }
                    } else {
                        result = "Unable to commit changes!";
                        return result;
                    }
                    return result;
                } else {
                    return "The file '" + path + "' has sub-files!";
                }
            } else {
                return "The file '" + path + "' does not exist!";
            }
        } else {
            TSocket transport = new TSocket("localhost", otherServers[serverHost]);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            FileSystem.Client client = new FileSystem.Client(protocol);
            return client.deleteFile(path, checkVersion, version);
        }
    }

    boolean checkParents(String path, String fileName) throws TTransportException, TException {
        if (!path.equals("/")) {
            String parent = getParent(path);
            int serverHost = parent.hashCode() % serversNumber;
            if (serverHost == this.serverID) {
                return this.checkFile(parent);
            } else {
                TSocket transport = new TSocket("localhost", otherServers[serverHost]);
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                FileSystem.Client client = new FileSystem.Client(protocol);
                return client.checkFile(parent);
            }
        } else {
            return true;
        }
    }

    @Override
    public boolean checkFile(String file) {
        return this.fileSyetem.containsKey(file);
    }

    @Override
    public boolean addChild(String path, String fileName) throws TException {
        if (path.equals(fileName)) {
            return true;
        } else {
            String parent = getParent(path);
            int serverHost = parent.hashCode() % serversNumber;
            if (serverHost == this.serverID) {
                if (commitChanges(path, "adding child")) {
                    FakeFile file = this.fileSyetem.get(parent);
                    file.children.add(fileName);
                    return true;
                } else {
                    return false;
                }
            } else {
                TSocket transport = new TSocket("localhost", otherServers[serverHost]);
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                FileSystem.Client client = new FileSystem.Client(protocol);
                return client.addChild(path, fileName);
            }
        }
    }
    
    @Override
    public boolean deleteChild(String path, String fileName) throws TException {
        if (path.equals(fileName)) {
            return true;
        } else {
            String parent = getParent(path);
            int serverHost = parent.hashCode() % serversNumber;
            if (serverHost == this.serverID) {
                if (commitChanges(path, "deleting child")) {
                    FakeFile file = this.fileSyetem.get(parent);
                    file.children.remove(fileName);
                    return true;
                } else {
                    return false;
                }
            } else {
                TSocket transport = new TSocket("localhost", otherServers[serverHost]);
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                FileSystem.Client client = new FileSystem.Client(protocol);
                return client.deleteChild(path, fileName);
            }
        }
    }

    public String getParent(String path) {
        String[] parents = path.split("/");
        String parent = "";
        if (path.startsWith("/") && parents.length == 2) {
            return "/";
        }
        for (int i = 1; i < parents.length - 1; i++) {
            parent += "/" + parents[i];
        }
        return parent;
    }

    boolean commitChanges(String file, String operation) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Commit changes about " + operation + " '" + file + "'? [Y/N] ");
        String response = scan.next();
        return response.toUpperCase().equals("Y");
    }
}
