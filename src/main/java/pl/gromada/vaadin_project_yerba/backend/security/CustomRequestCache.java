package pl.gromada.vaadin_project_yerba.backend.security;

import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// A cache to keep track of unauthenticated requests.
class CustomRequestCache extends HttpSessionRequestCache {

    /*
        Saves unauthenticated requests so we can redirect the user to the page they were
        trying to access once they’are logged in.
     */
    @Override
    public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
        if (!SecurityUtils.isFrameworkInternalRequest(request)) {
            super.saveRequest(request, response);
        }
    }

}