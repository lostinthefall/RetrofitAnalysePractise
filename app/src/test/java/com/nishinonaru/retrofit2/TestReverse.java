package com.nishinonaru.retrofit2;

/*
 * Created by 西野奈留NishinoNaru on 2016/11/27.
 */

import org.junit.Test;

public class TestReverse {

    @Test
    public void testReverse() {
        /**
         * 按位取反
         */
        int a = 8;
        int b = ~a; // b = -(a + 1);
        System.out.println(b);
    }

}
