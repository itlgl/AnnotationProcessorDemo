package demo.libdemo;

import java.util.List;

import demo.lib_annotation.Range;

@Range
public class RangeDemo<String> {
    @Range
    public List<Integer> numberList;

    @Range
    public <Integer> void test(@Range int param) {

    }
}
