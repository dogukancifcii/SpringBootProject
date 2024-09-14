package com.dogukan.payload.messages;

public class SuccessMessages {

    //bunu yapma sebebizmiz gereksiz yere nesne üretme durumunu kapatmak için private çektik const metodunu springde ayni matigi yapiyor aslinda arastir!!!
    //todo:framework icerisinde hangi katman icinde olsakta spring boot ayni nesne gonderiyor.Cunku framework singelton nesne gonderir. Bean component mantigini araştır.
    private SuccessMessages() {

    }

    public static final String PASSWORD_CHANGE_RESPONSE_MESSAGE = "Password Succcessfully Change";
    public static final String USER_CREATED = "User is Saved";
    public static final String USER_FOUND = "User is Found Successfully";
    public static final String USER_DELETE = "User is deleted successfully";
    public static final String USER_UPDATE_MESSAGE = "User is Updated Successfully";
    public static final String USER_UPDATE = "your information has been updated successfully";

    public static final String TEACHER_SAVE = "Teacher is Saved";
    public static final String TEACHER_UPDATE = "Teacher is Updated Successfully";

    public static final String TEACHER_ADVISOR_UPDATED = "Teacher advisor is updated";
}
