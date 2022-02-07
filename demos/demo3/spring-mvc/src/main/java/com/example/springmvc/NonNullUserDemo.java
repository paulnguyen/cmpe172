package com.example.springmvc;


import lombok.NonNull;

public class NonNullUserDemo {
    private int userId;
    private String userName;
    private int userAge;

    public NonNullUserDemo(int userId, @NonNull String userName, int userAge) {
        if (userName == null) {
            throw new NullPointerException("userName is marked non-null but is null");
        } else {
            this.userId = userId;
            this.userName = userName;
            this.userAge = userAge;
        }
    }

    public static void print()
    {
        System.out.println( "\n\n***** NonNullUserDemo *****" ) ;

        try {
            NonNullUserDemo val = new NonNullUserDemo(50, null, 25);
        } catch ( Exception e ) {
            System.out.println( e ) ;
        }

    }

}