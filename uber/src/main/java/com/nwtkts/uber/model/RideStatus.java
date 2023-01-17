package com.nwtkts.uber.model;

import javax.persistence.Column;

public enum RideStatus {
    CRUISING,
    SCHEDULED,
    WAITING,
    STARTED,
    ENDED,
    CANCELED

}
