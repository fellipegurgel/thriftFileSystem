/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.doublecloud.thriftdemo;
 
import org.apache.thrift.TException;
 
public class HelloImpl implements Hello.Iface
{
  public TPage GetFile(String hi) throws TException
  {
    return new TPage();
  }
  public String hi() throws TException
  {
    return "Hello DoubleCloud.org";
  }
}
