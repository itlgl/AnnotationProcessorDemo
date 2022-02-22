package demo.annotationprocessordemo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demo.lib_annotation.Getter;

@Getter
public class User {
    public String name = "segway";
    public int id = 5;
    public List<String> phoneNums = Arrays.asList("10010", "10086");
    public Map<String, String> map = new HashMap<>();
}
