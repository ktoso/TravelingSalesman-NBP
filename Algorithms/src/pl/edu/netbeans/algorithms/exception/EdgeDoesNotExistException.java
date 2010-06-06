/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.netbeans.algorithms.exception;

/**
 *
 * @author bartek
 */
public class EdgeDoesNotExistException extends AlgorithmException {

    public EdgeDoesNotExistException(String msg) {
        super(msg);
        System.err.println("EDE exp: " + msg);
    }

    public EdgeDoesNotExistException() {
        super();
    }



}
