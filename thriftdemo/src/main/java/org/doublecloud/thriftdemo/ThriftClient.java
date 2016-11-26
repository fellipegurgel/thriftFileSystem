/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.doublecloud.thriftdemo;
 
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
 
public class ThriftClient
{
 
  public static void main(String[] args)
  {
    try
    {
      TSocket transport = new TSocket("localhost", 7912);
      transport.open();
      TProtocol protocol = new TBinaryProtocol(transport);
 
      Hello.Client client = new Hello.Client(protocol);
 
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
