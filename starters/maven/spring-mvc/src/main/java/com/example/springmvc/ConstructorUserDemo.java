package com.example.springmvc;

import java.lang.reflect.* ;
import lombok.*;

/*
@NoArgsConstructor annotation for generating a constructor with no parameters
*/
@NoArgsConstructor
/*
@AllArgsConstructor annotation for generating a constructor
with 1 parameter for each field
*/
@AllArgsConstructor
public class ConstructorUserDemo {
    private int userId;
    private String userName;
    private int userAge;

    public static void print()
    {
        System.out.println( "\n\n***** ConstructorUserDemo Bytecode Dump *****" ) ;
        ConstructorUserDemo m = new ConstructorUserDemo() ;
        Class gmClass = m.getClass() ;

        System.out.println( "\nFIELDS:" ) ;
        Field[] fields = gmClass.getDeclaredFields();
        for (Field f : fields) {

            int mods = f.getModifiers();

            if ( Modifier.isPublic(mods) )
                System.out.format( "  public" ) ;
            else if ( Modifier.isPrivate(mods) )
                System.out.format( "  private" ) ;
            else if ( Modifier.isProtected(mods) )
                System.out.format( "  protected" ) ;

            if ( Modifier.isStatic(mods) )
                System.out.format( " static") ;

            System.out.format( " %s %s\n", f.getType(), f.getName() ) ;

        }

        System.out.println( "\nMETHODS:" ) ;
        Method gmMethods[] = gmClass.getMethods() ;
        for ( int i=0; i <gmMethods.length; i++ )
        {
            Method theMethod = gmMethods[i] ;
            String method = theMethod.toString() ;
            System.out.format( "  %s\n", method ) ;
        }

    }

}