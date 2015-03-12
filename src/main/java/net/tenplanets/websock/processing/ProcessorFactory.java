/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tenplanets.websock.processing;

/**
 *
 * @author Vadims Zemlanojs <vadim@tenplanets.net>
 */
public class ProcessorFactory {
    
      public  Processable getDummyProcessor(){
          return new DummyProcessor();
      }
}
