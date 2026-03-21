package com.victor.email.dto;

import java.util.UUID;

public record EmailDto(
        UUID id,
        String subject,
        String emailTo,
        String body
) {}
