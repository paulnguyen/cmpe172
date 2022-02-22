package com.example.springmvc;

import java.lang.reflect.* ;
import lombok.Getter;
import lombok.Setter;

public class FieldLevelGetterSetterDemo {

    private int userId;

    @Getter @Setter
    private String userName;

    @Getter
    private int userAge;

    public FieldLevelGetterSetterDemo(int userAge){
        this.userAge=userAge;
    }

    public static void print()
    {
        System.out.println( "\n\n***** FieldLevelGetterSetterDemo Bytecode Dump *****" ) ;
        FieldLevelGetterSetterDemo m = new FieldLevelGetterSetterDemo(10) ;
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