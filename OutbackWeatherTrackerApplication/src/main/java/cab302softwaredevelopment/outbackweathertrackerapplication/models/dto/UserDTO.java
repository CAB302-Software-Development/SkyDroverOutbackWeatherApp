package cab302softwaredevelopment.outbackweathertrackerapplication.models.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * This class represents the public view of user data, exposing only non-sensitive information.
 * It includes fields like username and email and is stored in the MongoDB collection "UserData".
 */
@Setter
@Getter
public class UserDTO {

    /**
     * The username of the user.
     * This field is mapped to the "user_name" field in the MongoDB document.
     */
    private String username;

    /**
     * The email address of the user.
     * This field is mapped to the "user_email" field in the MongoDB document.
     */
    private String userEmail;
}