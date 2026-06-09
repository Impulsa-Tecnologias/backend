package com.edw.Cibot_Chat.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemMetricsResponse {
    private long totalUsers;
    private long activeChats;
    private long savedRecipes;
}
