package myProject.voting.web.rest;


import myProject.voting.AuthorizedUser;
import myProject.voting.model.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

/**
 * Контроллер аутентификации.
 * 
 *
 *
 */
@RestController
@RequestMapping("/auth")
public class LoginController {



	/**
	 * Метод аутентификации/получения данных пользователя. GET
	 * запрос должен содержать заголовок аутентификации, с данными пользователя
	 * (логин и пароль). Если заголовок отсутствует или переданны неверные
	 * данные, то метод вернет HTTP статус 403.
	 *
	 * @param Данные пользователя, автоматически предоставляются Spring
	 *             Security, если в заголовке GET запроса были предоставлены
	 *             корректные данные пользователя.
	 * @return Данные пользователя или null.
	 */
	@RequestMapping(value = "/user", method = RequestMethod.GET, produces = "application/json")
	public AuthorizedUser getUser(Principal principial) {
		
		return  AuthorizedUser.get();

	}
	
	/**
	 * Выйти из системы. 
	 * @param rq {@link javax.servlet.http.HttpServletRequest}
	 * @param rs {@link javax.servlet.http.HttpServletResponse}
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public void logout(HttpServletRequest rq, HttpServletResponse rs) {
		
	    SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
	    securityContextLogoutHandler.logout(rq, rs, null);
		
	}
	
}
