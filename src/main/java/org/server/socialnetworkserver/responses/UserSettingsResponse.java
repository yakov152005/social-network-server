package org.server.socialnetworkserver.responses;

import lombok.Getter;
import lombok.Setter;
import org.server.socialnetworkserver.dtos.UserSettingsDto;

@Getter
@Setter
public class UserSettingsResponse extends BasicResponse{
    private UserSettingsDto userSettingsDto;

    public UserSettingsResponse(boolean success, String error, UserSettingsDto userSettingsDto) {
        super(success, error);
        this.userSettingsDto = userSettingsDto;
    }

    public UserSettingsResponse(UserSettingsDto userSettingsDto) {
        this.userSettingsDto = userSettingsDto;
    }
    public UserSettingsResponse(boolean success, String error) {
        super(success, error);
    }
}
