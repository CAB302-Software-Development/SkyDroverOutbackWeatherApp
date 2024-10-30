package cab302softwaredevelopment.outbackweathertrackerapplication.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {
    /**
     * The username of the updated user.
     */
    private String username;

    /**
     * The password for the new updated account.
     */
    private String userPassword;

    /**
     * The email address of the updated user.
     */
    private String userEmail;

    /**
     * The application theme of the updated user.
     */
    private String userTheme;
}
