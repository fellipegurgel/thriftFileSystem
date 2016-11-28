/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriftfilesystem.controller;
 
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import thriftclasses.FileSystem;
 
public class ThriftClient
{
 
  public static void main(String[] args)
  {
    try
    {
      TSocket transport = new TSocket("localhost", 9002);
      transport.open();
      TProtocol protocol = new TBinaryProtocol(transport);
 
      FileSystem.Client client = new FileSystem.Client(protocol);
 
      String result = client.hi();
      System.out.println("Return from server: " + result);
      transport.close();
    } 
    catch (TException e)
    {
      e.printStackTrace();
    }
  }
}
