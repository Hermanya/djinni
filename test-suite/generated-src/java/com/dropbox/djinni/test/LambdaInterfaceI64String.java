// AUTOGENERATED FILE - DO NOT MODIFY!
// This file generated by Djinni from 

package com.dropbox.djinni.test;

import java.util.function.Function;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**doc */
public  class LambdaInterfaceI64String {
    private Function<Long, String> actualLambda;
    public LambdaInterfaceI64String (Function<Long, String> lambda)
    {
        super();
        this.actualLambda = lambda;
    }
    public String run(long param1)
    {
        return this.actualLambda.apply(param1);
    }
}