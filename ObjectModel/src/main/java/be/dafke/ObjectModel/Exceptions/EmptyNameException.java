package be.dafke.ObjectModel.Exceptions;

/**
 * User: Dafke
 * Date: 24/02/13
 * Time: 12:07
 */
public class EmptyNameException extends Throwable {
    public EmptyNameException() {
    }

    public EmptyNameException(String message) {
        super(message);
    }
}
