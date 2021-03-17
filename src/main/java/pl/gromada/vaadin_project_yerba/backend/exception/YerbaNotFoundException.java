package pl.gromada.vaadin_project_yerba.backend.exception;

public class YerbaNotFoundException extends RuntimeException {

    public YerbaNotFoundException(long id) {
        super("Yerba with id: "+id+ " doesn't exist");
    }
}
