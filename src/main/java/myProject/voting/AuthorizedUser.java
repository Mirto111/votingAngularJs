package myProject.voting;

import myProject.voting.model.User;
import myProject.voting.util.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Created by Secret_Hero on 26.02.2018.
 */
public class AuthorizedUser extends org.springframework.security.core.userdetails.User  {

    private static final long serialVersionUID = 1L;

    private final User user;


    public static int id() {
        return get().user.getId();
    }


    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), true, true, true, true, user.getRoles());
        this.user = new User(user.getId(), user.getEmail(), user.getName(),user.getPassword(),user.getRoles());
    }

    public User getUser() {
        return user;
    }

    public static AuthorizedUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof AuthorizedUser) ? (AuthorizedUser) principal : null;
    }

    public static AuthorizedUser get() {
        AuthorizedUser user = safeGet();
        if(Objects.isNull(user)){
            throw  new NotFoundException("Authorized User not found");
        }

        return user;
    }

}
