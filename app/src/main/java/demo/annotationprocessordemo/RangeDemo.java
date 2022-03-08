package demo.annotationprocessordemo;

import demo.lib_annotation.Range;

@Range
public class RangeDemo {
    @Range
    public int number = 1;

    @Range
    public void test(@Range int param) {

    }
}
