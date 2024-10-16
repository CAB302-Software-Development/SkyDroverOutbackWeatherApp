package cab302softwaredevelopment.outbackweathertrackerapplication.models.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * This class represents the data model used for user login requests.
 * It contains the username and password required to authenticate a user.
 */
@Getter
@Setter
public class UserLoginRequestDTO {

    /**
     * The username of the user attempting to log in.
     */
    private String userName;

    /**
     * The password of the user attempting to log in.
     */
    private String password;
}