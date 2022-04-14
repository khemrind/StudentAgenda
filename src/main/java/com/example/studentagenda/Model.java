package com.example.studentagenda;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.UUID;

public class Model {

    public String identifier = UUID.randomUUID().toString();
    public Date creationTime = Calendar.getInstance().getTime();

}
